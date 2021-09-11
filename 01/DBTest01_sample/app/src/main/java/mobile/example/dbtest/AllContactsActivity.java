package mobile.example.dbtest;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AllContactsActivity extends Activity {
	
	private ListView lvContacts = null;

	private ArrayAdapter<ContactDto> adapter;
	private ContactDBHelper helper;
	private ArrayList<ContactDto> contactList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_contacts);

		helper = new ContactDBHelper(this);
		contactList = new ArrayList<ContactDto>();

		lvContacts = (ListView)findViewById(R.id.lvContacts);
		adapter = new ArrayAdapter<ContactDto>(this, android.R.layout.simple_list_item_1, contactList);

		lvContacts.setAdapter(adapter);

		lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				ContactDto contact = contactList.get(i);
				Intent intent = new Intent(AllContactsActivity.this, UpdateContactActivity.class);
				intent.putExtra("contact", contact);
				startActivity(intent);
			}
		});

		lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
				final int pos = i;
				AlertDialog.Builder builder = new AlertDialog.Builder(AllContactsActivity.this);
				builder.setTitle("연락처 삭제")
						.setMessage("연락처를 삭제하시겠습니까?")
						.setPositiveButton("예", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								SQLiteDatabase db = helper.getWritableDatabase();
								String whereClause = "_id=?";
								String[] whereArgs = new String[] { String.valueOf(contactList.get(pos).getId())};
								db.delete(ContactDBHelper.TABLE_NAME, whereClause, whereArgs);
								helper.close();
								onResume();
							}
						})
						.setNegativeButton("아니오", null)
						.show();
				return true;
			}
		});

	}



	@Override
	protected void onResume() {
		super.onResume();

		SQLiteDatabase db = helper.getReadableDatabase();

		Cursor cursor = db.rawQuery("select * from " + ContactDBHelper.TABLE_NAME, null);

		contactList.clear();

		while (cursor.moveToNext()) {
			ContactDto item = new ContactDto();
			item.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			item.setName(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_NAME)));
			item.setPhone(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_PHONE)));
			item.setCategory(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_CAT)));
			contactList.add(item);
		}

		adapter.notifyDataSetChanged();

		helper.close();
		cursor.close();
	}

}




