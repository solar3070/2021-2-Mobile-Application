package mobile.example.alarmtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/* 실습 2번에 필요한 액티비티*/
public class NotiActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                finish();
                break;
        }
    }
}
