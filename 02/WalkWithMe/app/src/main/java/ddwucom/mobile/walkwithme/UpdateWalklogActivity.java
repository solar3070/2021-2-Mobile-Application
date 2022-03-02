package ddwucom.mobile.walkwithme;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateWalklogActivity extends AppCompatActivity {
    EditText etName;
    EditText etPlace;
    EditText etDate;
    EditText etMemo;

    WalklogDBHelper helper;

    WalklogDto contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_walklog);

        etName = findViewById(R.id.etUpdateName);
        etPlace = findViewById(R.id.etUpdatePlace);
        etDate = findViewById(R.id.etUpdateDate);
        etMemo = findViewById(R.id.etUpdateMemo);

        helper = new WalklogDBHelper(this);

        contact = (WalklogDto) getIntent().getSerializableExtra("contact");

        etName.setText(contact.getName());
        etPlace.setText(contact.getPlace());
        etDate.setText(contact.getDate());
        etMemo.setText(contact.getMemo());
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnUpdateWalklog:
//                DB 데이터 업데이트 작업 수행
                SQLiteDatabase db = helper.getWritableDatabase();

                ContentValues row = new ContentValues();

                row.put(WalklogDBHelper.COL_NAME, etName.getText().toString());
                row.put(WalklogDBHelper.COL_PLACE, etPlace.getText().toString());
                row.put(WalklogDBHelper.COL_DATE, etDate.getText().toString());
                row.put(WalklogDBHelper.COL_MEMO, etMemo.getText().toString());

                String whereClause = "_id=?";
                String[] whereArgs = new String[] { String.valueOf(contact.getId()) };

                db.update(WalklogDBHelper.TABLE_NAME, row, whereClause, whereArgs);

                helper.close();

                setResult(RESULT_OK);
                finish();
                break;
            case R.id.btnUpdateWalklogClose:
//                DB 데이터 업데이트 작업 취소
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }
}
