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


        // build HTML code
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String name = request.getParameter("name");
        String count = request.getParameter("count");
        String min_rate = request.getParameter("min_rate");
        String range1 = request.getParameter("range1");
        String range2= request.getParameter("range2");
        Double[] range = new Double[2];
        range[0] = Double.parseDouble(range1);
        range[1] =  Double.parseDouble(range2);
        Double min = Double.parseDouble(min_rate);
        Product product = new Product(name,Integer.parseInt(count),range, min);
        Controller controller = new Controller();// coś nie pyka
        controller.Search(product);
        request.setAttribute("lol",controller.getResults()[0].getName());
        request.getRequestDispatcher("result.jsp").forward(request, response);

    }

}
