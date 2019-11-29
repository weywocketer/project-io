package com.controller;

import java.util.ArrayList;

public class Product {

    private String name;
    private int count;
    private Double[] range;
    final private Double min_rate = 4.0;
    private ArrayList<Result> results = new ArrayList<Result>();

    public Product(String name, int count, Double[] range)
    {
        this.name = name.replace(" ", "+"); // olus <3
        this.count = count;
        this.range = range;

    }

    public Product(){}

    public String Get_Name(){ return name; }

    public int Get_Count(){
        return count;
    }

    public Double[] get_Range(){return range;}

    public Double Get_Min_Rate(){return min_rate;}

    public ArrayList<Result> Get_Results(){return results;}


    public void Set_Range(Double[] range){this.range=range;}

    public void Set_Name(String name){
        this.name = name;
    }

    public void Set_Count(int count){
        this.count = count;
    }

    public void Set_Count(ArrayList<Result> results){ this.results = results; }

    public static void main(String args[]){

    }
}
