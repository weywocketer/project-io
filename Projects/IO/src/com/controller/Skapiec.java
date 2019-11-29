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


public class Skapiec implements Runnable{

    //Skąpiec.pl
    //moze by inaczej to zrobic
    private ArrayList<Product> products = new ArrayList<Product>(); //lista z produktami
   // private ArrayList<Result> results = new ArrayList<Result>(); //lista z wynikami
    Element boxe;
    Product product;

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

            long finish;
            long start = System.currentTimeMillis();
            long timeElapsed ;
            do{
               // connect = Jsoup.connect("https://www.skapiec.pl/szukaj/w_calym_serwisie/" + product.Get_Name()+"/"+i);
                document = connect.get();//łączenie - strona z wyszukiwaniami
                // trzeba jeszcze opcje gdy pojawi się 1 produkt - bo wtedy nic nie znajduje XDD
                search_site = document.select("a.pager-btn.arrow.right");
                Elements box = document.select("div.box-row.js"); //box

                Elements more_info;// = document.select("a.more-info"); //strona z produktem na Skapiec
                Elements compare_link;//= document.select("a.compare-link-1"); //strona z produktem w wielu sklepach

                for (Element box1 : box) {
                    this.boxe = box1;
                    this.product = product;
                    run();
                }

                for (Element elem : search_site) { // przelacznie po kolejnych stronach wynikow wyszukiwania
                    connect = Jsoup.connect("https://www.skapiec.pl" + elem.attr("href"));
                }
                finish = System.currentTimeMillis();
                timeElapsed = finish - start;
            }while (search_site.size() == 1 && product.Get_Results().size()<100 && timeElapsed/1000<10);
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

    public void Research(Element box1, Product product) throws IOException {
        String result_name ="";
        String result_link="";
        Double result_cost;
        Double result_shipping;
        Double box2 = Double.parseDouble(box1.select("strong.price.gtm_sor_price").text().replace(" zł", "").replace(",", ".").replace("od ", "").replace(" ", ""));
        if (box2 >= product.get_Range()[0] && box2 <= product.get_Range()[1]) {
            //JEDEN PRODUKT JEDEN SKLEP
            Elements more_info = box1.select("a.more-info"); //jeden produkt jeden sklep
            ////////////////////////////////////////////////

            Connection connect = Jsoup.connect("https://www.skapiec.pl" + more_info.attr("href"));
            Document document = connect.get();//łączenie
            Elements name = document.select("h1");
            Elements opinion;//opinia sklepu hmmm gwiazdki....
            Elements nr_opinions;//liczba opinii sklepu
            Elements shipping;//koszt dostawy
            Elements free_shipping;//darmowa dostawa
            Elements link;//link do sklepu
            Elements products = document.select("a.offer-row-item.gtm_or_row"); //wybieramy prostokaty

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
                                        //System.out.println("Nazwa: "+result_name);

                                        //link do sklepu
                                        link = p.select("a.offer-row-item.gtm_or_row");
                                        result_link ="https://www.skapiec.pl" + link.attr("href");
                                        //System.out.println("Link do sklepu: "+result_link);

                                        //cena wyniku
                                        result_cost = p_product;
                                        //System.out.println("Cena: "+result_cost);

                                        //koszt dostawy
                                        result_shipping = 0.0;
                                        //System.out.println("Dostawa: "+result_shipping);

                                        product.Get_Results().add(new Result(result_name, result_link,result_cost,result_shipping));
                                    }
                                    else if (shipping.size() != 0) {
                                        //jezeli jest dostawa
                                        if ( searchShipping("https://www.skapiec.pl" + shipping.attr("href"))!= null) {
                                            //nazwa wyniku
                                            result_name = name.text();
                                            // System.out.println("Nazwa: "+result_name);

                                            //link do sklepu
                                            link = p.select("a.offer-row-item.gtm_or_row");
                                            result_link ="https://www.skapiec.pl" + link.attr("href");
                                            //System.out.println("Link do sklepu: "+result_link);

                                            //cena wyniku
                                            result_cost = p_product;
                                            //System.out.println("Cena: "+result_cost);

                                            //koszt dostawy
                                            result_shipping = searchShipping("https://www.skapiec.pl" + shipping.attr("href"));
                                            //System.out.println("Dostawa: "+result_shipping);

                                            product.Get_Results().add(new Result(result_name, result_link,result_cost,result_shipping));

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }



//-----------------------------jeden produkt wiele sklepów--------------------------------------------------------------
            Elements compare_link = box1.select("a.compare-link-1");
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
                                        //System.out.println("Nazwa: "+result_name);

                                        //link do sklepu
                                        link = p.select("a.offer-row-item.gtm_or_row");
                                        result_link ="https://www.skapiec.pl" + link.attr("href");
                                        //System.out.println("Link do sklepu: "+result_link);

                                        //cena wyniku
                                        result_cost = p_product;
                                        //System.out.println("Cena: "+result_cost);

                                        //koszt dostawy
                                        result_shipping = 0.0;
                                        //System.out.println("Dostawa: "+result_shipping);

                                       product.Get_Results().add(new Result(result_name, result_link,result_cost,result_shipping));
                                    }
                                    if (shipping.size() != 0) {
                                        //jezeli jest dostawa
                                        if ( searchShipping("https://www.skapiec.pl" + shipping.attr("href"))!= null) {
                                            //nazwa wyniku
                                            result_name = name.text();
                                            // System.out.println("Nazwa: "+result_name);

                                            //link do sklepu
                                            link = p.select("a.offer-row-item.gtm_or_row");
                                            result_link ="https://www.skapiec.pl" + link.attr("href");
                                            //System.out.println("Link do sklepu: "+result_link);

                                            //cena wyniku
                                            result_cost = p_product;
                                            //System.out.println("Cena: "+result_cost);

                                            //koszt dostawy
                                            result_shipping = searchShipping("https://www.skapiec.pl" + shipping.attr("href"));
                                            //System.out.println("Dostawa: "+result_shipping);

                                            product.Get_Results().add(new Result(result_name, result_link,result_cost,result_shipping));

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


    public ArrayList<Product> getProducts() { return products;}


    public static void main (String[]args) throws IOException {
        Double[] range = new Double[2];
        range[0] = 25.0;
        range[1] = 30.0;

        Double[] range2 = new Double[2];
        range[0] = 9.0;
        range[1] = 11.0;

        Product product = new Product("lalka", 5, range, 4.0);
        Product product2 = new Product("chleb saluteo", 5, range, 4.0);
        Product product3 = new Product("chleb saluteo", 5, range, 4.0);
        Product product4 = new Product("chleb saluteo", 5, range, 4.0);
        Product product5 = new Product("chleb saluteo", 5, range, 4.0);

        Skapiec controller = new Skapiec();
        controller.getProducts().add(product);
        controller.getProducts().add(product2);
        controller.getProducts().add(product3);
        controller.getProducts().add(product4);
        controller.getProducts().add(product5);

//////////////////////////////////////////////////
        ArrayList<Thread> threads = new ArrayList<Thread>();

        // tworzymy tyle watkow ile produktow
        for (Product p: controller.getProducts()) {
            threads.add( new Thread(new Runnable() {
                public void run() {
                    try {
                        controller.Search(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }
        //////////////////////


        long start = System.currentTimeMillis();// czas rozpoczecia
        //
        //uruchamiamy wątki
        for (Thread t:threads){
            t.run();
        }
        long finish = System.currentTimeMillis(); //czas zakonczenia
        long timeElapsed = finish - start; //czas trwania programu

        System.out.println("Wyszukanu w ciągu:"+timeElapsed/1000+"s");

///////////////////////////////////////////////////
    }

    @Override
    public void run() {
        try{
           Research(this.boxe, this.product);
        }catch(Exception e){
            System.out.println("Sth went wrong :(");
        }

    }


}
