package com.bq.robotic.gamepad;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bq.robotic.androidino.activities.BaseBluetoothConnectionActivity;
import com.bq.robotic.androidino.utils.AndroidinoConstants;

public class SelectBotActivity extends BaseBluetoothConnectionActivity  {

/*	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_bot);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_bot, menu);
		return true;
	}*/
	
	// Debugging
    private static final String LOG_TAG = "SelectBotActivity";
    
    boolean isClick;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(Constants.D) Log.d(LOG_TAG, "+++ ON CREATE +++");

        // Set up the window layout
        setContentView(R.layout.activity_select_bot);
        
        ImageButton upButton = (ImageButton) findViewById(R.id.upButton);
        upButton.setOnTouchListener(buttonOnTouchListener);
        
        ImageButton downButton = (ImageButton) findViewById(R.id.downButton);
        downButton.setOnTouchListener(buttonOnTouchListener);
        
        ImageButton leftButton = (ImageButton) findViewById(R.id.leftButton);
        leftButton.setOnTouchListener(buttonOnTouchListener);
        
        ImageButton rightButton = (ImageButton) findViewById(R.id.rightButton);
        rightButton.setOnTouchListener(buttonOnTouchListener);
    }
    
    
    /**
     * Listener for the touch events. When action down, the user is pressing the button
     */
     OnTouchListener buttonOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			final View view = v; 
			
			Thread sendActionThread;
			
			switch (event.getAction()) {
			
				case MotionEvent.ACTION_DOWN:
					
					isClick = false;					
					sendActionThread = createSendActionThread(view.getId());										
					sendActionThread.start();
								
					break;
	
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					
					isClick = true;
					sendMessage(GamePadConstants.STOP_COMMAND);
					
					break;
				}		
			
			return false;
		}
    	 	 
     };
     
     
     /**
      * Thread to send the command but waits and send the stop command with a 130 delay
      * in case it was only a click and the arduino app didn't proccess the stop command 
      * because of itself delays
      * @param actionId the id of the view touched
      * @return
      */
     private Thread createSendActionThread(final int actionId) {
     	
     	Thread sendActionThread = new Thread() {
 			
 			@Override
 			public void run() {
 				try {
 					
 					if(!isClick) {
 						controlButtonActionDown(actionId);
 					}
 					
 					sleep(130);
 					
 					if(isClick) {
 						sendMessage(GamePadConstants.STOP_COMMAND);
 					}
 					
 				} catch (InterruptedException e) {
 					Log.e(LOG_TAG, "error in sendActionThread: )" + e);
 				}
 				
 			}
 			
 		};
 		
 		return sendActionThread;
     }
     
     
    /**
     * Send de message depending on de button pressed
     * @param viewId the id of the view pressed
     */
    public void controlButtonActionDown(int viewId) {
    	
    	switch(viewId) { 	
    			
    		case R.id.upButton:
    			sendMessage(GamePadConstants.UP_COMMAND);
				break;
    			
    		case R.id.downButton:
    			sendMessage(GamePadConstants.DOWN_COMMAND);
    			break;
    			
    		case R.id.leftButton:
    			sendMessage(GamePadConstants.LEFT_COMMAND);	
    			break;
    			
    		case R.id.rightButton:
    			sendMessage(GamePadConstants.RIGHT_COMMAND);	
    			break;
    			
    	}
    }
    
    
    /**
     * Callback method for clicking the claws buttons or the configuration button
     * @param v
     */
    public void onButtonClick(View v) {
    	
    	switch(v.getId()) {   
			
		case R.id.stopButton:
			Log.e(LOG_TAG, "stop command send");
			sendMessage(GamePadConstants.STOP_COMMAND);    				
			break;
    	}
    }
    
    
    /**
     * Callback for the changes of the connection status
     */
    @Override
    public void onConnectionStatusUpdate(int connectionState) {
      switch (connectionState) {
        case AndroidinoConstants.STATE_CONNECTED:
            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));                 
            break;
        case AndroidinoConstants.STATE_CONNECTING:
            setStatus(R.string.title_connecting);
            break;
        case AndroidinoConstants.STATE_LISTEN:
        case AndroidinoConstants.STATE_NONE:
            setStatus(R.string.title_not_connected);
            break;
      }
    }

    
    /**
     * Put the status of the connection in the action bar
     * @param resId
     */
    private final void setStatus(int resId) {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle(resId);
    }
    

    private final void setStatus(CharSequence subTitle) {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle(subTitle);
    }
         
    
    /**
     * Callback for the menu options
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.select_bot, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.connect_scan:
				requestDeviceConnection();
				return true;
			}
			return false;
    }
    

    /**
     * Callback for incoming messages from the bot
     */
	@Override
	public void onNewMessage(String message) {
		TextView receivedText = (TextView) findViewById(R.id.receivedText);
		receivedText.append(message + "\n");
	}

}
