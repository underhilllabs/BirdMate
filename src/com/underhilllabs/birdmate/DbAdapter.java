package com.underhilllabs.birdmate;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {
		public static final String KEY_COM_NAME = "com_name";
		public static final String KEY_SCI_NAME = "sci_name";
	    public static final String KEY_ROWID = "_id";
	    public static final String KEY_RARITY = "rarity";
	    	    
	    private static final String TAG = "DbAdapter";

	    private DatabaseHelper mDbHelper;
	    private SQLiteDatabase mDb;

	    
	    /**
	     * Database creation sql statement
	     */
	    private static final String DATABASE_CREATE =
	            "create table bird_name (_id integer primary key autoincrement, "
	                    + "com_name text not null, " +
	                      "sci_name text, " +
	                      "rarity int);";

	    private static final String DATABASE_NAME = "BirdMate";
	    private static final String DATABASE_TABLE = "bird_name";


	    private static final int DATABASE_VERSION = 1;

	    private final Context mCtx;




	    private static class DatabaseHelper extends SQLiteOpenHelper {
		    //The Android's default system path of your application database.
		    private static String DB_PATH = "/data/data/com.underhilllabs.birdmate/databases/";
		    private static String DB_NAME = "BirdMate";
		    private SQLiteDatabase myDataBase; 
		    private final Context myContext;
		    
	        DatabaseHelper(Context context) {
	            super(context, DATABASE_NAME, null, DATABASE_VERSION);
	            this.myContext = context;

	        }

	        
	        /**
	           * Creates a empty database on the system and rewrites it with your own database.
	           * */
	          public void createDataBase() throws IOException{
	        	  boolean dbExist = checkDataBase();
		        	if(dbExist){
		        		//do nothing - database already exist
		        	}else{
		        		//By calling this method and empty database will be created into the default system path
		                //of your application so we are gonna be able to overwrite that database with our database.
		            	this.getReadableDatabase();
		            	try {
		        			copyDataBase();
		        		} catch (IOException e) {
		            		throw new Error("Error copying database");
		            	}
		        	}
  
	          }

	          public void openDataBase() throws SQLException{
	        	  
	          	//Open the database
	            String myPath = DB_PATH + DB_NAME;
	          	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	       
	          }

	          
	        @Override
	        public void onCreate(SQLiteDatabase db) {
	        	
	        }

	        /**
	         * Check if the database already exist to avoid re-copying the file each time you open the application.
	         * @return true if it exists, false if it doesn't
	         */
	        private boolean checkDataBase(){
	     
	        	SQLiteDatabase checkDB = null;

	        	try{
	        		String myPath = DB_PATH + DB_NAME;
	        		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	        	}catch(SQLiteException e){
	        		//database does't exist yet.
	        	}
	        	if(checkDB != null){
	        		checkDB.close();
	        	}
	        	return checkDB != null ? true : false;
	        }
	     
	        /**
	         * Copies your database from your local assets-folder to the just created empty database in the
	         * system folder, from where it can be accessed and handled.
	         * This is done by transfering bytestream.
	         * */
	        private void copyDataBase() throws IOException{
	     
	        	//Open your local db as the input stream
	        	InputStream myInput = myContext.getAssets().open(DB_NAME);
	     
	        	// Path to the just created empty db
	        	String outFileName = DB_PATH + DB_NAME;
	     
	        	//Open the empty db as the output stream
	        	OutputStream myOutput = new FileOutputStream(outFileName);
	     
	        	//transfer bytes from the inputfile to the outputfile
	        	byte[] buffer = new byte[1024];
	        	int length;
	        	while ((length = myInput.read(buffer))>0){
	        		myOutput.write(buffer, 0, length);
	        	}
	     
	        	//Close the streams
	        	myOutput.flush();
	        	myOutput.close();
	        	myInput.close();
	     
	        }

	        
	        @Override
	        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
	                    + newVersion + ", which will destroy all old data");
	            if(oldVersion == 11) {
	            	db.execSQL("ALTER TABLE needles ADD COLUMN in_use integer");
	            	db.execSQL("ALTER TABLE needles ADD COLUMN project_id integer");
	            } else {
	            	db.execSQL("DROP TABLE IF EXISTS needles");
	            	db.execSQL("DROP TABLE IF EXISTS hooks");
	            	db.execSQL("DROP TABLE IF EXISTS projects");
	            	db.execSQL("DROP TABLE IF EXISTS counters");
	            	onCreate(db);
	            }
	        }
	    }

	    /**
	     * Constructor - takes the context to allow the database to be
	     * opened/created
	     * 
	     * @param ctx the Context within which to work
	     */
	    public DbAdapter(Context ctx) {
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
	    public DbAdapter open() throws SQLException {
	        DatabaseHelper myDbHelper = new DatabaseHelper(mCtx);
	        try {
	        	myDbHelper.createDataBase();
	        } catch (IOException ioe) {
	        	throw new Error("Unable to create database");
	        } try {
	        	myDbHelper.openDataBase();
	        }catch(SQLException sqle){
	        	throw sqle;
	        }
	        mDb = myDbHelper.getWritableDatabase();
	        return this;
	        
	    }
	    
	    public void close() {
	        mDbHelper.close();
	    }


	    /*
	     * bird_name Table functions
	     * 
	     */


	    public Cursor fetchAllBirds() {

	        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_COM_NAME, KEY_SCI_NAME, KEY_RARITY}, null, null, null, null, null);
	    }

	    /*
	    public Cursor fetchAllCounters_simple(String sort_criteria) {
	    	if( sort_criteria.equals("KEY_TYPE") ) {
	    		return mDb.query(C_DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_CURRENT_VAL, KEY_PROJECT_ID,KEY_PROJECT_NAME, 
	            		KEY_UP_OR_DOWN, KEY_TYPE, KEY_MULTIPLE,	KEY_NOTES}, 
	        			null, null, null, null, "type,name");	
	    	} else {
	    		return mDb.query(C_DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_CURRENT_VAL, KEY_PROJECT_ID,KEY_PROJECT_NAME, 
	            		KEY_UP_OR_DOWN, KEY_TYPE, KEY_MULTIPLE,	KEY_NOTES}, 
	        			null, null, null, null, KEY_NAME);
	    	}
	    	
	    }
        */

	    public Cursor fetchBird(long rowId) throws SQLException {

	        Cursor mCursor =

	                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_COM_NAME, KEY_SCI_NAME, KEY_RARITY}, KEY_ROWID + "=" + rowId, null,
	                        null, null, null, null);
	        if (mCursor != null) {
	            mCursor.moveToFirst();
	        }
	        return mCursor;

	    }
	    
	    public Cursor searchBird(String str) throws SQLException {
	    	String search_str = KEY_COM_NAME + " like '%"+str+"%'";
	    	Log.d("BirdMate","search_str "+search_str);
	    	Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_COM_NAME, KEY_SCI_NAME, KEY_RARITY}, KEY_COM_NAME + " LIKE "+ "'%"+str+"%'", null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
	    	
	    }

}
