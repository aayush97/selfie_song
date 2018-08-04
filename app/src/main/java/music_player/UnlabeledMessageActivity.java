package music_player;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bipinoli.selfie_song_minor.R;

public class UnlabeledMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlabeled_message);

        final String predictionText = getIntent().getStringExtra("INFERENCE");

        // ------------- Animation -------------------------
        ImageView imageView = (ImageView)findViewById(R.id.unlabeled_msg_imageView);
        TextView mood_text = (TextView)findViewById(R.id.unlabeled_msg_mood);

        final AnimatedVectorDrawableCompat avd_happy = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_romantic);
        final AnimatedVectorDrawableCompat avd_sad = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_gloomy);
        final AnimatedVectorDrawableCompat avd_surprised = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_angry);
        final AnimatedVectorDrawableCompat avd_neutral = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_romantic);
        final AnimatedVectorDrawableCompat avd_angry = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_angry);

        final AnimatedVectorDrawableCompat avd;

        // -----------------------------------------------------------



        switch (predictionText) {
            case "happy":
                mood_text.setText(R.string.happy_text);
                avd = avd_happy;
                imageView.setImageDrawable(avd);
                break;
            case "sad":
                mood_text.setText(R.string.sad_text);
                avd = avd_sad;
                imageView.setImageDrawable(avd);
                break;
            case "surprised":
                mood_text.setText(R.string.surprised_text);
                avd = avd_surprised;
                imageView.setImageDrawable(avd);
                break;
            case "neutral":
                mood_text.setText(R.string.neutral_text);
                avd = avd_neutral;
                imageView.setImageDrawable(avd);
                break;
            case "angry":
                mood_text.setText(R.string.angry_text);
                avd = avd_angry;
                imageView.setImageDrawable(avd);
                break;
            default:
                avd = avd_angry;
                imageView.setImageDrawable(avd);
        }


        // ----------- start animation ----------------------

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

        // --------------------------------------------------


        ImageButton tickBtn = (ImageButton)findViewById(R.id.unlabeled_msg_tick_btn);
        ImageButton crossBtn = (ImageButton)findViewById(R.id.unlabeled_msg_cross_btn);

        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UnlabeledMessageActivity.this, MusicPlayerActivity.class);
                intent.putExtra("INFERENCE", predictionText);
                startActivity(intent);
            }
        });


        tickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UnlabeledMessageActivity.this, SongLabelingActivity.class);
                intent.putExtra("INFERENCE", predictionText);
                startActivity(intent);
            }
        });




    }



}
