package music_player;

import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.bipinoli.selfie_song_minor.R;

public class MusicVisualizationActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{
    private static final String TAG = "MusicVisualizationActivity";
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;


    
    private ImageView mImageView;
    private ImageView mImageViewMusic;
    private GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inferenced);

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




        // ----------------- Gestures --------------------------------
        gestureDetector = new GestureDetectorCompat(this, this);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.e("GESTURE", "onFling() called");
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeLeft();
                    } else {
                        onSwipeRight();
                    }
                    result = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private void onSwipeRight() {
        // do nothing
        Log.e("SWIPING", "swipe right called!");
    }

    private void onSwipeLeft() {
        Log.e("SWIPING", "swipe left called!");
        finish();
    }
}
