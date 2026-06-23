The javascript code is loaded in the head of the index.html file and deferred until completion of the page load
the script.js file is loaded and the code executed as an IIFE immediately invoked function expression

the loadUserData async function is called to collect user data from the spark server in a fetch get request. 
If the request is successful, the data is passed to the printDataToDom function where it is converted to 
javascript object notation and added to the document object model.

The script also sets an event listener on the submit button to call the postFormData function to add a new user.
This function first validates content in the html user input fields. If successful, the data is passed in a 
fetch request body post to the Java Spark server. Once the new user data is processed and the server response returns ok, the 
script continues execution clearing the content of the input fields. Finally, the loadUserData function is called
to get a refreshed list of users from the server.

script at a glance
IIFE // the script is wrapped in an immediately invoked function expression which is executed as soon as it is defined.

Script functions:

1) postFormData
validates input fields then uses the fetch function to post data to the server

2) validateFormData
returns a boolean of true or false based on values returned from the form input field elements

3) loadUserData
make a default fetch get request and returns the user list from the spark server and passes all to the printDataToDom function

4) printDataToDom
converts the user data sent form the server from jason string to javascript object notation
populates the index field of the form input to the next value increment
parses the user data and converts to html li elements and adds all to the user data div as inner HTML
