package com.example.firstalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/** Class for receiving broadcast when the Alarm occurs */
public class AlarmReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) { 
	    	
	     //Create Intent to Start the AlarmActivity "Snooze" Activity
	      Intent myIntent = new Intent(context, AlarmActivity.class);
		  myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		  context.startActivity(myIntent);  
		}
}

