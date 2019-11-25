<%--
  Created by IntelliJ IDEA.
  User: Rozanna
  Date: 15/11/2019
  Time: 22:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Skąpiec.pl</title>
</head>
<body>
<!-- dodac ograniczenia na wpisywanie itd -->
<form  method="get" name="Servlet" action="Servlet">
  <label>Nati2</label> <input type="text" name="name"  size="50"/>
  <label>Ilość</label> <input type="text" name="count"  size="50"/>
    <label>Min reputacja sprzedawcy</label> <input type="text" name="min_rate"  size="50"/>
    <d>Wpisz zakres cen</d>
    <label>Od</label> <input type="text" name="range1"  size="50"/>
    <label>Do</label> <input type="text" name="range2"  size="50"/>
  <input type="submit" value="Szukaj" size="50"/>
</form>

</body>
</html>