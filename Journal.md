This is the journal page for wave2wake to record weekly progress

Introduction
I will be adding journal entries here weekly about:

The progress I've made (once you start coding, mention the revisions for that week).
The problems I have encountered.
Lessons learned.
What is planned for the following week.

# Details #

May 9, 2013

**Progress**

1. Fixed the undo implementation.

2. Worked on contextual action bar, select items on list to update action bar to perform desired action.

**Problem**

OnItemClicklistener not working on listview, maybe due to implementation of gesture listener. Spent a lot of time to fix it. No success.

May 9, 2013

**Progress**

1. Implemented the action bar to replace buttons for navigation through different activities and back to home. Changed the theme to Holo.

2. Implemented on LongPress

3. Worked on undo feature for deleted alarms from list.

4. Worked on fragment.

**Learning**

ifRoom|withText used in action bar to adjust items in action bar. Main activity to be declared as parent activity in manifest to be declared as home so that activities return on pressing back. splitActionBarWhenNarrow to be used to adjust large number of items to go to bottom.

**Planned**

1. Thinking of a feature to share on facebook. When alarm rings, pop up quote for the day with an option to post on facebook wall.

May 2, 2013

**Progress**

1. Implemented the fling gesture to delete the alarm from the list.

2. Implemented the Single Tap gesture on the list item to edit.

3. Handled settings: Alarm ringing on/off when phone is silent, Alarm Volume and snooze duration.

**Problems**

1. While testing swipe/fling gesture, first param of onFling happened to be null. After a series of testing I found that only the main Activity can detect the swipe gesture well. To resolve I kept track of the onDown event and use it in place of the first param of onFling.

**Learning**

Learnt how to use GestureDetector and AnimationListener.


April 25, 2013

**Progress**

1. Prepared a drop down for each alarm in the list, which gives an option for personal settings on the particular alarm, like it's label, ringtone and what days it should repeat on.

2. Changed the layout of main activity to include user friendly buttons and the overall background color of the app.

**Problems**

1. I resolved all problems, I faced to hide the layout on a click of button in the list.

**Learning**

Learnt how to use expandable list adapter, array adapter. How to incorporate multiple layouts with different attributes in a single layout xml file.

**Planned**

Will work more on UI and settings activity. Some improvements in exciting activities too. Also to implement alarm off when in pocket.


**April 18, 2013**

**Progress**

1. Fixed the error of updating the list of multiple alarms. Now, on clicking add alarm, it takes time as user input and adds the alarm to the list.

2. Implemented the on shake feature to snooze off/turn off the alarm.

**Learning**

I spent a lot of time to debug and find the error with the alarm addition. I tried to check if I am putting the sql database queries correctly, if the layout xml files are in place. Finally found a small mistake, while calling the array adapter for the updated list, I was returning the position of the new alarm rather than the view to be displayed.

**Planned**

1. Have started working on implementing change visibility on clicking a textview in the alarm list.

2. Improve the navigation between activities and UI.


**April 11, 2013**

**Progress**

1. Worked to generate a list of multiple alarms.

2. Created a writable database which can save alarms added by the user and retrieve them when user asks to delete or update the alarm configurations.

**Problems**

1. While I send the updated configuration back to the database, it doesn't show up on the updated list.

**Learning**

Learnt how to use adapters to provide the data to the ListView object. The adapter also defines how each row is the ListView is displayed.

**Planned**

Will implement the set alarm and edit alarm feature as described in the design document. Which includes setting up multiple alarms with different configuration parameters.

**April 4, 2013**

**Progress**

1. Successfully implemented the snooze feature for wave. Even when the app is not open and the screen is off, the wave over the phone snoozes off the alarm

2. Changed the ringtone of the alarm

3. Added a settings button which gives a list view which navigates you further to other pages and gives a toast where required.

4. App has both portrait and landscape mode.

5. On clicking button to set time a dialog appears not covering the full screen.

**Problems:**

1. Item click with List View does not work correctly, choosing one item and it navigates to some other page.

2. List view with Check Box single selection not working.

3. Some ringtones under "Audio Manager" not working.

4. Cannot select an option in radio button.

5. Tab bar not working.

**Learning:**

1. In case of listview position starts with 0 not 1.

2. For single selection have to set it using setChoiceMode.


**Planned:**

1. Include some new ringtone according to my choice.

2. Work on setting alarm volume.

3. Customize list view with both checkbox and buttons together at a time.

4. Include tab bars in the lists.

5. Voice detection


**March 20, 2013**

**Progress**

1. Implemented feature to set the time picker and enable the alarm.

2. Toast giving the notification of alarm set for a particular time.

3. Receive broadcast when alarm rings and open a screen with snooze button and options for snooze time for 1, 2 5 and 10minutes.

4. Settings saved when the activity goes in background and restored back when active.

5. Set feature to hide the notification bar and make UI full screen when alarm rings.

6. Get last used snooze value as default.

7. Created media player for sounding the system alarm.

8. Stop media player when snooze is clicked.

9. Ensure media player is cleaned up after.

10. Implementing proximity sensor to snooze alarm when waved over(in progress, certain issues yet to be resolved).


**Problems:**
1. Toast does not display the time and day for alarm set.

2. If alarm is set for time in past, it does not work.

3. Alarm never rings again when clicking on snooze.

4. Settings not restored after the app is launched again.


**Learning:**
1. Build pending Intent for alarm to ring in future on clicking snooze.

2. Set alarm for next day if time set is in past.

3. Build string using parseInt to get the month, day and year.

4. Use onPause() to Save Settings because it is always called before an Activity is Destroyed.

5. Use onRestoreInstanceState(Bundle savedInstanceState)to fetch Saved settings (Alarm Hour, Minute, Enable) and assign values accordingly when activity is restored.


**Planned:**

1. Snooze feature for wave using proximity.

2. Refine the UI with different screens for set alarm, edit alarm, set time and others.

3. Include more media type for alarm ringtone.

4. Start working on voice and motion detection
