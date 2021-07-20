package com.example.supercompare.Model;

//products in FireBase DB
public class Product implements Comparable{
    private String name,description,image,menuID;
    private String priceRamiLevi,priceShufersal,priceVictory,priceMega,priceYinotBitan;

    public Product() {
    }

    public Product(String name, String description, String image, String menuID, String priceRamiLevi, String priceShufersal, String priceVictory, String priceMega, String priceYinotBitan) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.menuID = menuID;
        this.priceRamiLevi = priceRamiLevi;
        this.priceShufersal = priceShufersal;
        this.priceVictory = priceVictory;
        this.priceMega = priceMega;
        this.priceYinotBitan = priceYinotBitan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMenuID() {
        return menuID;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }

    public String getPriceRamiLevi() {
        return priceRamiLevi;
    }

    public void setPriceRamiLevi(String priceRamiLevi) {
        this.priceRamiLevi = priceRamiLevi;
    }

    public String getPriceShufersal() {
        return priceShufersal;
    }

    public void setPriceShufersal(String priceShufersal) {
        this.priceShufersal = priceShufersal;
    }

    public String getPriceVictory() {
        return priceVictory;
    }

    public void setPriceVictory(String priceVictory) {
        this.priceVictory = priceVictory;
    }

    public String getPriceMega() {
        return priceMega;
    }

    public void setPriceMega(String priceMega) {
        this.priceMega = priceMega;
    }

    public String getPriceYinotBitan() {
        return priceYinotBitan;
    }

    public void setPriceYinotBitan(String priceYinotBitan) {
        this.priceYinotBitan = priceYinotBitan;
    }

    @Override
    public int compareTo(Object o) {
        Product p = (Product)o;
        if (this.getName().toLowerCase().contains(p.getName().toLowerCase())){
            return 1;
        }
        else
            return 0;
    }
}
