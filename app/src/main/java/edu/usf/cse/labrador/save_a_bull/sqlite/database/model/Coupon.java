package edu.usf.cse.labrador.save_a_bull.sqlite.database.model;

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
        Addr = addr;//Place = new Address(addr);
    }

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

        for(char c : chars) {
            if(Character.isLetter(c)) check = true;
            if(Character.isDigit(c)) check = false;
        }

        if(check) {
            Description = description;
        } else if(!check) {
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

    public void setExpire(String expire) {
        if(expire == "NA")
            Expire = "N/A";
        else
            Expire = expire;
    }

    public void setOther(String o) { Other = o; }
}

