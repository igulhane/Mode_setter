package com.example.igulhane73.appnew;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.igulhane73.appnew.Services.EveryDayService;
import com.example.igulhane73.appnew.dbOps.ConfigDatabaseOperations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class EventActivity extends AppCompatActivity {
    Toolbar toolbar;
    private RecyclerView recyclerView;
    private final int REQUEST_CODE = 20;
    EventViewAdapter eventViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ensuring all database exists or created
        ConfigDatabaseOperations cdo =  new ConfigDatabaseOperations(getApplicationContext());
        //cdo.dropDB(cdo);
        cdo.createTables(cdo);
        //created alarm manager to check if the everyday loading pending intent is added
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent regAlaram = new Intent(this, EveryDayService.class);

        // regAlaram.put
        // this is for the system to be able to distinguish pending intents
        regAlaram.setAction("AddAlarmIntents");
        regAlaram.setType("RepeatingAlarm");
        //checking if alarmmanager pending intent already there
        PendingIntent pi = PendingIntent.getService(this, 1, regAlaram, PendingIntent.FLAG_NO_CREATE);
        //if not then create one
        //and also start the service for now
        startService(regAlaram);
        if (pi == null) {

            pi = PendingIntent.getService(this, 1, regAlaram, PendingIntent.FLAG_UPDATE_CURRENT);
            Calendar cl = Calendar.getInstance();
            cl.set(Calendar.HOUR_OF_DAY, 0);
            cl.set(Calendar.MINUTE, 0);
            cl.set(Calendar.SECOND, 0);
           am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, cl.getTimeInMillis(), 24 * 60 * 60 * 1000, pi);
        }
        setContentView(R.layout.activity_main);
        //toolbar = (Toolbar) findViewById(R.id.app_bar);
        //setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.event_list);
        eventViewAdapter= new EventViewAdapter(this,getData(this.getApplicationContext()));
        recyclerView.setAdapter(eventViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button button = (Button) findViewById(R.id.add_event);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventActivity.this, AddDialog.class);
                i.putExtra("mode", 2); // pass arbitrary data to launched activity
                startActivityForResult(i, REQUEST_CODE);

            }
        });
    }


    public static List<UserEvent> getData(Context context){
        List<UserEvent> result= new ArrayList<UserEvent>();
        ConfigDatabaseOperations cdo = new ConfigDatabaseOperations(context);
        Cursor cur = cdo.retrieveNewTimeConfig(cdo.getReadableDatabase() , null , null);
        if(cur.moveToFirst()) {
            do {
                UserEvent event= new UserEvent();
                //id INTEGER,time TEXT,Etime TEXT ,mode TEXT,
                //      Sunday TEXT,Monday TEXT,Tuesday TEXT,
                //    Wednesday TEXT,Thursday TEXT,Friday TEXT,Saturday TEXT,Name TEXT,active BOOLEAN
                event.setId(cur.getInt(0));
                event.setStart_time(cur.getString(1));
                event.setEnd_time(cur.getString(2));
                Log.d("Mode in database ", cur.getString(3));
                event.setMode(cur.getString(3));
                event.setSun(Integer.parseInt(cur.getString(4)));
                event.setMon(Integer.parseInt(cur.getString(5)));
                event.setTue(Integer.parseInt(cur.getString(6)));
                event.setWed(Integer.parseInt(cur.getString(7)));
                event.setThur(Integer.parseInt(cur.getString(8)));
                event.setFri(Integer.parseInt(cur.getString(9)));
                event.setSat(Integer.parseInt(cur.getString(10)));
                event.setTitle(cur.getString(11));
                event.setActive((cur.getInt(12)>0));
                result.add(event);

            } while (cur.moveToNext());
        }
      /*  String[] op={"a","b","c","d","e","f"};
        for (int i = 0; i < op.length; i++) {
            UserEvent event= new UserEvent();
            event.title=op[i];
            result.add(event);
        }*/
        return result;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Toast.makeText(this, "Selected " + item.getTitle() + " Option", Toast.LENGTH_SHORT);
            return true;
        }
        /*if (id == R.id.navigate) {
            startActivity(new Intent(this, EventActivity.class));
        }*/
        return super.onOptionsItemSelected(item);
    }


    public void addEvent(UserEvent event) {
        eventViewAdapter.addEvent(event);
    }


    public void deleteEvent() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract Event value from result extras
            UserEvent event = (UserEvent) data.getExtras().getSerializable("event");
            addEvent(event);

        }
    }

}
