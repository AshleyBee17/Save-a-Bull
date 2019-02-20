package edu.usf.cse.labrador.save_a_bull;

public class Coupon {

    private String CompanyName;
    private String Description;
    private String Category;
    private int Img;

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
}
