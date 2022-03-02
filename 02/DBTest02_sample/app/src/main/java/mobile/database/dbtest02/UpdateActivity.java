package mobile.database.dbtest02;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {

    EditText etName;
    EditText etPhone;
    EditText etCategory;

    ContactDBHelper helper;

    ContactDto contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etCategory = findViewById(R.id.etCategory);

        helper = new ContactDBHelper(this);

        contact = (ContactDto) getIntent().getSerializableExtra("contact");

        etName.setText(contact.getName());
        etPhone.setText(contact.getPhone());
        etCategory.setText(contact.getCategory());
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnUpdateContact:
//                DB 데이터 업데이트 작업 수행
                SQLiteDatabase db = helper.getWritableDatabase();

                ContentValues row = new ContentValues();

                row.put(ContactDBHelper.COL_NAME, etName.getText().toString());
                row.put(ContactDBHelper.COL_PHONE, etPhone.getText().toString());
                row.put(ContactDBHelper.COL_CATEGORY, etCategory.getText().toString());

                String whereClause = "_id=?";
                String[] whereArgs = new String[] { String.valueOf(contact.getId()) };

                db.update(ContactDBHelper.TABLE_NAME, row, whereClause, whereArgs);

                helper.close();

                setResult(RESULT_OK);

                finish();
                break;
            case R.id.btnUpdateContactClose:
//                DB 데이터 업데이트 작업 취소

                setResult(RESULT_CANCELED);

                finish();
                break;
        }
    }


}
