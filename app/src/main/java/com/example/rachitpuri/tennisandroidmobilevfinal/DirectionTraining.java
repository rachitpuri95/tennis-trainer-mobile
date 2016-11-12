package com.example.rachitpuri.tennisandroidmobilevfinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class DirectionTraining extends AppCompatActivity {
    ImageView iTennis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_training);

        iTennis = (ImageView) findViewById(R.id.iTennis);
        iTennis.setImageResource(R.drawable.tennis);



    }
}
