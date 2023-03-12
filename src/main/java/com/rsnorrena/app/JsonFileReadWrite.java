package com.rsnorrena.app;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

public class JsonFileReadWrite {

    static String output = "";
    static List<User> userList = new ArrayList<>();
    static Gson gson = new Gson();

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

//        Spark.get("/get-users", JsonFileReadWrite::getUsers);
        Spark.get("/get-users", "application/json", (request, response) -> {
            return getUsers();
        }, new JsonTransformer());

        Spark.post("/post-user", "application/json", (request, response) -> {
            String name, email, id;
            id = request.queryParams("id");
            name = request.queryParams("name");
            email = request.queryParams("email");
            System.out.printf("id: %s, name: %s, email: %s\n", id, name, email);
            User user = new User();
            user.setId(Integer.parseInt(id));
            user.setName(name);
            user.setEmail(email);
            userList.add(user);
            writeUserListToFile();
//            addUserToUserList(user);
//            Gson g = new Gson();
            return gson.toJson(user);
        });

        File tempFile = new File("users.json");
        boolean exists = tempFile.exists();


//        Gson gson = new Gson();

        if (!exists) {
            resetUserList();
            System.out.println("user list size: " + userList.size());
            System.out.println();

            writeUserListToFile();
        }

        System.out.println("Read the user.json file back into memory, convert to arraylist then output details in forEach loop");

        Reader reader = null;

        try {
            reader = Files.newBufferedReader(Paths.get("users.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Type userListType = new TypeToken<ArrayList<User>>() {
        }.getType();

        userList = gson.fromJson(reader, userListType);

//        List<User> users = new Gson().fromJson(reader, new TypeToken<List<User>>() {}.getType());

        for (User user : userList) {
            System.out.printf("\tid: %d, name: %s, email: %s\n", user.getId(), user.getName(), user.getEmail());
        }

    }

    private static void writeUserListToFile() {
        output = gson.toJson(userList);
        System.out.println("userList after conversion to json with gson");
        System.out.printf("\t%s\n\n", output);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("users.json"));
            writer.write(output);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addUserToUserList(User user) {
        userList.add(user);
    }

    private static String getUsers() {
//        Reader reader = null;
//
//        try {
//            reader = Files.newBufferedReader(Paths.get("users.json"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Path fileName = Path.of("users.json");
        String data = null;
        try {
            data = Files.readString(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(data);
//        Gson g = new Gson();
//        return g.toJson(reader.toString());
//        return reader.toString();
        return data;
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

//    private Gson gson = new Gson();

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
