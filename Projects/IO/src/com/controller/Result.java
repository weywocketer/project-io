package com.controller;

public class Result implements Comparable<Result>{

    private String name;
    private String link;
    private Double cost;
    private Double min_shipping;
    private Double max_shipping;
    private Double sum;
    private int shop_id; //id sklepu


    public Result(){}

    public Result(String name, String link){
        this.name = name;
        this.link = link;
    }

    public Result(String name, String link, Double cost, Double min_shipping,Double max_shipping, Integer shop_id){
        this.name = name;
        this.cost = cost;
        this.min_shipping = min_shipping;
        this.max_shipping = max_shipping;
        this.link = link;
        this.sum = cost + min_shipping;
        this.shop_id = shop_id;
    }

    public String getName(){return name;}

    public String getLink(){return link;}

    public Double getCost(){return cost;}

    public Double getMin_Shipping(){return min_shipping;}

    public Double getMax_shipping(){return max_shipping;}

    public Double getSum(){return sum;}

    public Integer getShop_id(){return shop_id;}

    public void setName(String name){this.name = name;}

    public void setLink(String link){this.link = link;}

    public void setCost(Double cost){this.cost = cost;}

    public void setMin_Shipping(Double shipping){this.min_shipping = shipping;}

    public void setShop_id(Integer shop_id){this.shop_id = shop_id;}

    @Override
    public int compareTo(Result o) {
        if(this.getCost()==o.getCost()){
            return 0;
        }
        else if(this.getCost() > o.getCost()){
            return 1;
        }
        else{
            return -1;
        }
    }

}
