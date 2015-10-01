package com.example.igulhane73.appnew;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Switch;

public class DrivingActivity extends AppCompatActivity {

    int speed = 35;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving);
        Switch toggle = (Switch) findViewById(R.id.mode_on_off);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //ConfigDatabaseOperations cdp = new ConfigDatabaseOperations(mContext);
                // ContentValues cv = new ContentValues();
                if (isChecked) {
                    //   eventList.get(getPosition()).setActive(true);
                    // cv.put(ConfigTableData.TimeConfigTableInfo.active, true);

                } else {
                    //updateUsingId(false, eventList.get(getPosition()));
                    //eventList.get(getPosition()).setActive(false);
                    //cv.put(ConfigTableData.TimeConfigTableInfo.active, false);

                }
                //cdp.updateUserData(cv, ConfigTableData.TimeConfigTableInfo.id + "  = " + eventList.get(getPosition()).getId(), null, null);
                //updateUsingId(true, eventList.get(getPosition()));
            }
        });
        NumberPicker tens = (NumberPicker) findViewById(R.id.speed_tens);
        tens.setMinValue(0);
        tens.setMaxValue(99);
        tens.setValue(35);

        tens.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                speed = newVal;
            }
        });

        final ImageButton imageButton = (ImageButton) findViewById(R.id.driving_mode);
        imageButton.setImageResource(R.drawable.ic_star48dp);
        imageButton.setTag("Priority");
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageButton.getTag().toString().equals("All")) {
                    imageButton.setImageResource(R.drawable.ic_star48dp);
                    imageButton.setTag("Priority");
                } else if (imageButton.getTag().toString().equals("Priority")) {
                    imageButton.setImageResource(R.drawable.ic_remove_circle_48dp);
                    imageButton.setTag("None");
                } else if (imageButton.getTag().toString().equals("None")) {
                    imageButton.setImageResource(R.drawable.ic_audio_track48dp);
                    imageButton.setTag("All");
                }
            }

        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_driving, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
