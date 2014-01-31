package com.bq.robotic.gamepad;


public class GamePadConstants {
	
    // Debugging
    public static final boolean D = true;
    
    // Claw position
//    public static enum Claw_next_state {OPEN_STEP, CLOSE_STEP, FULL_OPEN};    
//    public static int MIN_CLOSE_CLAW_POS = 55;
//    public static int MAX_OPEN_CLAW_POS = 10;
//    public static final int INIT_CLAW_POS = 30;
//    public static int CLAW_STEP = 5;
//    public static String ADD_0_TO_POS = "0";
  
    // Commands to send to the beetle bot
//    public static final String CONFIGURATION_DIVISOR = "%";
//    public static final String INSTRUCTION_DIVISOR = "_";
    
    
    /**
     * Pines renacuajo bot! revisar pr si cambia de unos a otros, comentar los de arriba y comentar el case de abrir y cerrar la pinza
     */   
    public static final String UP_COMMAND = "U"; // servo digital port both wheels, left[pin 4, value = 0], right[pin 7, value = 180]
    public static final String DOWN_COMMAND = "D"; // servo digital port both wheels, left[pin 4, value = 180], right[pin 7, value = 0]
    public static final String LEFT_COMMAND = "L"; // servo digital port both wheels, left[pin 4, value = 90], right[pin 7, value = 0] //with 90 it is stop
    public static final String RIGHT_COMMAND = "R"; // servo digital port both wheels, left[pin 4, value = 0], right[pin 7, value = 90]
    public static final String STOP_COMMAND = "S"; //servo digital port both, stop both
    
}
