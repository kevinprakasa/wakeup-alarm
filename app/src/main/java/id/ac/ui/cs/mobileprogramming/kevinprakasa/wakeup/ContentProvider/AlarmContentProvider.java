package id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class AlarmContentProvider extends ContentProvider {
    public static final String PROVIDER_NAME = "id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.ContentProvider.AlarmContentProvider";
    public static final String URL = "content://"+PROVIDER_NAME+"/alarms";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String ID = "id";
    public static final String TIME = "time";
    public static final String LABEL = "label";
    public static final String SNOOZE = "snooze";


    private static HashMap<String, String> ALARMS_PROJECTION_MAP;

    public static final int ALARMS = 1;
    public static final int ALARM_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "alarms", ALARMS);
        uriMatcher.addURI(PROVIDER_NAME, "alarms/#", ALARM_ID);
    }



    /**
     * Database specific constant declarations
     */

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Alarms";
    static final String ALARMS_TABLE_NAME = "alarms";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + ALARMS_TABLE_NAME +
                    " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " TIME INT(255) NOT NULL, " +
                    " LABEL TEXT NOT NULL," +
                    " SNOOZE INT(255) NOT NULL);";


    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  ALARMS_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */

        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(ALARMS_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ALARMS:
                qb.setProjectionMap(ALARMS_PROJECTION_MAP);
                break;

            case ALARM_ID:
                qb.appendWhere( ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }

        if (sortOrder == null || sortOrder == ""){
            /**
             * By default sort on alarm names
             */
            sortOrder = TIME;
        }

        Cursor c = qb.query(db,	projection,	selection,
                selectionArgs,null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            /**
             * Get all alarm records
             */
            case ALARMS:
                return "vnd.android.cursor.dir/vnd.example.alarms";
            /**
             * Get a particular alarm
             */
            case ALARM_ID:
                return "vnd.android.cursor.item/vnd.example.alarms";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        /**
         * Add a new alarm record
         */
        long rowID = db.insert(	ALARMS_TABLE_NAME, "", contentValues);

        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case ALARMS:
                count = db.delete(ALARMS_TABLE_NAME, selection, selectionArgs);
                break;

            case ALARM_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( ALARMS_TABLE_NAME, ID +  " = " + id +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case ALARMS:
                count = db.update(ALARMS_TABLE_NAME, values, selection, selectionArgs);
                break;

            case ALARM_ID:
                count = db.update(ALARMS_TABLE_NAME, values,
                        ID + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
