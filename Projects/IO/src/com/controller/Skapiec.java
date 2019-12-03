package com.controller;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

//jezeli brak wyników to wyszukuje bez zakresu cenowego jesli dalej nic -> alert (jak bedzie stronka)

// posortować według ceny
// wziac mac przesylke!

/// ZMIENIAMY ZAMYSŁ XDDDD -> w klasie Skapiec
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// JEŻELI ZNALEZIONO TYLKO JEDEN PRODUKT to opcja - olewamy XD

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

// implements Runnable
public class Skapiec{

    //Skąpiec.pl
    //moze by inaczej to zrobic
    private ArrayList<Product> products = new ArrayList<Product>(); //lista z produktami
    private ArrayList<Double> sum_cost = new ArrayList<Double>(); //lista z wynikami
    Element boxe;
    Product product;

    public  void Search(Product product) throws IOException {

        Connection connect = Jsoup.connect("https://www.skapiec.pl/szukaj/w_calym_serwisie/" + product.Get_Name());
        Document document= connect.get();
        Elements no_results = document.select("p.content"); //gdy brak wyników
        Elements search_site;//strony z wynikami wyszukiwania
        System.out.println("hehe");

        if (no_results.text().equals("Brak produktów dla wyszukiwanej frazy."))
        {
            System.out.println("BRAK PRODUKTÓW");
        }
        else {
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
                ArrayList<Thread> threads = new ArrayList<Thread>();
                for (Element box1 : box) {
                    this.boxe = box1;
                    this.product = product;
                    //run();
                    threads.add( new Thread(new Runnable() {
                        public void run() {
                            try {
                                Research(box1, product);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }));
                }
                for (Thread t:threads){
                    t.run();
                }

                for (Element elem : search_site) { // przelacznie po kolejnych stronach wynikow wyszukiwania
                    connect = Jsoup.connect("https://www.skapiec.pl" + elem.attr("href"));
                }
                product.Get_Results().sort(Result::compareTo);
                finish = System.currentTimeMillis();
                timeElapsed = finish - start;
            }while (search_site.size() == 1 && product.Get_Results().size()<50 && timeElapsed/1000<8);
        }
        //posortuj i wybierz
        //select_results(product)

    }

    //funkcja szukająca najmniejszej dostawy
    //dziala
    public Double[] searchShipping(String url) throws IOException{
        ArrayList<Double> shipping_prices = new ArrayList<Double>();
        Double[] max_min_ship_prices = new Double[2];
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
            max_min_ship_prices[0] = shipping_prices.get(0);
            max_min_ship_prices[1] = shipping_prices.get(shipping_prices.size()-1);
            return max_min_ship_prices;
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
        Double min_result_shipping;
        Double max_result_shipping;
        Integer result_shop_id;
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
            Elements shop_ids;
            Elements products = document.select("div.offers-list:nth-child(2) > ul:nth-child(1) > li:nth-child(1)"); //wybieramy prostokaty

            for(Element p: products)
            {
                if (!p.select("span.price.gtm_or_price").text().isEmpty()){
                    //cena double
                    Double p_product = Double.parseDouble(p.select("span.price.gtm_or_price").first().text().replace(" zł","").replace(" ","").replace(",","."));
                    if (p_product>=product.get_Range()[0] && p_product<=product.get_Range()[1]){ //sprawdzamy cene
                        //liczba opinii
                        nr_opinions = p.select("span.counter"); //liczba opinii sklepu
                        if (!nr_opinions.text().isEmpty()) { //sprawdzenie czy brak opinii
                            if (Integer.parseInt(nr_opinions.first().text()) >= 50) {
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
                                        min_result_shipping = 0.0;
                                        max_result_shipping = 0.0;
                                        //System.out.println("Dostawa: "+result_shipping);

                                        //id sklepu
                                        shop_ids = p.select("li");
                                        String shop_id = shop_ids.attr("data-dealer-id");
                                        result_shop_id = Integer.parseInt(shop_id);
                                        //System.out.println(shop_id);

                                        product.Get_Results().add(new Result(result_name, result_link,result_cost,min_result_shipping,max_result_shipping,result_shop_id));
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
                                            min_result_shipping = searchShipping("https://www.skapiec.pl" + shipping.attr("href"))[0];
                                            max_result_shipping = searchShipping("https://www.skapiec.pl" + shipping.attr("href"))[1];
                                            //System.out.println("Dostawa: "+result_shipping);

                                            //id sklepu
                                            shop_ids = p.select("li");
                                            String shop_id = shop_ids.attr("data-dealer-id");
                                            result_shop_id = Integer.parseInt(shop_id);
                                            //System.out.println(shop_id);

                                            product.Get_Results().add(new Result(result_name, result_link,result_cost,min_result_shipping,max_result_shipping,result_shop_id));

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
            products = document.select("div.offers-list:nth-child(2) > ul:nth-child(1) > li:nth-child(1)"); //wybieramy prostokaty w ktorych sa dane
            //Thread t2 = new Thread(new MultiTask(document,product));
            //t2.run();

            for(Element p: products)
            {
                if (!p.select("span.price.gtm_or_price").text().isEmpty()){
                    //cena double
                    Double p_product = Double.parseDouble(p.select("span.price.gtm_or_price").first().text().replace(" zł","").replace(" ","").replace(",","."));
                    if (p_product>=product.get_Range()[0] && p_product<=product.get_Range()[1]){ //sprawdzamy cene
                        //liczba opinii
                        nr_opinions = p.select("span.counter"); //liczba opinii sklepu
                        if (!nr_opinions.text().isEmpty()) { //sprawdzenie czy brak opinii
                            if (Integer.parseInt(nr_opinions.first().text()) >= 50) {
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
                                        min_result_shipping = 0.0;
                                        max_result_shipping = 0.0;
                                        //System.out.println("Dostawa: "+result_shipping);

                                        //id sklepu
                                        shop_ids = p.select("li");
                                        String shop_id = shop_ids.attr("data-dealer-id");
                                        result_shop_id = Integer.parseInt(shop_id);
                                        //System.out.println(shop_id);

                                       product.Get_Results().add(new Result(result_name, result_link,result_cost,min_result_shipping,max_result_shipping, result_shop_id));
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
                                            min_result_shipping = searchShipping("https://www.skapiec.pl" + shipping.attr("href"))[0];
                                            max_result_shipping = searchShipping("https://www.skapiec.pl" + shipping.attr("href"))[1];
                                            //System.out.println("Dostawa: "+result_shipping);

                                            //id sklepu
                                            shop_ids = p.select("li");
                                            String shop_id = shop_ids.attr("data-dealer-id");
                                            result_shop_id = Integer.parseInt(shop_id);
                                            //System.out.println(shop_id);

                                            product.Get_Results().add(new Result(result_name, result_link,result_cost,min_result_shipping,max_result_shipping,result_shop_id ));

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

    //poustawia wyniki biorąc pod uwagę przedmioty z tego samego sklepu
    // EHHHHHHHHHHHHHHH
/*
    public void select_results(Product product){
        product.Get_Results().sort(Result::compareTo);
        //product.Set_Results((ArrayList<Result>) product.Get_Results().subList(0,3));

        for (int i=0; i<2; i++) { // dla kazdego zbioru produktow (results) obliczamy zbiorczy koszt wysylki
            //product.Get_Results().get(i)
            ArrayList<ArrayList> shops = new ArrayList<ArrayList>(); // w tej liscie trzymamy listy produktow o tych samych shop_id
            for p in produktyDlaTychResultow {
                for (shop:shops) {
                    if p.Get_Results().get(i).getShop_id() == shop.get(0) { // jesli lista dla tego shop_id juz istnieje
                        shop.add(p);
                    }
                    else{
                        shops.add(new ArrayList());  // utworz nowa liste dla tego shop_id
                        shops.get(shops.size() - 1).add(p);  // i dopisz p do tej listy
                    }
                }
            }

        }

 */

        //product.Set_Results(product.Get_Results().subList(0,3).toArray());



        //}
    //}

 //*/


    public ArrayList<Product> getProducts() { return products;}

    public ArrayList<Double> getSum_cost() { return sum_cost;}

    public static void main (String[]args) throws IOException {
        Double[] range = new Double[2];
        range[0] = 30.0;
        range[1] = 40.0;

        Double[] range2 = new Double[2];
        range2[0] = 9.0;
        range2[1] = 11.0;

        Product product = new Product("lalka", 5, range);
        Product product2 = new Product("zeszyt", 5, range2);
        Product product3 = new Product("chleb saluteo", 5, range2);
        Product product4 = new Product("chleb saluteo", 5, range2);
        Product product5 = new Product("chleb saluteo", 5, range2);

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

        //zliczamy sume kosztow dla danego zestawienia
        //controller.count_sum();

    }

    /*
    @Override
    public void run() {
        try{
           Research(this.boxe, this.product);
        }catch(Exception e){
            System.out.println();
            e.printStackTrace();
        }

    }
     */
}
