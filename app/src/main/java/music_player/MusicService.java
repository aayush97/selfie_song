package music_player;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.PowerManager;
import android.util.Log;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import android.app.Notification;
import android.app.PendingIntent;
import android.widget.Toast;

import com.bipinoli.selfie_song_minor.R;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
MediaPlayer.OnErrorListener,MediaPlayer.OnCompletionListener{

    private boolean shuffle = false;
    private Random rand;
    private String songTitle;
    private static final int NOTIFY_ID =1;
    private MediaPlayer player;
    private  ArrayList<Song> songs;
    private Queue<Long> queuedSongs;
    private boolean queued = false;
    private int songPosn;
    private final IBinder musicBind = new MusicBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        if(player.getCurrentPosition() > 0){
            mp.reset();
            if(queued)
                playNextInQueue();
            else
                playNext();
        }
    }

    private void playNextInQueue() {
        if(queuedSongs.isEmpty()){
            queued = false;
            playNext();
            return;
        }
        long songId = queuedSongs.remove();
        DBHelper db = new DBHelper(getApplicationContext());
        Cursor cursor = db.getData(songId);
        cursor.moveToFirst();
        songTitle = cursor.getString(cursor.getColumnIndex(DBHelper.SONGS_COLUMN_NAME));
        playSong(songId);
        queuedSongs.add(songId);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // start playback
        mp.start();
        // notification part
        // user can return to music player via notification
        Intent notIntent = new Intent(this, MusicPlayerActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification not = builder.getNotification();

        startForeground(NOTIFY_ID, not);
    }

    public void onCreate(){
        super.onCreate();
        songPosn = 0;
        player = new MediaPlayer();
        initMusicPlayer();
        rand = new Random();
    }

    public void queueSongs(ArrayList<Long> songs){
        queued = true;
        queuedSongs = new LinkedList<>();
        for(long songId: songs)
            queuedSongs.add(songId);
        playNextInQueue();

    }

    public void initMusicPlayer(){
       // player.setWakeMode(getApplicationContext(),
         //       PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> theSongs){
        songs = theSongs;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }
    public void setShuffle(){
        if(shuffle) shuffle=false;
        else shuffle = true;
    }
    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }
    // only to be called after setting song using setSong
    // for song selected by user directly from the list
    public void playSong(){
        //play a song
        player.reset();
        //get song
        Song playSong = songs.get(songPosn);
        songTitle = playSong.getTitle();
        //get id
        long currSong = playSong.getId();
        playSong(currSong);

    }


    // play song directly from id
    // for song recommended by classifier
    public void playSong(long id){
        player.reset();
        DBHelper db = new DBHelper(getApplicationContext());
        db.updatePlayedTime(id);
        Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }catch(Exception e){
            Log.e("Music Service","Error setting data source",e);
            //Toast.makeText(this, "mp3 not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        player.prepareAsync();

    }
    @Override
    public void onDestroy(){
        stopForeground(true);
    }
    public void setSong(int songIndex){
        songPosn = songIndex;
    }

    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }
    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }

    public void playPrev(){
        songPosn--;
        if(songPosn < 0) songPosn = songs.size()-1;
        playSong();
    }

    public void playNext(){
        if(queued) {
            playNextInQueue();
            return;
        }
        if(shuffle){
            int newSong = songPosn;
            while(newSong==songPosn)
                newSong = rand.nextInt(songs.size());
            songPosn = newSong;
        }else {
            songPosn++;
            if (songPosn >= songs.size()) songPosn = 0;
        }
        playSong();
    }
}
