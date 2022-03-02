package ddwucom.mobile.walkwithme;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class InsertWalkLogActivity extends AppCompatActivity {
    EditText etName;
    EditText etPlace;
    EditText etDate;
    EditText etMemo;

    WalklogDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_walklog);

        etName = findViewById(R.id.etInsertPlace);
        etPlace = findViewById(R.id.etInsertPlace);
        etDate = findViewById(R.id.etUpdateDate);
        etMemo = findViewById(R.id.etInsertMemo);

        helper = new WalklogDBHelper(this);
    }


    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnUpdateWalklog:
//			DB 데이터 삽입 작업 수행
                SQLiteDatabase db = helper.getWritableDatabase();

                ContentValues row = new ContentValues();
                row.put(WalklogDBHelper.COL_NAME, etName.getText().toString());
                row.put(WalklogDBHelper.COL_PLACE, etPlace.getText().toString());
                row.put(WalklogDBHelper.COL_DATE, etDate.getText().toString());
                row.put(WalklogDBHelper.COL_MEMO, etMemo.getText().toString());

                long result = db.insert(WalklogDBHelper.TABLE_NAME, null, row);

                helper.close();
                finish();
                break;
            case R.id.btnUpdateWalklogClose:
//			DB 데이터 삽입 취소 수행
                finish();
                break;
        }
    }

}
