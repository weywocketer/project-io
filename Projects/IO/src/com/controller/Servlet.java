package com.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //RequestDispatcher view = request.getRequestDispatcher("/Home.jsp");
        //view.include(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html, charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        //tworzenie parametrów
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<Integer> counts = new ArrayList<Integer>();
        ArrayList<Double[]> ranges = new ArrayList<Double[]>();
        //ArrayList<Double> ranges2 = new ArrayList<Double>();
        ArrayList<Thread> threads = new ArrayList<Thread>(); //do watkow
        //dodajemy do poszczegolnych list podane dane przez użytkownika

        //pobieranie danych wpisanych przez uzytkownika
        for(int i = 0;i<5;i++){
            if(!request.getParameter("name"+i).isEmpty()) {
                try {
                    names.add(request.getParameter("name" + i));
                    request.setAttribute("name"+i,request.getParameter("name" + i)); //nazwa wpisana przez użytkownika
                    counts.add(Integer.parseInt(request.getParameter("count" + i)));
                    Double[] range = new Double[2];
                    range[0] = Double.parseDouble(request.getParameter("range1" + i));
                    range[1] = Double.parseDouble(request.getParameter("range2" + i));
                    ranges.add(range);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('Wprowadź poprawne dane!');");
                    //out.println("location='index.jsp';");
                    out.println("</script>");

                }
            }

        }
        Skapiec skapiec = new Skapiec();

        //tworzymy obiekty klasy Product i dodajemy do naszej listy produktow klasy Skapiec
        if(names.isEmpty())
        {
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Wprowadź jakieś dane!');");
            out.println("location='index.jsp';");
            out.println("</script>");
        }
        else {
            for (int i = 0; i < names.size(); i++) {
                skapiec.getProducts().add(new Product(names.get(i), counts.get(i), ranges.get(i)));
            }
        }

        //tworzymy watek dla kazdego produktu z naszej listy
        for (Product p: skapiec.getProducts()) {
            threads.add( new Thread(new Runnable() {
                public void run() {
                    try {
                        skapiec.Search(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }

        //uruchamiamy watki
        for (Thread t:threads){
            t.run();
        }

        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<title>SkąpiecPRO</title>");
        out.println("<link rel=\"stylesheet\" href=\"styles.scss\">");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Wyniki wyszukiwania</h1>");
      //do sortowania list
        ListComparator<Result> r = new ListComparator<>();
        ArrayList<ArrayList<Result>> top3 = new ArrayList<ArrayList<Result>>();
        top3 = skapiec.choose_results(); //wybor wynikow!!! ważne


        //wypisanie zestawien
        for(int i=0;i<top3.size();i++){
            out.println("<h3>Zestawienie: "+(i+1)+"</h3>");
            out.println("<div class=\"products-result\">");
            for(Result result: top3.get(i)){
                out.println("<div class=\"result-page\">");
                out.println("<div class=\"found-product\">");
                out.println("<div class=\"product-cost\">");
                out.println("<h4>" + result.getName() + "</h4>");
                out.println("</div>");
                out.println("<div class=\"product-cost\">");
                out.println("Koszt produktu: " + result.getCost() + " PLN");
                out.println("</div>");
                out.println("<div class=\"product-shipping\">");
                out.println("Koszt dostawy: " + result.getMin_Shipping() + " PLN");
                out.println("</div>");
                out.println("<div class=\"link-product\">");
                out.println(" <a href=" + result.getLink()  + ">Link do produktu w sklepie</a>");
                out.println("</div>");
                out.println("</div>");
                out.println("</div>");
            }
            out.println("</div>");
            out.println("<div class=\"product-cost\">");
            out.println("<h3>Suma zestawienia "+(i+1)+": " + r.count_sum(top3.get(i)) + "</h3>");
            out.println("</div>");
        }

        for(Product product:skapiec.getProducts()){
            if(product.Get_without_range()){
                out.println("<div class=\"result-page\">");
                out.println("<div class=\"found-product\">");
                out.println("<h3>Brak wyników dla: "+product.Get_Name()+" w danym zakresie cen!</h3>");
                out.println("<h3>Wyszukano bez zakresu cenowego.</h3>");
                out.println("</div>");
                out.println("</div>");
            }
            if(product.Get_Results().isEmpty()){
                out.println("<div class=\"result-page\">");
                out.println("<div class=\"found-product\">");
                out.println("<h3>Brak wyników dla: "+product.Get_Name()+"</h3>");
                out.println("</div>");
                out.println("</div>");
            }
        }

        out.println("<div class=\"back-container\">");
        out.println("<a class=\"back-to-homepage\" href=\"index.jsp\">Szukaj innych produktów</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
        //request.getRequestDispatcher("result.jsp").forward(request, response);

    }

}