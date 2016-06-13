package com.sachin.sachinshrestha.extractdatafromweb;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Button btnLoad;
    TextView tvDisplayContent;
    EditText inputTextURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplayContent = (TextView)findViewById(R.id.tvDisplayContent);
        inputTextURL =(EditText) findViewById(R.id.inputTextURL);
        btnLoad = (Button) findViewById(R.id.btnLoad);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        tvDisplayContent.setMovementMethod(new ScrollingMovementMethod());

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = inputTextURL.getText().toString();
                if (url.isEmpty()){
                    Toast.makeText(getApplicationContext(),"URL input text field empty", Toast.LENGTH_LONG).show();
                } else{
                    if (isOnline()){
                        requestData(url);
                    } else{
                        Toast.makeText(getApplicationContext(),"Network isn't available", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void displayInTextView(String msg){
        tvDisplayContent.setText(msg);
    }

    // check whether the network is availed or not
    // Note that the permissions, ACCESS_NETWORK_STATE and INTERNET should be set first in manifest file
    private boolean isOnline(){
        ConnectivityManager cm= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        } else{
            return false;
        }
    }

    private void requestData(String uri) {
        asyncTasks tasks = new asyncTasks();
        tasks.execute(uri);
    }

    private class asyncTasks extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String stringFromDoInBckGnd) {
            if(stringFromDoInBckGnd == null){
                Toast.makeText(getApplicationContext(),"Invalid URL address.", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"Note: Don't forget to use https://", Toast.LENGTH_LONG).show();
            } else{
                displayInTextView(stringFromDoInBckGnd);
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
