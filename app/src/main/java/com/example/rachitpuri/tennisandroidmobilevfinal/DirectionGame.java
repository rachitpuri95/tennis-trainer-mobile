package com.example.rachitpuri.tennisandroidmobilevfinal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.api.client.repackaged.com.google.common.base.Strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class DirectionGame extends AppCompatActivity {
TextView tvLeft, tvRight, tvCenter, score;
    static int[] tennisShots = new int[50];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DirectionGame.this);
        builder1.setMessage("Are you ready to play?");


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
        score = (TextView) findViewById(R.id.tvScoreCount);
        score.setText("0");
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    class StartAPI extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new HttpClient();
            String apiUrl= "https://tennis-trainer.appspot.com/data/begin";
            return httpClient.makePOST(apiUrl);
        }

        protected void onPostExecute(String response){
            if(response==null) {
            //error handling
            }else{
                new GetData().execute();
                // For each shot data coming in!
            }
        }
    }

    class GetData extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new HttpClient();
            String apiUrl= "https://tennis-trainer.appspot.com/mobile/direction";
            int count = 0;
            int failures = 0;
            int prevCount = -1;
            int correct = 0;

            while(count < 50) {
                if (prevCount != count) {
                    int currDirection = tennisShots[count];

                    if (currDirection == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.left);
                                mp.start();
                                tvLeft.setVisibility(View.VISIBLE);
                                tvRight.setVisibility(View.INVISIBLE);
                                tvCenter.setVisibility(View.INVISIBLE);
                            }
                        });
                    } else if (currDirection == 1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.center);
                                mp.start();
                                tvCenter.setVisibility(View.VISIBLE);
                                tvRight.setVisibility(View.INVISIBLE);
                                tvLeft.setVisibility(View.INVISIBLE);
                            }
                        });
                    } else if (currDirection == 2) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.right);
                                mp.start();
                                tvRight.setVisibility(View.VISIBLE);
                                tvLeft.setVisibility(View.INVISIBLE);
                                tvCenter.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    prevCount = count;
                }

                String direction = httpClient.makePOST(apiUrl);

                if(!Strings.isNullOrEmpty(direction)) {
                    direction = direction.trim();

                    Log.d(Constants.TAG, direction);

                    if( direction.equals("Left") && tennisShots[count] == 0 ||
                            direction.equals("Center") && tennisShots[count] == 1 ||
                                direction.equals("Right") && tennisShots[count] == 2) {
                        Log.d(Constants.TAG, "Correct Shot!");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                score.setText(Integer.toString(Integer.parseInt((score.getText().toString())) + 1));
                            }
                        });
                    }

                    count += 1;
                    failures = 0;
                }
                else {
                    failures += 1;
                }
                if (failures >= 120) {
                    break;
                }
                int sleepTime = 400;    // in ms
                SystemClock.sleep(sleepTime);
            }
            apiUrl = "https://tennis-trainer.appspot.com/data/end";
            httpClient.makePOST(apiUrl);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(DirectionGame.this);
            builder1.setMessage("Game Has Ended");
            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Call Start Game API

                            dialog.cancel();

                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }
}



