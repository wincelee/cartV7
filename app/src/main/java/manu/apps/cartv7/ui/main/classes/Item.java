package manu.apps.cartv7.ui.main.classes;

public class Item {

    private String code, name, description, upcCode, photoBlob;
    private double discount, price;
    private String taxGroup;
    private String brand;
    private String unitOfMeasure;
    private String postingGroup;
    private String location;
    private int available,quantity;


    public Item() {

    }

    public Item(String code, String name, String description, String upcCode, String photoBlob,
                double discount, double price, String taxGroup, String brand, String unitOfMeasure,
                String postingGroup, String location, int available, int quantity) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.upcCode = upcCode;
        this.photoBlob = photoBlob;
        this.discount = discount;
        this.price = price;
        this.taxGroup = taxGroup;
        this.brand = brand;
        this.unitOfMeasure = unitOfMeasure;
        this.postingGroup = postingGroup;
        this.location = location;
        this.available = available;
        this.quantity = quantity;
    }

    // Setters

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUpcCode(String upcCode) {
        this.upcCode = upcCode;
    }

    public void setPhotoBlob(String photoBlob) {
        this.photoBlob = photoBlob;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setTaxGroup(String taxGroup) {
        this.taxGroup = taxGroup;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public void setPostingGroup(String postingGroup) {
        this.postingGroup = postingGroup;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    // Getters

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUpcCode() {
        return upcCode;
    }

    public String getPhotoBlob() {
        return photoBlob;
    }

    public double getDiscount() {
        return discount;
    }

    public double getPrice() {
        return price;
    }

    public String getTaxGroup() {
        return taxGroup;
    }

    public String getBrand() {
        return brand;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public String getPostingGroup() {
        return postingGroup;
    }

    public String getLocation() {
        return location;
    }

    public int getAvailable() {
        return available;
    }

    public int getQuantity() {
        return quantity;
    }
}
