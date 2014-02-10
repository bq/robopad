package com.bq.robotic.gamepad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bq.robotic.androidino.activities.BaseBluetoothConnectionActivity;
import com.bq.robotic.androidino.utils.AndroidinoConstants;
import com.bq.robotic.gamepad.GamePadConstants.robotType;
import com.bq.robotic.gamepad.fragments.BeetleFragment;
import com.bq.robotic.gamepad.fragments.GenericRobotFragment;
import com.bq.robotic.gamepad.fragments.PollywogFragment;
import com.bq.robotic.gamepad.fragments.RhinoFragment;
import com.bq.robotic.gamepad.fragments.RobotFragment;
import com.bq.robotic.gamepad.fragments.SelectBotFragment;

public class GamePad extends BaseBluetoothConnectionActivity implements RobotListener, SelectBotListener {
	
	// Debugging
    private static final String LOG_TAG = "GamePad";
    
    private ActionBar actionBar;   
    private ImageButton selectBotButton;
    private ImageView gamePadIcon; 
    TextView bottomTitleBar;
    private FragmentManager fragmentManager;
    

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gamepad);
		
		fragmentManager = getSupportFragmentManager();
		actionBar = getSupportActionBar();
		// Hide the action bar
		actionBar.hide();
		
		selectBotButton = (ImageButton) findViewById(R.id.select_bot_button);
		gamePadIcon = (ImageView) findViewById(R.id.pad_button);
		bottomTitleBar = (TextView) findViewById(R.id.title_view);
		
		// If we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
        	
        	if(fragmentManager.findFragmentById(R.id.game_pad_container) instanceof RobotFragment) {
    			selectBotButton.setClickable(true);
    			gamePadIcon.setVisibility(View.VISIBLE);
        	} 
       
            return;
        }
		
        // Show the select robot fragment
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(R.id.game_pad_container, new SelectBotFragment());
		ft.commit();
			
	}
	
	
	/**
	 * Put the text in the title bar in the bottom of the screen
	 * 
	 * @param textId the text to put in the bottom title bar
	 */
    public void setFragmentTitle(int textId) {
    	bottomTitleBar.setText(textId);
    }
    
    
    /**
     * Callback for the changes of the bluetooth connection status
     * 
     * @param connectionState The state of the bluetooth connection
     */
    @Override
    public void onConnectionStatusUpdate(int connectionState) {
      switch (connectionState) {
        case AndroidinoConstants.STATE_CONNECTED:
            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
            changeButtonConnectionState(connectionState);
            break;
        case AndroidinoConstants.STATE_CONNECTING:
            setStatus(R.string.title_connecting);
            break;
        case AndroidinoConstants.STATE_LISTEN:
        case AndroidinoConstants.STATE_NONE:
            setStatus(R.string.title_not_connected);
            changeButtonConnectionState(connectionState);
            break;
      }
    }
    
    
    /**
     * Change the button in the current RobotFragment from 'Connect' to 'Disconnect' and vice versa
     * 
     * @param connectionState The state of the bluetooth connection
     */
    private void changeButtonConnectionState(int connectionState) {
    	
    	if(! (fragmentManager.findFragmentById(R.id.game_pad_container) instanceof RobotFragment)) {
    		return;
    	}
    	
        Button connectbutton = (Button) fragmentManager.findFragmentById(R.id.game_pad_container).getView().findViewById(R.id.connect_button);
        Button disconnectButton = (Button) fragmentManager.findFragmentById(R.id.game_pad_container).getView().findViewById(R.id.disconnect_button);
        
        switch (connectionState) {
        	case AndroidinoConstants.STATE_CONNECTED:
        		connectbutton.setVisibility(View.GONE);
        		disconnectButton.setVisibility(View.VISIBLE);
        		break;
        	
        	case AndroidinoConstants.STATE_LISTEN:
        	case AndroidinoConstants.STATE_NONE:
        		connectbutton.setVisibility(View.VISIBLE);
        		disconnectButton.setVisibility(View.GONE);
        }
    }

    
    /**
     * Put the status of the connection in the bottom title bar
     * 
     * @param textId The text id in the R.xml file 
     */
    private final void setStatus(int textId) {
    	bottomTitleBar.setText(textId);
    }
    
    
    /**
     * Put the status of the connection in the bottom title bar
     * 
     * @param subTitle The text string
     */
    private final void setStatus(CharSequence subTitle) {
        if (subTitle != null) {
        	bottomTitleBar.setText(subTitle);
        }
    }

    
    /**
     * Callback method invoked when the device receives a message from the Arduino
     * through the bluetooth connection
     * 
     * @param message The message received from the Arduino
     */
	@Override
	public void onNewMessage(String message) {
		// Nothing to be done here		
	}
	
	
	/**
	 * Callback from the select_bot_button, in the custom navigation bar.
	 * As the button for showing the SelectBotListener is not clickable when the user is 
	 * in the SelectBotFragment we don't need to manage that case. If the user clicks in the 
	 * button it is because he/she is in a RobotFragment.
	 * 
	 * @param v The select_bot_button
	 */
	public void onSelectBotButtonClick(View v) {
		
		if(!isConnectedWithoutToast()) {
			showSelectBotFragment();
			return;
		}
		
		// Show a dialog to confirm that the user wants to choose a new robot type
		// and to inform that the connection with the current robot will be lost
        AlertDialog exitRobotControl = new AlertDialog.Builder(this)
        .setMessage(getResources().getString(R.string.exit_robot_control_dialog))
        .setTitle(R.string.exit_robot_control_dialog_title)
        .setCancelable(true)
        .setPositiveButton(android.R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				stopBluetoothConnection();
				
				showSelectBotFragment();
			}
		})
		.setNegativeButton(android.R.string.no, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(LOG_TAG, "Choose a new robot cancelled");
			}
		})
        .create();
        
        exitRobotControl.show();
	}
	
	
	/**
	 * Replaces the current RobotFragment with the SelectBotFragment 
	 */
	private void showSelectBotFragment() {
		FragmentTransaction ft = fragmentManager.beginTransaction();		
		SelectBotFragment selectBotFragment = new SelectBotFragment();
		ft.replace(R.id.game_pad_container, selectBotFragment);
		ft.commit();
		
		// the select_bot_button must be no clickable when the SelectBotFragment is showed
		// and the gamepad icon must be hidden too
		selectBotButton.setClickable(false);
		gamePadIcon.setVisibility(View.GONE);
		
		bottomTitleBar.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0); 
	}

	
	/**************************************************************************************
	 **************************   ROBOTLISTENER CALLBACKS   *******************************
	 **************************************************************************************/
	
	/**
	 * Callback from the RobotFragment for checking if the device is connected to an Arduino 
	 * through the bluetooth connection.
	 * If the device is not connected it warns the user of it through a Toast.
	 * 
	 * @return true if is connected or false if not
	 */
	@Override
	public boolean onCheckIsConnected() {
		return isConnected();
	}
	
	
	/**
	 * Callback from the RobotFragment for checking if the device is connected to an Arduino 
	 * through the bluetooth connection
	 * without the warning toast if is not connected. 
	 * 
	 * @return true if is connected or false if not
	 */
	public boolean onCheckIsConnectedWithoutToast() {
		return isConnectedWithoutToast();
	}
	
	
	/**
	 * Callback from the RobotFragment for sending a message to the Arduino through the bluetooth 
	 * connection. 
	 * 
	 * @param The message to be send to the Arduino
	 */
	@Override
	public void onSendMessage(String message) {
//		Log.e(LOG_TAG, "message to send to arduino: " + message);
		sendMessage(message);
	}

	
	/**
	 * Callback from the RobotFragment for initializing the search of Arduino's bluetooths and connecting
	 * with the one selected by the user 
	 */
	@Override
	public void onConnectRobot() {
		requestDeviceConnection();
		
		// Style the search bluetooth devices dialog
//		DeviceListDialogStyle deviceListDialogStyle = requestDeviceConnection();
//		deviceListDialogStyle.getSearchDevicesTitleView().setTextColor(Color.MAGENTA);
//		deviceListDialogStyle.getDevicesPairedTitleView().setBackgroundResource(R.color.holo_green_dark);
	}
	
	
	/**
	 * Callback from the RobotFragment for ending the bluetooth connection with the Arduino board  
	 */
	@Override
	public void onDisconnectRobot() {
		stopBluetoothConnection();
	}
	
	
	
	/********************************************************************************************
	 ****************************   SELECTBOTLISTENER CALLBACKS   *******************************
	 ********************************************************************************************/

	/**
	 * Callback from the SelectBotFragment for showing the selected type of robot fragment.
	 * Replaces the current RobotFragment with the SelectBotFragment. 
	 * 
	 * @param botType The type of robot selected by the user
	 */
	@Override
	public void onRobotSelected(robotType botType) {

		FragmentTransaction ft = fragmentManager.beginTransaction();
		RobotFragment robotFragment = null;

		if (botType == robotType.POLLYWOG) {
			bottomTitleBar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bot_pollywog_action_bar_icon, 0, 0, 0);
			robotFragment = new PollywogFragment();

		} else if (botType == robotType.BEETLE) {
			bottomTitleBar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bot_beetle_action_bar_icon, 0, 0, 0);
			robotFragment = new BeetleFragment();

		} else if (botType == robotType.RHINO) {	
			bottomTitleBar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bot_rhino_action_bar_icon, 0, 0, 0);
			robotFragment = new RhinoFragment();

		} else if (botType == robotType.GENERIC_ROBOT) {
			bottomTitleBar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bot_generic_action_bar_icon, 0, 0, 0);
			robotFragment = new GenericRobotFragment();
		}
		
		if(robotFragment != null) {
			ft.replace(R.id.game_pad_container, robotFragment);
			ft.commit();

			// the select_bot_button must be clickable when the RobotFragment is showed
			// and the gamepad icon must be visible too
			selectBotButton.setClickable(true);
			gamePadIcon.setVisibility(View.VISIBLE);
		}

	}
	
	
	
	/********************************************************************************************
	 ****************************   BOTH LISTENER CALLBACKS   *******************************
	 ********************************************************************************************/
	
	/**
	 * Callback from the RobotFragment or SelectBotFragment for changing the bottom title bar.
	 * 
	 * @param titleId The text resource id
	 */
	@Override
	public void onSetFragmentTitle(int titleId) {
		setFragmentTitle(titleId);		
	}


}
