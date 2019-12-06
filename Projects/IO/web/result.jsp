<%--
  Created by IntelliJ IDEA.
  User: Rozanna
  Date: 18/11/2019
  Time: 00:42
  To change this template use File | Settings | File Templates.

 **************
 na tej stronie mialy wyswietlac sie wyniki, ale strona do wyswietlania wynikow jest dynamicznie robiona
 w klasie Servlet
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
  <meta charset="UTF-8">

  <title>SkąpiecPRO</title>

  <link rel="stylesheet" href="styles.scss">

</head>

<body>
  <div class="result-page">
    <div class="found-product">
      <h4>${result00}</h4>
    </div>
    <div class="product-cost">
      Koszt produktu: ${cost00} PLN
    </div>
    <div class="product-shipping">
      Koszt dostawy: ${shipping00} PLN
    </div>
    <div class="link-product">
      <a href="${link00}">Link do produktu w sklepie</a>
    </div>
    <div class="back-container">
      <a class="back-to-homepage" href="index.jsp">Szukaj innych produktów</a>
    </div>
  </div>


</body>

</html>