package com.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String name = request.getParameter("name");
        String count = request.getParameter("count");
        String range1 = request.getParameter("range1");
        String range2= request.getParameter("range2");
        Double[] range = new Double[2];
        range[0] = Double.parseDouble(range1);
        range[1] =  Double.parseDouble(range2);
        Product product = new Product(name,Integer.parseInt(count),range);
        //Controller controller = new Controller();// coś nie pyka
        Skapiec skapiec = new Skapiec();
        skapiec.Search(product);
        if(product.Get_Results().size()!=0) {
            request.setAttribute("result11", product.Get_Results().get(0).getName());
            request.setAttribute("link11", product.Get_Results().get(0).getLink());
            request.getRequestDispatcher("result.jsp").forward(request, response);
        }
        else{
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Brak wyników! Spróbuj zmienić zakres cen!');");
            out.println("location='index.jsp';");
            out.println("</script>");
        }

    }

}
