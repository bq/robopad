/*
* This file is part of the GamePad
*
* Copyright (C) 2013 Mundo Reader S.L.
* 
* Date: February 2014
* Author: Estefanía Sarasola Elvira <estefania.sarasola@bq.com>
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bq.robotic.droid2ino.activities.BaseBluetoothSendOnlyActivity;
import com.bq.robotic.droid2ino.utils.AndroidinoConstants;
import com.bq.robotic.droid2ino.utils.DeviceListDialogStyle;
import com.bq.robotic.gamepad.GamePadConstants.robotType;
import com.bq.robotic.gamepad.fragments.BeetleFragment;
import com.bq.robotic.gamepad.fragments.GenericRobotFragment;
import com.bq.robotic.gamepad.fragments.PollywogFragment;
import com.bq.robotic.gamepad.fragments.RhinoFragment;
import com.bq.robotic.gamepad.fragments.RobotFragment;
import com.bq.robotic.gamepad.fragments.SelectBotFragment;


/**
 * Main activity of the app that contains the different fragments to show to the user 
 */

public class GamePad extends BaseBluetoothSendOnlyActivity implements RobotListener, SelectBotListener {
	
	// Debugging
    private static final String LOG_TAG = "GamePad";
    
    private ActionBar mActionBar;   
    private ImageButton mSelectBotButton;
    private ImageView mGamePadIcon; 
    private TextView mBottomTitleBar;
    private FragmentManager mFragmentManager;
    

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gamepad);
		
		mFragmentManager = getSupportFragmentManager();
		mActionBar = getSupportActionBar();
		// Hide the action bar
		mActionBar.hide();
		
		mSelectBotButton = (ImageButton) findViewById(R.id.select_bot_button);
		mGamePadIcon = (ImageView) findViewById(R.id.pad_button);
		mBottomTitleBar = (TextView) findViewById(R.id.title_view);
		
		// If we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
        	
        	if(mFragmentManager.findFragmentById(R.id.game_pad_container) instanceof RobotFragment) {
    			mSelectBotButton.setClickable(true);
    			mGamePadIcon.setVisibility(View.VISIBLE);
        	} 
       
            return;
        }
		
        // Show the select robot fragment
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.replace(R.id.game_pad_container, new SelectBotFragment());
		ft.commit();
			
	}
	
	
	/**
	 * Put the text in the title bar in the bottom of the screen
	 * 
	 * @param textId the text to put in the bottom title bar
	 */
    public void setFragmentTitle(int textId) {
    	mBottomTitleBar.setText(textId);
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
            if(mFragmentManager.findFragmentById(R.id.game_pad_container) instanceof RobotFragment) {
            	((RobotFragment) mFragmentManager.findFragmentById(R.id.game_pad_container)).onBluetoothConnected();
            }
            break;
        case AndroidinoConstants.STATE_CONNECTING:
            setStatus(R.string.title_connecting);
            break;
        case AndroidinoConstants.STATE_LISTEN:
        case AndroidinoConstants.STATE_NONE:
            setStatus(R.string.title_not_connected);
            if(mFragmentManager.findFragmentById(R.id.game_pad_container) instanceof RobotFragment) {
            	((RobotFragment) mFragmentManager.findFragmentById(R.id.game_pad_container)).onBluetoothDisconnected();
            }
            break;
      }
    }

    
    /**
     * Put the status of the connection in the bottom title bar
     * 
     * @param textId The text id in the R.xml file 
     */
    private final void setStatus(int textId) {
    	mBottomTitleBar.setText(textId);
    }
    
    
    /**
     * Put the status of the connection in the bottom title bar
     * 
     * @param subTitle The text string
     */
    private final void setStatus(CharSequence subTitle) {
        if (subTitle != null) {
        	mBottomTitleBar.setText(subTitle);
        }
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
		FragmentTransaction ft = mFragmentManager.beginTransaction();		
		SelectBotFragment selectBotFragment = new SelectBotFragment();
		ft.replace(R.id.game_pad_container, selectBotFragment);
		ft.commit();
		
		// the select_bot_button must be no clickable when the SelectBotFragment is showed
		// and the gamepad icon must be hidden too
		mSelectBotButton.setClickable(false);
		mGamePadIcon.setVisibility(View.GONE);
		
		mBottomTitleBar.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0); 
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
	 * Callback from the RobotFragment for initializing the search of Arduino's bluetooths and 
	 * connecting with the one selected by the user 
	 */
	@Override
	public void onConnectRobot() {
		DeviceListDialogStyle deviceListDialogStyle = requestDeviceConnection();
		
		// Style the search bluetooth devices dialog			
		deviceListDialogStyle.getSearchDevicesTitleView().setTextColor(getResources().getColor(R.color.holo_green_dark));
		deviceListDialogStyle.getDevicesPairedTitleView().setBackgroundResource(R.color.holo_green_dark);
		deviceListDialogStyle.getNewDevicesTitleView().setBackgroundResource(R.color.holo_green_dark);	
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

		FragmentTransaction ft = mFragmentManager.beginTransaction();
		RobotFragment robotFragment = null;

		if (botType == robotType.POLLYWOG) {
			mBottomTitleBar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bot_pollywog_action_bar_icon, 0, 0, 0);
			robotFragment = new PollywogFragment();

		} else if (botType == robotType.BEETLE) {
			mBottomTitleBar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bot_beetle_action_bar_icon, 0, 0, 0);
			robotFragment = new BeetleFragment();

		} else if (botType == robotType.RHINO) {	
			mBottomTitleBar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bot_rhino_action_bar_icon, 0, 0, 0);
			robotFragment = new RhinoFragment();

		} else if (botType == robotType.GENERIC_ROBOT) {
			mBottomTitleBar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bot_generic_action_bar_icon, 0, 0, 0);
			robotFragment = new GenericRobotFragment();
		}
		
		if(robotFragment != null) {
			ft.replace(R.id.game_pad_container, robotFragment);
			ft.commit();

			// the select_bot_button must be clickable when the RobotFragment is showed
			// and the gamepad icon must be visible too
			mSelectBotButton.setClickable(true);
			mGamePadIcon.setVisibility(View.VISIBLE);
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
