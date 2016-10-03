package com.example.simonliu.togglebuttonedittextarrayswitch;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import static com.example.simonliu.togglebuttonedittextarrayswitch.R.id.radioGroup1;


public class MainActivity extends AppCompatActivity {

    private Button powerOnButton;
    private Button shutdownButton;

    public int delayMinutes = 0;
    public int delaySeconds = 0;
    public int delayResults = 0;
    public String modeMSG = "";


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
        modeMSG = getRadioButton1Text();


        //        Integer[] delayOptions = new Integer[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59}
//res.getIntArray(R.array.delaySettingsArray) is the int[] array get from resources but we need to convert it to Integer[]
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


    public String getRadioButton1Text() {
        RadioButton rb = (RadioButton) findViewById(R.id.radioButton1);
        String modeMSG = rb.getText().toString();
        return modeMSG;
    }


    public void addListenerOnButton() {
        powerOnButton = (Button) findViewById(R.id.powerOnButtonId);
        shutdownButton = (Button) findViewById(R.id.shutdowButtonId);

        powerOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String powerOnMessage = getString(R.string.powerOnMSG) + modeMSG;
                Toast.makeText(MainActivity.this, powerOnMessage, Toast.LENGTH_SHORT).show();
            }
        });

        shutdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shutdownMessage = getString(R.string.showdownMSG) + " " + delayMinutes + " " + getString(R.string.minutes) + " " + delaySeconds + " " + getString(R.string.seconds);
                Toast.makeText(MainActivity.this, shutdownMessage, Toast.LENGTH_SHORT).show();
            }
        });


    }

//Another way to get the checkedID text to String
//    RadioGroup rb = (RadioGroup)findViewById(R.id.radioGroup1);
//    modeMSG = ((RadioButton)findViewById(rb.getCheckedRadioButtonId())).getText().toString();

    public void addListenerOnRadioButton() {
        RadioGroup radioGroupPi = (RadioGroup) findViewById(radioGroup1);
        radioGroupPi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    modeMSG = rb.getText().toString();
                    Toast.makeText(MainActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


//    public void shutdownSubmit(View view) {
////       Button delayShutdownButton = (Button) findViewById(delayShutdowButtonId);
//        String shutdownMessage = getString(R.string.showdownMSG) + delayMinutes + " Minutes" + delaySeconds + " Seconds";
//        Toast.makeText(MainActivity.this, shutdownMessage, Toast.LENGTH_SHORT).show();
//
//    }

}


