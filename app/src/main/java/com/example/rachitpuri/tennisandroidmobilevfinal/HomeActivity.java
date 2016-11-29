package com.example.rachitpuri.tennisandroidmobilevfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    public void DirectionTraining(View v) {
        // obtain myValue
        Intent DirectionPage = new Intent(HomeActivity.this, DirectionGame.class);
       HomeActivity.this.startActivity(DirectionPage);
    }

    public void DepthTraining(View v) {
        // obtain myValue
        Intent DepthPage = new Intent(HomeActivity.this, DepthActivity.class);
        HomeActivity.this.startActivity(DepthPage);
    }

    public void ViewProgress(View v) {
        // obtain myValue
        //String temp  = "hello";
    }

}
