package teamrocket.androidautotamu;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.System.currentTimeMillis;


public class Main extends Activity implements SensorEventListener {
    private float mLastX, mLastY, mLastZ;
    private long time = -1;
    private int count = 0;
    private boolean mInitialized;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 2.0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * Called when the activity is first created.
     */

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
// can be safely ignored for this demo
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double arrayV[] = new double[1000];

        TextView tvX = (TextView) findViewById(R.id.x_axis);
        TextView tvY = (TextView) findViewById(R.id.y_axis);
        TextView tvZ = (TextView) findViewById(R.id.z_axis);
        TextView ta = (TextView) findViewById(R.id.a_Total);
        TextView tt = (TextView) findViewById(R.id.a_Time);
        TextView debugOne = (TextView) findViewById(R.id.d_One);
        TextView debugTwo = (TextView) findViewById(R.id.d_Two);
        TextView debugThree = (TextView) findViewById(R.id.d_Thre);
        TextView debugFour = (TextView) findViewById(R.id.d_Four);
        ImageView iv = (ImageView) findViewById(R.id.image);


        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        double totalAcc = 0;
        double avgTime = 0;
        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            tvX.setText("0.0");
            tvY.setText("0.0");
            tvZ.setText("0.0");
            ta.setText("0.0");
            tt.setText("0.0");
            mInitialized = true;
        } else {
            float deltaX = Math.abs(mLastX - x);
            float deltaY = Math.abs(mLastY - y);
            float deltaZ = Math.abs(mLastZ - z);
            if (deltaX < NOISE) deltaX = (float) 0.0;
            if (deltaY < NOISE) deltaY = (float) 0.0;
            if (deltaZ < NOISE) deltaZ = (float) 0.0;
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            totalAcc = pow((double) deltaX, 2) + pow((double) deltaY, 2) + pow((double) deltaZ, 2);
            totalAcc = sqrt(totalAcc);

            long whyDammit = currentTimeMillis();

            if (time != -1){
                if(whyDammit > time + 1000){
                    debugThree.setText("In If");
                    time = -1;

                    count = 0;
                    arrayV[0]=0;
                }else{
                    debugThree.setText("In Else");
                    arrayV[count] = totalAcc;
                    count++;
                }

            }else{
                time = currentTimeMillis();
                count = 1;
                arrayV[0] = totalAcc;
            }
            avgTime = 0;
            for (int i = 0; i < count; i++) {
              //  debugOne.setText(Double.toString(avgTime));
                avgTime = avgTime + arrayV[i];
            }
            debugOne.setText(Double.toString(count));
            debugTwo.setText(Double.toString(count));

            if(avgTime != 0) {
                avgTime = avgTime / count;
            }

            tvX.setText(Float.toString(deltaX));
            tvY.setText(Float.toString(deltaY));
            tvZ.setText(Float.toString(deltaZ));
            ta.setText(Double.toString(totalAcc));
            tt.setText(Double.toString(avgTime));
            iv.setVisibility(View.VISIBLE);
//            if (deltaX > deltaY) {
//                iv.setImageResource(R.drawable.horizontal);
//            } else if (deltaY > deltaX) {
//                iv.setImageResource(R.drawable.vertical);
//            } else {
//                iv.setVisibility(View.INVISIBLE);
//            }
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//
//    }
}