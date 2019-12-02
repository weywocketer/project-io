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
            if(request.getParameter("name"+i)!=null ) {
                try {
                    names.add(request.getParameter("name" + i));
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
                    out.println("location='index.jsp';");
                    out.println("</script>");

                }
            }

        }
        Skapiec skapiec = new Skapiec();

        //tworzymy obiekty klasy Product i dodajemy do naszej listy produktow klasy Skapiec
        for(int i =0;i<names.size();i++){
           skapiec.getProducts().add(new Product(names.get(i),counts.get(i),ranges.get(i)));
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
        range[0] = Double.parseDouble(range1);
        range[1] =  Double.parseDouble(range2);
        Product product = new Product(name,Integer.parseInt(count),range);
         */
        // DODAĆ ŻEBY WYŚWIETLAŁO 3 I DLA KAŻDEGO PRODUKTU!!!
        for (int i = 0; i <skapiec.getProducts().size();i++) {
            if (skapiec.getProducts().get(i).Get_Results().size()!=0) {
                request.setAttribute("result11", skapiec.getProducts().get(i).Get_Results().get(0).getName());
                request.setAttribute("cost11", skapiec.getProducts().get(i).Get_Results().get(0).getCost());
                request.setAttribute("shipping11", skapiec.getProducts().get(i).Get_Results().get(0).getMin_Shipping());
                request.setAttribute("link11", skapiec.getProducts().get(i).Get_Results().get(0).getLink());
                request.getRequestDispatcher("result.jsp").forward(request, response);

            } else {
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Brak wyników dla:"+skapiec.getProducts().get(i).Get_Name()+"!Spróbuj zmienić zakres cen!');");
                out.println("location='index.jsp';");
                out.println("</script>");
            }
        }
        //request.getRequestDispatcher("result.jsp").forward(request, response);


    }

}
