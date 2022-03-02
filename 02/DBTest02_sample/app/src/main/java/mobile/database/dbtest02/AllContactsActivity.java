package mobile.database.dbtest02;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AllContactsActivity extends AppCompatActivity {

	final static int UPDATE_ACTIVITY_CODE = 100;

	ListView lvContacts = null;
	ContactDBHelper helper;
	Cursor cursor;
	SimpleCursorAdapter adapter;
//	MyCursorAdapter adapter;

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
		setContentView(R.layout.activity_all_contacts);
		lvContacts = (ListView)findViewById(R.id.lvContacts);

		helper = new ContactDBHelper(this);

//		  SimpleCursorAdapter 객체 생성
        adapter = new SimpleCursorAdapter (this, android.R.layout.simple_list_item_2, null,
				new String[] { ContactDBHelper.COL_NAME, ContactDBHelper.COL_CATEGORY },
				new int[] { android.R.id.text1, android.R.id.text2 },
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

//		adapter = new MyCursorAdapter(this, R.layout.listview_layout, null);

		lvContacts.setAdapter(adapter);

//		리스트 뷰 클릭 처리
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				ContactDto contact = new ContactDto();
				contact.setId(id);
				contact.setName(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_NAME)));
				contact.setPhone(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_PHONE)));
				contact.setCategory(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_CATEGORY)));

				Intent intent = new Intent(AllContactsActivity.this, UpdateActivity.class);
				intent.putExtra("contact", contact);
				startActivityForResult(intent, UPDATE_ACTIVITY_CODE);
            }
        });

//		리스트 뷰 롱클릭 처리
		lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(AllContactsActivity.this);
				builder.setTitle("연락처 삭제")
						.setMessage("연락처를 삭제하시겠습니까?")
						.setPositiveButton("예", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								SQLiteDatabase db = helper.getWritableDatabase();
								String whereClause = "_id=?";
								String[] whereArgs = new String[] { String.valueOf(id)};
								db.delete(ContactDBHelper.TABLE_NAME, whereClause, whereArgs);
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
//      DB에서 데이터를 읽어와 Adapter에 설정
		SQLiteDatabase db = helper.getReadableDatabase();
		cursor = db.rawQuery("select * from " + ContactDBHelper.TABLE_NAME, null);
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
//      cursor 사용 종료
		if (cursor != null) cursor.close();
	}

}




