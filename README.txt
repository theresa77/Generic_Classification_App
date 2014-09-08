Generic Classification App


Developed by Theresa Froeschl

This application can record images and allows to enter user scribbles for object selection. 
The user can send the image and his scribbles to some server for classification and gets a 
response message as result back.

Build and Run:
To Run the application on an android mobile device the USB debugging on the device must be enabled. 
This is usually in the Developer Settings to find.
The Project was developed with ADT. Thus, the project can be easily imported in Eclipse or ADT. 
To built and run it, you just have to select Run > Run or click on the Run-button.
Eclipse will then compile the project, create a run configuration and automatically install 
the application in the linked device.
For debugging you can select Run > Debug or click on the Debug-button.

Known Bugs:
It can happen that the application has problems during starting
to get access to the camera of the device. In this situation you just have
to restart the application.

Used Libraries:
	Android 4.4 
	android-support-v4

Packages:
The Project is split into 5 packages.
The activity package contains all activities and fragments for the UserScribbleMainActivity.
The dialog package contains all DialogFragment classes of the project.
In the view package are all view classes stored.
The domain package contains the Picture class which represents the picture taken by the camera, all classes which represent the scribbles types the user can draw and the Scribble interface for the method an scribble class has to implement.
In the http package is the RetrieveHttpTask which gets created and executed when the user wants
to send the picture and his scribbles to a server.

Transmission to Server:
When the user wants to send the taken picture with his scribbles to the server the RetrieveHttpTask gets executed. During this task a JSONObject gets send to a server by an http post request. For that request the IP address and the port for the server are needed. These two values are stored in the server.xml file in the values folder.

The JSONObject contains following name-values pairs:
- "picture": the corresponding values is a byte array of the taken photo
- "landscape": boolean value for the information whether the photo is in landscape (boolean is true) or in portrait (boolean is false)
- "foreground-background": byte array of another picture with the same size as the photo which comprise all foreground-background scribbles only
- "object-contour": byte array of an third picture with the same size as the photo for all object-contour scribbles
- "min-bounding-rectangle": byte array for the bounds of all drawn minimum-bounding-rectangles. These array has to be converted into an simple array of integer to get the bounds. The different drawn rectangles were saved one after another into that integer array. So every rectangle is composed of four integers in the following order: left, top, right, bottom.

The JSONObject has always these 5 values stored, even if there is only one type of scribbles for the picture. e.g. has the user only drawn one minimum-bounding-rectangle the values for the foreground-background and object-contour scribbles are just two empty pictures.


Usage:
The application starts with the Camera Activity during which the user can take his picture.
After that he gets to the Picture Activity. Here the captured picture is shown.
For the next step the user has to decide how he wants to select the object on the picture.
He can choose between drawing a Minimum Bounding Box, selecting the Foreground and Background or drawing the Object Contour. For that he has to select one of the corresponding buttons in the button bar.
Then the UserScribbleMain Activity starts with the corresponding Fragment for the previous selection.
Now the user can draw his scribbles. Is he finished he can send the picture with his scribbles to a server for classification. For that he has to select the Send to Server option in the menu dialog.
The Transmission to a server starts and returns a message with the result.