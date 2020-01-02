package com.example.competition;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Buildingcontents extends AppCompatActivity {

    String name = "";
    String pk = "";
    String etc_strct = "";
    String etc_purps = "";
    String etc_roof = "";
    String height = "";
    String grnd = "";
    String ugrnd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buildingcontents);

        Intent intent = getIntent();
        name = intent.getStringExtra("buildingname");
        pk = intent.getStringExtra("buildingpk");
        etc_strct = intent.getStringExtra("strct");
        etc_purps = intent.getStringExtra("purps");
        etc_roof = intent.getStringExtra("roof");
        height = intent.getStringExtra("heit");
        grnd = intent.getStringExtra("grnd_flr_cnt");
        ugrnd = intent.getStringExtra("ugrnd_flr_cnt");

        TextView titleTV = (TextView) findViewById(R.id.find_title);
        TextView pkTV = (TextView) findViewById(R.id.find_pk);
        TextView structTV = (TextView) findViewById(R.id.find_struct);
        TextView purpsTV = (TextView) findViewById(R.id.find_purps);
        TextView heightTV = (TextView) findViewById(R.id.find_height);
        TextView groundTV = (TextView) findViewById(R.id.find_ground);
        TextView ugroundTV = (TextView) findViewById(R.id.find_uground);
        ImageView bcontentsiv = (ImageView) findViewById(R.id.building_contents_home);
        bcontentsiv.setImageResource(R.mipmap.home);

        titleTV.setText(name);
        pkTV.setText(pk);
        structTV.setText(etc_strct);
        purpsTV.setText(etc_purps);
        heightTV.setText(height);
        groundTV.setText(grnd);
        ugroundTV.setText(ugrnd);

        bcontentsiv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Buildingcontents.this, Choose.class);
                startActivity(intent);
                overridePendingTransition(R.anim.topin_activity, R.anim.bottomout_activity);
            }
        });
    }
}
