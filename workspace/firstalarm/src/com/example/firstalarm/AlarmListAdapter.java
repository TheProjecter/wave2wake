package com.example.firstalarm;

import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * This class is responsible for building up list view
 *
 */
public class AlarmListAdapter extends ArrayAdapter<Alarm> implements
		OnCheckedChangeListener{

		private Context context;
		private AlarmClockApplication application;
		private static int count = 0;
		private static ArrayList<Integer> ids_to_delete = new ArrayList<Integer>();

		
	public AlarmListAdapter(Application application, Context context) {
		super(context, android.R.layout.simple_list_item_1,((AlarmClockApplication) application).getAlarms());
	    this.application = (AlarmClockApplication) application;
		this.context = context;
		
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		
		final AlarmListItem alarmListItem = new AlarmListItem(context, null);
		View view;				
	
		view = AlarmListItem.inflate(context, R.layout.list_item, alarmListItem); 						
		alarmListItem.setAlarm(application.get(position));
		return view;
	}
	
	public void addAlarm(Alarm alarm) {
		application.addAlarm(alarm);
		this.notifyDataSetChanged();
	}

	public void removeAlarm(int position) {		
		application.deleteAlarm(getItem(position).getId());		
		this.notifyDataSetChanged();
	}
	
	public void deleteSelectedAlarms(){
		for(int i:ids_to_delete){			
			application.deleteAlarm(getItem(i).getId());
		}
		this.notifyDataSetChanged();
	}
	
	public void undoDeleteAlarm() {
		application.undoDeleteLastAlarm();
		this.notifyDataSetChanged();
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		AlarmListItem li = (AlarmListItem) buttonView.getParent();
		Alarm alarm = li.getAlarm();
		alarm.setEnable(isChecked);
		application.saveAlarm(alarm);
	}

	public void saveAlarm(Alarm alarm) {
		application.saveAlarm(alarm);
		this.notifyDataSetChanged();
	}
	
	void setSelectedListItem(int position){
		ids_to_delete.add( new Integer(position));
		count++;		
	}
	
	public int getSelectedCount(){
		return count;
	}
}
