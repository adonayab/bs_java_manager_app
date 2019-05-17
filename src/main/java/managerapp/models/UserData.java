package managerapp.models;

import java.util.ArrayList;

public class UserData {

    static ArrayList<OldUser> oldUsers = new ArrayList<>();

    // getAll

    public static ArrayList<OldUser> getAll() {
        return oldUsers;
    }

    // add
    public static void add(OldUser newOldUser) {
        oldUsers.add(newOldUser);
    }

    // getById
    public static OldUser getById(int id) {
        OldUser theOldUser = null;
        for (OldUser candidateOldUser : oldUsers) {
            if (candidateOldUser.getUserId() == id) {
                theOldUser = candidateOldUser;
            }
        }
        return theOldUser;
    }
}


