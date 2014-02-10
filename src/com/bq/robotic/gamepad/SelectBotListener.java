package com.bq.robotic.gamepad;

import com.bq.robotic.gamepad.GamePadConstants.robotType;

public interface SelectBotListener {

	/**
	 * Callback from the SelectBotFragment for changing the bottom title bar.
	 * 
	 * @param titleId The text resource id
	 */
	void onSetFragmentTitle(int titleId);
	
	/**
	 * Callback from the SelectBotFragment for showing the selected type of robot fragment 
	 * 
	 * @param botType The type of robot selected by the user
	 */
	void onRobotSelected(robotType botType);
}
