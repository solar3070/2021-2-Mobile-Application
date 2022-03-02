package mobile.example.alarmtest;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

public class MainActivity extends Activity {
	
	PendingIntent sender = null;
	AlarmManager alarmManager = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_test);

		alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	}

	public void mOnClick(View v) {
		
		Intent intent = null;

		switch (v.getId()) {
		case R.id.onetime:
			// 예약에 의해 호출될 BR 지정
			intent = new Intent(this, MyBroadcastReceiver.class);
			sender = PendingIntent.getBroadcast(this, 0, intent, 0);

			/* 실습 2번 */
			// 예약에 의해 호출될 액티비티 지정
//			intent = new Intent(this, NotiActivity.class);
//			sender = PendingIntent.getActivity(this, 0, intent, 0);

			// 알람 시간. 3초후
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.SECOND, 3);

			// 알람 등록
			alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), sender);
			break;
		case R.id.repeat:
			intent = new Intent(this, RepeatReceiver.class);
			sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			/* 실습 2번 */
//			intent = new Intent(this, NotiActivity.class);
//			sender = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//			60초당 한번 알람 등록 --> 최소 1분 정도로 반복을 설정하여야 함
			alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
					SystemClock.elapsedRealtime() + 3000,  10000 * 6, sender);

//			정확도가 떨어지는 반복 알람 설정 시		
//			alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 3000,
//					AlarmManager.INTERVAL_FIFTEEN_MINUTES, sender);
            break;
		case R.id.stop:
			intent = new Intent(this, RepeatReceiver.class);
			sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			if (sender != null) alarmManager.cancel(sender);
			break;
		}

	}



//	일정 시간 간격으로 작업을 반복하고자 할 때는 Handler 를 사용할 수도 있음
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.arg1 != 5) {
//                Toast.makeText(AlarmTestActivity.this, "Alarm!!! " + msg.arg1, Toast.LENGTH_SHORT).show();
//                Message newMsg = handler.obtainMessage();
//                newMsg.arg1 = msg.arg1 + 1;
//                this.sendMessageDelayed(newMsg, 3000);
//            }
//        }
//    };


}

