package settings;

public class User {
    private int userID;
    private String username;
    private String userType;


    public User(int userID, String username, String userType) {
        this.userID = userID;
        this.username = username;
        this.userType = userType;

    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

}

