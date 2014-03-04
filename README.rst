=======
GAMEPAD
=======

GamePad is an Android app that consists on a robots controller via the device's Bluetooth. You have several types of robots with differents types of control for each of them. The robots must be controlled by a Arduino board with a Bluetooth module.

Right now, the type of robot that you can choose are the printbots designed in the Bq company or whichever robot with an Arduino board. 

You can use a 3D printer to create your printbot (Pollywog, Beetle and Rhino). You can find the printable parts and the code of each one in http://diy.bq.com/printbots/ (the code in the web is not available yet).

You can use the generic robot controller if you create your own robot with its own Arduino code or if you wants to add more functionality to the other ones, through the buttons 1 to 6.

The buttons send the following character to the Arduino board: 

| Button 1 - '1'
| Button 2 - '2'
| Button 3 - '3'
| Button 4 - '4'
| Button 5 - '5'
| Button 6 - '6'


Features
========

#. Control Arduino robots via Bluetooth

#. 6 buttons in the generic robot mode for use as you want in your Arduino code

#. Specific controls for the Pollywog, Beetle and Rhino printbots of Bq


Installation
============

#. Follow the instructions in the github page of `droid2ino <https://github.com/bq/droid2ino>`_ in order to install the android SDK and the droid2ino library.

#. Import the GamePad project in Eclipse in ``File`` > ``Import`` > ``Existing Projects into Workspace`` and browse to the GamePad project.

#. Maybe, you will need to update the reference of droid2ino this way:  
	
   - In Eclipse, select your project in the ``Package Explorer`` > ``File`` > ``Properties`` > ``Android`` 

   - Remove the previous wrong library reference.

   - Press the ``Add`` button and select the droid2ino library. 

#. Upload the Arduino code to your robot. You can find it in the Arduino folder of this project or in `DIY web of Bq  <http://diy.bq.com/printbots/>`_ (the code in the web is not available yet).


Requirements
============

- Android SDK

- Eclipse IDE (recommended)
  
- Arduino IDE

- Arduino board with Bluetooth


Limitations
===========

- In order to fix the problem when receiving the messages from the Arduino board of obtaining empty strings or divided strings, the droid2ino library uses escape characters in order to obtaining the entire message well.
 
  - Start escape characters: ``&&`` 

  - End escape characters : ``%%``

  So an example of a message written by the Arduino program for a generic robot would be::

	  &&I write from Arduino%%

- The generic robot has 6 buttons that sends the commands '1', '2', '3', '4', '5' and '6' respectively to the Arduino board.


License
=======

GamePad is distributed in terms of GPL license. See http://www.gnu.org/licenses/ for more details.
