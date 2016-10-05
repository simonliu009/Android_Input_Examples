package com.example.simonliu.togglebuttonedittextarrayswitch;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;

import static com.example.simonliu.togglebuttonedittextarrayswitch.R.id.radioGroup1;


public class MainActivity extends AppCompatActivity {

    private Button powerOnButton;
    private Button shutdownButton;

    public int delayMinutes = 0;
    public int delaySeconds = 0;
    public int delayResults = 0;
    public String modeMSG = "";
    public boolean onCmd;

    private static int lightModeOld = 0;
    private int lightModeNew;


//
//    private static final Integer[] delayOptions={1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60};
//    private TextView minutesView;
//    private TextView secondsView;
//    private Spinner minutesSpinner;
//    private Spinner secondsSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();
        addListenerOnRadioButton();
        modeMSG = getDefaultCheckedRBText();
        displayVersionName();


        //     Integer[] delayOptions = new Integer[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59}
//      res.getIntArray(R.array.delaySettingsArray) is the int[] array get from resources but we need to convert it to Integer[]
        Resources res = getResources();
        final Integer[] delayOptions = new Integer[res.getIntArray(R.array.delaySettingsArray).length];
        for (int i = 0; i < res.getIntArray(R.array.delaySettingsArray).length; i++) {
            delayOptions[i] = Integer.valueOf(res.getIntArray(R.array.delaySettingsArray)[i]);
        }

        Spinner minutesSpinner = (Spinner) findViewById(R.id.minutes_spinner);
        Spinner secondsSpinner = (Spinner) findViewById(R.id.seconds_spinner);
        ArrayAdapter<Integer> minutesArrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, delayOptions);
        ArrayAdapter<Integer> secondsArrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, delayOptions);

        //设置下拉列表风格
        minutesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secondsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //设置适配器
        minutesSpinner.setAdapter(minutesArrayAdapter);
        secondsSpinner.setAdapter(secondsArrayAdapter);

        //设置监听器,但是在onCreate里面会执行两次,有帖子说放到onStart里面就可以避免这个问题,但是还是会执行两次(第一次delayMinutes/seconds都为0,第二次则是array的列表第一个数字的真实值)(仅仅对赋值来说,执行两次也没有什么问题)
        minutesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                delayMinutes = i ;

                delayMinutes = Integer.valueOf(delayOptions[i]);
                delayResults = delayMinutes * 60 + delaySeconds;
//                Toast.makeText(MainActivity.this, "Delay Setting for Minutes " + delayMinutes + " Minutes", Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this, "Delay Setting for Seconds: " + delaySeconds + " Seconds", Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this, "Delay Final Results: " + delayResults + " Seconds in Total", Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }

        });

        secondsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Resources res = getResources();
//                Integer[] delayOptions = new Integer[res.getIntArray(R.array.delaySettingsArray).length];
//                for (int j = 0; j<res.getIntArray(R.array.delaySettingsArray).length; j++){
//                    delayOptions[j] = Integer.valueOf(res.getIntArray(R.array.delaySettingsArray)[j]);
//                }
//                delaySeconds = i;
                delaySeconds = Integer.valueOf(delayOptions[i]);
                delayResults = delayMinutes * 60 + delaySeconds;
//                Toast.makeText(MainActivity.this, "Delay Setting for Minutes " + delayMinutes + " Minutes", Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this, "Delay Setting for Seconds: " + delaySeconds + " Seconds", Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this, "Delay Final Results: " + delayResults + " Seconds in Total", Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }


    protected void onStart() {
        super.onStart();

    }


    public String getDefaultCheckedRBText() {
        RadioGroup radioGroupLamp = (RadioGroup) findViewById(radioGroup1);
        int checkedRadioButtonId = radioGroupLamp.getCheckedRadioButtonId();
        String modeMSG = "";
        if (checkedRadioButtonId == -1) {
            Toast.makeText(MainActivity.this, "Please set checked=true in one of the radio button in activity_main.xml", Toast.LENGTH_SHORT).show();
        } else {
            if (checkedRadioButtonId == R.id.radioButton1) {
                RadioButton rb = (RadioButton) findViewById(R.id.radioButton1);
                modeMSG = rb.getText().toString();
//  set lightMode =  2 because the lamp need to be turned on 2 times to be in warm yellow mode (radioButton1) ,
//  and it is the most useful mode so it is on top and checked by default
//  it only works for my ChangHong 3-mode LED bubble
                lightModeNew = 2;
            }

            if (checkedRadioButtonId == R.id.radioButton2) {
                RadioButton rb = (RadioButton) findViewById(R.id.radioButton2);
                modeMSG = rb.getText().toString();
                lightModeNew = 3;
            }

            if (checkedRadioButtonId == R.id.radioButton3) {
                RadioButton rb = (RadioButton) findViewById(R.id.radioButton3);
                modeMSG = rb.getText().toString();
                lightModeNew = 1;
            }

        }
        return modeMSG;
    }
//        Below codes also work but need to be changed when the default checked item is not radioButton1
//        RadioButton rb = (RadioButton) findViewById(R.id.radioButton1);
//        String modeMSG = rb.getText().toString();
//        return modeMSG;


    //Another way to get the checkedID text to String
//    RadioGroup rb = (RadioGroup)findViewById(R.id.radioGroup1);
//    modeMSG = ((RadioButton)findViewById(rb.getCheckedRadioButtonId())).getText().toString();

    public void addListenerOnRadioButton() {
        final RadioGroup radioGroupLamp = (RadioGroup) findViewById(radioGroup1);
        radioGroupLamp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                int checkedRadioButtonId = radioGroupLamp.getCheckedRadioButtonId();
                if (null != rb && checkedId > -1) {
                    modeMSG = rb.getText().toString();
//                    Toast.makeText(MainActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();

                    if (checkedRadioButtonId == R.id.radioButton1) {
//  set lightMode =  2 because the lamp need to be turned on 2 times to be in WarmYellow mode (radioButton1) ,
//  and it is the most useful mode so it is on top and checked by default
//  it only works for my ChangHong 3-mode LED bubble
                        lightModeNew = 2;
                    }

                    if (checkedRadioButtonId == R.id.radioButton2) {
                        lightModeNew = 3;
                    }

                    if (checkedRadioButtonId == R.id.radioButton3) {
                        lightModeNew = 1;
                    }

                }
            }
        });

    }


    public void addListenerOnButton() {
        powerOnButton = (Button) findViewById(R.id.powerOnButtonId);
        shutdownButton = (Button) findViewById(R.id.shutdowButtonId);

        powerOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String powerOnMessage = getString(R.string.powerOnMSG) + modeMSG;
                Toast.makeText(MainActivity.this, powerOnMessage, Toast.LENGTH_SHORT).show();
                onCmd = true;
                lampCtrlUDPClient();
            }
        });

        shutdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shutdownMessage = getString(R.string.showdownMSG) + " " + delayMinutes + " " + getString(R.string.minutes) + " " + delaySeconds + " " + getString(R.string.seconds);
                Toast.makeText(MainActivity.this, shutdownMessage, Toast.LENGTH_SHORT).show();
                onCmd = false;
                lampCtrlUDPClient();
            }
        });


    }

//    private Handler mHandler = new Handler();
//    private Runnable mUpdateTimeTask = new Runnable() {
//        public void run() {
//            // Do some stuff that you want to do here
//
//            // You could do this call if you wanted it to be periodic:
//            mHandler.postDelayed(this, 5000 );
//
//        }
//    };


    // Delay Methord
    private void myDelay(int delayMS) {
        try {
            Thread.currentThread().sleep(delayMS);//阻断delayMS (毫秒) block the thread for delayMS(in milliseconds)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    ;

    private void lampCtrlUDPClient() {
        final String brCastIP = "255.255.255.255";
        final int remotePort = 8267;
        final int localPort = 8266;

        new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramSocket socket = null;
                try {
                    // 1、创建套接字 create the DatagramSocket
                    socket = new DatagramSocket(localPort);

                    // 2、创建host的地址包装实例 create the socket address instance with host address and port
                    SocketAddress socketAddr = new InetSocketAddress(brCastIP, remotePort);


                    String packetDataStringOn = "LAMPON";
                    String packetDataStringOff = "LAMPOFF";
                    // Add delayResults to the packetData string so that the remote server could handle the delay itself
                    String packetDataStringDelayOff = "LAMPOFF" + delayResults;

                    // 3、创建数据报。包含要发送的数据、与目标主机地址 create the DatagramPacket with the data to send
                    // the data is set here but it is actually changed below when using it
                    byte[] data = packetDataStringOff.getBytes(Charset.forName("UTF-8"));
                    DatagramPacket packet = new DatagramPacket(data, data.length, socketAddr);

                    if (onCmd) {
                        int lightModeCycle = lightModeNew - lightModeOld;
//                        Log.v("LightModeOld", String.valueOf(lightModeOld));
//                        Log.v("LightModeNew", String.valueOf(lightModeNew));
//                        Log.v("LightModeCycle", String.valueOf(lightModeCycle));
//                      当状态有变化即lightModeNew - lightModeOld 不为0时, 把它+3取模避免负数出现, 得出循环开关次数
//                      Use % (mod) to calculate how many times that the LED need to be turned off/on to get to the correct mode
                        if (lightModeCycle != 0) {
                            lightModeCycle += 3;
                            lightModeCycle %= 3;
//                           Log.v("LightModeCycle Mod()", String.valueOf(lightModeCycle));
                            int i;
                            for (i = 0; i < lightModeCycle; i++) {
//                                Log.v("i = ", String.valueOf(i));
                                //                                broadcast turn off command
                                packet.setData(packetDataStringOff.getBytes(Charset.forName("UTF-8")));
                                socket.send(packet);
                                // 线程阻断 block the thread for 500ms
                                myDelay(800);

                                //                              broadcast turn on command
                                packet.setData(packetDataStringOn.getBytes(Charset.forName("UTF-8")));
                                socket.send(packet);
                                myDelay(800);
                            }
//                           update the lightModeOld value to the latest state
                            lightModeOld = lightModeNew;
//                           Log.v("LightModeOld Update", String.valueOf(lightModeOld));
                        } else {
                            return;
                        }

//                        //broadcast turn off command first no matter it is already on or off
//                        packet.setData(packetDataStringOff.getBytes(Charset.forName("UTF-8")));
//                        socket.send(packet);
//
//                        // 线程阻断 block the thread
//                        myDelay(500);
//
////                         发送数据开灯一次 broadcast the packet to turn on the LED for the 1st time
//                        packet.setData(packetDataStringOn.getBytes(Charset.forName("UTF-8")));
//                        socket.send(packet);
//
//                        myDelay(500);
//
//                        // 再次发送数据关灯 broadcast the packet to turn off the LED after 300ms
//                        packet.setData(packetDataStringOff.getBytes(Charset.forName("UTF-8")));
//                        socket.send(packet);
//
//                        myDelay(500);
//
////                         再次发送数据开灯第二次 broadcast the packet to turn on the LED for the 2nd time
//                        packet.setData(packetDataStringOn.getBytes(Charset.forName("UTF-8")));
//                        socket.send(packet);


                    }
// When click power off button, broadcast the turn off command with delayResults
                    else {

                        packet.setData(packetDataStringDelayOff.getBytes(Charset.forName("UTF-8")));
                        socket.send(packet);
                        lightModeOld = 0;
                        Log.v("LightModeOld Shutdown", String.valueOf(lightModeOld));

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (null != socket) {
                        socket.close();
                    }
                }
            }
        }).start();
    }


    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }


    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public void displayVersionName() {
        TextView versionName = (TextView) findViewById(R.id.version_name_id);
        String rev = getResources().getString(R.string.rev) + getVersionName(MainActivity.this) + "." + getVersionCode(MainActivity.this);
        versionName.setText(rev);
    }

//    public void shutdownSubmit(View view) {
////       Button delayShutdownButton = (Button) findViewById(delayShutdowButtonId);
//        String shutdownMessage = getString(R.string.showdownMSG) + delayMinutes + " Minutes" + delaySeconds + " Seconds";
//        Toast.makeText(MainActivity.this, shutdownMessage, Toast.LENGTH_SHORT).show();
//
//    }

}


