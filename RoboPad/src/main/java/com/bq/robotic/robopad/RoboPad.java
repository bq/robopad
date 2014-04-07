/*
* This file is part of the RoboPad
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

package com.bq.robotic.robopad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bq.robotic.droid2ino.activities.BaseBluetoothSendOnlyActivity;
import com.bq.robotic.droid2ino.utils.AndroidinoConstants;
import com.bq.robotic.droid2ino.utils.DeviceListDialogStyle;
import com.bq.robotic.robopad.fragments.BeetleFragment;
import com.bq.robotic.robopad.fragments.CrabFragment;
import com.bq.robotic.robopad.fragments.GenericRobotFragment;
import com.bq.robotic.robopad.fragments.PollywogFragment;
import com.bq.robotic.robopad.fragments.RhinoFragment;
import com.bq.robotic.robopad.fragments.RobotFragment;
import com.bq.robotic.robopad.fragments.SelectBotFragment;
import com.bq.robotic.robopad.utils.RoboPadConstants.robotType;
import com.bq.robotic.robopad.utils.RobotListener;
import com.bq.robotic.robopad.utils.SelectBotListener;


/**
 * Main activity of the app that contains the different fragments to show to the user 
 */

public class RoboPad extends BaseBluetoothSendOnlyActivity implements RobotListener, SelectBotListener {
	
	// Debugging
    private static final String LOG_TAG = "GamePad";
    
    private ActionBar mActionBar;   
    private ImageButton mSelectBotButton;
    private ImageView mGamePadButton; 
//    private TextView mBottomTitleBar;
    private FragmentManager mFragmentManager;

    private ImageButton connectButton;
    private ImageButton disconnectButton;

    private Animation anim;
    

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_robopad);
		
		mFragmentManager = getSupportFragmentManager();
		mActionBar = getSupportActionBar();
		// Hide the action bar
		mActionBar.hide();
		
		mSelectBotButton = (ImageButton) findViewById(R.id.select_bot_button);
		mGamePadButton = (ImageView) findViewById(R.id.pad_button);
        connectButton = (ImageButton) findViewById(R.id.connect_button);
        disconnectButton = (ImageButton) findViewById(R.id.disconnect_button);
//		mBottomTitleBar = (TextView) findViewById(R.id.title_view);
        anim = AnimationUtils.loadAnimation(this, R.anim.bluetooth_spiner);
		
		// If we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
        	
        	if(mFragmentManager.findFragmentById(R.id.game_pad_container) instanceof RobotFragment) {
    			mSelectBotButton.setClickable(true);
    			mGamePadButton.setVisibility(View.VISIBLE);

            } else if(mFragmentManager.findFragmentById(R.id.game_pad_container) instanceof SelectBotFragment) {
                connectButton.setVisibility(View.GONE);
                disconnectButton.setVisibility(View.GONE);
                return;
            }
       
            return;
        }
		
        // Show the select robot fragment
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.replace(R.id.game_pad_container, new SelectBotFragment());
		ft.commit();

        connectButton.setVisibility(View.GONE);
        disconnectButton.setVisibility(View.GONE);
			
	}


    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if(connectButton == null && disconnectButton == null) {
            return;
        }

        if(mFragmentManager.findFragmentById(R.id.game_pad_container) instanceof SelectBotFragment) {
            connectButton.setVisibility(View.GONE);
            disconnectButton.setVisibility(View.GONE);
            return;
        }

        // Check the status for the connect / disconnect buttons
        if(isConnectedWithoutToast()) {
            connectButton.setVisibility(View.GONE);
            disconnectButton.setVisibility(View.VISIBLE);
        } else {
            connectButton.setVisibility(View.VISIBLE);
            disconnectButton.setVisibility(View.GONE);
        }

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
            if(mFragmentManager.findFragmentById(R.id.game_pad_container) instanceof RobotFragment) {
            	((RobotFragment) mFragmentManager.findFragmentById(R.id.game_pad_container)).onBluetoothConnected();
            }
            break;

        case AndroidinoConstants.STATE_CONNECTING:
            break;

        case AndroidinoConstants.STATE_LISTEN:
        case AndroidinoConstants.STATE_NONE:
            if(mFragmentManager.findFragmentById(R.id.game_pad_container) instanceof RobotFragment) {
                ((RobotFragment) mFragmentManager.findFragmentById(R.id.game_pad_container)).onBluetoothDisconnected();
            }
            break;
      }
        changeViewsVisibility(connectionState);
    }


    /**
     * Change the visibility of some views as the connect/disconnect button depending on the
     * bluetooth connection state The state of the bluetooth connection
     *
     * @param connectionState the state of the current bluetooth connection
     */
    private void changeViewsVisibility(int connectionState) {

        switch (connectionState) {

            case AndroidinoConstants.STATE_CONNECTED:
                findViewById(R.id.bluetooth_spinner_view).setVisibility(View.INVISIBLE);
                findViewById(R.id.bluetooth_spinner_view).clearAnimation();

                connectButton.setVisibility(View.GONE);
                disconnectButton.setVisibility(View.VISIBLE);
                break;

            case AndroidinoConstants.STATE_CONNECTING:

                if(anim != null) {
                    anim.setInterpolator(new Interpolator() {
                        private final int frameCount = 8;

                        @Override
                        public float getInterpolation(float input) {
                            return (float) Math.floor(input * frameCount) / frameCount;
                        }
                    });

                    findViewById(R.id.bluetooth_spinner_view).setVisibility(View.VISIBLE);
                    findViewById(R.id.bluetooth_spinner_view).startAnimation(anim);
                } else {
                    Log.e(LOG_TAG, "Anim null!!!");
                }

                break;

            case AndroidinoConstants.STATE_LISTEN:
            case AndroidinoConstants.STATE_NONE:
                findViewById(R.id.bluetooth_spinner_view).setVisibility(View.INVISIBLE);
                findViewById(R.id.bluetooth_spinner_view).clearAnimation();

                if(mFragmentManager.findFragmentById(R.id.game_pad_container) instanceof SelectBotFragment) {
                    connectButton.setVisibility(View.GONE);
                    disconnectButton.setVisibility(View.GONE);
                } else {
                    connectButton.setVisibility(View.VISIBLE);
                    disconnectButton.setVisibility(View.GONE);
                }
                break;
        }
    }


    /**
     * Callback for the connect and disconnect buttons
     * @param v view pressed
     */
    public void onChangeConnection(View v) {

        switch (v.getId()) {

            case R.id.connect_button:
                requestDeviceConnection();
                break;

            case R.id.disconnect_button:
                stopBluetoothConnection();
                break;
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
		mGamePadButton.setVisibility(View.GONE);
		mGamePadButton.setSelected(false);

        connectButton.setVisibility(View.GONE);
        disconnectButton.setVisibility(View.GONE);
		
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
	 * @param message to be send to the Arduino
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
			robotFragment = new PollywogFragment();

		} else if (botType == robotType.BEETLE) {
			robotFragment = new BeetleFragment();

		} else if (botType == robotType.RHINO) {	
			robotFragment = new RhinoFragment();

		} else if (botType == robotType.CRAB) {
            robotFragment = new CrabFragment();

        } else if (botType == robotType.GENERIC_ROBOT) {
			robotFragment = new GenericRobotFragment();
		}
		
		if(robotFragment != null) {
			ft.replace(R.id.game_pad_container, robotFragment);
			ft.commit();

			// the select_bot_button must be clickable when the RobotFragment is showed
			// and the gamepad icon must be visible too
			mSelectBotButton.setClickable(true);
			mGamePadButton.setVisibility(View.VISIBLE);
			mGamePadButton.setSelected(true);

            connectButton.setVisibility(View.VISIBLE);
            disconnectButton.setVisibility(View.GONE);
		}

	}

}
