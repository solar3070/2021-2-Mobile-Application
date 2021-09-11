package mobile.example.dbtest;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SearchContactActivity extends Activity {

	ContactDBHelper helper;

	EditText etSearch;
	TextView tvResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_contact);
		
		helper = new ContactDBHelper(this);

		etSearch = findViewById(R.id.etSearchName);
		tvResult = findViewById(R.id.tvSearchResult);
	}

	
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnSearchContact:
			SQLiteDatabase db = helper.getReadableDatabase();

			String selection = ContactDBHelper.COL_NAME + " LIKE ?";
			String[] selectArgs = new String[] { "%" + etSearch.getText().toString() + "%" };

			Cursor cursor = db.query(ContactDBHelper.TABLE_NAME, null, selection, selectArgs, null, null, null, null);

			String result = "";
			while (cursor.moveToNext()) {
				int id = cursor.getInt(cursor.getColumnIndex("_id"));
				String name = cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_NAME));
				String phone = cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_PHONE));
				String category = cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_CAT));

				result += name + " " + phone + " " + category + "\n";
			}

			tvResult.setText(result);
			helper.close();
		}
	}

	
}
