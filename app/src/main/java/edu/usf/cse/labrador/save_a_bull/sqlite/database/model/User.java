package edu.usf.cse.labrador.save_a_bull.sqlite.database.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private long userID;
    private String fName;
    private String lName;
    private String username;
    private String password;
    public  byte[] Faves;

    public User(long usrID, String name, String lastName, String userName, String pass, byte[] favorites) {
        this.userID = usrID;
        this.fName = name;
        this.lName = lastName;
        this.username = userName;
        this.password = pass;
        Faves = favorites;
    }

    public User(long usrID, String name, String lastName, String userName, String pass) {
        this.userID = usrID;
        this.fName = name;
        this.lName = lastName;
        this.username = userName;
        this.password = pass;
        Faves = null;
    }

    public User(){}

    public long getUserID(){
        return userID;
    }

    public String getfName(){
        return fName;
    }

    public String getlName(){
        return lName;
    }

    public byte[] getFaves() {
        return Faves;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setFaves(byte[] faves) {
        Faves = faves;
    }

    public void setUserID(long id){ userID = id;}

    public void setfName(String firstName){ fName = firstName;}

    public void setlName(String lastName) {lName = lastName;}

    public void setUsername(String usrName) {username = usrName;}

    public void setPassword(String passWord) {password = passWord;}



    public String toString(){
        return "" + fName + " " + lName + "";
    }

}