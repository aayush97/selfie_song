package com.bipinoli.selfie_song_minor;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class TestActivity extends AppCompatActivity {

    Button mGloomyBtn, mHappyBtn, mSurprisedBtn, mAngryBtn;

    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mGloomyBtn = (Button)findViewById(R.id.btn_gloomy);
        mAngryBtn = (Button)findViewById(R.id.btn_angry);
        mHappyBtn = (Button)findViewById(R.id.btn_happy);
        mSurprisedBtn = (Button)findViewById(R.id.btn_surprised);

        mImageView = (ImageView)findViewById(R.id.imageView_test);


        // Animation Stuffs
        final AnimatedVectorDrawableCompat avd_happy = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_happy);

        final Handler handler = new Handler(Looper.myLooper());
        avd_happy.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                super.onAnimationEnd(drawable);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        avd_happy.start();
                    }
                });
            }
        });

        // Animation
        final AnimatedVectorDrawableCompat avd_angry = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_angry);

        final Handler handler2 = new Handler(Looper.myLooper());
        avd_angry.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                super.onAnimationEnd(drawable);
                handler2.post(new Runnable() {
                    @Override
                    public void run() {
                        avd_angry.start();
                    }
                });
            }
        });

        // Animation
        final AnimatedVectorDrawableCompat avd_surprised = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_romantic);

        final Handler handler3 = new Handler(Looper.myLooper());
        avd_surprised.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                super.onAnimationEnd(drawable);
                handler3.post(new Runnable() {
                    @Override
                    public void run() {
                        avd_surprised.start();
                    }
                });
            }
        });

        // Animation

        final AnimatedVectorDrawableCompat avd_gloomy = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_gloomy);

        avd_gloomy.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                super.onAnimationEnd(drawable);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        avd_gloomy.start();
                    }
                });
            }
        });








        mSurprisedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageView.setImageDrawable(avd_surprised);
                avd_surprised.start();
            }
        });

        mAngryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageView.setImageDrawable(avd_angry);
                avd_angry.start();
            }
        });

        mHappyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageView.setImageDrawable(avd_happy);
                avd_happy.start();
            }
        });

        mGloomyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageView.setImageDrawable(avd_gloomy);
                avd_gloomy.start();
            }
        });
    }
}
