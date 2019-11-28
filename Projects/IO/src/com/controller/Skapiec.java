package com.controller;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

//jezeli brak wyników to wyszukuje bez zakresu cenowego jesli dalej nic -> alert (jak bedzie stronka)
//jesli dwa produkty ta sama cena to bierzemy ten o lepszej opini sprzedawcy


/// ZMIENIAMY ZAMYSŁ XDDDD -> w klasie Skapiec
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// JEŻELI ZNALEZIONO TYLKO JEDEN PRODUKT to opcja
//JAKOS OGRANICZYC TE STRONY XDDD
//moze dodac funkcje zeby nie powtarzac tego kodu :(
//watki :)

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


public class Skapiec{

    //Skąpiec.pl
    //moze by inaczej to zrobic
    private ArrayList<Product> products = new ArrayList<Product>(); //lista z produktami
    private ArrayList<Result> results = new ArrayList<Result>(); //lista z wynikami

    public  void Search(Product product) throws IOException {

        Connection connect = Jsoup.connect("https://www.skapiec.pl/szukaj/w_calym_serwisie/" + product.Get_Name());
        Document document= connect.get();
        Elements no_results = document.select("p.content"); //gdy brak wyników
        Elements search_site;//strony z wynikami wyszukiwania


        if (no_results.text().equals("Brak produktów dla wyszukiwanej frazy."))
        {
            System.out.println("BRAK PRODUKTÓW");
        }
        else {
            String result_name ="";
            String result_link="";
            Double result_cost;
            Double result_shipping;

            do{
               // connect = Jsoup.connect("https://www.skapiec.pl/szukaj/w_calym_serwisie/" + product.Get_Name()+"/"+i);
                document = connect.get();//łączenie - strona z wyszukiwaniami
                // trzeba jeszcze opcje gdy pojawi się 1 produkt - bo wtedy nic nie znajduje XDD
                search_site = document.select("a.pager-btn.arrow.right");
                Elements box = document.select("div.box-row.js"); //box

                Elements more_info;// = document.select("a.more-info"); //strona z produktem na Skapiec
                Elements compare_link;//= document.select("a.compare-link-1"); //strona z produktem w wielu sklepach

                for (Element box1 : box) {

                    /*
                    class MultiTask implements Runnable{

                        Document document;
                        Product product;
                        MultiTask(Document document, Product product){
                            this.document = document;
                            this.product = product;
                        }
                        @Override
                        public void run() {
                            try {
                                Research(document, product);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    Thread t = new Thread(document,product);
                    t.run();
                    */
                    Double box2 = Double.parseDouble(box1.select("strong.price.gtm_sor_price").text().replace(" zł", "").replace(",", ".").replace("od ", "").replace(" ", ""));
                    if (box2 >= product.get_Range()[0] && box2 <= product.get_Range()[1]) {
                        //JEDEN PRODUKT JEDEN SKLEP
                        more_info = box1.select("a.more-info"); //jeden produkt jeden sklep


                        connect = Jsoup.connect("https://www.skapiec.pl" + more_info.attr("href"));
                        document = connect.get();//łączenie
                        Elements name = document.select("h1");
                        Elements opinion;// = document.select("span.stars.green"); //opinia sklepu hmmm gwiazdki....
                        Elements nr_opinions;// = document.select("span.counter"); //liczba opinii sklepu
                        Elements shipping;// = document.select("a.delivery-cost.link.gtm_oa_shipping");
                        Elements free_shipping;// = document.select("a.delivery-cost.link.gtm_oa_shipping");
                        Elements link;// = document.select("a.offer-row-item.gtm_or_row");
                        Elements products = document.select("a.offer-row-item.gtm_or_row"); //wybieramy prostokaty



                        //Thread t = new Thread(new MultiTask(document,product));
                        //t.run();

                        for(Element p: products)
                        {
                            if (!p.select("span.price.gtm_or_price").text().isEmpty()){
                                //cena double
                                Double p_product = Double.parseDouble(p.select("span.price.gtm_or_price").text().replace(" zł","").replace(" ","").replace(",","."));
                                if (p_product>=product.get_Range()[0] && p_product<=product.get_Range()[1]){ //sprawdzamy cene
                                    //liczba opinii
                                    nr_opinions = p.select("span.counter"); //liczba opinii sklepu
                                    if (!nr_opinions.text().isEmpty()) { //sprawdzenie czy brak opinii
                                        if (Integer.parseInt(nr_opinions.text()) >= 50) {
                                            //"gwiazdki"
                                            opinion = p.select("span.stars.green"); //opinia sklepu hmmm gwiazdki....
                                            if (Double.parseDouble(opinion.attr("style").replace("width: ", "").replace("%", "")) >= product.Get_Min_Rate() * 100 / 5) {
                                                //link do mozliwych dostaw
                                                shipping = p.select("a.delivery-cost.link.gtm_oa_shipping");
                                                free_shipping = p.select("span.delivery-cost.free-delivery.badge.gtm_bdg_fd");
                                                if(free_shipping.size()!=0){
                                                    //nazwa wyniku
                                                    result_name = name.text();
                                                    System.out.println("Nazwa: "+result_name);

                                                    //link do sklepu
                                                    link = p.select("a.offer-row-item.gtm_or_row");
                                                    result_link ="https://www.skapiec.pl" + link.attr("href");
                                                    System.out.println("Link do sklepu: "+result_link);

                                                    //cena wyniku
                                                    result_cost = p_product;
                                                    System.out.println("Cena: "+result_cost);

                                                    //koszt dostawy
                                                    result_shipping = 0.0;
                                                    System.out.println("Dostawa: "+result_shipping);

                                                    results.add(new Result(result_name, result_link,result_cost,result_shipping));
                                                }
                                                else if (shipping.size() != 0) {
                                                    //jezeli jest dostawa
                                                    if ( searchShipping("https://www.skapiec.pl" + shipping.attr("href"))!= null) {
                                                        //nazwa wyniku
                                                        result_name = name.text();
                                                        System.out.println("Nazwa: "+result_name);

                                                        //link do sklepu
                                                        link = p.select("a.offer-row-item.gtm_or_row");
                                                        result_link ="https://www.skapiec.pl" + link.attr("href");
                                                        System.out.println("Link do sklepu: "+result_link);

                                                        //cena wyniku
                                                        result_cost = p_product;
                                                        System.out.println("Cena: "+result_cost);

                                                        //koszt dostawy
                                                        result_shipping = searchShipping("https://www.skapiec.pl" + shipping.attr("href"));
                                                        System.out.println("Dostawa: "+result_shipping);

                                                        results.add(new Result(result_name, result_link,result_cost,result_shipping));

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }



//-----------------------------jeden produkt wiele sklepów--------------------------------------------------------------
                        compare_link = box1.select("a.compare-link-1");
                        connect = Jsoup.connect("https://www.skapiec.pl" + compare_link.attr("href"));
                        document = connect.get();//łączenie
                        name = document.select("h1");
                        products = document.select("a.offer-row-item.gtm_or_row"); //wybieramy prostokaty w ktorych sa dane
                        //Thread t2 = new Thread(new MultiTask(document,product));
                        //t2.run();

                        for(Element p: products)
                        {
                            if (!p.select("span.price.gtm_or_price").text().isEmpty()){
                                //cena double
                                Double p_product = Double.parseDouble(p.select("span.price.gtm_or_price").text().replace(" zł","").replace(" ","").replace(",","."));
                                if (p_product>=product.get_Range()[0] && p_product<=product.get_Range()[1]){ //sprawdzamy cene
                                    //liczba opinii
                                    nr_opinions = p.select("span.counter"); //liczba opinii sklepu
                                     if (!nr_opinions.text().isEmpty()) { //sprawdzenie czy brak opinii
                                         if (Integer.parseInt(nr_opinions.text()) >= 50) {
                                            //"gwiazdki"
                                             opinion = p.select("span.stars.green"); //opinia sklepu hmmm gwiazdki....
                                             if (Double.parseDouble(opinion.attr("style").replace("width: ", "").replace("%", "")) >= product.Get_Min_Rate() * 100 / 5) {
                                                 shipping = p.select("a.delivery-cost.link.gtm_oa_shipping");
                                                 free_shipping = p.select("span.delivery-cost.free-delivery.badge.gtm_bdg_fd");
                                                 if(free_shipping.size()!=0){
                                                     //nazwa wyniku
                                                     result_name = name.text();
                                                     System.out.println("Nazwa: "+result_name);

                                                     //link do sklepu
                                                     link = p.select("a.offer-row-item.gtm_or_row");
                                                     result_link ="https://www.skapiec.pl" + link.attr("href");
                                                     System.out.println("Link do sklepu: "+result_link);

                                                     //cena wyniku
                                                     result_cost = p_product;
                                                     System.out.println("Cena: "+result_cost);

                                                     //koszt dostawy
                                                     result_shipping = 0.0;
                                                     System.out.println("Dostawa: "+result_shipping);

                                                     results.add(new Result(result_name, result_link,result_cost,result_shipping));
                                                 }
                                                 if (shipping.size() != 0) {
                                                     //jezeli jest dostawa
                                                     if ( searchShipping("https://www.skapiec.pl" + shipping.attr("href"))!= null) {
                                                         //nazwa wyniku
                                                         result_name = name.text();
                                                         System.out.println("Nazwa: "+result_name);

                                                         //link do sklepu
                                                         link = p.select("a.offer-row-item.gtm_or_row");
                                                         result_link ="https://www.skapiec.pl" + link.attr("href");
                                                         System.out.println("Link do sklepu: "+result_link);

                                                         //cena wyniku
                                                         result_cost = p_product;
                                                         System.out.println("Cena: "+result_cost);

                                                         //koszt dostawy
                                                         result_shipping = searchShipping("https://www.skapiec.pl" + shipping.attr("href"));
                                                         System.out.println("Dostawa: "+result_shipping);

                                                         results.add(new Result(result_name, result_link,result_cost,result_shipping));

                                                     }
                                                 }
                                             }
                                         }
                                     }
                                }
                            }
                        }
                    }
                }

                for (Element elem : search_site) { // przelacznie po kolejnych stronach wynikow wyszukiwania
                    connect = Jsoup.connect("https://www.skapiec.pl" + elem.attr("href"));
                }
            }while (search_site.size() == 1 && results.size()<100);
        }
    }

    //funkcja szukająca najmniejszej dostawy
    //dziala
    public Double searchShipping(String url) throws IOException{
        ArrayList<Double> shipping_prices = new ArrayList<Double>();
        Double shipping_price=0.0;
        Connection connect = Jsoup.connect(url);
        Connection connect_shipping;
        Document document= connect.get();
        Elements types_of_shipping = document.select("ul.menu").select("a"); //inne typy dostawy
        Elements active_shipping = document.select("table");//w domyślnej dostawie wybieramy koszty
        Elements other_shipping = document.select("table");//w domyślnej dostawie wybieramy koszty
        if(types_of_shipping.size()!=0){ //jeżeli niepuste
            for (Element type_of_shipping : types_of_shipping)
            {
                connect_shipping = Jsoup.connect("https://www.skapiec.pl/delivery.php"+type_of_shipping.attr("href"));
                document = connect_shipping.get();
                other_shipping = document.select("table");
                if(other_shipping.size()!=0) {
                    other_shipping = other_shipping.select("b");
                    for (Element cost : other_shipping){
                        //System.out.println(cost.text());
                        shipping_price = Double.parseDouble(cost.text().replace(" zł",""));
                        shipping_prices.add(shipping_price);
                    }
                }
            }
        }
        if(active_shipping.size()!=0) {
            active_shipping = active_shipping.select("b");
            for (Element cost : active_shipping){
                //System.out.println(cost.text());
                shipping_price = Double.parseDouble(cost.text().replace(" zł",""));
                shipping_prices.add(shipping_price);
            }
        }
        if (shipping_prices.size()!=0)
        {
            shipping_prices.sort(Double::compareTo);
            shipping_price = shipping_prices.get(0);
            return shipping_price;
        }
        else
        {
            return null;
        }
    }



    public ArrayList<Result> getResults() {return results;}

    public ArrayList<Product> getProducts() { return products;}


    public static void main (String[]args) throws IOException {
        Double[] range = new Double[2];
        range[0] = 25.0;
        range[1] = 30.0;
        Product product2 = new Product("lalka", 5, range, 4.0);
        Skapiec controller = new Skapiec();
        long start = System.currentTimeMillis();
        controller.Search(product2);
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println(timeElapsed/1000+"s");
    }
}
