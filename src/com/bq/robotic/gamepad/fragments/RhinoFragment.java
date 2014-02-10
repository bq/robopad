package com.bq.robotic.gamepad.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.bq.robotic.gamepad.GamePadConstants;
import com.bq.robotic.gamepad.R;
import com.bq.robotic.gamepad.SliderView;

public class RhinoFragment extends RobotFragment {

	// Debugging
	private static final String LOG_TAG = "PollywogFragment";
	
	private SliderView leftSlider;
	private SliderView rightSlider;


	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {

		View layout = inflater.inflate(R.layout.activity_rhino, container, false);

		if(listener != null) {
			listener.onSetFragmentTitle(R.string.rhino);
		}

		setUiListeners(layout);

		return layout;

	}
	
	
	/**
	 * Stop the rhino robot when exiting this fragment
	 */
	@Override
	public void onDestroy() {	
		leftSlider.setProgress(90);
		rightSlider.setProgress(90);
		
		super.onDestroy();
	}
	
	

	/**
	 * Stop the rhino robot when the app is paused 
	 */
	@Override
	public void onPause() {	
		leftSlider.setProgress(90);
		rightSlider.setProgress(90);
		
		super.onPause();
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
		
		Button chargeButton = (Button) containerLayout.findViewById(R.id.charge_button);
		chargeButton.setOnClickListener(onButtonClick);

		leftSlider = (SliderView) containerLayout.findViewById(R.id.left_slider);
		leftSlider.setOnSeekBarChangeListener(sliderListener);

		rightSlider = (SliderView) containerLayout.findViewById(R.id.right_slider);
		rightSlider.setOnSeekBarChangeListener(sliderListener);

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
				leftSlider.setProgress(90);
				rightSlider.setProgress(90);
				
				listener.onDisconnectRobot();    				
				break;

			case R.id.stop_button:
				listener.onSendMessage(GamePadConstants.STOP_COMMAND);
				leftSlider.setProgress(90);
				rightSlider.setProgress(90);
				break;
			
			case R.id.charge_button:
				listener.onSendMessage(GamePadConstants.CHARGE_COMMAND);
				leftSlider.setProgress(90);
				rightSlider.setProgress(90);
				break;
			}
		}
	};


	/**
	 * Listener for the sliders
	 */
	protected OnSeekBarChangeListener sliderListener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

			if(listener != null && listener.onCheckIsConnectedWithoutToast()) {
				switch (seekBar.getId()) {
					case R.id.left_slider:
						listener.onSendMessage(GamePadConstants.COMMAND_DIVISOR + GamePadConstants.LEFT_COMMAND + (180 - progress) + GamePadConstants.COMMAND_DIVISOR);
						break;
	
					case R.id.right_slider:
						listener.onSendMessage(GamePadConstants.COMMAND_DIVISOR + GamePadConstants.RIGHT_COMMAND + progress + GamePadConstants.COMMAND_DIVISOR);
						break;
				}

			}

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// nothing to be done
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// nothing to be done
		}

	};


	@Override
	protected void controlButtonActionDown(int viewId) {
		// None button controlled by MotionEvent 
	}



}
