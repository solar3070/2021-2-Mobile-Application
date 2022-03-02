package ddwu.mobile.week4.threadbasic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    EditText etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etText = findViewById(R.id.etText);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnStart:
                // 스레드를 생성할 때 생성자의 매개변수로 넣어주자
                TestThread t = new TestThread(handler);
                t.start();
                etText.setText("Thread start!");
                Toast.makeText(this, "Running!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // 메인 액티비티 멤버로 handler 정의
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) { //  sendMessage에 의해 호출
            int i = msg.arg1;
            // 핸들러는 메인 액티비티 안에 만들어진 멤버
            // etText에 접근해서 값 표시하는 것에 문제 없음
            etText.setText("i: " + i);
        }
    };

    class TestThread extends Thread {
        Handler handler;

        public TestThread(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            for (int i=0; i < 100; i++) {
//                Log.d(TAG, "i: " + i );
                Message msg = Message.obtain();
                msg.arg1 = i;
                // 핸들러에게 메시지를 보냄
                handler.sendMessage(msg); // sendMessage 호출되는 순간 handleMessage 호출
//                 etText.setText("i: " + i);
                // etText는 화면에 있는 요소
                // 화면에 있는 요소는 기본적으로 메인 스레드(UI 스레드)에서만 접근 가능
                // 그런데 지금 직접 만든 TestThread에서 접근해서 화면에 속한 etText의 값을 바꾸려 하고 있음
                // 메인 스레드에서만 사용할 수 있는데 내가 만든 별도의 쓰레드에서 사용하고자 하니까 에러가 발생
                // 핸들러 사용하자
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

