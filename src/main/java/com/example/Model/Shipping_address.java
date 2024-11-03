package com.example.Model;

public class Shipping_address {
    private int adress_id;
    private int order_id;
    private String street_adress;
    private String city;
    private String commune;
    private String zip_code;
    private String contry;

    public Shipping_address() {
    }

    
    
    public Shipping_address(int adress_id, int order_id, String street_adress, String city, String commune, String zip_code, String contry) {
        this.adress_id = adress_id;
        this.order_id = order_id;
        this.street_adress = street_adress;
        this.city = city;
        this.commune = commune;
        this.zip_code = zip_code;
        this.contry = contry;
    }

    public Shipping_address(int order_id, String street_adress, String city, String commune, String zip_code, String contry) {
        this.order_id = order_id;
        this.street_adress = street_adress;
        this.city = city;
        this.commune = commune;
        this.zip_code = zip_code;
        this.contry = contry;
    }

    public int getAdress_id() {
        return adress_id;
    }

    public void setAdress_id(int adress_id) {
        this.adress_id = adress_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getStreet_adress() {
        return street_adress;
    }

    public void setStreet_adress(String street_adress) {
        this.street_adress = street_adress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getContry() {
        return contry;
    }

    public void setContry(String contry) {
        this.contry = contry;
    }
    
    
}
