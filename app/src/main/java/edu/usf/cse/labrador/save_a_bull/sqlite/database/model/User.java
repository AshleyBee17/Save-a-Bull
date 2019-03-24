package edu.usf.cse.labrador.save_a_bull.sqlite.database.model;

import java.util.List;

import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Coupon;

public class User {

    private String userID;
    private String fName;
    private String lName;
    private String username;
    private String password;
    public List<Coupon> Faves;

    public User(String fn, String ln, String un, String pwd)
    {
        this.fName = fn;
        this.lName = ln;
        this.username = un;
        this.password = pwd;
        this.Faves = null;
    }
    public User(String usrID, String name, String lastName, String userName, String pass) {
        this.userID = usrID;
        this.fName = name;
        this.lName = lastName;
        this.username = userName;
        this.password = pass;
        Faves = null;
    }

    public String getUserID(){
        return userID;
    }

    public String getfName(){
        return fName;
    }

    public String getlName(){
        return lName;
    }

    public List<Coupon> getFaves() {
        return Faves;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setFaves(List<Coupon> faves) {
        Faves = faves;
    }

    public String toString(){
        return "" + fName + " " + lName + "";
    }

}


//import java.util.Properties;
//
//import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Coupons;
//import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Deals;
//
//public class User
//{
//    String fname; //first name
//    String lname; //last name
//    String uname; //user name
//    String pwd; //password
//    Deals[] Faves = null; //array for favorites
//    Coupons[] Gallery = null; //array for added coupons
//
//
//
//    public User(String f, String l, String u, String p)
//    {
//        setFname(f);
//        setLname(l);
//        setUname(u);
//        setPwd(p);
//
//    }
//
//    public void setFname(String f) {
//        this.fname = f;
//    }
//
//    public String getFname() {
//        return fname;
//    }
//
//    public void setLname(String l) {
//        this.lname = l;
//    }
//
//    public String getLname() {
//        return lname;
//    }
//
//    public void setUname(String u) {
//        this.uname = u;
//    }
//
//    public String getUname() {
//        return uname;
//    }
//
//    public void setPwd(String p) {
//        this.pwd = p;
//    }
//
//    public String getPwd() {
//        return pwd;
//    }
//
//    public User()
//    {
//
//    }
//}