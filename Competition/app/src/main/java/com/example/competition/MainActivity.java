package com.example.competition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageVIew;
    private int nBefore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageVIew = (ImageView) findViewById(R.id.titleimg);
        mImageVIew.setImageResource(R.drawable.start);

        //Rotation(nBefore - 10);
        final ConstraintLayout CL = (ConstraintLayout) findViewById(R.id.constraintLayout);
        CL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Intent intent = new Intent(MainActivity.this, Choose.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.alphain_activity, R.anim.alphaout_activity);
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_MOVE:
                }
                return true;
            }
        });
    }

    /*
    public void Rotation(int i){
        RotateAnimation ra = new RotateAnimation(nBefore, i, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(50);
        ra.setFillAfter(true);
        mImageVIew.startAnimation(ra);
        nBefore = i;
    }
    */
}
