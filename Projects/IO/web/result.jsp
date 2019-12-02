<%--
  Created by IntelliJ IDEA.
  User: Rozanna
  Date: 18/11/2019
  Time: 00:42
  To change this template use File | Settings | File Templates.
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
      <h4>${result11}</h4>
    </div>
    <div class="product-cost">
      Koszt produktu: ${cost11} PLN
    </div>
    <div class="product-shipping">
      Koszt dostawy: ${shipping11} PLN
    </div>
    <div class="link-product">
      <a href="${link11}">${link11}</a>
    </div>
    <div class="back-container">
      <a class="back-to-homepage" href="index.jsp">Szukaj innych produktów</a>
    </div>

  </div>


</body>

</html>