package com.example.igulhane73.appnew;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.igulhane73.appnew.dbOps.ConfigDatabaseOperations;

/**
 * Created by igulhane73 on 8/8/15.
 */
public class AddDialog extends ActionBarActivity {
    private int[] weekday = new int[7];
    int sun;
    int mon;
    int tue;
    int wed;
    int thur;
    int fri;
    int sat;
    String start_time="00:00 AM";
    String end_time="00:00 AM";
    private String mode="All";
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_element);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Spinner spinner = (Spinner) findViewById(R.id.event_mode);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.event_mode, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final TextView start = (TextView) findViewById(R.id.start_time);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = Integer.parseInt(start.getText().toString().split(":")[0]) , minutes = Integer.parseInt(start.getText().toString().split(":")[1].split(" ")[0]);
                if (start.getText().toString().split(":")[1].split(" ")[1].trim().equals("PM")){
                    hour = hour + 12;
                }
                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minutes) {
                        start_time=getTime(hour,minutes);
                        start.setText(start_time);
                    }
                } ,  hour, minutes, false);
                timePickerDialog.show();

            }
        });

        final TextView end = (TextView) findViewById(R.id.end_time);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = Integer.parseInt(end.getText().toString().split(":")[0]) , minutes = Integer.parseInt(end.getText().toString().split(":")[1].split(" ")[0]);
                if (end.getText().toString().split(":")[1].split(" ")[1].trim().equals("PM")){
                    hour = hour + 12;
                }
                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minutes) {
                        end_time=getTime(hour,minutes);
                        end.setText(end_time);
                    }
                } ,  hour, minutes, false);
                timePickerDialog.show();

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mode=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final Button save_button = (Button) findViewById(R.id.save);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfigDatabaseOperations cdo = new ConfigDatabaseOperations(getApplicationContext());
                int highestId = cdo.getHighestId(cdo.getWritableDatabase());
                highestId++;
                Log.d(" Id 0" , highestId + " ");

                EditText etName = (EditText) findViewById(R.id.event_id);
                EditText location = (EditText) findViewById(R.id.event_location);
                Intent data = new Intent();
                UserEvent userEvent = new UserEvent();
                userEvent.setTitle(etName.getText().toString());
                userEvent.setStart_time(start_time);
                userEvent.setEnd_time(end_time);
                userEvent.setSun(sun);
                userEvent.setMon(mon);
                userEvent.setTue(tue);
                userEvent.setWed(wed);
                userEvent.setThur(thur);
                userEvent.setFri(fri);
                userEvent.setSat(sat);
                userEvent.setMode(mode);
                userEvent.setLocation(location.getText().toString());
                userEvent.setActive(true);
                userEvent.setId(highestId);
                String chk=getValidation(userEvent);
                if(chk.equals("valid")) {
                    data.putExtra("event", userEvent);
                    setResult(RESULT_OK, data);
                    cdo.addNewTimeConfig(cdo ,highestId , start_time,end_time , mode ,
                            "" + sun ,"" + mon ,"" + tue , "" + wed ,
                            "" + thur , "" + fri , "" + sat , etName.getText().toString() , true);
                    finish();
                }else{
                    Toast toast = Toast.makeText(v.getContext(),chk,Toast.LENGTH_SHORT);
                    toast.show();
                }



            }
        });
        Button cancel_button = (Button) findViewById(R.id.cancel);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent data = new Intent();

                setResult(RESULT_CANCELED, data);
                finish();
            }
        });
    }

    public String getTime(int hour,int minute){
        String am_pm = "AM";
        if (hour > 12) {
            am_pm = "PM";
            hour = hour % 12;
        }
        StringBuffer time= new StringBuffer();
        if(hour<10){
            time.append("0"+hour+":");
        }else{
            time.append(hour+":");
        }

        if(minute<10){
            time.append("0"+minute+" ");
        }else{
            time.append(minute+" ");
        }
        time.append(am_pm);
        return time.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub, menu);
        return true;
    }

    public void selectDeselect(View view) {
        TextView textView = (TextView) findViewById(view.getId());
        int position = 0;
        int i = textView.getCurrentTextColor();
        if (i == Color.BLACK) {
            textView.setTextColor(Color.RED);
            position = 1;
        } else {
            textView.setTextColor(Color.BLACK);
            position = 0;
        }
        switch (view.getId()) {
            case R.id.Sunday:
                sun=position;
                break;
            case R.id.Monday:
                mon = position;
                break;
            case R.id.Tuesday:
                tue = position;
                break;
            case R.id.Wednesday:
                wed=position;
                break;
            case R.id.Thursday:
                thur=position;
                break;
            case R.id.Friday:
                fri=position;
                break;
            case R.id.Saturday:
                sat=position;
                break;


        }


    }


    public String getValidation(UserEvent event){
        if(event.getTitle()==null||event.getTitle().equals("")){
            return "Event Name Missing..!!";
        }
        if(event.getSun()==0 && event.getMon()==0 && event.getTue()==0 &&
                event.getWed()==0 && event.getThur()==0 && event.getFri()==0
                && event.getSat()==0){
            return "Select Event Day..!!";
        }
        return "valid";
    }

}
