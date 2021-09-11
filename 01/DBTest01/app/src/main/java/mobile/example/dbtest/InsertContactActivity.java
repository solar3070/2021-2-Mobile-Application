package mobile.example.dbtest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class InsertContactActivity extends Activity {

	ContactDBHelper helper;
	EditText etName;
	EditText etPhone;
	EditText etCategory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insert_contact);

//      DBHelper 생성
		helper = new ContactDBHelper(this);
		
		etName = (EditText)findViewById(R.id.editText1);
		etPhone = (EditText)findViewById(R.id.editText2);
		etCategory = (EditText)findViewById(R.id.editText3);
	}
	
	
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnNewContactSave:
				SQLiteDatabase db = helper.getWritableDatabase();

				// 메소드 사용방식
				ContentValues row = new ContentValues();
				row.put(ContactDBHelper.COL_NAME, etName.getText().toString());
				// Q. helper.COL_NAME으로 가져오지 않고 왜 ContactDBHelper.COL_NAME으로 가져올까?
				// etName.getText() 반환 타입이 Editable이란 객체이므로 toString()을 사용해 문자열로 변경
				row.put(ContactDBHelper.COL_PHONE, etPhone.getText().toString());
				row.put(ContactDBHelper.COL_CAT, etCategory.getText().toString());

				long result = db.insert(ContactDBHelper.TABLE_NAME, null, row);
				// long타입의 반환 값 존재 (영향을 받은 row의 개수 반환), result값이 0보다 크면 정상적으로 반환됐다고 판단 가능

				// 직접 SQL문 작성
				db.execSQL("insert into " + ContactDBHelper.TABLE_NAME + " values (null, '"
						+ etName.getText().toString() + "', '" + etPhone.getText().toString() + "', '"
						+ etCategory.getText().toString() + "');");
				// 반환 값이 없으므로 기능이 잘 수행되었는지 판단 불가능 -> try-catch문 사용

				helper.close();
		}

	}
	

}
