package com.example.igulhane73.appnew;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.igulhane73.appnew.dbOps.ConfigDatabaseOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igulhane73 on 8/22/15.
 */
public class EventFragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private RecyclerView recyclerView;
    EventViewAdapter eventViewAdapter;
    private final int REQUEST_CODE = 20;

    @Override
    public void onResume() {
        super.onResume();
        updateGUI();
    }


    public void updateGUI() {
       FetchEvents fetchEvents= new FetchEvents();
        fetchEvents.execute();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateGUI();
        setRetainInstance(true);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // ensuring all database exists or created

        View rootView = inflater.inflate(R.layout.event_list_data, container, false);
        rootView.setTag(TAG);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.event_list);
        eventViewAdapter = new EventViewAdapter(getActivity(), getData(getActivity()));
        recyclerView.setAdapter(eventViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Button button = (Button) rootView.findViewById(R.id.add_event);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddDialog.class);
                i.putExtra("mode", 2); // pass arbitrary data to launched activity
                startActivityForResult(i, REQUEST_CODE);

            }
        });

        return rootView;
    }


    public List<UserEvent> getData(Context context) {
        List<UserEvent> result = new ArrayList<UserEvent>();
        ConfigDatabaseOperations cdo = new ConfigDatabaseOperations(context);
        Cursor cur = cdo.retrieveNewTimeConfig(cdo.getReadableDatabase(), null, null);
        if (cur.moveToFirst()) {
            do {
                UserEvent event = new UserEvent();
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
                event.setActive((cur.getInt(12) > 0));
                result.add(event);

            } while (cur.moveToNext());
        }

        return result;
    }

    public void addEvent(UserEvent event) {
        eventViewAdapter.addEvent(event);
    }

    public class FetchEvents extends AsyncTask<Void, Void, EventViewAdapter> {

        @Override
        protected EventViewAdapter doInBackground(Void... params) {
            List<UserEvent> result = new ArrayList<UserEvent>();
            ConfigDatabaseOperations cdo = new ConfigDatabaseOperations(getActivity());
            Cursor cur = cdo.retrieveNewTimeConfig(cdo.getReadableDatabase(), null, null);
            if (cur.moveToFirst()) {
                do {
                    UserEvent event = new UserEvent();
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
                    event.setActive((cur.getInt(12) > 0));
                    result.add(event);

                } while (cur.moveToNext());
            }
            EventViewAdapter eventViewAdapter = new EventViewAdapter(getActivity(), result);
            return eventViewAdapter;
        }

        @Override
        protected void onPostExecute(EventViewAdapter result) {
            if (result != null) {
                eventViewAdapter = result;
            }

        }
    }

}