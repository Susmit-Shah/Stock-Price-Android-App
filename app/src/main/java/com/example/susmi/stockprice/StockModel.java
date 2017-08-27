package com.example.susmi.stockprice;

/**
 * Created by susmi on 24-Aug-17.
 */

public class StockModel {
    String stockName;
    String stockPrice;
    String stockPriceChange, stockPriceChangePercent, stockNav, stockUnits;

    public void setStockName(String stockName){
        this.stockName =  stockName;
    }

    public String getStockName(){
        return stockName;
    }

    public void setStockPrice(String stockPrice){
        this.stockPrice = stockPrice;
    }

    public String getStockPrice(){
        return stockPrice;
    }

    public void setStockPriceChange(String stockPriceChange){
        this.stockPriceChange =  stockPriceChange;
    }

    public String getStockPriceChange(){
        return stockPriceChange;
    }

    public void setStockPriceChangePercent(String stockPriceChangePercent){
        this.stockPriceChangePercent =  stockPriceChangePercent;
    }

    public String getStockPriceChangePercent(){
        return stockPriceChangePercent;
    }

    public void setStockNav(String stockNav){
        this.stockNav =  stockNav;
    }

    public String getStockNav() {
        return stockNav;
    }

    public void setStockUnits(String stockUnits){
        this.stockUnits = stockUnits;
    }

    public String getStockUnits(){
        return stockUnits;
    }
}
