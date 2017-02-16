package tourist.android.com.newproject.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class PlaceDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = {
			MySQLiteHelper.COLUMN_PLACE_ID, MySQLiteHelper.COLUMN_PLACE_NAME,
			MySQLiteHelper.COLUMN_PLACE_TYPE, MySQLiteHelper.COLUMN_PLACE_DESCRIPTION,
			MySQLiteHelper.COLUMN_PLACE_LAT, MySQLiteHelper.COLUMN_PLACE_LNG, MySQLiteHelper.COLUMN_PLACE_IMAGES};

	public PlaceDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Place savePlace(String name, String type, String description, float lat, float lng, String images) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_PLACE_NAME, name);
		values.put(MySQLiteHelper.COLUMN_PLACE_TYPE, type);
		values.put(MySQLiteHelper.COLUMN_PLACE_DESCRIPTION, description);
		values.put(MySQLiteHelper.COLUMN_PLACE_LAT, lat);
		values.put(MySQLiteHelper.COLUMN_PLACE_LNG, lng);
		values.put(MySQLiteHelper.COLUMN_PLACE_IMAGES, images);
		long insertId = database.insert(MySQLiteHelper.TABLE_PLACE, null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_PLACE, allColumns, MySQLiteHelper.COLUMN_PLACE_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Place place = cursorToPlace(cursor);
		cursor.close();
		return place;
	}
	
	public int updatePlace(int id, String name, String type, String description, float lat, float lng, String images) {
		ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PLACE_NAME, name);
        values.put(MySQLiteHelper.COLUMN_PLACE_TYPE, type);
        values.put(MySQLiteHelper.COLUMN_PLACE_DESCRIPTION, description);
        values.put(MySQLiteHelper.COLUMN_PLACE_LAT, lat);
        values.put(MySQLiteHelper.COLUMN_PLACE_LNG, lng);
		values.put(MySQLiteHelper.COLUMN_PLACE_IMAGES, images);
		int result = database.update(MySQLiteHelper.TABLE_PLACE, values, "id=" + String.valueOf(id), null);
		
		return result;
	}

	public void deletePlace(Place place) {
		long id = place.getId();
		System.out.println("task deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_PLACE, MySQLiteHelper.COLUMN_PLACE_ID + " = " + id, null);
	}

	public void deleteAll() {
		database.delete(MySQLiteHelper.TABLE_PLACE, null, null);
	}
	
	public List<Place> getAllPlaces() {
		List<Place> places = new ArrayList<Place>();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_PLACE, allColumns, null, null, null, null, "id DESC", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
            Place place = cursorToPlace(cursor);
			places.add(place);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return places;
	}

    public List<Place> filterPlaces(String name, String type, String date) {
        List<Place> places = new ArrayList<Place>();
        String condition = "1=1";
        if(name != null && !name.isEmpty()) {
            condition += " AND " + MySQLiteHelper.COLUMN_PLACE_NAME + " = '" + name + "'";
        }
        if(type != null && !type.isEmpty()) {
            condition += " AND " + MySQLiteHelper.COLUMN_PLACE_TYPE + " = '" + type + "'";
        }
        if(date != null && !date.isEmpty()) {
            condition += " AND " + MySQLiteHelper.COLUMN_PLACE_DESCRIPTION + " LIKE '%" + date + "%'";
        }
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PLACE, allColumns, condition, null, null, null, "id DESC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Place place = cursorToPlace(cursor);
            places.add(place);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return places;
    }

	private Place cursorToPlace(Cursor cursor) {
        Place place = new Place();
		place.setId(cursor.getInt(0));
		place.setPlaceName(cursor.getString(1));
		place.setPlaceType(cursor.getString(2));
		place.setDescription(cursor.getString(3));
		place.setLat(cursor.getFloat(4));
		place.setLng(cursor.getFloat(5));
		place.setImages(cursor.getString(6));
		return place;
	}

	public Place getPlaceById(int id) {
		Place place = null;
		String condition = MySQLiteHelper.COLUMN_PLACE_ID + "=" + id;
		Cursor cursor = database.query(MySQLiteHelper.TABLE_PLACE, allColumns, condition, null, null, null, "id DESC", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			place = cursorToPlace(cursor);
			break;
		}
		cursor.close(); //Closing the cursor
		return place;
	}
}
