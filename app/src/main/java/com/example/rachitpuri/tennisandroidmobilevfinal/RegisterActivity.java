package com.example.rachitpuri.tennisandroidmobilevfinal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etUsername, etPassword, etEmail;
    String username, password, email;


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bRegister:
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                email= etEmail.getText().toString();
                JSONObject post_dict = new JSONObject();
                try{
                    post_dict.put("username",username);
                    post_dict.put("email",email);
                    post_dict.put("password",password);

                    new RegisterAPI().execute(String.valueOf(post_dict));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;



        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

         etEmail = (EditText) findViewById(R.id.etEmail);
        etUsername = (EditText) findViewById(R.id.etUsername);
         etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bRegister = (Button)  findViewById(R.id.bRegister);

        bRegister.setOnClickListener(this);


    }

    class RegisterAPI extends AsyncTask<String,String,String> {

        String baseApiUrl;

        protected void onPreExecute(){

            baseApiUrl= "https://tennis-trainer.appspot.com/signup";
        }

        protected String doInBackground(String... params){
                String JsonResponse = null;
                String JsonDATA = params[0];
            BufferedReader reader = null;
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(baseApiUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");

                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
                writer.close();

                InputStream inputStream= urlConnection.getInputStream();

                StringBuffer buffer = new StringBuffer();
                if(inputStream==null){
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                JsonResponse = buffer.toString();
//response data

                try {
//send to post execute
                    return JsonResponse;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //send to post execute
            return null;



            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }
            }
            return null;
        }
        protected void onPostExecute(String response){
            // Do error checking here if necessary
            if(response==null){
                response = "THERE WAS AN ERROR";
            }


            // Registration is complete
            AlertDialog.Builder builder1 = new AlertDialog.Builder(RegisterActivity.this);
            builder1.setMessage("Your registration is complete");

            builder1.setPositiveButton(
                    "Continue",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            Intent loginpageIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                            RegisterActivity.this.startActivity(loginpageIntent);

                        }
                    });



            AlertDialog alert11 = builder1.create();
            alert11.show();
            Log.i("INFO", response);
        }


    }
}
