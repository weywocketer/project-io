package com.controller;

public class Result {

    private String name;
    private String link;
    private Double cost;
    private Double shipping;
    private Double sum = cost+shipping;


    public Result(){}

    public Result(String name, String link){
        this.name = name;
        this.link = link;
    }

    public Result(String name, String link, Double cost, Double shipping){
        this.name = name;
        this.cost = cost;
        this.shipping = shipping;
        this.link = link;
    }

    public String getName(){return name;}

    public String getLink(){return link;}

    public Double getCost(){return cost;}

    public Double getShipping(){return shipping;}

    public Double getSum(){return sum;}

    public void setName(String name){this.name = name;}

    public void setLink(String link){this.link = link;}

    public void setCost(Double cost){this.cost = cost;}

    public void setShipping(Double shipping){this.shipping = shipping;}



}
