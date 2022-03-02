package ddwu.com.mobile.multimedia.photomemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ShowMemoActivity extends AppCompatActivity {

    final static String TAG = "ShowMemoActivity";

    final static int REQUEST_PERMISSION_CODE = 100;
    final static int GALLERY_CODE = 200;

    Cursor cursor;
    MemoDBHelper helper;
    ImageView ivPhoto;
    TextView tvMemo;

    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_memo);

        helper = new MemoDBHelper(this);

        ivPhoto = (ImageView)findViewById(R.id.ivShow);
        tvMemo = (TextView) findViewById(R.id.tvMemo);

//        MainActivity 에서 전달 받은 _id 값을 사용하여 DB 레코드를 가져온 후 ImageView 와 TextView 설정
        Intent intent = getIntent();
        Long id = intent.getExtras().getLong("id");
        Log.d(TAG, "id:" + id);

        SQLiteDatabase db = helper.getReadableDatabase();
        String selectClause = helper.ID + "=?";
        String[] selectArgs = new String[] { String.valueOf(id) };
        cursor = db.query(helper.TABLE_NAME, null, selectClause, selectArgs,
                null, null, null, null);
        if (cursor.moveToNext()) {
            mCurrentPhotoPath = cursor.getString(cursor.getColumnIndex(helper.PATH));
            tvMemo.setText(cursor.getString(cursor.getColumnIndex(helper.MEMO)));
        }
        getPic();

        cursor.close();
        helper.close();

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnClose:
                finish();
                break;
        }
    }

    private void getPic() {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        bmOptions.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        ivPhoto.setImageBitmap(bitmap);
    }

}
