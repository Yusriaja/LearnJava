package model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBConnect extends SQLiteOpenHelper
{
	public static final String TABLE_QUERIES = "queries";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_CATEGORY = "category";
	public static final String COLUMN_SUBCATEGORY = "subcategory";
	public static final String COLUMN_QUESTION = "question";
	public static final String COLUMN_ANSWER = "answer";
	public static final String COLUMN_STARRED = "starred";
	
	public static final String DATABASE_NAME = "questionbank.db";
	public static final int DATABASE_VERSION = 1;
	
	public static final String DATABASE_CREATE_QUERY = "create table "+TABLE_QUERIES+" ( "+COLUMN_ID+" integer primary key autoincrement, " +
			" "+COLUMN_CATEGORY+" text not null, "+COLUMN_SUBCATEGORY+" text not null, "+COLUMN_QUESTION+" text not null, "+COLUMN_ANSWER+" text not null, "+COLUMN_STARRED+" text not null )  ";

/*	public DBConnect(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	*/
	/////////////////////////////////////////////////////////////////////////////////////
	
	private static String DB_PATH = "/data/quarkstar.android.javainterviewquestions/databases/";
	 
    private final Context myContext;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DBConnect(Context context) {
 
    	super(context, DATABASE_NAME, null, 1);
        this.myContext = context;
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/"; 
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
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DATABASE_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
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
    	InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DATABASE_NAME;
 
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
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DATABASE_NAME;
    	SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
	
	///////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onCreate(SQLiteDatabase database)
	{
//		database.execSQL(DATABASE_CREATE_QUERY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		 Log.w(DBConnect.class.getName(),
			        "Upgrading database from version " + oldVersion + " to "
			            + newVersion + ", which will destroy all old data");
			    db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUERIES);
			    onCreate(db);
	}

}
