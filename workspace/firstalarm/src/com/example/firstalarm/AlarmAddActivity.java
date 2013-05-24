package com.example.firstalarm;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TimePicker;
import android.widget.ToggleButton;
/**
 * Activity to add and edit an alarm on list of alarms
 */
public class AlarmAddActivity extends Activity implements OnClickListener, OnCheckedChangeListener {

	private static int[] daysIDList = new int[] { R.id.sun_tgl, R.id.mon_tgl,
		R.id.tue_tgl, R.id.wed_tgl, R.id.thu_tgl, R.id.fri_tgl,
		R.id.sat_tgl };
	private static String[] weekDays;
	private TimePicker timePicker;
	static {
		ArrayList<String> daysFormated = new ArrayList<String>();
		for (String shortDay : DateFormatSymbols.getInstance(
				Locale.getDefault()).getShortWeekdays()) {
			// remove first blank element
		if (0 == shortDay.length()) {
			continue;
		}
		// make sure strings are capitalized and don't contain
		// extra punctuation 
		daysFormated.add(Character.toUpperCase(shortDay.charAt(0))
				+ shortDay.substring(1, shortDay.length())
						.replace('.', ' ').trim());
		}
		weekDays = daysFormated.toArray(new String[daysFormated.size()]);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_alarm);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// ok button 
		Button add = (Button) findViewById(R.id.add_button);
		
		// cancel 
		Button cancel = (Button) findViewById(R.id.cancel_button);
		
		// repeat 
		CheckBox repeat = (CheckBox) findViewById(R.id.repeat_chk);
		
		// time picker
		timePicker = (TimePicker) findViewById(R.id.timePicker);
		
		repeat.setOnCheckedChangeListener(this);
		add.setOnClickListener(this);
		cancel.setOnClickListener(this);
		handleIntent(getIntent());
			
		//repeat day buttons
		setUpToggles();
	}
	
	private void setUpToggles() {
		ToggleButton toggle;
		for (int i = 0; i < daysIDList.length; i++) {
			toggle = (ToggleButton) findViewById(daysIDList[i]);
			toggle.setText(weekDays[i]);
			toggle.setTextOn(weekDays[i]);
			toggle.setTextOff(weekDays[i]);
		}
	}
	/**
	 * Set any Alarm on the list of multiple alarms 
	 * @param alarm
	 */
	private void setAlarm(Alarm alarm){
		int hour = Integer.valueOf(alarm.getTime().split(":")[0]);
		int minute = Integer.valueOf(alarm.getTime().split(":")[1]);
		
		//Build Calendar objects for setting alarm
		Calendar curCalendar = Calendar.getInstance();
		Calendar alarmCalendar = Calendar.getInstance();
		
		//Initialize Seconds and Milliseconds to 0 for both calendars
		curCalendar.set(Calendar.SECOND, 0);
		curCalendar.set(Calendar.MILLISECOND, 0);    		
		alarmCalendar.set(Calendar.SECOND, 0);
		alarmCalendar.set(Calendar.MILLISECOND, 0);
	
		//Update alarmCalendar with Alarm Hour and Minute Settings
		alarmCalendar.set(Calendar.HOUR_OF_DAY, hour);
		alarmCalendar.set(Calendar.MINUTE, minute);
	
		//If Alarm Time is now or in the past, set it for tomorrow 24 hours in advance from time selected
		if (alarmCalendar.getTimeInMillis() <= curCalendar.getTimeInMillis()) {
			alarmCalendar.add(Calendar.HOUR, 24);
		}

		//Set the alarm
		//Build Intent/Pending Intent for setting the alarm
		Intent AlarmIntent = new Intent(this, AlarmReceiver.class);
		AlarmManager AlmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);
		PendingIntent Sender = PendingIntent.getBroadcast(this, 0, AlarmIntent, 0);    	
		AlmMgr.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), Sender);
		
		
		//Build the Strings for displaying the alarm time through Toast
		String CalendarHourStr;
		if (hour > 12) {
			CalendarHourStr = Integer.toString(hour - 12);
		} else {
			CalendarHourStr = Integer.toString(hour);
		}
		String CalendarMinStr = Integer.toString(minute);
		if (minute < 10) {
			CalendarMinStr = "0" + CalendarMinStr;
		}
		
		String strAmPM;
		if (hour < 12) {
			strAmPM = "AM";
		}
		else {
			strAmPM = "PM";
		}
	    Toast.makeText(this, "Alarm Set For " + Integer.toString(alarmCalendar.get(Calendar.MONTH) + 1) + "/" + Integer.toString(alarmCalendar.get(Calendar.DAY_OF_MONTH)) + "/" + Integer.toString(alarmCalendar.get(Calendar.YEAR)) + " " + CalendarHourStr + ":" + CalendarMinStr + " " + strAmPM, Toast.LENGTH_LONG).show();    	
	}
	
	private void handleIntent(Intent intent) {
	
		if (null == intent) {
			return;
		}
	
		Alarm alarm = (Alarm) intent.getSerializableExtra("ALARM");
		if (null == alarm) {
			return;
		}
		
		int hour = Integer.valueOf(alarm.getTime().split(":")[0]);
		int minutes = Integer.valueOf(alarm.getTime().split(":")[1]);
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minutes);
	
		CheckBox repeat = (CheckBox) findViewById(R.id.repeat_chk);
		boolean[] repeatOn = alarm.getRepeatOn();
	
		if (null == repeatOn) {
			repeat.setChecked(false);
			return;
		}
	
		ToggleButton toggle;
		boolean isRepeating = false;
	
		for (int i = 0; i < daysIDList.length; i++) {
			toggle = (ToggleButton) findViewById(daysIDList[i]);
			toggle.setChecked(repeatOn[i]);
			isRepeating = isRepeating | repeatOn[i];
			toggle.setText(weekDays[i]);
			toggle.setTextOn(weekDays[i]);
			toggle.setTextOff(weekDays[i]);
		}
		repeat.setChecked(isRepeating);
	}
	/**
	 * Button for Ok and Cancel for newly added alarm
	 */
	public void onClick(View v) {
		Intent intent = getIntent();
		switch (v.getId()) {
		case R.id.add_button:
			Alarm alarm = (Alarm) intent.getSerializableExtra("ALARM");
			if (null == alarm) {
				// new alarm
				alarm = new Alarm();
			}
			alarm.setTime(getTime());
			alarm.setRepeatOn(getRepeatOn());
			alarm.setEnable(getEnableCheckbox());
			if(alarm.isEnable()){
				setAlarm(alarm);
			}
			intent.putExtra("ALARM", alarm);
			setResult(RESULT_OK, intent);
			break;
		case R.id.cancel_button:
			setResult(RESULT_CANCELED, intent);
			break;
		}
		finish();
	}
	
	public boolean getEnableCheckbox(){
		CheckBox enable = (CheckBox) findViewById(R.id.enable);
		return enable.isChecked();
	} 
	
	public String getTime() {
		StringBuilder sb = new StringBuilder();
		if (timePicker.getCurrentHour() < 10) {
			//sb.append("0");
		}
		sb.append(timePicker.getCurrentHour());
		sb.append(":");
		if (timePicker.getCurrentMinute() < 10) {
			sb.append("0");
		}
		sb.append(timePicker.getCurrentMinute());
		return sb.toString();
	}
	
	public boolean[] getRepeatOn() {
		boolean[] repeatOn = null;
		CheckBox repeat = (CheckBox) findViewById(R.id.repeat_chk);
		if (repeat.isChecked()) {
			repeatOn = new boolean[7];
			ToggleButton toggle;
			for (int i = 0; i < daysIDList.length; i++) {
				toggle = (ToggleButton) findViewById(daysIDList[i]);
				repeatOn[i] = toggle.isChecked();
			}
		}
		return repeatOn;
	}
	
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		ToggleButton toggle;
		for (int i = 0; i < daysIDList.length; i++) {
			toggle = (ToggleButton) findViewById(daysIDList[i]);
			toggle.setEnabled(isChecked);
		}
	
	}
	/**
	 * Action Bar with settings and Home on back pressed.
	 */
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    switch (item.getItemId()) 
	    {
	    case android.R.id.home: 
	        onBackPressed();
	        break;
	    case R.id.menu_settings:
			Intent intent1 = new Intent(this, SettingsActivity.class);
			startActivityForResult(intent1, 0);
			break;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	    return true;
	}
}
