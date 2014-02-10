package com.bq.robotic.gamepad.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.bq.robotic.gamepad.GamePadConstants;
import com.bq.robotic.gamepad.RobotListener;

public abstract class RobotFragment extends Fragment {

	// Debugging
	private static final String LOG_TAG = "RobotFragment";

	protected boolean isClick;
	protected boolean isConnected = false;

	protected RobotListener listener;

	/**
	 * Set the listeners to the UI views
	 * @param containerLayout
	 */
	protected abstract void setUiListeners (View containerLayout);


	/**
	 * Send the message depending on the button pressed
	 * @param viewId the id of the view pressed
	 */
	protected abstract void controlButtonActionDown(int viewId);


	/**
	 * Set the fragmentActivity listener. Right now it is not necessary because the 
	 * fragment activity that contains the fragments is the one that implements the listener
	 * so it is done in the onAttach of RobotFragment. But with this method can be another class 
	 * witch implements the listener not the container fragment activity.
	 * 
	 * @param listener The RobotListener
	 */
	public void setRobotListener(RobotListener listener) {
		this.listener = listener;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Retain this fragment across configuration changes.
		setRetainInstance(true);
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Check the listener is the correct one: the fragment activity container
		// implements that listener
		if (activity instanceof RobotListener) {
			this.listener = (RobotListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement robotListener");
		}
	}


	/**
	 * Listener for the touch events. When action_down, the user is pressing the button
	 * so we send the message to the arduino, and when action_up it is send a message to the arduino
	 * in order to stop it.
	 */
	protected OnTouchListener buttonOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			final View view = v;

			Thread sendActionThread;

			switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:

				if(listener != null && !listener.onCheckIsConnected()) {
					isConnected = false;
					break;
				} else {
					isConnected = true;
				}

				isClick = false;					
				sendActionThread = createSendActionThread(view.getId());										
				sendActionThread.start();

				break;

			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:

				if(!isConnected) {
					break;
				}

				isClick = true;
				if (listener != null) {
					listener.onSendMessage(GamePadConstants.STOP_COMMAND);
				}

				break;

			}		

			return false;
		}

	};



	/**
	 * Thread to send the command but waits and send the stop command with a 130 delay
	 * in case it was only a click and the arduino app didn't process the stop command 
	 * because of itself delays
	 * 
	 * @param actionId the id of the view touched
	 * @return Thread The thread that send the commands when pressed the corresponding buttons
	 */
	private Thread createSendActionThread(final int actionId) {

		Thread sendActionThread = new Thread() {

			@Override
			public void run() {
				try {

					if(!isClick) {
						controlButtonActionDown(actionId);
					}

					sleep(GamePadConstants.CLICK_SLEEP_TIME);

					if(isClick && listener != null) {
						Log.e(LOG_TAG, "stop command in thread send");
						listener.onSendMessage(GamePadConstants.STOP_COMMAND);
					}

				} catch (InterruptedException e) {
					Log.e(LOG_TAG, "error in sendActionThread: )" + e);
				}

			}

		};

		return sendActionThread;
	}

}
