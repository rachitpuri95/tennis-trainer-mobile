package com.example.rachitpuri.tennisandroidmobilevfinal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.api.client.repackaged.com.google.common.base.Strings;

import java.util.Random;

public class DepthActivity extends AppCompatActivity {
    TextView tvDeep, tvService,score, correctness;
    static int[] tennisShots = new int[50];


    Runnable mRunnable;
    Handler mHandler;
    @Override
    public void onBackPressed() {
        // your code. Do Nothing  HAHAHHAHA !
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depth);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DepthActivity.this);
        builder1.setMessage("Are you ready to play?");

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Call Start Game API

                        Random r = new Random();

                        for (int i = 0; i < 50; i++) {
                            tennisShots[i] = r.nextInt(2);
                        }

                        new StartAPI().execute();

                        dialog.cancel();

                    }
                });







        tvDeep = (TextView) findViewById(R.id.tvDeepShot);
        tvService = (TextView) findViewById(R.id.tvserviceBox);

        score = (TextView) findViewById(R.id.tvScoreCountDepth);
        correctness=(TextView) findViewById(R.id.tvCorrectnessDepth);
        correctness.setVisibility(View.INVISIBLE);
        mHandler=new Handler();
        score.setText("0/0");
        AlertDialog alert11 = builder1.create();
        alert11.show();


        mRunnable=new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                correctness.setVisibility(View.INVISIBLE); //If you want just hide the View. But it will retain space occupied by the View.
                // correctness.setVisibility(View.GONE); //This will remove the View. and free s the space occupied by the View
            }
        };
    }

    class StartAPI extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new HttpClient();
            String apiUrl= "https://tennis-trainer.appspot.com/data/begin";     // Alter the API string
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

        int count;
        int correct;
        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new HttpClient();
            String apiUrl= "https://tennis-trainer.appspot.com/mobile/direction";       // getting the depth data
            count = 0;
            int failures = 0;
            int prevCount = -1;
            correct = 0;


            while(count < 50) {
                if (prevCount != count) {
                    int currDirection = tennisShots[count];

                    if (currDirection == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.servicebox);
                                mp.start();
                                tvService.setVisibility(View.VISIBLE);
                                tvDeep.setVisibility(View.INVISIBLE);

                            }
                        });
                    } else if (currDirection == 1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.deepshot);
                                mp.start();
                                tvDeep.setVisibility(View.VISIBLE);
                                tvService.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    prevCount = count;
                }

                String depth = httpClient.makePOST(apiUrl);

                if(!Strings.isNullOrEmpty(depth)) {
                    depth = depth.trim();

                    Log.d(Constants.TAG, depth);

                    if( depth.equals("service") && tennisShots[count] == 0 ||
                            depth.equals("deep") && tennisShots[count] == 1) {

                        Log.d(Constants.TAG, "Correct Shot!");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count += 1;
                                correct= correct+1;
                                score.setText(Integer.toString(correct)+ "/"+Integer.toString(count)  );
                                correctness.setVisibility(View.VISIBLE);
                                correctness.setText("Correct!");

                                mHandler.postDelayed(mRunnable,1000);



                            }
                        });
                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count += 1;
                                score.setText(Integer.toString(correct)+ "/"+Integer.toString(count));
                                correctness.setVisibility(View.VISIBLE);
                                correctness.setText("Incorrect!");
                                mHandler.postDelayed(mRunnable,1000);



                            }
                        });
                    }
                    failures = 0;


                }
                else {
                    failures += 1;
                }
                if (failures >= 50) {  // Failure after 30 seconds of inactivity.
                    break;
                }
                int sleepTime = 600;    // in ms
                SystemClock.sleep(sleepTime);
            }
            apiUrl = "https://tennis-trainer.appspot.com/data/end";
            httpClient.makePOST(apiUrl);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {



            AlertDialog.Builder builder1 = new AlertDialog.Builder(DepthActivity.this);
            if(count < 50){
                builder1.setMessage("Game Has Ended due to Inactivity. Your Score was: "+ score.getText().toString());
            }
            else{
                builder1.setMessage("Well Done! Your score was: "+score.getText().toString() );
            }

            builder1.setPositiveButton(
                    "Return",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Call Start Game API
                            Intent DepthPage = new Intent(DepthActivity.this, HomeActivity.class);
                            DepthActivity.this.startActivity(DepthPage);
                            dialog.cancel();

                        }
                    });


            builder1.setCancelable(false);


            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
    }





}
