package model;

import java.util.HashMap;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class QueryDataSource
{
	private SQLiteOpenHelper sqliteHelper;
	private SQLiteDatabase database;
	private String[] allColumns = { DBConnect.COLUMN_ID,
			DBConnect.COLUMN_CATEGORY, DBConnect.COLUMN_SUBCATEGORY, DBConnect.COLUMN_QUESTION, DBConnect.COLUMN_ANSWER , DBConnect.COLUMN_STARRED };

	public QueryDataSource(Context context)
	{
		sqliteHelper = new DBConnect(context);
	}

	public void open() throws SQLException
	{
		database = sqliteHelper.getWritableDatabase();
	}

	public void close()
	{
		sqliteHelper.close();
	}

	public Query createQuery(String category, String subcategory, String question, String answer)
	{
		ContentValues values = new ContentValues();
		values.put(DBConnect.COLUMN_CATEGORY, category);
		values.put(DBConnect.COLUMN_SUBCATEGORY, subcategory);
		values.put(DBConnect.COLUMN_QUESTION, question);
		values.put(DBConnect.COLUMN_ANSWER, answer);
		values.put(DBConnect.COLUMN_STARRED, "n");

		long insertId = database.insert(DBConnect.TABLE_QUERIES, null, values);
		Cursor cursor = database.query(false, DBConnect.TABLE_QUERIES,
				allColumns, DBConnect.COLUMN_ID + "=" + insertId, null, null,
				null, null, null, null);

		cursor.moveToFirst();
		Query query = cursorToQuery(cursor);
		cursor.close();

		return query;
	}
	
	public int updateFavStatus(long qId, String status) 
	{
		ContentValues values = new ContentValues();
		values.put(DBConnect.COLUMN_STARRED, status);
		
		return database.update(DBConnect.TABLE_QUERIES, values, DBConnect.COLUMN_ID + " = " + qId, null);
	}
	
	public int justChangedToYes() 
	{
		ContentValues values = new ContentValues();
		values.put(DBConnect.COLUMN_STARRED, "no");
		
		return database.update(DBConnect.TABLE_QUERIES, values, DBConnect.COLUMN_STARRED+ " = 'just changed'", null);
	}

	public void deleteRecords()
	{
		database.delete(DBConnect.TABLE_QUERIES, null, null);
	}

	public HashMap<String, Query> getAllComments()
	{
		HashMap<String, Query> queryMap = new HashMap<String, Query>();

		Cursor cursor = database.query(DBConnect.TABLE_QUERIES, allColumns,
				null, null, null, null, DBConnect.COLUMN_ID+" asc");

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			Query query = cursorToQuery(cursor);
			queryMap.put(cursor.getString(0), query);
			cursor.moveToNext();
		}
		cursor.close();
		return queryMap;
	}
	
	public HashMap<String, Query> getSelectedData(String condition)
	{
		HashMap<String, Query> queryMap = new HashMap<String, Query>();

		Cursor cursor = database.query(DBConnect.TABLE_QUERIES, allColumns,
				condition, null, null, null, DBConnect.COLUMN_ID+" asc");

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			Query query = cursorToQuery(cursor);
			queryMap.put(cursor.getString(0), query);
			cursor.moveToNext();
		}
		cursor.close();
		return queryMap;
	}

	private Query cursorToQuery(Cursor cursor)
	{
		Query query = new Query();
		query.setId(cursor.getLong(0));
		query.setCategory(cursor.getString(1));
		query.setSubcategory(cursor.getString(2));
		query.setQuestion(cursor.getString(3));
		query.setAnswer(cursor.getString(4));
		query.setStarred(cursor.getString(5));
		
		return query;
	}
}
