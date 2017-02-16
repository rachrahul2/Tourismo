package tourist.android.com.newproject.sql;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

  // Table Place
  public static final String TABLE_PLACE = "tbl_place";
  public static final String COLUMN_PLACE_ID = "id";
  public static final String COLUMN_PLACE_NAME = "place_name";
  public static final String COLUMN_PLACE_TYPE = "place_type";
  public static final String COLUMN_PLACE_DESCRIPTION = "place_description";
  public static final String COLUMN_PLACE_LAT = "place_lat";
  public static final String COLUMN_PLACE_LNG = "place_lng";
  public static final String COLUMN_PLACE_IMAGES = "images";

  // Table User
  public static final String TABLE_USER = "tbl_user";
  public static final String COLUMN_USER_ID = "id";
  public static final String COLUMN_USER_NAME = "ủe_name";
  public static final String COLUMN_USER_FULL_NAME = "user_full_name";
  public static final String COLUMN_USER_PASSWORD = "user_password";


  private static final String DATABASE_NAME = "tourist.db";
  private static final int DATABASE_VERSION = 3;

  // Database creation sql statement
  private static final String DATABASE_CREATE_PLACE = "create table "
      + TABLE_PLACE + "(" + COLUMN_PLACE_ID
      + " integer primary key autoincrement, " 
      + COLUMN_PLACE_NAME + " text not null, "
      + COLUMN_PLACE_TYPE + " text not null, "
      + COLUMN_PLACE_DESCRIPTION + " text not null, "
      + COLUMN_PLACE_LAT + " float null, "
      + COLUMN_PLACE_LNG + " float null,"
          + COLUMN_PLACE_IMAGES + " text not null);";

  private static final String DATABASE_CREATE_USER = "create table "
          + TABLE_USER + "(" + COLUMN_USER_ID
          + " integer primary key autoincrement, "
          + COLUMN_USER_NAME + " text not null, "
          + COLUMN_USER_FULL_NAME + " text not null, "
          + COLUMN_USER_PASSWORD + " text not null);";

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE_PLACE);
    database.execSQL(DATABASE_CREATE_USER);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACE);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
    onCreate(db);
  }

} 