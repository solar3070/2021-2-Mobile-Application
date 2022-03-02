package mobile.example.movingballtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

	final static String TAG = "MainActivity";

	BallView ballView;

	private SensorManager sensorManager;
	private Sensor accelerometer;
	private Sensor magnetometer;

	private float[] mGravity;
	private float[] mGeomagnetic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		ballView = new BallView(this);
		setContentView(ballView);

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		sensorManager.registerListener(ballView, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(ballView, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(ballView);
	}


	@Override
	protected void onResume() {
		super.onResume();
		
	}

	class BallView extends View implements SensorEventListener {

		Paint paint;

		int width;
		int height;
		int color;

		int x;
		int y;
		int r;
		
		boolean isStart;
		
		public BallView(Context context) {
			super(context);
			color = Color.RED;
			paint = new Paint();
			paint.setColor(color);
			paint.setAntiAlias(true);
			isStart = true;
			r = 100;
		}

		// onDraw를 다시 호출할 때는 invalidate()
		public void onDraw(Canvas canvas) {
			if(isStart) {
				width = canvas.getWidth();
				height = canvas.getHeight(); 
				x =  width / 2;
				y =  height / 2;
				isStart = false;
			} 
			
			canvas.drawCircle(x, y, r, paint);
		}

		@Override
		public void onSensorChanged(SensorEvent sensorEvent) {
			// x, y 좌표 계산후 invalidate() 호출
			if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				mGravity = sensorEvent.values.clone();
			}
			if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				mGeomagnetic = sensorEvent.values.clone();
			}

			if (mGravity != null && mGeomagnetic != null) {
				float rotationMatrix[] = new float[9];
				boolean success = SensorManager.getRotationMatrix(rotationMatrix, null, mGravity, mGeomagnetic);
				if (success) {
					float values[] = new float[3];
					SensorManager.getOrientation(rotationMatrix, values);
					for (int i=0; i < values.length; i++) {
						Double degrees = Math.toDegrees(values[i]);
						values[i] = degrees.floatValue();
					}
					float azimuth = values[0];
					float pitch = values[1];
					float roll = values[2];

					if (pitch < 0) {
						y++;
					} else {
						y--;
					}

					if (roll < 0) {
						x--;
					} else {
						x++;
					}

					invalidate();
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int i) {

		}
	}
}
