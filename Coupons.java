package edu.usf.cse.labrador.save_a_bull;

public class Coupons
{
    String Name; //Name of Business
    String Category;
    String Address;
    String Expire;
    String Describe;
    String ImgPath;
    int Zip;

    public Coupons(String n, String c, String a, String e, String d, String i, int z)
    {
        Name = n;
        Category = c;
        Address = a;
        Expire = e;
        Describe = d;
        ImgPath = i;
        Zip = z;
    }

    public Coupons()
    {

    }
}
