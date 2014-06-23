Generic_Classification_App
==========================

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
The activity package contains all activities and Fragments for the UserScribbleMainActivity.
The dialog package contains all DialogFragment classes of the project.
In the view package are all view classes stored.
The picture package contains the Picture class which represents the picture taken by the camera.
In the http package is the RetrieveHttpTask which gets created and executed when the user wants
 to send the picture and his scribbles to a server.

Transmission to Server:
When the user send the taken picture with his scribbles to the server the RetrieveHttpTask gets executed. During this task a JSONObject gets send to a server that includes the byte array of the picture, a byte array of another picture which contains only the scribbles from the user. 
Furthermore the JSONObject stores the information if the picture is taken in landscape or in portrait and a tag which says gives information what kind scribbles the user draw. 
Should the scribbles represent a Minimum Bounding Box the tag is „MINIMUM_BOUNDING_BOX“. For an Object Contour it is „OBJECT_CONTOUR“. Did the user select the foreground it is „FOREGROUND“ and for the background it is „BACKGROUND“.

Usage:
The application starts with the Camera Activity during which the user can take his picture.
After that he gets to the Picture Activity. Here the captured picture is shown.
For the next step the user has to decide how he wants to select the object  on the picture.
He can choose between drawing a Minimum Bounding Box, selecting the Foreground or Background or drawing the Object Contour. For that he has to select one of the corresponding buttons in the button bar.
Then the UserScribbleMain Activity starts with the corresponding Fragment for the previous selection.
Now the user can draw his user scribbles. Is he finished he can send the picture with his scribbles to a server for classification. For that he has to select the Send to Server option in the menu dialog.
The Transmission to a server starts and returns a message with the result.
