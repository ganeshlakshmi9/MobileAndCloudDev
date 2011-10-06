/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package ncsu.sourse.android.broadcastchat1.dao;

import ncsu.course.android.broadcastchat1.model.UserMessage;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class BroadcastDbAdapter {
	private static final String TAG = "DbAdapter";
	
	public static final String KEY_ID = "_id";
    public static final String KEY_MESSAGE_USER = "username";
    public static final String KEY_MESSAGE_ROOM = "room";
    public static final String KEY_MESSAGE_MSG = "msg";
    public static final String KEY_MESSAGE_TS = "ts";
    
    private static final String DATABASE_TABLE = "message";
    
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE_MESSAGE_TABLE =
        "create table " + DATABASE_TABLE + "(_id integer primary key autoincrement, "
        + "username text not null, room text not null,  msg text, ts timestamp not null);";
    
    private static final String DATABASE_NAME = "chatData";
    
    private static final int DATABASE_VERSION = 2;
    
    private final Context mCtx;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public BroadcastDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public BroadcastDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    /**
     * Inserts new profile using the values: username, msg, room and ts provided by the profile object.
     * 
     * @param msg
     * @return rowId or -1 if failed
     */
    public long insertMessage(UserMessage msg){
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MESSAGE_USER, msg.getUser());
        initialValues.put(KEY_MESSAGE_MSG, msg.getMsg());
        initialValues.put(KEY_MESSAGE_TS, msg.getTs().toGMTString());
        initialValues.put(KEY_MESSAGE_ROOM, msg.getRoom());
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    /**
     * Delete the messages with the given room
     * 
     * @param room
     * @return true if deleted, false otherwise
     */
    public boolean deleteRoom(String room) {
        return mDb.delete(DATABASE_TABLE, KEY_MESSAGE_ROOM + "=" + room, null) > 0;
    }
    
    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllRooms() {
        return mDb.rawQuery("select distinct "+KEY_MESSAGE_ROOM+" "+ KEY_ID +" from "+DATABASE_TABLE, null);
    }
    
    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchMessagesByRoom(String room) {
        return mDb.query(DATABASE_TABLE, new String[] {KEY_ID, KEY_MESSAGE_MSG, KEY_MESSAGE_ROOM, KEY_MESSAGE_TS, KEY_MESSAGE_USER}, KEY_MESSAGE_ROOM + "=" +room, null, null, null, KEY_ID);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	db.execSQL(DATABASE_CREATE_MESSAGE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS "+BroadcastDbAdapter.DATABASE_NAME);
            onCreate(db);
        }
    }
}
