package mobile.example.alarmtest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.*;
import android.os.Build;
import android.widget.*;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyBroadcastReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
//		Toast.makeText(context, "one time!", Toast.LENGTH_LONG).show();

		/* 실습 1번 */

		// 채널 생성
		createNotificationChannel(context);

		// Notification 출력 (알림 생성)
		NotificationCompat.Builder builder
				= new NotificationCompat.Builder(context, context.getResources().getString(R.string.CHANNEL_ID))
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("기상 시간")
				.setContentText("일어나! 공부할 시간이야!")
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .bigText("기본적인 알림의 메시지보다 더 많은 양의 내용을 알림에 표시하고자 할 때 메시지가 잘리지 않도록 함"))
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setAutoCancel(true); // 알림을 터치하면 닫혀짐(사라짐)

		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

		int notificationId = 100;
		notificationManager.notify(notificationId, builder.build());
	}

	private void createNotificationChannel(Context context) {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence name = context.getResources().getString(R.string.channel_name);       // strings.xml 에 채널명 기록
			String description = context.getResources().getString(R.string.channel_description);       // strings.xml에 채널 설명 기록
			int importance = NotificationManager.IMPORTANCE_DEFAULT;    // 알림의 우선순위 지정
			NotificationChannel channel = new NotificationChannel(context.getResources().getString(R.string.CHANNEL_ID), name, importance);    // CHANNEL_ID 지정
			channel.setDescription(description);
			// Register the channel with the system; you can't change the importance
			// or other notification behaviors after this
			NotificationManager notificationManager = context.getSystemService(NotificationManager.class);  // 채널 생성
			notificationManager.createNotificationChannel(channel);
		}
	}

}