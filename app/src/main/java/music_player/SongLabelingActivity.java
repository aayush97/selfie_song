package music_player;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bipinoli.selfie_song_minor.R;


public class SongLabelingActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    String[] mDataSet = {"Bipin", "Binay", "Avisekh", "Aayush", "Madhav",
            "Sanita", "Dilli Parsad", "Simran", "Ram Sharan", "Rita",
            "Sudip", "Bibek", "Himalaya", "Hanuman", "Romio", "Mo Salah", "Umbape",
            "Keke Do you Love me by Drake such a nice song i love it what a good song Keke Do you Love me by Drake such a nice song i love it what a good song",
            "Sanita", "Dilli Parsad", "Simran", "Ram Sharan", "Rita",
            "Sudip", "Bibek", "Himalaya", "Hanuman", "Romio", "Mo Salah", "Umbape"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_labeling);

        mRecyclerView = (RecyclerView)findViewById(R.id.song_labeling_recyclerView);

        // improves the performance
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);



    }

}


