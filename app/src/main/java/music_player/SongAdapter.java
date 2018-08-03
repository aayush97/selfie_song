package music_player;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bipinoli.selfie_song_minor.R;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private ArrayList<Song> songs;

    private MusicPlayerActivity.MyCallbackClass myCallbackClass;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView songTitle;
        TextView artistName;


        public ViewHolder(@NonNull View view) {
            super(view);

            songTitle = (TextView)view.findViewById(R.id.song_list_item_song_title);
            artistName = (TextView)view.findViewById(R.id.song_list_item_artist_name);
        }
    }

    public SongAdapter(ArrayList<Song> theSongs, MusicPlayerActivity.MyCallbackClass callbackClass) {
        songs = theSongs;
        myCallbackClass = callbackClass;
    }

    @NonNull
    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_list_item, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.ViewHolder holder, final int position) {

        Song currSong = songs.get(position);
        //get title and artist strings
        holder.songTitle.setText(currSong.getTitle());
        holder.artistName.setText(currSong.getArtist());

        holder.songTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(v.getContext(), position + " title Name", Toast.LENGTH_SHORT).show();
                myCallbackClass.position = position;
                try {
                    myCallbackClass.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(v.getContext(), position + " itemView ", Toast.LENGTH_SHORT).show();
                myCallbackClass.position = position;
                try {
                    myCallbackClass.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.artistName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(v.getContext(), position + " artist Name", Toast.LENGTH_SHORT).show();
                myCallbackClass.position = position;
                try {
                    myCallbackClass.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
}



//
//
//
//
//public class SongAdapter extends BaseAdapter {
//    private ArrayList<Song> songs;
//    private LayoutInflater songInf;
//
//
//    public SongAdapter(Context c, ArrayList<Song> theSongs){
//        songs=theSongs;
//        songInf=LayoutInflater.from(c);
//    }
//
//    @Override
//    public int getCount() {
//        // TODO Auto-generated method stub
//        return songs.size();
//    }
//
//    @Override
//    public Object getItem(int arg0) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public long getItemId(int arg0) {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        //map to song layout
//        LinearLayout songLay = (LinearLayout)songInf.inflate
//                (R.layout.song, parent, false);
//        //get title and artist views
//        TextView songView = (TextView)songLay.findViewById(R.id.song_title);
//        TextView artistView = (TextView)songLay.findViewById(R.id.song_artist);
//        //get song using position
//        Song currSong = songs.get(position);
//        //get title and artist strings
//        songView.setText(currSong.getTitle());
//        artistView.setText(currSong.getGenre());
//        //set position as tag
//        songLay.setTag(position);
//        return songLay;
//    }
//}
