package com.bq.robotic.robopad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bq.robotic.robopad.utils.RoboPadConstants;

public class RoboPadSplashScreen extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_splash_screen);

        /* New Handler to start the Menu-Activity 
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
            	
                /* Create an Intent that will start the RoboPad-Activity. */
                Intent mainIntent = new Intent(RoboPadSplashScreen.this, SelectRobotActivity.class);
                RoboPadSplashScreen.this.startActivity(mainIntent);
                RoboPadSplashScreen.this.finish();
            }
        }, RoboPadConstants.SPLASH_DISPLAY_TIME);
    }
	
}
