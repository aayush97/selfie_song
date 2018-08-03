package music_player;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

public class SongExtractor {

    public static void getSongList(Activity activity, DBHelper database, ArrayList<Song> songList){
        //retrieve song info
        ContentResolver musicResolver  = activity.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri,null,null,null,null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            int genreColumn = musicCursor.getColumnIndex(MediaStore.Audio.Genres.NAME);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisGenre = null; //musicCursor.getString(genreColumn);
                if(database.exists(thisId)) {
                    Cursor cursor = database.getData(thisId);
                    cursor.moveToFirst();
                    songList.add(new Song(cursor.getLong(cursor.getColumnIndex(DBHelper.SONGS_COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(DBHelper.SONGS_COLUMN_NAME)),
                            cursor.getString(cursor.getColumnIndex(DBHelper.SONGS_COLUMN_ARTIST)),
                            cursor.getString(cursor.getColumnIndex(DBHelper.SONGS_COLUMN_GENRE))));
                    continue;
                }
                // get genre of the song
                Uri genreUri = MediaStore.Audio.Genres.getContentUriForAudioId("external",(int)thisId);
                Cursor genreCursor = musicResolver.query(genreUri,null,null,null,null);
                if(genreCursor!=null && genreCursor.moveToFirst()){
                    int genreColumnIndex = genreCursor.getColumnIndex(MediaStore.Audio.GenresColumns.NAME);
                    do{
                        String genre = genreCursor.getString(genreColumnIndex);
                        if(thisGenre==null){
                            thisGenre = genre;
                        }else{
                            thisGenre += ", " + genre;
                        }
                    }while(genreCursor.moveToNext());
                }
                songList.add(new Song(thisId, thisTitle, thisArtist, thisGenre));
                String tag;
                if(thisGenre==null) {
                    tag = "Others";
                }else if(thisGenre.equals("Jazz") || thisGenre.equals("Rock")){
                    tag = "Happy";
                }else if(thisGenre.equals("Blues")){
                    tag = "Sad";
                }else if(thisGenre.equals("Metal")){
                    tag = "Angry";
                }else{
                    tag = "Surprise";
                }
                database.insertData(thisId,thisTitle,thisArtist,thisGenre,0,tag);
            } while (musicCursor.moveToNext());
        }
    }
}
