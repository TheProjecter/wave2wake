package com.example.firstalarm;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
/**
 * The list activity having multiple alarms
 *
 */
public class AlarmListActivity extends ListActivity implements UndoBarController.UndoListener {
	
	private ListView alarmList;
	private UndoBarController mUndoBarController;
	private AlarmClockApplication application;
	private static final int REQUEST_ALARM_NEW = 1;
	private static final int REQUEST_ALARM_UPDATE = 2;
	private int lastDeletedPosition;
	private static final int SWIPE_MIN_DISTANCE = 150;
	private static final int SWIPE_MAX_OFF_PATH = 100;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	private GestureDetector gestureScanner;
	
	public int selectedItem = -1;
	private ActionMode mActionMode = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_list);
		
		alarmList = getListView();
		
		alarmList.setAdapter(new AlarmListAdapter(getApplication(), this));
		alarmList.setClickable(true);
		alarmList.setLongClickable(true);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		registerForContextMenu(alarmList);
		
		this.application = (AlarmClockApplication) application;
		mUndoBarController = new UndoBarController(findViewById(R.id.undobar), this);
		
		/**Instantiate Gesture detector**/
		gestureScanner = new GestureDetector(this, simpleOnGestureListener);
		View.OnTouchListener gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureScanner.onTouchEvent(event);
			}
		};
		alarmList.setOnTouchListener(gestureListener);
	}
	/** Action Bar**/
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_alarm, menu);
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	/**Undo Bar, new alarm and settings in action bar**/
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
	    case android.R.id.home: 
	        onBackPressed();
	        break;
		case R.id.menu_add_new_alarm:
			Intent intent = new Intent(this, AlarmAddActivity.class);
			startActivityForResult(intent, REQUEST_ALARM_NEW);
			break;
		case R.id.menu_settings:
			Intent intent1 = new Intent(this, SettingsActivity.class);
			startActivityForResult(intent1, 0);
			break;
		case R.id.menu_action:
			mUndoBarController.showUndoBar(
                    false,
                    getString(R.string.undobar_sample_message),
                    null);
            return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode) {
			return;
		}
		Bundle extras = data.getExtras();
		Alarm alarm = (Alarm) extras.getSerializable("ALARM");
		switch (requestCode) {
		case REQUEST_ALARM_NEW:
			((AlarmListAdapter) alarmList.getAdapter()).addAlarm(alarm);
			break; 
		case REQUEST_ALARM_UPDATE:
			((AlarmListAdapter) alarmList.getAdapter()).saveAlarm(alarm);
			break;
		}
	}
	
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mUndoBarController.onSaveInstanceState(outState);
	}

	protected void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		mUndoBarController.onRestoreInstanceState(state);
	}
	
	MotionEvent mLastOnDownEvent = null;

	GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener(){
		
		public boolean onDown(MotionEvent event) {
			   mLastOnDownEvent = event;
	           return true;
	    }
		/** Swipe feature on List view **/
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if(e1==null){
				e1 = mLastOnDownEvent;
			}
			if (e1 == null || e2 == null) {
				return false;
			}

			lastDeletedPosition = alarmList.pointToPosition((int) e1.getX(),
					(int) e1.getY());
			if (AdapterView.INVALID_POSITION == lastDeletedPosition) {
				return false;
			}

			AlarmListItem item = (AlarmListItem) alarmList
					.getChildAt(lastDeletedPosition);

			if (null == item) {
				return false;
			}

			float dX = e2.getX() - e1.getX();
			float dY = e1.getY() - e2.getY();

			if (Math.abs(dY) < SWIPE_MAX_OFF_PATH
					&& Math.abs(velocityX) >= SWIPE_THRESHOLD_VELOCITY
					&& Math.abs(dX) >= SWIPE_MIN_DISTANCE) {
				Animation anim;
				if (dX > 0) {
					anim = AnimationUtils.makeOutAnimation(item.getContext(), true);
				} else {
					anim = AnimationUtils
							.makeOutAnimation(item.getContext(), false);
				}
				if (AdapterView.INVALID_POSITION == lastDeletedPosition) {
					lastDeletedPosition = AdapterView.INVALID_POSITION;
					anim.setAnimationListener((AnimationListener) anim);
				}
				((AlarmListAdapter) alarmList.getAdapter())
						.removeAlarm(lastDeletedPosition);
				
				mUndoBarController.showUndoBar(false,
						getString(R.string.undo_delete_alarm), null);				
				
				
				return true;
 			}
			return false;

		}
		/** Contextual Action bar **/
		public void onLongPress(MotionEvent e){
			
			selectedItem = alarmList.pointToPosition((int)e.getX(), (int)e.getY());
			
			((AlarmListAdapter)alarmList.getAdapter()).setSelectedListItem(selectedItem);
			
			 // Start the CAB using the ActionMode.Callback defined above
	         mActionMode = AlarmListActivity.this.startActionMode(mCallback);
	  
	    }
		/** To edit a alarm**/
		public boolean onSingleTapUp(MotionEvent e){
			int position = alarmList
					.pointToPosition((int) e.getX(), (int) e.getY());
			if (AdapterView.INVALID_POSITION == position) {
				return false;
			}

			AlarmListItem item = (AlarmListItem) alarmList.getChildAt(position);

			if (null == item) {
				return false;
			}

			Intent intent = new Intent(alarmList.getContext(), AlarmAddActivity.class);
			intent.putExtra("ALARM", item.getAlarm());
			startActivityForResult(intent, REQUEST_ALARM_UPDATE);
			
			return true;
		}
	};
	
	/** To select multiple alarms and delete **/
	private ActionMode.Callback mCallback = new ActionMode.Callback() {
		
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			// remove previous items
            menu.clear();
            final int checked = ((AlarmListAdapter)alarmList.getAdapter()).getSelectedCount();
            // update title with number of checked items
            mode.setTitle(checked + " selected");
           
           // Inflate a menu resource providing context menu items
		    MenuInflater inflater = mode.getMenuInflater();
		      
		    inflater.inflate(R.menu.rowselection, menu);
         
			return true;
		}
		
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
		    selectedItem = -1;
		}
		
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {

			// save global action mode
			selectedItem = -1;
	        mActionMode = mode;
	        return true;
		}
		
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			
			switch (item.getItemId()) {
		      case R.id.menuitem2_delete:
		        show();
		        // Action picked, so close the CAB
		        mode.finish();
		        return true;
		      default:
		        return false;
		      }
		}
	};
	
	/** Confirmation Dialog to delete multiple alarms**/
	private void show() {
		
		final AlertDialog.Builder b = new AlertDialog.Builder(AlarmListActivity.this);
		b.setTitle(getString(R.string.delete_alarm));
		b.setMessage(getString(R.string.delete_alarm_confirm));
		b.setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d,
                                        int w) {
                                	((AlarmListAdapter) alarmList.getAdapter())
        							.deleteSelectedAlarms();
                                }
                            })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
	}
	
	@Override
	public void onUndo(Parcelable token) {

		((AlarmListAdapter) alarmList.getAdapter()).undoDeleteAlarm();
	}

	}

