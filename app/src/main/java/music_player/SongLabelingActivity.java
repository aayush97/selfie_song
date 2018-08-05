package music_player;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bipinoli.selfie_song_minor.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import music_player.database.DBHelper;
import music_player.music.Song;
import music_player.music.SongAdapter;
import music_player.music.SongExtractor;


public class SongLabelingActivity extends AppCompatActivity {
    private static final String TAG = "SongLabelingActivity";

    DBHelper database;

    private ArrayList<Song> songList;
    private RecyclerView songView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_labeling);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant

                return;
            }}



        database = new DBHelper(this);
        songList = new ArrayList<>();
        SongExtractor.getSongList(this, database, songList, true);
        if(songList.size()==0){
            Intent intent = new Intent(SongLabelingActivity.this, MusicPlayerActivity.class);
            intent.putExtra("INFERENCE", getIntent().getStringExtra("INFERENCE"));
            startActivity(intent);
        }
        /*
            Added code
         */
        Collections.sort(songList, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });


        songView = (RecyclerView) findViewById(R.id.song_labeling_recyclerView);

        // improves the performance
        songView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        songView.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new SongAdapter(songList, new SongLabelingActivity.MyOnClickListener(),
                new SongLabelingActivity.MyOnLongClickListener());
        songView.setAdapter(adapter);


        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SongLabelingActivity.this, MusicPlayerActivity.class);
                intent.putExtra("INFERENCE", getIntent().getStringExtra("INFERENCE"));
                startActivity(intent);
            }
        });

        final String predictionText = getIntent().getStringExtra("INFERENCE");

        // ------------- Animation -------------------------
        ImageView imageView = (ImageView)findViewById(R.id.song_labeling_imageView);
        TextView mood_text = (TextView)findViewById(R.id.song_labeling_mood_text);

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





    }









    // ------------------------- Custom Listeners below -------------------------------

    public class MyOnClickListener extends CustomListeners.CustomOnClickListener {

        @Override
        public Void call() throws Exception {

            Log.e(TAG, "MyOnLongClickListener callback called with position " + position);

            int index = position;
            final long songId = songList.get(index).getId();
            String songName = songList.get(index).getTitle();

            //Creating the instance of PopupMenu

            PopupMenu popup = new PopupMenu(getApplicationContext(), view);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
            popup.show();
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    String tag = item.getTitle().toString();
                    Toast.makeText(getApplicationContext(), tag, Toast.LENGTH_SHORT).show();
                    boolean updated  = database.updateData(songId, tag);
                    if(updated)
                        Toast.makeText(getApplicationContext(),"updated",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(),"Not Updated",Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            return null;

        }
    }

    public class MyOnLongClickListener extends CustomListeners.CustomOnLongClickListener {

        @Override
        public Void call() throws Exception {
            return null;
        }
    }

}


