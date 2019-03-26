package edu.usf.cse.labrador.save_a_bull.sqlite.database.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {

    private long userID;
    private String fName;
    private String lName;
    private String username;
    private String password;
    public  String Line;
    public List<String> Faves;

    public User(long usrID, String name, String lastName, String userName, String pass, String line) {
        this.userID = usrID;
        this.fName = name;
        this.lName = lastName;
        username = userName;
        this.password = pass;
        this.Line = " dkjfhdbkjfhsd";
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


    public static String convertArrayToString(String[] array){
        StringBuilder str = new StringBuilder();
        for (int i = 0;i<array.length; i++) {
            str.append(array[i]);
            // Do not append comma at the end of last element
            if(i<array.length-1){
                String strSeparator = ",";
                str.append(strSeparator);
            }
        }
        return str.toString();
    }

    public static List<String> convertStringToArray(String str){
        List<String> couponIdList = new ArrayList<>();
        String strSeparator = ",";
        return  Arrays.asList(str.split(strSeparator));
    }

}