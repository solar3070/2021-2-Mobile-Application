package ddwu.com.mobile.example.lbs.placetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    EditText etName;
    EditText etPhone;
    EditText etAddress;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        imageView = findViewById(R.id.imageView);

        Intent intent = getIntent();
        etName.setText(intent.getStringExtra("name"));
        etPhone.setText(intent.getStringExtra("phone"));
        etAddress.setText(intent.getStringExtra("address"));

        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("photo");
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                Toast.makeText(this, "Save Place information", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnClose:
                finish();
                break;
        }
    }
}
