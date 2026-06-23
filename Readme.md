jsonFileReadWrite is a programing exercise that demonstrates how to read and write json files in Java using the Gson library.
The project also includes a simple web server built using Apache Spark that allows users to interact with the program through
HTTP requests.

The main source code in this repository uses Maven to build a Java application into an executable jar file. Run of the Jar
starts a Apache Spark web server that exposes two http method end points to listen for requests on the following urls.

http://localhost:4567/get-users
http://localhost:4567/post-user

The get request returns a list of users. The post request is used to add data for a new user.

On first launch, the app will look for a local data file named "user.json" in the root context. If the file exists, the user 
data is read into memory and logged out to console. If the file does not exist, the app will seed the program with a small set
of user data and write a new "user.json" file to the program directory. The app will then wait for http requests to be received.

If a http post request is received including new user data in the method body, the app will add the new user to the in memory
user data list. The updated list is then saved back to the file system in the"user.json" file and the new list is console
logged out to confirm completion of the update.

The second part of this project is a html/Javascript application saved in the webApp folder. The Javascript loaded in a simple
web page uses the fetch api to send get and post requests to the java application. The web app is the web browser interface that 
allows a user to view the list of users and add new users to the list.

Program dependencies are managed using Maven. The following dependencies are used in this project:
lombok library for data class generation
Gson library for json read and write
slf4j library for logging
Node is used to run the web application in the webApp folder.

The following node modules are used in the web application:
concurrently for running the node application and the java application at the same time.
live-server for running a simple web server to serve the web application.

Java 8 is used to run the Apache Spark server application. 
Maven is used to build the project and manage dependencies. 

How to build and run the project:

The project is built using the command "mvn clean package" run in the program root directory. This command creates an executable 
jar file in the target directory.

Navigate the webApp directory

Run the command "npm run start" to start the Spark Server and open the index.html page in the default web browser
with Live-Server. The web page will display a list of users and allow the user to add new users to the list.

Tip:
Navigate to the target folder and run the command "java -jar jsonFileReadWrite-1.0-SNAPSHOT.jar" to start the Spark server.
Postman can be used to test the get and post requests to the Spark server. The get request can be tested by sending a GET 
request to http://localhost:4567/get-users. The post request can be tested by sending a POST request to 
http://localhost:4567/post-user with a JSON body containing the new user data. 
ex. {id:1, name:"John Doe", email:"john.doe@example.com"}
