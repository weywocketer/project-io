package com.controller;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
// koszty dostawy = jaka ta dostawa? XD
//jezeli brak wyników to wyszukuje bez zakresu cenowego jesli dalej nic -> alert (jak bedzie stronka)
//jesli dwa produkty ta sama cena to bierzemy ten o lepszej opini sprzedawcy

//wynik nazwa produktu wraz z linkiem


public class Controller {

    //Skąpiec.pl
   // private com.controller.Product[] products = new com.controller.Product[5];
    private Result[] results = new Result[3];

    // bez com.controller.Product product, bedzie korzystac z tablicy, niech wyrzuca tablice wynikow
    public  void Search(Product product) throws IOException {


            int help = 0;
            Connection connect = Jsoup.connect("https://www.skapiec.pl/szukaj/w_calym_serwisie/" + product.Get_Name() + "/price");
            Document document;
            Elements search_site;//strony z wynikami wyszukiwania
            String product_name = "";
            String url = "";

            do {
                document = connect.get();//łączenie - strona z wyszukiwaniami
                // trzeba jeszcze opcje gdy pojawi się 1 produkt - bo wtedy nic nie znajduje XDD
                search_site = document.select("a.pager-btn.arrow.right");
                Elements box = document.select("div.box-row.js"); //box

                Elements more_info;// = document.select("a.more-info"); //strona z produktem na Skapiec
                Elements compare_link;//= document.select("a.compare-link-1"); //strona z produktem w wielu sklepach

                for (Element box1 : box) {
                    Double box2 = Double.parseDouble(box1.select("strong.price.gtm_sor_price").text().replace(" zł", "").replace(",", ".").replace("od ", ""));
                    if (box2 >= product.get_Range()[0] && box2 <= product.get_Range()[1]) {
                        //System.out.println(box2);
                        more_info = box1.select("a.more-info"); //jeden produkt jeden sklep

                        connect = Jsoup.connect("https://www.skapiec.pl" + more_info.attr("href"));
                        document = connect.get();//łączenie
                        Elements name = document.select("h1");
                        // do ifów
                        Elements price = document.select("span.price.gtm_or_price");
                        Elements shipping = document.select("a.delivery-cost.link.gtm_oa_shipping");
                        Elements link = document.select("a.offer-row-item.gtm_or_row");
                        Elements nr_opinions = document.select("span.counter"); //liczba opinii sklepu
                        Elements opinion = document.select("span.stars.green"); //opinia sklepu hmmm gwiazdki....
                        for (Element eh : price) {
                            String url_link = "";
                            Double double_price = Double.parseDouble(eh.text().replace(" zł", "").replace(",", ".").replace(" ", ""));
                            if (double_price < product.get_Range()[1] && double_price > product.get_Range()[0]) {
                                if (!nr_opinions.text().isEmpty()) { //sprawdzenie czy brak opinii
                                    if (Integer.parseInt(nr_opinions.text()) >= 50) { //ograniczenie na liczbę opinii sklepu

                                        if (Double.parseDouble(opinion.attr("style").replace("width: ", "").replace("%", "")) >= product.Get_Min_Rate() * 100 / 5) {
                                            for (Element eh2 : name) {
                                                System.out.println("Name: " + eh2.text());
                                               product_name = eh2.text();

                                            }

                                            System.out.println("Cena:" + double_price);

                                            for (Element eh3 : shipping) { //po kij shipping XD
                                                System.out.println("https://www.skapiec.pl" + eh3.attr("href"));//tutaj trzeba link i wziąć przesyłkę itd
                                            }

                                            for (Element eh4 : link) {
                                                //działa XD
                                                // System.out.println("https://www.skapiec.pl"+eh4.attr("href"));//link do produktu w sklepie
                                                url_link = "https://www.skapiec.pl" + eh4.attr("href");
                                                System.out.println(url_link);
                                                url = url_link;
                                            }

                                            for (int i =0; i<3; i++)
                                            {
                                                if (results[i]==null){
                                                    results[i] = new Result(product_name, url);
                                                    break;
                                                }
                                            }

                                        }
                                    }

                                }
                            }
                        }

                        compare_link = box1.select("a.compare-link-1"); //jeden produkt wiele sklepów
                        connect = Jsoup.connect("https://www.skapiec.pl" + compare_link.attr("href"));
                        document = connect.get();//łączenie
                        name = document.select("h1");
                        // do ifów
                        Elements square = document.select("a.offer-row-item.gtm_or_row");
                        price = document.select("span.price.gtm_or_price");
                        shipping = document.select("a.delivery-cost.link.gtm_oa_shipping");
                        link = document.select("a.offer-row-item.gtm_or_row");
                        nr_opinions = document.select("span.counter"); //liczba opinii sklepu
                        opinion = document.select("span.stars.green"); //opinia sklepu hmmm gwiazdki....
                        for (Element eh : square) {

                            price = eh.select("span.price.gtm_or_price");
                            String url_link = "";
                            //System.out.println(document.select("h1"));
                            if (!price.text().isEmpty()) {
                                Double double_price = Double.parseDouble(price.text().replace(" zł", "").replace(",", ".").replace(" ", ""));
                                if (double_price < product.get_Range()[1] && double_price > product.get_Range()[0]) {
                                    nr_opinions = eh.select("span.counter");
                                    for (Element nr_opinion : nr_opinions) {
                                        if (!nr_opinion.text().isEmpty()) { //sprawdzenie czy brak opinii
                                            if (Integer.parseInt(nr_opinion.text()) >= 50) { //ograniczenie na liczbę opinii sklepu
                                                if (Double.parseDouble(opinion.attr("style").replace("width: ", "").replace("%", "")) >= product.Get_Min_Rate() * 100 / 5) {
                                                    System.out.println("Cena:" + price.text());
                                                    for (Element eh2 : name) {
                                                        System.out.println("Name: " + eh2.text());
                                                        product_name = eh2.text();

                                                    }
                                                    shipping = eh.select("a.delivery-cost.link.gtm_oa_shipping");
                                                    for (Element eh3 : shipping) { //po kij shipping XD
                                                        System.out.println("https://www.skapiec.pl" + eh3.attr("href"));//tutaj trzeba link i wziąć przesyłkę itd
                                                    }
                                                    link = eh.select("a.offer-row-item.gtm_or_row");
                                                    for (Element eh4 : link) {
                                                        //działa XD
                                                        // System.out.println("https://www.skapiec.pl"+eh4.attr("href"));//link do produktu w sklepie
                                                        url_link = "https://www.skapiec.pl" + eh4.attr("href");
                                                        System.out.println(url_link);
                                                        url = url_link;

                                                    }
                                                    for (int i =0; i<3; i++) // złe
                                                    {
                                                        if (results[i]==null){
                                                            results[i] = new Result(product_name, url);
                                                            break;
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
                }





            /*
            for (int i = 0; i<3 ;i ++){
            for (Element alem : more_info) {
                connect = Jsoup.connect("https://www.skapiec.pl" + alem.attr("href"));
                document = connect.get();//łączenie
                Elements name = document.select("h1");
                // do ifów
                Elements price = document.select("span.price.gtm_or_price");
                Elements shipping = document.select("a.delivery-cost.link.gtm_oa_shipping");
                Elements link = document.select("a.offer-row-item.gtm_or_row");
                Elements nr_opinions = document.select("span.counter"); //liczba opinii sklepu
                Elements opinion = document.select("span.stars.green"); //opinia sklepu hmmm gwiazdki....
                for (Element eh : price) {
                    String url_link = "";
                    Double double_price = Double.parseDouble(eh.text().replace(" zł", "").replace(",", ".").replace(" ", ""));
                    if (double_price < product.get_Range()[1] && double_price > product.get_Range()[0]) {
                        if (!nr_opinions.text().isEmpty()) { //sprawdzenie czy brak opinii
                            if (Integer.parseInt(nr_opinions.text()) >= 50) { //ograniczenie na liczbę opinii sklepu

                                if (Double.parseDouble(opinion.attr("style").replace("width: ", "").replace("%", "")) >= product.Get_Min_Rate() * 100 / 5) {
                                    for (Element eh2 : name) {
                                        System.out.println("Name: " + eh2.text());
                                        results[i].setName(eh2.text());
                                    }

                                    System.out.println("Cena:" + double_price);

                                    for (Element eh3 : shipping) { //po kij shipping XD
                                        System.out.println(eh3.text());//tutaj trzeba link i wziąć przesyłkę itd
                                    }

                                    for (Element eh4 : link) {
                                        //działa XD
                                        // System.out.println("https://www.skapiec.pl"+eh4.attr("href"));//link do produktu w sklepie
                                        url_link = "https://www.skapiec.pl" + eh4.attr("href");
                                        System.out.println(url_link);
                                        results[i].setLink(url_link);

                                    }

                                }
                            }

                        }
                    }
                }
                }
            }
            */


                for (Element elem : search_site) {
                    connect = Jsoup.connect("https://www.skapiec.pl" + elem.attr("href"));
                }


            } while (search_site.size() == 1 || results[2]==null);

    }

    public Result[] getResults()
    {
        return results;
    }




        public static void main (String[]args) throws IOException {
            Double[] range = new Double[2];
            range[0] = 10.0;
            range[1] = 11.0;
            Product product2 = new Product("lalka", 5, range, 4.5);
            //dodac produkty do tablicy
            // w search zrobić pętle po tablicy
            Controller controller = new Controller();
            controller.Search(product2);

            System.out.println("hehe działa!");
            System.out.println(controller.getResults()[0].getName());
            System.out.println(controller.getResults()[0].getLink());
            System.out.println(controller.getResults()[1].getName());
            System.out.println(controller.getResults()[1].getLink());
            System.out.println(controller.getResults()[2].getName());
            System.out.println(controller.getResults()[2].getLink());


        }
    }
