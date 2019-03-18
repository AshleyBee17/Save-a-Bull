package edu.usf.cse.labrador.save_a_bull.sqlite.database.model;

import java.util.Date;

public class Coupon {

    public static final String TABLE_NAME = "Coupon";

    public static final String COLUMN_ID = "Id";
    public static final String COLUMN_COMPANY_NAME = "CompanyName";
    public static final String COLUMN_DESCRIPTION = "Description";
    public static final String COLUMN_CATEGORY = "Category";
    public static final String COLUMN_IMAGE = "Image";
    public static final String COLUMN_PHONE = "Phone";
    public static final String COLUMN_LONGITUDE = "Longitude";
    public static final String COLUMN_LATITUDE = "Latitude";
    public static final String COLUMN_EXPIRY = "Expiry";

    private int Id;
    private String CompanyName;
    private String Description;
    private String Category;
    private byte[] Img;
    private String Phone;
    private Double Longitude;
    private Double Latitude;
    private String Expiry;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_COMPANY_NAME + " TEXT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_CATEGORY + " TEXT,"
                    + COLUMN_IMAGE + " BLOB,"
                    + COLUMN_PHONE + " TEXT,"
                    + COLUMN_LONGITUDE + " TEXT,"
                    + COLUMN_LATITUDE + " TEXT,"
                    + COLUMN_EXPIRY + "TEXT"
                    + ")";


    public Coupon(String string, String cString, String category, int type){};

    public Coupon(int id, String companyName, String description, String category, byte[] img, String phone, Double longitude, Double latitude) {
        Id = id;
        CompanyName = companyName;
        Description = description;
        Category = category;
        Img = img;
        Phone = phone;
        Longitude = longitude;
        Latitude = latitude;
    }

    public Coupon(String companyName, String description, String category, byte[] img) {
        CompanyName = companyName;
        Description = description;
        Category = category;
        Img = img;
    }

    public Coupon(String companyName, String description, String category) {
        CompanyName = companyName;
        Description = description;
        Category = category;
    }

    public Coupon(String companyName, String description, String category, byte[] img, double lon, double lat, String phone) {
        CompanyName = companyName;
        Description = description;
        Category = category;
        Latitude = lat;
        Longitude = lon;
        Img = img;
        Phone = phone;
    }

    public Coupon() { }

    public Coupon(String companyName, String description, String category, int img, double lon, double lat, String phone) {
        CompanyName = companyName;
        Description = description;
        Category = category;
        //Img = img;
        Latitude = lat;
        Longitude = lon;
        Phone = phone;

    }

    public Coupon(String companyName, String description, String category, int image, String expiry) {
//        CompanyName = companyName;
//        Description = description;
//        Category = category;
//        Img = image;
//        Expiry = expiry;
    }

    public int getId() {
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

    public byte[] getImg() {
        return Img;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public String getExpiry() {
        return Expiry;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public void setImg(byte[] img) {
        Img = img;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public void setExpiry(String expiry) {
        Expiry = expiry;
    }
}
