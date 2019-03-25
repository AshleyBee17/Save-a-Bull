package edu.usf.cse.labrador.save_a_bull.sqlite.database.model;

import android.location.Location;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;

import edu.usf.cse.labrador.save_a_bull.sqlite.database.model.Address;

public class Coupon {


    private String Id;
    private String CompanyName;
    private String Description;
    private String Other;
    private String Category;
    private String Img;
    private String Phone;
    private Double Longitude;
    private Double Latitude;
    private String Expire;
    private String Addr;
    private Address Place;

    public Coupon(){}

    public Coupon(String id, String name, String desc, String category, String img, String phone, String expire, String addr){
        Id = id;
        CompanyName = name;
        Description = desc;
        Category = category;
        Img = img;
        Phone = phone;
        Expire = expire;
        Place = new Address(addr);
    }

//    public Coupon(String addr, String cat, String name, String des, String expire, String other, String phone)
//    {
//        setAddr(addr);
//        setCategory(cat);
//        setCompanyName(name);
//        setDescription(des);
//        setPhone(phone);
//        setExpire(expire);
//        setOther(other);
//        place = new Address(addr);
//    }
//
//    public Coupon(String id, String addr, String cat, String name, String des, String expire, String other, String phone)
//    {
//        setId(id);
//        setAddr(addr);
//        setCategory(cat);
//        setCompanyName(name);
//        setDescription(des);
//        setPhone(phone);
//        setExpire(expire);
//        setOther(other);
//        place = new Address(addr);
//    }
//
//    public Coupon(String id, String addr, String cat, String name, String des, String expire, byte[] img, String phone)
//    {
//        setId(id);
//        setAddr(addr);
//        setCategory(cat);
//        setCompanyName(name);
//        setDescription(des);
//        setPhone(phone);
//        setExpire(expire);
//        setImg(img);
//        place = new Address(addr);
//    }
//
//    public Coupon(String id, String companyName, String description, String category, byte[] img, String phone, Double longitude, Double latitude) {
//        Id = id;
//        CompanyName = companyName;
//        Description = description;
//        Category = category;
//        Img = img;
//        Phone = phone;
//        Longitude = longitude;
//        Latitude = latitude;
//    }
//
//    public Coupon(String name, String des, String cat, String phone, String addr)
//    {
//        CompanyName = name;
//        Description = des;
//        Category = cat;
//        Phone = phone;
//        Addr = addr;
//    }
//
//    public Coupon(String companyName, String description, String category, byte[] img) {
//        CompanyName = companyName;
//        Description = description;
//        Category = category;
//        Img = img;
//    }
//
//    public Coupon(String companyName, String description, String category) {
//        CompanyName = companyName;
//        Description = description;
//        Category = category;
//    }
//
//    public Coupon(String companyName, String description, String category, byte[] img, double lon, double lat, String phone) {
//        CompanyName = companyName;
//        Description = description;
//        Category = category;
//        Latitude = lat;
//        Longitude = lon;
//        Img = img;
//        Phone = phone;
//    }
//
//    public Coupon(String companyName, String description, String category, int img, double lon, double lat, String phone) {
//        CompanyName = companyName;
//        Description = description;
//        Category = category;
//        //Img = img;
//        Latitude = lat;
//        Longitude = lon;
//        Phone = phone;
//
//    }

    public String getId() {
        return Id;
    }

    public String getAddress() {
        return Latitude +","+ Longitude;
    }

    public String getPhone() {
        return Phone;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public String getDescription() {
        return Description;
    }

    public String getCategory() {
        return Category;
    }

    public String getImg() {
        return Img;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public String getAddr() {return Addr;}

    public String getExpire() { return Expire; }

    public String getOther() { return Other; }

    public void setId(String id) {
        Id = id;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public void setDescription(String description)
    {
        boolean check = true;  //true for letters, false for num
        char[] chars = description.toCharArray();

        for(char c : chars)
        {
            if(Character.isLetter(c))
                check = true;

            if(Character.isDigit(c))
                check = false;
        }

        if(check == true)
        {
            Description = description;
        }

        else if(check == false)
        {
            Description = description + "% off";
        }


        Description = description;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public void setImg(String img) {
        Img = img;
    }

    public void setPhone(String phone)
    {
        Phone = phone;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public void setAddr(String addr) { Addr = addr; }

    public void setExpire(String expire)
    {
        if(expire == "NA")
            Expire = "N/A";
        else
            Expire = expire;
    }

    public void setOther(String o) { Other = o; }
}
//package edu.usf.cse.labrador.save_a_bull.sqlite.database.model;
//
//import android.location.Location;
//import android.os.Parcelable;
//
//import edu.usf.cse.labrador.save_a_bull.Address;
//
//public class Coupon {
//
//
//    private String Id;
//    private String CompanyName;
//    private String Description;
//    private String Other;
//    private String Category;
//    private byte[] Img;
//    private String Phone;
//    private Double Longitude;
//    private Double Latitude;
//    private String Expire;
//    private String Addr;
//    private Address place;
//
//    public Coupon(String addr, String cat, String name, String des, String expire, String other, String phone)
//    {
//        setAddr(addr);
//        setCategory(cat);
//        setCompanyName(name);
//        setDescription(des);
//        setPhone(phone);
//        setExpire(expire);
//        setOther(other);
//        place = new Address(addr);
//    }
//
//    public Coupon(String id, String addr, String cat, String name, String des, String expire, String other, String phone)
//    {
//        setId(id);
//        setAddr(addr);
//        setCategory(cat);
//        setCompanyName(name);
//        setDescription(des);
//        setPhone(phone);
//        setExpire(expire);
//        setOther(other);
//        place = new Address(addr);
//    }
//    public Coupon(String id, String companyName, String description, String category, byte[] img, String phone, Double longitude, Double latitude) {
//        Id = id;
//        CompanyName = companyName;
//        Description = description;
//        Category = category;
//        Img = img;
//        Phone = phone;
//        Longitude = longitude;
//        Latitude = latitude;
//    }
//
//    public Coupon(String name, String des, String cat, String phone, String addr)
//    {
//        CompanyName = name;
//        Description = des;
//        Category = cat;
//        Phone = phone;
//        Addr = addr;
//    }
//
//    public Coupon(String companyName, String description, String category, byte[] img) {
//        CompanyName = companyName;
//        Description = description;
//        Category = category;
//        Img = img;
//    }
//
//    public Coupon(String companyName, String description, String category) {
//        CompanyName = companyName;
//        Description = description;
//        Category = category;
//    }
//
//    public Coupon(String companyName, String description, String category, byte[] img, double lon, double lat, String phone) {
//        CompanyName = companyName;
//        Description = description;
//        Category = category;
//        Latitude = lat;
//        Longitude = lon;
//        Img = img;
//        Phone = phone;
//    }
//
//    public Coupon(String companyName, String description, String category, int img, double lon, double lat, String phone) {
//        CompanyName = companyName;
//        Description = description;
//        Category = category;
//        //Img = img;
//        Latitude = lat;
//        Longitude = lon;
//        Phone = phone;
//
//    }
//
//    public String getId() {
//        return Id;
//    }
//
//    public String getAddress() {
//        return Latitude +","+ Longitude;
//    }
//
//    public String getPhone() {
//        return Phone;
//    }
//
//    public String getCompanyName() {
//        return CompanyName;
//    }
//
//    public String getDescription() {
//        return Description;
//    }
//
//    public String getCategory() {
//        return Category;
//    }
//
//    public byte[] getImg() {
//        return Img;
//    }
//
//    public Double getLatitude() {
//        return Latitude;
//    }
//
//    public Double getLongitude() {
//        return Longitude;
//    }
//
//    public String getAddr() {return Addr;}
//
//    public String getExpire() { return Expire; }
//
//    public String getOther() { return Other; }
//
//    public void setId(String id) {
//        Id = id;
//    }
//
//    public void setCompanyName(String companyName) {
//        CompanyName = companyName;
//    }
//
//    public void setDescription(String description)
//    {
//        boolean check = true;  //true for letters, false for num
//        char[] chars = description.toCharArray();
//
//        for(char c : chars)
//        {
//            if(Character.isLetter(c))
//                check = true;
//
//            if(Character.isDigit(c))
//                check = false;
//        }
//
//        if(check == true)
//        {
//            Description = description;
//        }
//
//        else if(check == false)
//        {
//            Description = description + "% off";
//        }
//
//
//        Description = description;
//    }
//
//    public void setCategory(String category) {
//        Category = category;
//    }
//
//    public void setImg(byte[] img) {
//        Img = img;
//    }
//
//    public void setPhone(String phone)
//    {
//        Phone = phone;
//    }
//
//    public void setLatitude(double latitude) {
//        Latitude = latitude;
//    }
//
//    public void setLongitude(double longitude) {
//        Longitude = longitude;
//    }
//
//    public void setAddr(String addr) { Addr = addr; }
//
//    public void setExpire(String expire)
//    {
//        if(expire == "NA")
//            Expire = "N/A";
//        else
//            Expire = expire;
//    }
//
//    public void setOther(String o) { Other = o; }
//}
