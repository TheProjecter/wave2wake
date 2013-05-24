package com.example.firstalarm;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/** Class with functions for all list items**/
public class AlarmListItem extends RelativeLayout{

	private TextView repeatText;
	private TextView alarmTime;
	private EditText text;
	private Alarm alarm;
	private boolean hasFoundViews;
	private ListView alarmList;
	private RelativeLayout rlayout;
	private boolean layoutVisible = false;
	private CheckBox repeat;
	private CheckBox editRepeat;
	private CheckBox enable;
	private CheckBox editEnable;
	private ImageButton delete;

	private static String[] weekDays;
	
	private int lastDeletedPosition;
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
	
	
	public AlarmListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		hasFoundViews = false;
	}
            
	private void findViews() {
		
		if (!hasFoundViews)
		{
			alarmTime = (TextView) findViewById(R.id.list_item_text);
			repeatText = (TextView) findViewById(R.id.alarm_repeat);
			setRepeat((CheckBox)findViewById(R.id.repeat_chk));
			enable = (CheckBox)findViewById(R.id.enable);
		}
	}

	public void setAlarm(Alarm alarm) {

		findViews();
		this.alarm = alarm;
		alarmTime.setText(alarm.getTime());
		repeatText.setText(alarm.getRepeatString());
		enable.setChecked(alarm.isEnable());
	}

	public Alarm getAlarm() {
		return alarm;
	}

	private boolean getEditEnableOn(){
		CheckBox enable = (CheckBox) findViewById(R.id.enable);
		return enable.isChecked();
	}
	

	public CheckBox getRepeat() {
		return repeat;
	}
	
	public void setRepeat(CheckBox repeat) {
		this.repeat = repeat;
	}
	
	public CheckBox getEnable() {
		return enable;
	}
	
	public void setEnable(CheckBox enable) {
		this.enable = enable;
	}
	
	public ImageButton getDelete() {
		return delete;
	}
	
	public void setDelete(ImageButton delete) {
		this.delete = delete;
	}

}

