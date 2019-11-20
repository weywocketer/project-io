import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
//jeszcze potrzeba - pobrac nazwe XD, koszty dostawy,ocene
//CENE SPRAWDZAĆ W PASKU WYSZUKIWAŃ BO JAK BEDZIE SIE WCHODZIĆ W STRONĘ TO ZAJMIE CIUT WIECEJ NIZ 15 SEKUND XD
// warunki! czyli zakres cen, minalna ilosć opini 50
//jak brak opinii/kosztów to ignorujemy
//jezeli brak wyników to wyszukuje bez zakresu cenowego jesli dalej nic -> alert (jak bedzie stronka)
//jesli dwa produkty ta sama cena to bierzemy ten o lepszej opini sprzedawcy
//brak licytacji

//wynik nazwa produktu wraz z linkiem


public class Controller {

    //Skąpiec.pl
    private Product[] products = new Product[5];
    private Result[][] results = new Result[5][3];

    // bez Product product, bedzie korzystac z tablicy, niech wyrzuca tablice wynikow
    public void Search(Product product) throws IOException {

        Connection connect = Jsoup.connect("https://www.skapiec.pl/szukaj/w_calym_serwisie/" + product.Get_Name() + "/price");
        Document document;
        Elements search_site;//strony z wynikami wyszukiwania
        do {
            document = connect.get();//łączenie - strona z wyszukiwaniami
            // trzeba jeszcze opcje gdy pojawi się 1 produkt - bo wtedy nic nie znajduje XDD
            search_site = document.select("a.pager-btn.arrow.right");
            //Elements start_price = document.select("a.box"); //strona z produktem na Skapiec
            Elements more_info = document.select("a.more-info"); //strona z produktem na Skapiec
            Elements compare_link = document.select("a.compare-link-1"); //strona z produktem w wielu sklepach


            for (Element alem : more_info) {
                connect = Jsoup.connect("https://www.skapiec.pl" + alem.attr("href"));
                document = connect.get();//łączenie
                Elements name = document.select("h1");
                Elements price = document.select("span.price.gtm_or_price");
                Elements shipping = document.select("a.delivery-cost.link.gtm_oa_shipping");
                Elements link = document.select("a.offer-row-item.gtm_or_row");
                Elements nr_opinions = document.select("span.counter"); //liczba opinii sklepu
                Elements opinion = document.select("span.counter"); //opinia sklepu hmmm gwiazdki....

                for (Element eh : price) {
                    String url_link = "";
                    Double double_price = Double.parseDouble(eh.text().substring(0, eh.text().length() - 2).replace(",", "."));
                    if (double_price < product.get_Range()[1] && double_price > product.get_Range()[0]) {

                        if (Integer.parseInt(nr_opinions.text())>= 50) { //ograniczenie na liczbę opinii sklepu
                            //if (Integer.parseInt(eh1.text()) >= 50) {

                                for (Element eh2 : name) {
                                    System.out.println("Name: " + eh2.text());
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

                                }

                            //}

                        }
                    }
                }
            }

                /*
                for (Element eh : shipping )
                {
                    System.out.println(eh.text());//tutaj trzeba link i wziąć przesyłkę itd
                }
                for (Element eh : link )
                {
                    System.out.println(eh.text());//link do produktu w sklepie
                }
*/

            /*
            for (Element alem : compare_link) {
                connect = Jsoup.connect("https://www.skapiec.pl" + alem.attr("href"));
                document = connect.get();//łączenie
                Elements price = document.select("span.price.gtm_or_price");
                Elements dostawa = document.select("a.delivery-cost.link.gtm_oa_shipping");
                Elements link = document.select("a.offer-row-item.gtm_or_row");
                for (Element eh : price )
                {
                    System.out.println(eh.text()+"DUZOOOO");
                }
                for (Element eh : dostawa )
                {
                    System.out.println(eh.text());//tutaj trzeba link i wziąć przesyłkę itd
                }
                for (Element eh : link )
                {
                    System.out.println(eh.text());//link do produktu w sklepie
                }


                */


            for (Element elem : search_site) {
                connect = Jsoup.connect("https://www.skapiec.pl" + elem.attr("href"));
            }
        }
                while (search_site.size() == 1) ;

    }

        public static void main (String[]args) throws IOException {
            Double[] range = new Double[2];
            range[0] = 10.0;
            range[1] = 40.0;
            Product product2 = new Product("lalka h", 5, range, 4.5);
            //dodac produkty do tablicy
            // w search zrobić pętle po tablicy
            Controller controller = new Controller();
            controller.Search(product2);

        }
    }
