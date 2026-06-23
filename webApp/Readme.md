Launch of this web app is done by running a script named "start" defined in the package.json file. 

npm run start

The script uses the concurrently node module to run both the Java Spark server and the Live-Server web server at the same time. 
By default run of Live-Server opens the index.html page in the program root directory in the system default web browser.

The index.html file includes a simple web page that defines a form with input fields for user data and a submit button. 
The page also includes a div element to display the list of users.

A Javascript script source file is defined in the head of the index.html file and set to execute after load
of the page. The script is coded as an IIFE (immediately invoked function expression) which allows for immediate execution
of the script and avoids polluting the global scope.

The functions defined in the IIFE script are used to handle user input and display user data on the web page.
Various Fetch methods are used to send and receive data from the Java Spark server. Fetch API is a modern, 
built-in global interface used to make asynchronous HTTP requests over the network.

The loadUserData function is called to collect and return user data in an http get request method. 
If the request is successful, the response data is passed to the printDataToDom function where it is converted to 
javascript object notation and added to the document object model.

The script also sets an event listener on the submit button to call the postFormData function to add a new user.
This function first validates content in the html user input fields. If valid, the data is converted to a JSON string and 
passed in a fetch request body post to the Java app. Once the new user data is processed and the server response returns ok, the 
script continues execution clearing the content of the input fields. Finally, the loadUserData function is called
to get a refreshed list of users to update the DOM.

On the front end, the user data is processed as Javascript object and as JSON string. On load of the web page, a
data JSON string is passed by the Spark server to the web page in the response body of a get request. The JSON string 
is converted to a Javascript object array list of users and used to populate the DOM with user data. JSON.stringify is 
used to convert the collected html form data for a new user to Javascript object then to JSON string for sending in the 
body of a fetch post request to the Spark server.

On the back end, the GSON library is used to convert the JSON string to a Java object and vice versa. The Java
object is used to update the in memory user data list and write the updated list back to the user.json file.
The updated list is also returned in the response body of the post request to the web page. The web page then uses 
the updated list to refresh the DOM with the new user data.
