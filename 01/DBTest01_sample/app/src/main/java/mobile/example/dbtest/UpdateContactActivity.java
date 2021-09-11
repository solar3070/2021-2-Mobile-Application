package mobile.example.dbtest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class UpdateContactActivity extends Activity {

    ContactDBHelper helper;
    EditText etName;
    EditText etPhone;
    EditText etCat;

    ContactDto contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        helper = new ContactDBHelper(this);

        etName = findViewById(R.id.updateName);
        etPhone = findViewById(R.id.updatePhone);
        etCat = findViewById(R.id.updateCat);

        contact = (ContactDto) getIntent().getSerializableExtra("contact");

        etName.setText(contact.getName());
        etPhone.setText(contact.getPhone());
        etCat.setText(contact.getCategory());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdateContactSave:
                SQLiteDatabase db = helper.getWritableDatabase();

                ContentValues row = new ContentValues();

                row.put(ContactDBHelper.COL_NAME, etName.getText().toString());
                row.put(ContactDBHelper.COL_PHONE, etPhone.getText().toString());
                row.put(ContactDBHelper.COL_CAT, etCat.getText().toString());

                String whereClause = "_id=?";
                String[] whereArgs = new String[] { String.valueOf(contact.getId()) };

                db.update(ContactDBHelper.TABLE_NAME, row, whereClause, whereArgs);

                helper.close();
                break;
        }
        finish();
    }
}
