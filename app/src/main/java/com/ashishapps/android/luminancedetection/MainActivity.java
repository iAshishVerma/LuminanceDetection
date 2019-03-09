package com.ashishapps.android.luminancedetection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
   TextView textView;
    private static final String TAG = "bluetooth1";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = "00:15:FF:F2:19:5F";
   // private BluetoothAdapter btAdapter2 = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    int i=0;
    BluetoothAdapter btAdapter2 = BluetoothAdapter.getDefaultAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        textView=findViewById(R.id.text);

        if (lightSensor == null) {
            Toast.makeText(this, "The device has no light sensor !", Toast.LENGTH_SHORT).show();
            finish();
        }
        // max value for light sensor
       // maxValue = lightSensor.getMaximumRange();

        lightEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float value = sensorEvent.values[0];
                getSupportActionBar().setTitle("Luminosity : " + value + " lx");
                  if(value>300){
                      i++;
                      textView.setText(String.valueOf(i));

                }


                // between 0 and 255
//                int newValue = (int) (255f * value / maxValue);
//                root.setBackgroundColor(Color.rgb(newValue, newValue, newValue));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
        //////////tr code
        Log.d(TAG, "...onResume - try connect...");

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter2.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.

//        try {
//            btSocket = createBluetoothSocket(device);
//        } catch (IOException e1) {
//            Log.e("Fatal Error", "In onResume() and socket create failed: " + e1.getMessage() + ".");
//        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
//        Intent discoverablrIntent =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//        btAdapter2.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting...");
        try {
            btSocket.connect();
            Log.d(TAG, "...Connection ok...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                Log.e("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Create Socket...");

        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            Log.e("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightEventListener);
    }
    //////////Transmission to bluetooth
//    BluetoothAdapter btAdapter2 = BluetoothAdapter.getDefaultAdapter();
//    checkBTState();
//    sendData("1");
//    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
//        if(Build.VERSION.SDK_INT >= 10){
//            try {
//                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
//                return (BluetoothSocket) m.invoke(device, MY_UUID);
//            } catch (Exception e) {
//                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
//            }
//        }
//        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }


