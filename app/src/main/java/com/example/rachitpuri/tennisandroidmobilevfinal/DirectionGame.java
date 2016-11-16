package com.example.rachitpuri.tennisandroidmobilevfinal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class DirectionGame extends AppCompatActivity {
TextView tvLeft, tvRight, tvCenter;
    int[] tennisShots = new int[50];
    PubSubClient myClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DirectionGame.this);
        builder1.setMessage("Are you ready to play?");

        myClient = new PubSubClient(this);



        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Call Start Game API

                        Random r = new Random();

                        for (int i = 0; i < 50; i++) {
                            tennisShots[i] = r.nextInt(3);

                        }

                        new StartAPI().execute();

                        dialog.cancel();

                    }
                });

        tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvCenter = (TextView) findViewById(R.id.tvCenter);
        tvRight=(TextView) findViewById(R.id.tvRight);
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    class StartAPI extends AsyncTask<Void, Void, String> {

        String baseApiUrl;

        protected void onPreExecute(){

            baseApiUrl= "https://tennis-trainer.appspot.com/data/begin";
        }
        @Override
        protected String doInBackground(Void... params) {


            String JsonResponse = null;

            BufferedReader reader = null;
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(baseApiUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");



                InputStream inputStream = urlConnection.getInputStream();

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

            if(response==null) {
            //error handling



            }else{

                new GetData().execute();


               /* int count = 0;
                while(count < 50) {
                    if(myClient.queueData.peek() != null) {
                        String direction = myClient.queueData.remove();
                        Log.d(Constants.TAG, direction);
                        count += 1;
                    }
                }
*/


                // For each shot data coming in!


            }
        }


    }



    class GetData extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {

            try {
                myClient.pullAsync("mobile-app");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }
    }



}



