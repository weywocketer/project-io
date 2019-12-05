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
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        //tworzenie parametrów
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<Integer> counts = new ArrayList<Integer>();
        ArrayList<Double[]> ranges = new ArrayList<Double[]>();
        //ArrayList<Double> ranges2 = new ArrayList<Double>();
        ArrayList<Thread> threads = new ArrayList<Thread>(); //do watkow
        //dodajemy do poszczegolnych list podane dane przez użytkownika

        //ZMIENIC OZNACZENIA W STRONCE!!!! NP NAME0,NAME1, ...NAME4
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
        /*
        String name = request.getParameter("name");
        String count = request.getParameter("count");
        String range1 = request.getParameter("range1");
        String range2= request.getParameter("range2");

        Double[] range = new Double[2];
        range[1] =  Double.parseDouble(range2);
        Product product = new Product(name,Integer.parseInt(count),range);
         */
        // DODAĆ ŻEBY WYŚWIETLAŁO 3 I DLA KAŻDEGO PRODUKTU!!!
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<title>SkąpiecPRO</title>");
        out.println("<link rel=\"stylesheet\" href=\"styles.scss\">");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Wyniki wyszukiwania</h1>");
        /// divy kolo siebie w jednym produkcie!!
        //sumy zestawien!!!
        ListComparator<Result> r = new ListComparator<>();
        ArrayList<ArrayList<Result>> top3 = new ArrayList<ArrayList<Result>>();
        top3 = skapiec.choose_results();
        System.out.println(skapiec.getProducts().get(0).Get_Results().size());

        for (int i = 0; i <skapiec.getProducts().size();i++) {
            if (skapiec.getProducts().get(i).Get_Results().size()!=0) {
                out.println("<h3>"+names.get(i)+"</h3>");
                //System.out.println(skapiec.getProducts().get(i).Get_Results().size());
                for(int j=0;j<3;j++) {
                    //out.println("<script type=\"text/javascript\">");
                    try {
                        out.println("<div class=\"result-page\">");
                        out.println("<div class=\"found-product\">");
                        out.println("<h4>" + top3.get(j).get(i).getName() + "</h4>");
                        out.println("</div>");
                        out.println("<div class=\"product-cost\">");
                        out.println("Koszt produktu: " + top3.get(j).get(i).getCost() + " PLN");
                        out.println("</div>");
                        out.println("<div class=\"product-shipping\">");
                        out.println("Koszt dostawy: " + top3.get(j).get(i).getMin_Shipping()  + " PLN");
                        out.println("</div>");
                        out.println("<div class=\"link-product\">");
                        out.println(" <a href=" + top3.get(j).get(i).getLink()  + ">Link do produktu w sklepie</a>");
                        out.println("</div>");
                        out.println("</div>");
                        //out.println("location='result.jsp';");
                        //out.println("</script>");
                    /*
                    request.setAttribute("result" + i+""+j, skapiec.getProducts().get(i).Get_Results().get(j).getName());
                    request.setAttribute("cost" + i+""+j, skapiec.getProducts().get(i).Get_Results().get(j).getCost());
                    request.setAttribute("shipping" + i+""+j, skapiec.getProducts().get(i).Get_Results().get(j).getMin_Shipping());
                    request.setAttribute("link" + i+""+j, skapiec.getProducts().get(i).Get_Results().get(j).getLink());

                     */
                        //request.getRequestDispatcher("result.jsp").forward(request, response);
                    }catch (Exception e){
                        out.println("</div>");
                        out.println("</div>");}

                }

            } else {
                /*
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Brak wyników dla:"+skapiec.getProducts().get(i).Get_Name()+"!Spróbuj zmienić zakres cen!');");
                //out.println("location='index.jsp';");
                out.println("</script>");
                 */
                out.println("<div class=\"result-page\">");
                out.println("<div class=\"found-product\">");
                out.println("<h3>Brak wyników dla: "+skapiec.getProducts().get(i).Get_Name()+"</h3>");
                out.println("<h3>Spróbuj zmienić zakres cen!</h3>");
                out.println("</div>");
                out.println("</div>");

            }
        }

        out.println("<h3>Suma:</h3>");
        for(int m = 1; m<4; m++) {
            try {
                out.println("<h3> Zestaw " + m + ": " +r.count_sum(top3.get(m-1))+ "</h3>");
            }
            catch (Exception e){
                e.printStackTrace(); //wyszlo poza zakres top3
                //out.println("<h3> Zestaw " + m + ": " +"EH" + "</h3>");
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
