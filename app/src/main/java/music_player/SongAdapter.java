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

    private CustomListeners.CustomOnClickListener mMyOnClickListener;
    private CustomListeners.CustomOnLongClickListener mMyOnLongClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView songTitle;
        TextView artistName;


        public ViewHolder(@NonNull View view) {
            super(view);

            songTitle = (TextView)view.findViewById(R.id.song_list_item_song_title);
            artistName = (TextView)view.findViewById(R.id.song_list_item_artist_name);
        }
    }

    public SongAdapter(ArrayList<Song> theSongs, CustomListeners.CustomOnClickListener onClickListener,
                       CustomListeners.CustomOnLongClickListener onLongClickListener) {
        songs = theSongs;
        mMyOnClickListener = onClickListener;
        mMyOnLongClickListener = onLongClickListener;
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
                mMyOnClickListener.position = position;
                mMyOnClickListener.view = v;
                try {
                    mMyOnClickListener.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(v.getContext(), position + " itemView ", Toast.LENGTH_SHORT).show();
                mMyOnClickListener.position = position;
                mMyOnClickListener.view = v;
                try {
                    mMyOnClickListener.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.artistName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(v.getContext(), position + " artist Name", Toast.LENGTH_SHORT).show();
                mMyOnClickListener.position = position;
                mMyOnClickListener.view = v;
                try {
                    mMyOnClickListener.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.songTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                // Toast.makeText(view.getContext(), position + " songTitle long press", Toast.LENGTH_SHORT).show();
                mMyOnLongClickListener.position = position;
                mMyOnLongClickListener.view = view;
                try {
                    mMyOnLongClickListener.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        holder.artistName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                // Toast.makeText(view.getContext(), position + " songTitle long press", Toast.LENGTH_SHORT).show();
                mMyOnLongClickListener.position = position;
                mMyOnLongClickListener.view = view;
                try {
                    mMyOnLongClickListener.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                // Toast.makeText(view.getContext(), position + " songTitle long press", Toast.LENGTH_SHORT).show();
                mMyOnLongClickListener.position = position;
                mMyOnLongClickListener.view = view;
                try {
                    mMyOnLongClickListener.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
}