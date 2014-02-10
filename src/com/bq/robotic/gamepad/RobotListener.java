package com.bq.robotic.gamepad;

public interface RobotListener {
	
	/**
	 * Callback from the RobotFragment for checking if the device is connected to an Arduino 
	 * through the bluetooth connection.
	 * If the device is not connected it warns the user of it through a Toast.
	 * 
	 * @return true if is connected or false if not
	 */
	boolean onCheckIsConnected();
	
	
	/**
	 * Callback from the RobotFragment for checking if the device is connected to an Arduino 
	 * through the bluetooth connection
	 * without the warning toast if is not connected. 
	 * 
	 * @return true if is connected or false if not
	 */
	boolean onCheckIsConnectedWithoutToast();
	
	
	/**
	 * Callback from the RobotFragment for sending a message to the Arduino through the bluetooth 
	 * connection. 
	 * 
	 * @param The message to be send to the Arduino
	 */
	void onSendMessage(String message);

	
	/**
	 * Callback from the RobotFragment for changing the bottom title bar.
	 * 
	 * @param titleId The text resource id
	 */
	void onSetFragmentTitle(int titleId);
	
	
	/**
	 * Callback from the RobotFragment for initializing the search of Arduino's bluetooths and connecting
	 * with the one selected by the user 
	 */
	void onConnectRobot();

	
	/**
	 * Callback from the RobotFragment for ending the bluetooth connection with the Arduino board  
	 */
	void onDisconnectRobot();
	
}
