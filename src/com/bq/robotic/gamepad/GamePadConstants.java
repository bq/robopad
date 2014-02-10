package com.bq.robotic.gamepad;


public class GamePadConstants {
	
    // Debugging
    public static final boolean D = true;
    
    public static final long CLICK_SLEEP_TIME = 150;
    
    public static enum robotType {POLLYWOG, BEETLE, RHINO, GENERIC_ROBOT};
    
    public static final String COMMAND_DIVISOR = "_";
    
    
    /**
     * Beetle robot
     */
    public static enum Claw_next_state {OPEN_STEP, CLOSE_STEP, FULL_OPEN};    
    public static int MIN_CLOSE_CLAW_POS = 55;
    public static int MAX_OPEN_CLAW_POS = 10;
    public static final int INIT_CLAW_POS = 30;
    public static int CLAW_STEP = 5;
    public static String CLAW_COMMAND = "_C";
  
    
    /**
     * Pins renacuajo bot! revise for each robot
     */  
    //FIXME: Put in comments the correct pins
    public static final String UP_COMMAND = "U"; // servo digital port both wheels, left[pin 4, value = 0], right[pin 7, value = 180]
    public static final String DOWN_COMMAND = "D"; // servo digital port both wheels, left[pin 4, value = 180], right[pin 7, value = 0]
    public static final String LEFT_COMMAND = "L"; // servo digital port both wheels, left[pin 4, value = 90], right[pin 7, value = 0] //with 90 it is stop
    public static final String RIGHT_COMMAND = "R"; // servo digital port both wheels, left[pin 4, value = 0], right[pin 7, value = 90]
    public static final String STOP_COMMAND = "S"; //servo digital port both, stop both
    
    
    /**
     * Rhino Robot
     */
    public static String CHARGE_COMMAND = "C";
    
    /**
     * Generic Robot
     */
    public static final String COMMAND_1 = "1";
    public static final String COMMAND_2 = "2";
    public static final String COMMAND_3 = "3";
    public static final String COMMAND_4 = "4";
    public static final String COMMAND_5 = "5";
    public static final String COMMAND_6 = "6";
}
