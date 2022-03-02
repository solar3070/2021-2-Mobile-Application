package ddwucom.mobile.walkwithme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WalklogDBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "walklog_db";
    public final static String TABLE_NAME = "walklog_table";
    public final static String COL_ID = "_id";
    public final static String COL_NAME = "name";
    public final static String COL_PLACE = "place";
    public final static String COL_DATE = "date";
    public final static String COL_MEMO = "memo";

    public WalklogDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement,"
                + COL_NAME + " TEXT, " + COL_PLACE + " TEXT, " + COL_DATE + " TEXT, " + COL_MEMO + " TEXT);");

        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '아리', '고덕천', '21-09-13', '오늘 아리가 좋아하는 보더콜리 친구를 산책하다 만났다.');");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '여우', '명일 공원', '21-09-17', '여우가 오늘 껌을 밟았다. 속상하다.');");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '아리', '한강', '21-09-20', '아리 배변활동 횟수: 4번');");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '곰이', '고덕천', '21-09-20', '곰이는 오늘도 걷기 싫다고 누워버려서 결국 내가 안고 산책했다.');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);
        onCreate(db);
    }
}
