package com.rsnorrena.app;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import spark.Filter;
import spark.ResponseTransformer;
import spark.Spark;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static spark.Spark.after;
import static spark.Spark.threadPool;

@Slf4j
public class JsonFileReadWrite {

    static String output = "";
    static List<User> userList = new ArrayList<>();
    static Gson gson = new Gson();

    public static final String USER_JSON_FILE = "users.json";

    public static void main(String[] args) {

        int maxThreads = 32;
        int minThreads = 2;
        int timeOutMillis = 30000;
        threadPool(maxThreads, minThreads, timeOutMillis);

        after((Filter) (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET");
            response.header("Access-Control-Allow-Methods", "POST");
        });

        Spark.get("/get-users", "application/json", (request, response) -> {
            return getUsers();
        });

        Spark.post("/post-user", "application/json", (request, response) -> {

            String jsonBody = request.body();
            log.info("json body is {}", jsonBody);
            User user = gson.fromJson(jsonBody, User.class);

            userList.add(user);

            writeUserListToFile();

            return gson.toJson(user);
        });

        File tempFile = new File(USER_JSON_FILE);
        boolean exists = tempFile.exists();


        if (!exists) {
            resetUserList();
            log.info("user list size: {} /n",userList.size());

            writeUserListToFile();
        }

        log.info("Read the user.json file back into memory, convert to arraylist then output details in forEach loop");

        Reader reader = null;

        try {
            reader = Files.newBufferedReader(Paths.get(USER_JSON_FILE));
        } catch (IOException e) {
            log.warn(Level.WARN.name(), "Error reading user.json file: {}", e.getMessage());
        }

        Type userListType = new TypeToken<ArrayList<User>>() {
        }.getType();

        assert reader != null;
        userList = gson.fromJson(reader, userListType);

        for (User user : userList) {
            log.info("user: {}", gson.toJson(user));
        }

    }

    private static void writeUserListToFile() {
        output = gson.toJson(userList);
        log.info("userList after conversion to json with gson");
        log.info("userList size: {} /n",userList.size());

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(USER_JSON_FILE));
            writer.write(output);
            writer.close();
        } catch (IOException e) {
            log.warn(Level.WARN.name(), "Error writing user list to file: {}", e.getMessage());
        }
    }

    private static String getUsers() {

        Path fileName = Path.of(USER_JSON_FILE);
        String data = null;

        try {
            data = Files.readString(fileName);
        } catch (IOException e) {
            log.warn(Level.WARN.name(), "Error reading user list from file: {}", e.getMessage());
        }

        log.info("data: {}", data);

        return gson.toJson(data);
    }

    private static void resetUserList() {

        User user1 = new User(1, "Scott", "snorrena@gmail.com");
        User user2 = new User(2, "Tammy", "tamarajones123@gmail.com");
        User user3 = new User(3, "Creighton", "crey@hotmail.com");
        User user4 = new User(4, "Mystery", "mystery@yahoo.com");
        List<User> users = new ArrayList<>(Arrays.asList(user1, user2, user3, user4));

        userList.clear();

        userList.addAll(users);

    }
}

class JsonTransformer implements ResponseTransformer {

    @Override
    public String render(Object model) {
        return JsonFileReadWrite.gson.toJson(model);
    }

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class User {
    private int id;
    private String name;
    private String email;
}
