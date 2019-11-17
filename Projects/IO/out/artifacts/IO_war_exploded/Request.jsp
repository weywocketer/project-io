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

<form action="/IO_war_exploded/Request.jsp">
   <label>Nazwa Produktu</label> <input type="text" name="name"  size="50"/>
   <label>Ilość</label> <input type="text" name="count"  size="50"/>
   <input type="submit" value="Szukaj" size="50"/>
</form>

</body>
</html>
