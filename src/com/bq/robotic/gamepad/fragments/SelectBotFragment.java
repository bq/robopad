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

package com.bq.robotic.gamepad.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.bq.robotic.gamepad.GamePadConstants;
import com.bq.robotic.gamepad.R;
import com.bq.robotic.gamepad.SelectBotListener;


/**
 * Fragment for selecting a new type of robot to control it
 * 
 * @author Estefanía Sarasola Elvira
 *
 */

public class SelectBotFragment extends Fragment {

	// Debugging
	private static final String LOG_TAG = "SelectBotFragment";

	SelectBotListener listener;

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {

		View layout = inflater.inflate(R.layout.activity_select_bot, container, false);

		if(listener != null) {
			listener.onSetFragmentTitle(R.string.select_robot);
		}

		setUiListeners(layout);

		return layout;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Retain this fragment across configuration changes.
		setRetainInstance(true);
	}
	
	
    /**
     * Set the fragmentActivity listener. Right now it is not necessary because the 
     * fragment activity that contains the fragments is the one that implements the listener
     * so it is done in the onAttach of RobotFragment. But with this method can be another class 
     * witch implements the listener not the container fragment activity.
     * 
     * @param listener The SelectBotListener
     */
    public void setRobotListener(SelectBotListener listener) {
    	this.listener = listener;
    }
    

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Check the listener is the correct one: the fragment activity container
		// implements that listener
		if (activity instanceof SelectBotListener) {
			this.listener = (SelectBotListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement SelectBotListener");
		}
	}


	/**
	 * Send the message to the Arduino board depending on the button pressed
	 * 
	 * @param viewId The id of the view pressed
	 */
	protected void setUiListeners(View containerLayout) {

		Button pollywogButton = (Button) containerLayout.findViewById(R.id.pollywog_button);
		pollywogButton.setOnClickListener(onButtonClick);

		Button beetleButton = (Button) containerLayout.findViewById(R.id.beetle_button);
		beetleButton.setOnClickListener(onButtonClick);

		Button rhinoButton = (Button) containerLayout.findViewById(R.id.rhino_button);
		rhinoButton.setOnClickListener(onButtonClick);

		Button genericRobotButton = (Button) containerLayout.findViewById(R.id.generic_button);
		genericRobotButton.setOnClickListener(onButtonClick);
	}


	/**
	 * Listener for the views that manage only clicks
	 */
	protected OnClickListener onButtonClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if(listener == null) {
				Log.e(LOG_TAG, "SelectBotListener is null");
				return;
			}

			switch(v.getId()) { 

			case R.id.pollywog_button:
				listener.onRobotSelected(GamePadConstants.robotType.POLLYWOG);    				
				break;

			case R.id.beetle_button:
				listener.onRobotSelected(GamePadConstants.robotType.BEETLE);      				
				break;
			case R.id.rhino_button:
				listener.onRobotSelected(GamePadConstants.robotType.RHINO);    				
				break;

			case R.id.generic_button:
				listener.onRobotSelected(GamePadConstants.robotType.GENERIC_ROBOT);      				
				break;

			}

		}
	};

}
