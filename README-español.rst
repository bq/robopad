=======
RoboPad
=======

RoboPad es una aplicación Android para controlar robots a través del Bluetooth del dispositivo móvil. Puedes elegir distintos tipos de robots con sus mandos de control específicos de cada uno. Todos los robots deben utilizar una placa Arduino y un módulo Bluetooth.

Los tipos de robots que se pueden seleccionar hoy en día son aquellos diseñados por la empresa Bq, además de cualquier otro que sea controlado por una placa Arduino.

Puedes usar una impresora 3D para crear tu printbot (Renacuajo, Escarabajo y Rhino). Tanto los archivos con las partes imprimibles como el código Arduino de cada uno de los printbots de Bq se puede descargar desde la web http://diy.bq.com/printbots/ (el código no está disponible aún en la web).

Hay un mando de control para robots genéricos que te permite controlar tu propio robot con hasta 6 funcionalidades más la cruceta de movimientos o bien aumentar el número de funcionalidades de algún printbot ya existente con los 6 botones de comandos.

Estos 6 botones mandan el siguiente carácter a la placa Arduino:

| Botón 1 - '1'
| Botón 2 - '2'
| Botón 3 - '3'
| Botón 4 - '4'
| Botón 5 - '5'
| Botón 6 - '6'


Características
===============

#. Controla robots que utilicen una placa Arduino a través del Bluetooth de tu dispositivo móvil

#. 6 botones en el tipo de robot genérico para usar en tus propios robots

#. Mandos de control específicos para los printbots Renacuajo, Escarabajo y Rhino de Bq.


Instalación
===========

#. Sigue las instrucciones del proyecto en github de `droid2ino <https://github.com/bq/droid2ino>`_ para instalar la librería droid2ino, el Android SDK y la librería de compatibilidad de Android v7.

#. Importa el proyecto RoboPad en Eclipse in ``File`` > ``Import`` > ``Existing Projects into Workspace`` and busca el proyecto RoboPad.

#. Puede que tengas que actualizar la referencia de la librería droid2ino del siguiente modo:  
	
   - En Eclipse, selecciona tu proyecto en la ventana de ``Package Explorer`` > ``File`` > ``Properties`` > ``Android``

   - Elimina la referencia errónea de la librería droid2ino

   - Pulsa en el botón de ``Add`` y selecciona la librería droid2ino

#. Sube el código Arduino adecuado a tu robot. Puedes encontrarlo en la carpeta Arduino de este proyecto o en la `web de DIY de Bq  <http://diy.bq.com/printbots/>`_ (el código no está disponible aún en la web).


Requisitos
==========

- Android SDK

- Eclipse IDE (recomendado)

- Arduino IDE
  
- Placa Arduino con módulo Bluetooth


Limitaciones
============

- Para evitar el problema de mostrar mensajes recibidos por parte de la placa Arduino vacíos o partidos, la librería droid2ino utiliza una serie de carácteres de escape. 
 
  - Carácter de escape de inicio del mensaje: ``&&`` 

  - Carácter de escape de fin del mensaje : ``%%``

  Por lo tanto, un ejemplo de cómo el programa Arduino tiene que mandar un mesaje sería::

	  &&Hola mundo desde Arduino%%

- El mando de control de robot genérico tiene 6 botones que pueden ser usados para dotar a tu propio robot de más funcionalidad. Estos botones mandan los mensajes '1', '2', '3', '4', '5' y '6' respectivamnete a la placa Arduino.


Licencia
========

RoboPad es distribuido en términos de la licencia GPL. Consulte la web http://www.gnu.org/licenses/ para más detalles.
