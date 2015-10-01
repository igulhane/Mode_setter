package com.example.igulhane73.appnew;

import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.igulhane73.appnew.dbOps.ConfigDatabaseOperations;
import com.example.igulhane73.appnew.info.ConfigTableData;
import com.example.igulhane73.appnew.utils.AddingPDS;

import java.util.Collections;
import java.util.List;

/**
 * Created by igulhane73 on 8/6/15.
 */
public class EventViewAdapter extends RecyclerView.Adapter<EventViewAdapter.EventViewHolder> {
    private Context mContext;
    String location = "";
    private LayoutInflater inflator;
    List<UserEvent> eventList = Collections.emptyList();
    public EventViewAdapter(Context context){
        mContext = context;
    }
    public void delete(int position) {
        int id = eventList.get(position).getId();
        ConfigDatabaseOperations cdo = new ConfigDatabaseOperations(mContext);
        cdo.deleteUserData(cdo.getWritableDatabase() , ConfigTableData.TimeConfigTableInfo.id + " = " + id , null );
        eventList.remove(position);
        notifyItemRemoved(position);
    }

    public EventViewAdapter(Context context, List<UserEvent> list) {
        inflator = LayoutInflater.from(context);
        eventList = list;
        mContext = context;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row, null);
        EventViewHolder eventViewHolder = new EventViewHolder(view);
        return eventViewHolder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        UserEvent event = eventList.get(position);
        holder.textView.setText(event.getTitle());
        holder.start_time.setText(event.getStart_time());
        holder.end_time.setText(event.getEnd_time());
        holder.days_set[0]=event.getSun()==1?true:false;
        holder.days_set[1]=event.getMon()==1?true:false;
        holder.days_set[2]=event.getTue()==1?true:false;
        holder.days_set[3]=event.getWed()==1?true:false;
        holder.days_set[4]=event.getThur()==1?true:false;
        holder.days_set[5]=event.getFri()==1?true:false;
        holder.days_set[6]=event.getSat()==1?true:false;
        for (int i = 0; i < 7; i++) {
            holder.temp[i] = holder.days_set[i];
        }
        holder.toggle.setChecked(event.isActive());
        location = event.getLocation();
        String s=event.mode;
        if(event.mode.equals("Sound")){
            holder.modebutton.setImageResource(R.drawable.ic_audio_track);
            holder.modebutton.setTag("Sound");
        }else if(event.mode.equals("Vibrate")){
            holder.modebutton.setImageResource(R.drawable.ic_star);
            holder.modebutton.setTag("Vibrate");
        }else if(event.mode.equals("Silent")){
            holder.modebutton.setImageResource(R.drawable.ic_remove_circle);
            holder.modebutton.setTag("Silent");
        }

    }

    public int getColor(int i){
        if(i==1){
            return Color.RED;
        }
        return Color.BLACK;
    }

    @Override
    public int getItemCount() {
        return (null != eventList ? eventList.size() : 0);
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        EditText textView;
        TextView start_time;
        TextView end_time;
        Switch toggle;
        ImageButton imageButton;
        ImageButton modebutton;
        ImageButton weeklySchedule;
        ImageButton map;
        ImageButton event_location_detail;
        boolean days_set[] = new boolean[7];
        boolean temp[] = new boolean[7];

        public EventViewHolder(final View itemView) {
            super(itemView);
            textView = (EditText) itemView.findViewById(R.id.event_name);
            String text=textView.getText().toString();
            textView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    textView.setFocusable(true);
                    Drawable dbl = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.done, null);
                    if (event.getRawX() >= (textView.getRight() - dbl.getIntrinsicWidth())) {
                        //textView.setFocusable(true);
                        textView.setCompoundDrawables(null, null, null, null);
                        textView.setFocusable(false);
                        //textView.clearFocus();
                        textView.setFocusableInTouchMode(false);
                        ConfigDatabaseOperations cdp = new ConfigDatabaseOperations(mContext);
                        ContentValues cv = new ContentValues();
                        cv.put(ConfigTableData.TimeConfigTableInfo.name, textView.getText().toString());
                        updateTable(cdp,cv, eventList.get(getPosition()).getId(),eventList.get(getPosition()));
                    } else {
                        int h = dbl.getIntrinsicHeight();
                        int w = dbl.getIntrinsicWidth();
                        dbl.setBounds(0, 0, w, h);
                        textView.setCompoundDrawables(null, null, dbl, null);
                        textView.setFocusable(true);
                        //textView.clearFocus();
                        textView.setFocusableInTouchMode(true);

                    }
                    return false;
                }
            });

            start_time = (TextView) itemView.findViewById(R.id.start_time);
            start_time.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int hour = Integer.parseInt(start_time.getText().toString().split(":")[0]) , minutes = Integer.parseInt(start_time.getText().toString().split(":")[1].split(" ")[0]);
                    if (start_time.getText().toString().split(":")[1].split(" ")[1].trim().equals("PM")){
                        hour = hour + 12;
                    }
                    TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hour, int minutes) {
                            String time = getTime(hour, minutes);
                            if (start_time.getText() != time) {
                                start_time.setText(time);
                                //Update query here
                                ConfigDatabaseOperations cdp = new ConfigDatabaseOperations(view.getContext());
                                ContentValues cv = new ContentValues();
                                cv.put(ConfigTableData.TimeConfigTableInfo.time , time);
                                updateTable(cdp,cv, eventList.get(getPosition()).getId(), eventList.get(getPosition()));
                            }

                        }
                    }, hour, minutes, false);
                    timePickerDialog.show();

                }
            });

            end_time = (TextView) itemView.findViewById(R.id.end_time);
            end_time.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int hour = Integer.parseInt(end_time.getText().toString().split(":")[0]);
                    int minutes = Integer.parseInt(end_time.getText().toString().split(":")[1].split(" ")[0]);
                    if (end_time.getText().toString().split(":")[1].split(" ")[1].trim().equals("PM")){
                        hour = hour + 12;
                    }
                    TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hour, int minutes) {
                            String time = getTime(hour, minutes);
                            if (end_time.getText() != time) {
                                end_time.setText(time);
                                //Update query here
                                ConfigDatabaseOperations cdp = new ConfigDatabaseOperations(mContext);
                                ContentValues cv = new ContentValues();
                                cv.put(ConfigTableData.TimeConfigTableInfo.Etime, time);
                                updateTable(cdp,cv, eventList.get(getPosition()).getId(), eventList.get(getPosition()));
                            }
                        }
                    }, hour, minutes, false);
                    timePickerDialog.show();
                }
            });

            modebutton = (ImageButton) itemView.findViewById(R.id.mode);
            modebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(modebutton.getTag().toString().equals("Sound")){
                        modebutton.setImageResource(R.drawable.ic_star);
                        modebutton.setTag("Vibrate");
                    } else if (modebutton.getTag().toString().equals("Vibrate")) {
                        modebutton.setImageResource(R.drawable.ic_remove_circle);
                        modebutton.setTag("Silent");
                    } else if (modebutton.getTag().toString().equals("Silent")) {
                        modebutton.setImageResource(R.drawable.ic_audio_track);
                        modebutton.setTag("Sound");
                    }
                    ConfigDatabaseOperations cdp = new ConfigDatabaseOperations(mContext);
                    ContentValues cv = new ContentValues();
                    cv.put(ConfigTableData.TimeConfigTableInfo.mode , modebutton.getTag().toString());
                    updateTable(cdp, cv, eventList.get(getPosition()).getId(), eventList.get(getPosition()));
                }
            });

            imageButton = (ImageButton) itemView.findViewById(R.id.delete);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(imageButton.getContext());
                    builder.setTitle("Delete Event");
                    builder.setMessage("Do you want to remove event ?");
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateUsingId(eventList.get(getPosition()));
                            delete(getPosition());
                        }
                    });
                    builder.show();

                }
            });
            final CharSequence[] items = {" Sunday "," Monday "," Tuesday "," Wednesday " ," Thursday ",
            " Friday ", " Saturday "};
            for (int i=0;i<7;i++){
                temp[i]=days_set[i];
            }
            weeklySchedule = (ImageButton) itemView.findViewById(R.id.week_schedule);
            weeklySchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(weeklySchedule.getContext());
                    builder.setTitle("Current Schedule");
                    builder.setMultiChoiceItems(items, days_set, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if (isChecked) {
                              days_set[which] = true;
                            } else {
                                days_set[which] = false;
                            }
                        }
                    });

                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i < 7; i++) {
                                days_set[i] = temp[i];
                            }
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i=0;i<7;i++){
                                temp[i]=days_set[i];
                            }
                            ConfigDatabaseOperations cdp = new ConfigDatabaseOperations(mContext);
                            ContentValues cv = new ContentValues();
                            cv.put(ConfigTableData.TimeConfigTableInfo.SunDay , days_set[0]==true?""+1:""+0);
                            cv.put(ConfigTableData.TimeConfigTableInfo.MonDay , days_set[1]==true?""+1:""+0);
                            cv.put(ConfigTableData.TimeConfigTableInfo.TuesDay , days_set[2]==true?""+1:""+0);
                            cv.put(ConfigTableData.TimeConfigTableInfo.WednesDay , days_set[3]==true?""+1:""+0);
                            cv.put(ConfigTableData.TimeConfigTableInfo.ThursDay , days_set[4]==true?""+1:""+0);
                            cv.put(ConfigTableData.TimeConfigTableInfo.FriDay , days_set[5]==true?""+1:""+0);
                            cv.put(ConfigTableData.TimeConfigTableInfo.SaturDay, days_set[6] == true?""+1:""+ 0);
                            updateTable(cdp, cv, eventList.get(getPosition()).getId(), eventList.get(getPosition()));

                        }
                    });
                    builder.show();
                }
            });

            toggle = (Switch) itemView.findViewById(R.id.event_switch);
            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ConfigDatabaseOperations cdp = new ConfigDatabaseOperations(mContext);
                    ContentValues cv = new ContentValues();
                    if (isChecked) {
                        eventList.get(getPosition()).setActive(true);
                        cv.put(ConfigTableData.TimeConfigTableInfo.active, true);

                    } else {
                        updateUsingId(eventList.get(getPosition()));
                        eventList.get(getPosition()).setActive(false);
                        cv.put(ConfigTableData.TimeConfigTableInfo.active, false);

                    }
                    updateTable(cdp,cv, eventList.get(getPosition()).getId(),eventList.get(getPosition()));
                }
            });
        }

        public void updateTable(ConfigDatabaseOperations cdp, ContentValues cv,int id, UserEvent position){
            cdp.updateUserData(cv, ConfigTableData.TimeConfigTableInfo.id + "  = " + id, null, null);
            updateUsingId(position);
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

    }



    public void addEvent(UserEvent event) {
        eventList.add(event);
        notifyDataSetChanged();
    }
    public void updateUsingId( UserEvent ue){
            PendingIntent pd = AddingPDS.get_PendingIntent(mContext,
            ue.getTitle(),2,ue.getId() , mContext.getString(R.string.startMode),
            "-", ue.getMode(),1 , PendingIntent.FLAG_UPDATE_CURRENT);
            pd.cancel();
            pd = AddingPDS.get_PendingIntent(mContext,
                    ue.getTitle(),3,ue.getId() , mContext.getString(R.string.startMode),
                    "-", ue.getMode(),2 , PendingIntent.FLAG_UPDATE_CURRENT);
            pd.cancel();
            pd = AddingPDS.get_PendingIntent(mContext,
                    ue.getTitle(),4,ue.getId() , mContext.getString(R.string.stopMode),
                    "+", ue.getMode(),1 , PendingIntent.FLAG_UPDATE_CURRENT);
            pd.cancel();
            pd = AddingPDS.get_PendingIntent(mContext,
                    ue.getTitle(),5,ue.getId() , mContext.getString(R.string.stopMode),
                    "+", ue.getMode(),2 , PendingIntent.FLAG_UPDATE_CURRENT);
            pd.cancel();
            AddingPDS.updateAlarmById(ue.getId(), mContext);
   }

}
