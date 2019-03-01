package edu.usf.cse.labrador.save_a_bull.sqlite.database.model;

public class Coupon {
    
    private String CompanyName;
    private String Description;
    private String Category;
    private int Img;
    private String Phone;
    private Double Longitude;
    private Double Latitude;

    public Coupon(){};

    public Coupon(String companyName, String description, String category, int img) {
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

    public Coupon(String companyName, String description, String category, int img, double lon, double lat, String phone) {
        CompanyName = companyName;
        Description = description;
        Category = category;
        Latitude = lat;
        Longitude = lon;
        Img = img;
        Phone = phone;
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

    public int getImg() {
        return Img;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public Double getLongitude() {
        return Longitude;
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

    public void setImg(int img) {
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
}
