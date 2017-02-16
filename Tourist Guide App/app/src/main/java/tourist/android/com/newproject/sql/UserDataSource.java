package tourist.android.com.newproject.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UserDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = {
			MySQLiteHelper.COLUMN_USER_ID, MySQLiteHelper.COLUMN_USER_NAME,
			MySQLiteHelper.COLUMN_USER_FULL_NAME, MySQLiteHelper.COLUMN_USER_PASSWORD};

	public UserDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public User saveUser(String userName, String fullName, String password) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_USER_NAME, userName);
		values.put(MySQLiteHelper.COLUMN_USER_FULL_NAME, fullName);
		values.put(MySQLiteHelper.COLUMN_USER_PASSWORD, password);
		long insertId = database.insert(MySQLiteHelper.TABLE_USER, null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_USER, allColumns, MySQLiteHelper.COLUMN_USER_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		User user = cursorToUser(cursor);
		cursor.close();
		return user;
	}
	
	public int updateUser(int id, String userName, String fullName, String password) {
		ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_USER_NAME, userName);
        values.put(MySQLiteHelper.COLUMN_USER_FULL_NAME, fullName);
        values.put(MySQLiteHelper.COLUMN_USER_PASSWORD, password);
		int result = database.update(MySQLiteHelper.TABLE_USER, values, "id=" + String.valueOf(id), null);
		return result;
	}

	public void deleteUser(User user) {
		long id = user.getId();
		System.out.println("task deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_USER, MySQLiteHelper.COLUMN_USER_ID + " = " + id, null);
	}

	public void deleteAll() {
		database.delete(MySQLiteHelper.TABLE_USER, null, null);
	}
	
	public User getUser(String userName, String password) {
		User user = null;
		String condition = "1=1";
		if(userName != null && !userName.isEmpty()) {
			condition += " AND " + MySQLiteHelper.COLUMN_USER_NAME + " = '" + userName + "'";
		}
		if(password != null && !password.isEmpty()) {
			condition += " AND " + MySQLiteHelper.COLUMN_USER_PASSWORD + " = '" + password + "'";
		}
		Cursor cursor = database.query(MySQLiteHelper.TABLE_USER, allColumns, condition, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
            user = cursorToUser(cursor);
			break;
		}
		// make sure to close the cursor
		cursor.close();
		return user;
	}

	private User cursorToUser(Cursor cursor) {
        User user = new User();
		user.setId(cursor.getInt(0));
		user.setUserName(cursor.getString(1));
		user.setFullName(cursor.getString(2));
		user.setPassword(cursor.getString(3));
		return user;
	}

	public boolean isClosed() {
		return !database.isOpen();
	}
}
