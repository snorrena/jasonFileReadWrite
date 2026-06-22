what is this program.

this program includes two parts. 1) a java executable that includes an embedded spark server and 2) a web app that connects to that server

what does it do?

when the java program is running it exposes rest endpoints for get and post of user data. The java app host a data source of user information save to disk as a json file. The user data is loaded into memory and expose via rest endpoints on a spark web server when the jar file is executed. The javascript in the web app runs a javascript that 1)connects to the end point and returns a list of users an displays the data on the web page and 2) returns user data from an input form back to the java program to be saved in a json file.

how to run

1. execute the java program from the commandline
   java -jar jsonFileReadWrite.jar
2. run live-server in the program directory to load the index web page which in turn will run the embedded javascript file. Live-server will launch the default web browser and dislay all user data and set an input form for add new users

notes
the java app uses a spark server to expose the rest endpoints. The following urls are used by fetch in the javascript to connect to the java application
http://localhost:4567/get-user
http://localhost:4567/post-user

the javascipt code is loaded in the head of the index.html file and defered until completion of the page load
the script is loaded and executed inside an IEFE immediately invoked function expression

the loadUserData async function is called to collect user data from the spark server in a fetch get request. If the request is successful, the data is passed to the printDataToDom function where it is converted to javascript object notation and added to the document object model.

the script also sets event listeners on a submit button to call the postFormData function to add a new user.
the postFormData method first validates content in the input fields before processing the post request
if the user input is valid, the form dom element is passed to the constructor of new FormData which in turn is passed to the contructor of a new UrlSearchParms object and assigned to the variable: data. The 'data' is then passed is a fetch post to the java spark server in post request as value to the body key. On sucessful response from the server on the post request, the script executes the then function to clear the content of the input fields and calls the loadUserData function to get a refreshed list of users from the server

script at a glance
IEFE // the script is wrapped in an immediately invoked function expression
and click event listener is set on the submit button the call the postFormData function

postFormData
validates input fields then uses the fetch function to post data to the server
loads a refreshed list of users received from the server

validateFormData
returns a boolean of true or false based on values returned from the form input field elements

loadUserData
make a default fetch get request and returns the user list from the spark server and passes all to the printDataToDom function

printDataToDom
converts the user data sent form the server from jason string to javascript object notation
populates the index field of the form input to the next value increment
parses the user data and converts to html li elements and adds all to the user data div as inner HTML
