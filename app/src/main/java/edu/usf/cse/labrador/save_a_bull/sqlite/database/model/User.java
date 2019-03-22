package edu.usf.cse.labrador.save_a_bull.sqlite.database.model;
import java.util.Properties;

import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Coupons;
import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Deals;

public class User
{
    String fname; //first name
    String lname; //last name
    String uname; //user name
    String pwd; //password
    Deals[] Faves = null; //array for favorites
    Coupons[] Gallery = null; //array for added coupons



    public User(String f, String l, String u, String p)
    {
        setFname(f);
        setLname(l);
        setUname(u);
        setPwd(p);

    }

    public void setFname(String f) {
        this.fname = f;
    }

    public String getFname() {
        return fname;
    }

    public void setLname(String l) {
        this.lname = l;
    }

    public String getLname() {
        return lname;
    }

    public void setUname(String u) {
        this.uname = u;
    }

    public String getUname() {
        return uname;
    }

    public void setPwd(String p) {
        this.pwd = p;
    }

    public String getPwd() {
        return pwd;
    }

    public User()
    {

    }
}