package com.bq.robotic.gamepad.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.bq.robotic.gamepad.GamePadConstants;
import com.bq.robotic.gamepad.R;

public class PollywogFragment extends RobotFragment {

	// Debugging
	private static final String LOG_TAG = "PollywogFragment";


	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {

		View layout = inflater.inflate(R.layout.activity_pollywog, container, false);

		if(listener != null) {
			listener.onSetFragmentTitle(R.string.pollywog);
		}

		setUiListeners(layout);

		return layout;

	}


	/**
	 * Set the listeners to the views that need them. It must be done here in the fragment in order
	 * to get the callback here and not in the FragmentActivity, that would be a mess with all the callbacks 
	 * of all the possible fragments
	 * 
	 * @param The view used as the main container for this fragment
	 */
	@Override
	protected void setUiListeners(View containerLayout) {

		Button connectButton = (Button) containerLayout.findViewById(R.id.connect_button);
		connectButton.setOnClickListener(onButtonClick);

		Button disconnectButton = (Button) containerLayout.findViewById(R.id.disconnect_button);
		disconnectButton.setOnClickListener(onButtonClick);

		ImageButton stopButton = (ImageButton) containerLayout.findViewById(R.id.stop_button);
		stopButton.setOnClickListener(onButtonClick);

		ImageButton upButton = (ImageButton) containerLayout.findViewById(R.id.up_button);
		upButton.setOnTouchListener(buttonOnTouchListener);

		ImageButton downButton = (ImageButton) containerLayout.findViewById(R.id.down_button);
		downButton.setOnTouchListener(buttonOnTouchListener);

		ImageButton leftButton = (ImageButton) containerLayout.findViewById(R.id.left_button);
		leftButton.setOnTouchListener(buttonOnTouchListener);

		ImageButton rightButton = (ImageButton) containerLayout.findViewById(R.id.right_button);
		rightButton.setOnTouchListener(buttonOnTouchListener);
	}


	/**
	 * Send the message to the Arduino board depending on the button pressed
	 * 
	 * @param viewId The id of the view pressed
	 */
	@Override
	public void controlButtonActionDown(int viewId) {

		if(listener == null) {
			Log.e(LOG_TAG, "RobotListener is null");
			return;
		}

		switch(viewId) { 	

		case R.id.up_button:
			listener.onSendMessage(GamePadConstants.UP_COMMAND);
			//	    			Log.e(LOG_TAG, "up command send");
			break;

		case R.id.down_button:
			listener.onSendMessage(GamePadConstants.DOWN_COMMAND);
			//	    			Log.e(LOG_TAG, "down command send");
			break;

		case R.id.left_button:
			listener.onSendMessage(GamePadConstants.LEFT_COMMAND);	
			//	    			Log.e(LOG_TAG, "left command send");
			break;

		case R.id.right_button:
			listener.onSendMessage(GamePadConstants.RIGHT_COMMAND);
			//	    			Log.e(LOG_TAG, "right command send");
			break;

		}
	}


	/**
	 * Listeners for the views that manage only clicks
	 */
	protected OnClickListener onButtonClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if(listener == null) {
				Log.e(LOG_TAG, "RobotListener is null");
				return;
			}

			switch(v.getId()) { 

			case R.id.connect_button:
				listener.onConnectRobot();    				
				break;

			case R.id.disconnect_button:
				listener.onDisconnectRobot();    				
				break;

			case R.id.stop_button:
				listener.onSendMessage(GamePadConstants.STOP_COMMAND);    				
				break;
			}

		}
	};



}
