package edu.usf.cse.labrador.save_a_bull.sqlite.database.model;
import java.util.Date;

public class Deals
{
    String Name;
    String ImgPath;
    String Category;
    String Describe;
    String Address;
    int ZipCode;
    String Expiration;

    public Deals(String name, String category, String describe, String address, int zipCode, String expiration)
    {
        Name = name;
        Category = category;
        Describe = describe;
        Address = address;
        ZipCode = zipCode;
        Expiration = expiration;

    }

    public Deals()
    {

    }
}