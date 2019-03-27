package edu.usf.cse.labrador.save_a_bull.sqlite.database.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class User {

    public static final String USER_DB_TABLE = "users";
    public static final String USER_KEY_ROWID = "_id";
    public static final String USER_KEY_FIRST_NAME = "fName";
    public static final String USER_KEY_LAST_NAME = "lName";
    public static final String USER_KEY_USERNAME = "username";
    public static final String USER_KEY_PASSWORD = "password";
    public static final String USER_KEY_FAVORITES = "favorites";

    private long userID;
    private String fName;
    private String lName;
    private String username;
    private String password;
    public  String Line;
    public List<String> Faves;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + USER_DB_TABLE + "("
                    + USER_KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + USER_KEY_FIRST_NAME + " TEXT,"
                    + USER_KEY_LAST_NAME + " TEXT,"
                    + USER_KEY_USERNAME + " TEXT,"
                    + USER_KEY_PASSWORD + " TEXT,"
                    + USER_KEY_FAVORITES + " TEXT"
                    + ")";

    public User(long usrID, String name, String lastName, String userName, String pass, String line) {
        this.userID = usrID;
        this.fName = name;
        this.lName = lastName;
        username = userName;
        this.password = pass;
        this.Line = line;
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

    public String getLine() {
        return Line;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setLine(String line) {
        Line = line;
    }

    public void setUserID(long id){ userID = id;}

    public void setfName(String firstName){ fName = firstName;}

    public void setlName(String lastName) {lName = lastName;}

    public void setUsername(String usrName) {username = usrName;}

    public void setPassword(String passWord) {password = passWord;}

    public String toString(){
        return "" + fName + " " + lName + "";
    }


    public static String convertArrayToString(List<String> array){
        StringBuilder str = new StringBuilder();
        for (int i = 0;i<array.size(); i++) {
            str.append(array.get(i));
            // Do not append comma at the end of last element
            if(i<array.size()-1){
                String strSeparator = ",";
                str.append(strSeparator);
            }
        }
        return str.toString();
    }

    public static List<String> convertStringToArray(String str){
        List<String> couponIdList = new ArrayList<>();
        String strSeparator = ",";
        //return  Arrays.asList(str.split(strSeparator));
        return new LinkedList<>(Arrays.asList(str.split(strSeparator)));
    }

}