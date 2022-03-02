package ddwucom.mobile.walkwithme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class AllWalklogsActivity  extends AppCompatActivity {
    final static int UPDATE_ACTIVITY_CODE = 100;

    ListView lvWalklog = null;
    WalklogDBHelper helper;
    Cursor cursor;
//    SimpleCursorAdapter adapter;
	MyCursorAdapter adapter;

    boolean read = true;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPDATE_ACTIVITY_CODE:
                if (resultCode == RESULT_OK) {
                    read = true;
                } else {
                    read = false;
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_all_walklogs);
        lvWalklog = (ListView)findViewById(R.id.lvWalklog);

        helper = new WalklogDBHelper(this);

//		  SimpleCursorAdapter 객체 생성
//        adapter = new SimpleCursorAdapter (this, android.R.layout.simple_list_item_2, null,
//                new String[] { WalklogDBHelper.COL_NAME, WalklogDBHelper.COL_DATE },
//                new int[] { android.R.id.text1, android.R.id.text2 },
//                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		adapter = new MyCursorAdapter(this, R.layout.listview_layout, null);

        lvWalklog.setAdapter(adapter);

//		리스트 뷰 클릭 처리
        lvWalklog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                WalklogDto contact = new WalklogDto();
                contact.setId(id);
                contact.setName(cursor.getString(cursor.getColumnIndex(WalklogDBHelper.COL_NAME)));
                contact.setPlace(cursor.getString(cursor.getColumnIndex(WalklogDBHelper.COL_PLACE)));
                contact.setDate(cursor.getString(cursor.getColumnIndex(WalklogDBHelper.COL_DATE)));
                contact.setMemo(cursor.getString(cursor.getColumnIndex(WalklogDBHelper.COL_MEMO)));

                Intent intent = new Intent(AllWalklogsActivity.this, UpdateWalklogActivity.class);
                intent.putExtra("contact", contact);
                startActivityForResult(intent, UPDATE_ACTIVITY_CODE);
            }
        });

//		리스트 뷰 롱클릭 처리
        lvWalklog.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AllWalklogsActivity.this);
                builder.setTitle("산책 기록 삭제")
                        .setMessage("산책 기록을 삭제하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SQLiteDatabase db = helper.getWritableDatabase();
                                String whereClause = "_id=?";
                                String[] whereArgs = new String[] { String.valueOf(id)};
                                db.delete(WalklogDBHelper.TABLE_NAME, whereClause, whereArgs);
                                helper.close();
                                readDB();
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .show();
                return true;
            }
        });

    }

    public void readDB() {
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + WalklogDBHelper.TABLE_NAME, null);
        adapter.changeCursor(cursor);
        helper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (read) {
            readDB();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
    }
}
