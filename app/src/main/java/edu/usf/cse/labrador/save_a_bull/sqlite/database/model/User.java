package edu.usf.cse.labrador.save_a_bull.sqlite.database.model;

public class User {

    private long userID;
    private String fName;
    private String lName;
    private String username;
    private String password;

    public User(long usrID, String name, String lastName, String userName, String pass) {
        this.userID = usrID;
        this.fName = name;
        this.lName = lastName;
        this.username = userName;
        this.password = pass;
    }

    public long getUserID(){
        return userID;
    }

    public String getfName(){
        return fName;
    }

    public String getlName(){
        return lName;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String toString(){
        return "" + fName + " " + lName + "";
    }
}

