package music_player;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TaggedMusic.db";
    public static final String SONGS_TABLE_NAME = "songs";
    public static final String SONGS_COLUMN_ID = "id";
    public static final String SONGS_COLUMN_NAME = "name";
    public static final String SONGS_COLUMN_ARTIST = "artist";
    public static final String SONGS_COLUMN_GENRE = "genre";
    public static final String SONGS_COLUMN_PLAYED = "played"; //played times
    public static final String SONGS_COLUMN_TAG = "tag";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table songs " +
                        "(id integer primary key, name text, artist text, genre text, tag text, played integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS songs");
        onCreate(db);
    }
    public boolean insertData (Long id, String name, String artist, String genre, int played,String tag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SONGS_COLUMN_NAME, name);
        contentValues.put(SONGS_COLUMN_ARTIST, artist);
        contentValues.put(SONGS_COLUMN_GENRE, genre);
        contentValues.put(SONGS_COLUMN_ID, id);
        contentValues.put(SONGS_COLUMN_PLAYED, played);
        contentValues.put(SONGS_COLUMN_TAG, tag);
        if(db.insert(SONGS_TABLE_NAME, null, contentValues)==-1) {
            return false;
        }
        return true;
    }

    public Cursor getData(String tag) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from songs where tag="+"'"+tag+"'"+" order by played desc", null );
        return res;
    }

    public Cursor getData(Long id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select * from songs where id=" + id + "", null);
            return res;
        }catch(Exception e){
            Log.e("Database not opened",e.toString());
            return null;
        }
    }

    public boolean updateData (Long id, String tag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SONGS_COLUMN_TAG, tag);
        if(db.update(SONGS_TABLE_NAME, contentValues, "id = ? ", new String[] { Long.toString(id) } )>0) {
            return true;
        }

        return false;
    }

    public boolean updatePlayedTime(long id){
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        SQLiteDatabase dbRead = this.getReadableDatabase();
        Cursor cursor = dbRead.rawQuery("select * from songs where id = "+id+"",null);
        if(cursor!=null && cursor.moveToFirst()){
            int played = cursor.getInt(cursor.getColumnIndex(SONGS_COLUMN_PLAYED));
            ContentValues contentValues = new ContentValues();
            contentValues.put(SONGS_COLUMN_PLAYED,played+1);
            dbWrite.update(SONGS_TABLE_NAME,contentValues,"id = ? ",new String[]{Long.toString(id)});
        }
        return true;
    }

}
