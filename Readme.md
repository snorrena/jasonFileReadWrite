what is this program

this program starts a spark web server that exposed two end points to listen for get and pot requests.
the app get request return a list of user data in json string format
the post request, accepts jason data and adds a user to the database

the app saves the user data list as a json file and writes it to a file

on load the app start the spark server and exposes the rest end points

it checks for the existence of the user data json file in the root directory. If it does not exist, a new file is created and populated with seed data.
the seed data file is converted to json string and written to a file
the user data file is then read from the file and loaded into memory as a list of type user

features
Spark web server

static methods
threadPool //sets min/max threads and timeout
after //sets access controls and methods ie get, post
spark.get
spark.post

User data class set using Lombok

gson is used to convert the user list array to json prior to read and write from a local file
the gson.toJson method is used to convert the userList<User> for read and write to file

BufferedFileWriter is used to write the json file to file on local disk
BufferedWriter writer = new BufferedWriter(new FileWriter("users.json"));

Files.newBufferedReader is used to retrieve the json file
reader = Files.newBufferedReader(Paths.get("users.json"));

gson.toJson(read, userListType) is used to convert the json file back into an arrayList of type user
the toJson method takes in the buffered reader and token types as parameters and returns the user array list
the userListType is created as a type helper file used by gson to convert json back into the user array list

Type userListType = new TypeToken<ArrayList<User>>() {
}.getType();
userList = gson.fromJson(reader, userListType);


