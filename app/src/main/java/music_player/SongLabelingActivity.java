package music_player;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bipinoli.selfie_song_minor.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Callable;


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
        SongExtractor.getSongList(this, database, songList);
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
                Log.e(TAG, "fab clicked");
            }
        });
    }

    public class MyOnClickListener extends CustomListeners.CustomOnClickListener {

        @Override
        public Void call() throws Exception {

            Log.e(TAG, "MyOnLongClickListener callback called with position " + position);

            int index = position;
            final long songId = songList.get(index).getId();
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
                    database.updateData(songId, tag);
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


