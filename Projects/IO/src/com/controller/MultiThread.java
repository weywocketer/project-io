package com.controller;

import javax.swing.text.Document;

public abstract class MultiThread implements Runnable {

    public void run(Document document, Product product){
        try{
            System.out.println("Proukt XD");
        }
        catch (Exception e){
            System.out.println("Sth went wrong :(");
        }
    }
}
