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
import com.bq.robotic.gamepad.GamePadConstants.Claw_next_state;
import com.bq.robotic.gamepad.R;

/**
 * Fragment of the game pad controller for the Beetle robot.
 * 
 * @author Estefanía Sarasola Elvira
 *
 */

public class BeetleFragment extends RobotFragment {

	// Debugging
	private static final String LOG_TAG = "BeetleFragment";

	private int clawPosition; // Current position of the claw 

	Button fullOpenClawButton;
	Button openStepClawButton;
	Button closeStepClawButton;


	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {

		View layout = inflater.inflate(R.layout.activity_beetle, container, false);

		if(listener != null) {
			listener.onSetFragmentTitle(R.string.beetle);
		}
		
		// Put the servo of the claws in a initial position 
		clawPosition = GamePadConstants.INIT_CLAW_POS; // default open 30 (values from 5 to 50) 

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

		fullOpenClawButton = (Button) containerLayout.findViewById(R.id.full_open_claw_button);
		fullOpenClawButton.setOnClickListener(onButtonClick);

		openStepClawButton = (Button) containerLayout.findViewById(R.id.open_claw_button);
		openStepClawButton.setOnClickListener(onButtonClick);

		closeStepClawButton = (Button) containerLayout.findViewById(R.id.close_claw_button);
		closeStepClawButton.setOnClickListener(onButtonClick);

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
	 * Listener for the views that manage only clicks
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

			case R.id.full_open_claw_button: 
				if(listener != null && listener.onCheckIsConnected()) {
					listener.onSendMessage(GamePadConstants.CLAW_COMMAND + getNextClawPostion(Claw_next_state.FULL_OPEN) 
							+ GamePadConstants.COMMAND_DIVISOR);
				}
				break;

			case R.id.open_claw_button:  
				if(listener != null && listener.onCheckIsConnected()) {
					listener.onSendMessage(GamePadConstants.CLAW_COMMAND + getNextClawPostion(Claw_next_state.OPEN_STEP)
							+ GamePadConstants.COMMAND_DIVISOR);
				}
				break;

			case R.id.close_claw_button:
				if(listener != null && listener.onCheckIsConnected()) {
					listener.onSendMessage(GamePadConstants.CLAW_COMMAND + getNextClawPostion(Claw_next_state.CLOSE_STEP)
							+ GamePadConstants.COMMAND_DIVISOR);
				}
				break;	

			}

		}
	};


	/**
	 * Get the next position for the claw of the beetle robot
	 * 
	 * @param nextState The next state depending on the button that was pressed
	 * @return The message for controlling the position of the servo of the claws
	 */
	private String getNextClawPostion(Claw_next_state nextState) {

		// Show buttons enabled or disabled if the claw gets to max or min position
		if(clawPosition == GamePadConstants.MAX_OPEN_CLAW_POS && nextState == Claw_next_state.CLOSE_STEP) {
			openStepClawButton.setEnabled(true);
			fullOpenClawButton.setEnabled(true);

		} else if(clawPosition == GamePadConstants.MIN_CLOSE_CLAW_POS && (nextState == Claw_next_state.OPEN_STEP || nextState == Claw_next_state.FULL_OPEN) ) {
			closeStepClawButton.setEnabled(true);
		}

		if (nextState == Claw_next_state.OPEN_STEP) {
			clawPosition -= GamePadConstants.CLAW_STEP;

		} else if (nextState == Claw_next_state.CLOSE_STEP) {
			clawPosition += GamePadConstants.CLAW_STEP;

		} else if (nextState == Claw_next_state.FULL_OPEN) {
			clawPosition = GamePadConstants.MAX_OPEN_CLAW_POS;

		}

		// Don't exceed the limits
		if (clawPosition <= GamePadConstants.MAX_OPEN_CLAW_POS) {

			clawPosition = GamePadConstants.MAX_OPEN_CLAW_POS;	
			openStepClawButton.setEnabled(false);
			fullOpenClawButton.setEnabled(false);

		} else if (clawPosition >= GamePadConstants.MIN_CLOSE_CLAW_POS) {

			clawPosition = GamePadConstants.MIN_CLOSE_CLAW_POS; 
			closeStepClawButton.setEnabled(false);
		}

		return String.valueOf(clawPosition);

	}

}
