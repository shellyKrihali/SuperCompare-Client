package com.example.supercompare.Model;

public class Comparison {

    private String productID, productName, quantity;
    private String priceRamiLevi, priceShufersal, priceYinotBitan, priceVictory, priceMega;

    public Comparison(String productID, String productName, String quantity, String priceRamiLevi, String priceShufersal, String priceYinotBitan, String priceVictory, String priceMega) {
        this.productID = productID;
        this.productName = productName;
        this.quantity = quantity;
        this.priceRamiLevi = priceRamiLevi;
        this.priceShufersal = priceShufersal;
        this.priceYinotBitan = priceYinotBitan;
        this.priceVictory = priceVictory;
        this.priceMega = priceMega;
    }

    public Comparison() {
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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

    public String getPriceYinotBitan() {
        return priceYinotBitan;
    }

    public void setPriceYinotBitan(String priceYinotBitan) {
        this.priceYinotBitan = priceYinotBitan;
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
}
