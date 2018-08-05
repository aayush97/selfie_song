package com.bipinoli.selfie_song_minor.Test;

import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bipinoli.selfie_song_minor.R;

public class InferencedActivity extends AppCompatActivity {
    private static final String TAG = "InferencedActivity";
    
    private ImageView mImageView;
    private ImageView mImageViewMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inferenced);
        Bundle extras = getIntent().getExtras();
        String prediction = extras.getString("PREDICTION");
        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText("Feeling " + prediction);
        mImageView = (ImageView) findViewById(R.id.imageView_animaton);
        mImageViewMusic = (ImageView) findViewById(R.id.imageView_music_visualizer);

        final AnimatedVectorDrawableCompat avd = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_romantic);
        mImageView.setImageDrawable(avd);
        final AnimatedVectorDrawableCompat avd2 = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_music_visualizer);
        mImageViewMusic.setImageDrawable(avd2);

        // animation on infinite loop (make it repeat baby)
        final Handler mainHandler = new Handler(Looper.getMainLooper());
        avd.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
            @Override
            public void onAnimationEnd(final Drawable drawable) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        avd.start();
                    }
                });
            }
        });
        avd.start();


        avd2.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
            @Override
            public void onAnimationEnd(final Drawable drawable) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        avd2.start();
                    }
                });
            }
        });
        avd2.start();
    }
}
