package com.example.simonliu.togglebuttonedittextarrayswitch;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import static com.example.simonliu.togglebuttonedittextarrayswitch.R.id.radioGroup1;


public class MainActivity extends AppCompatActivity {

    private ToggleButton toggleButton1, toggleButton2;
    private Button displayButton;
    private Button editTextButton;
    private EditText inputText;

    public int delayMinutes = 0;
    public int delaySeconds = 0;
    public int delayResults = 0;
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
        addListenerOnSwitch();


        String autoText[] = {"Baidu","Google","Bing","Yahoo","Sogou","Sohu","Sina"};
        AutoCompleteTextView t1 = (AutoCompleteTextView) findViewById(R.id.auto_Complete_TextView);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, autoText);
        t1.setThreshold(1);
        t1.setAdapter(adp);

        //        Integer[] delayOptions = new Integer[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59}


//res.getIntArray(R.array.delaySettingsArray) is the int[] array get from resources but we need to convert it to Integer[]
        Resources res = getResources();
        final   Integer[] delayOptions = new Integer[res.getIntArray(R.array.delaySettingsArray).length];
        for (int i = 0; i<res.getIntArray(R.array.delaySettingsArray).length; i++){
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
                Toast.makeText(MainActivity.this, "Delay Setting for Minutes " + delayMinutes + " Minutes", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Delay Setting for Seconds: " + delaySeconds + " Seconds", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Delay Final Results: " + delayResults + " Seconds in Total", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "Delay Setting for Minutes " + delayMinutes + " Minutes", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Delay Setting for Seconds: " + delaySeconds + " Seconds", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Delay Final Results: " + delayResults + " Seconds in Total", Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }



    protected void onStart() {
        super.onStart();

    }





    public void addListenerOnButton (){
        toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
        toggleButton2 = (ToggleButton) findViewById(R.id.toggleButton2);
        displayButton = (Button) findViewById(R.id.btnDisplay);
        editTextButton = (Button) findViewById(R.id.edit_text_button);
        inputText = (EditText) findViewById(R.id.edittext);

        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer displayMessage = new StringBuffer();
                displayMessage.append("Toggle Button 1: ").append(toggleButton1.getText());
                displayMessage.append("\nToggle Button 2: ").append(toggleButton2.getText());
                int duration = Toast.LENGTH_LONG;
                Toast finalDisplay = Toast.makeText(MainActivity.this, displayMessage, duration);
                finalDisplay.show();
//                Toast.makeText(MainActivity.this, displayMessage, Toast.LENGTH_SHORT).show();
            }
        });

        editTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputString = inputText.getText().toString();
                int duration = Toast.LENGTH_SHORT;
                Toast inputTextDisplay = Toast.makeText(MainActivity.this, inputString, duration);
                inputTextDisplay.show();
            }
        });
    }


    public void addListenerOnRadioButton(){
        RadioGroup radioGroupPi = (RadioGroup) findViewById(radioGroup1);
        radioGroupPi.clearCheck();
        radioGroupPi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                if (null!= rb && checkedId > -1) {
                    Toast.makeText(MainActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

//Missing (View view) will cause crash
    public void clearButton(View view){
        RadioGroup radioGroupPi = (RadioGroup) findViewById(radioGroup1);
        radioGroupPi.clearCheck();
    }


    public void delayShutdownSubmit(View view){
//       Button delayShutdownButton = (Button) findViewById(delayShutdowButtonId);
        Toast.makeText(MainActivity.this, "Delay Setting for Minutes " + delayMinutes + " Minutes", Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity.this, "Delay Setting for Seconds: " + delaySeconds + " Seconds", Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity.this, "Delay Final Results: " + delayResults + " Seconds in Total", Toast.LENGTH_SHORT).show();
    }


    public void addListenerOnSwitch(){
        Switch lampSwitch1 = (Switch) findViewById(R.id.switch_1);
        lampSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(MainActivity.this, R.string.switch_1_On, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, R.string.switch_off, Toast.LENGTH_SHORT).show();
                }
            }
        });

        Switch lampSwitch2 = (Switch) findViewById(R.id.switch_2);
        lampSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(MainActivity.this, R.string.switch_2_on, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, R.string.switch_off, Toast.LENGTH_SHORT).show();
                }
            }
        });


        Switch lampSwitch3 = (Switch) findViewById(R.id.switch_3);
        lampSwitch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(MainActivity.this, R.string.switch_3_on, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, R.string.switch_off, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
