package managerapp.models;

import java.util.ArrayList;

public class UserData {

    static ArrayList<User> users = new ArrayList<>();

    // getAll

    public static ArrayList<User> getAll() {
        return users;
    }

    // add
    public static void add(User newUser) {
        users.add(newUser);
    }

    // getById
    public static User getById(int id) {
        User theUser = null;
        for (User candidateUser: users) {
            if (candidateUser.getUserId() == id) {
                theUser = candidateUser;
            }
        }
        return theUser;
    }
}


