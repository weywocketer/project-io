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
  <meta charset="UTF-8">

  <title>SkąpiecPRO</title>

  <link rel="stylesheet" href="styles.scss">

</head>

<body>

  <h1>SkąpiecPRO - find the best products!</h1>

  <!-- dodac ograniczenia na wpisywanie itd -->

  <div class="product-forms">
    <div class="servlet-form-1">
      <form method="get" name="Servlet" action="Servlet">
        <div>
          <label>Nazwa produktu</label> <input type="text" name="name" size="50"
            placeholder="Wprowadź nazwę produktu" />
        </div>
        <div>
          <label>Ilość</label> <input type="text" name="count" size="50" placeholder="Wprowadź ilość" />
        </div>
        <div>
          <label>Cena</label>
          <div class="form-cena">
            <h5>Od</h5> <input type="text" name="range1" size="50" id="input-cena" placeholder="Cena minimalna" />
            <h5>Do</h5> <input type="text" name="range2" size="50" id="input-cena" placeholder="Cena maksymalna" />
          </div>
        </div>
        <input type="submit" value="Szukaj" size="50" />
      </form>
    </div>

    <div class="servlet-form-2">
      <form method="get" name="Servlet" action="Servlet">
        <div>
          <label>Nazwa produktu</label> <input type="text" name="name" size="50"
            placeholder="Wprowadź nazwę produktu" />
        </div>
        <div>
          <label>Ilość</label> <input type="text" name="count" size="50" placeholder="Wprowadź ilość" />
        </div>
        <div>
          <label>Cena</label>
          <div class="form-cena">
            <h5>Od</h5> <input type="text" name="range1" size="50" id="input-cena" placeholder="Cena minimalna" />
            <h5>Do</h5> <input type="text" name="range2" size="50" id="input-cena" placeholder="Cena maksymalna" />
          </div>
        </div>
      </form>
    </div>


    <div class="servlet-form-3">
      <form method="get" name="Servlet" action="Servlet">
        <div>
          <label>Nazwa produktu</label> <input type="text" name="name" size="50"
            placeholder="Wprowadź nazwę produktu" />
        </div>
        <div>
          <label>Ilość</label> <input type="text" name="count" size="50" placeholder="Wprowadź ilość" />
        </div>
        <div>
          <label>Cena</label>
          <div class="form-cena">
            <h5>Od</h5> <input type="text" name="range1" size="50" id="input-cena" placeholder="Cena minimalna" />
            <h5>Do</h5> <input type="text" name="range2" size="50" id="input-cena" placeholder="Cena maksymalna" />
          </div>
        </div>
      </form>
    </div>

    <div class="servlet-form-4">
      <form method="get" name="Servlet" action="Servlet">
        <div>
          <label>Nazwa produktu</label> <input type="text" name="name" size="50"
            placeholder="Wprowadź nazwę produktu" />
        </div>
        <div>
          <label>Ilość</label> <input type="text" name="count" size="50" placeholder="Wprowadź ilość" />
        </div>
        <div>
          <label>Cena</label>
          <div class="form-cena">
            <h5>Od</h5> <input type="text" name="range1" size="50" id="input-cena" placeholder="Cena minimalna" />
            <h5>Do</h5> <input type="text" name="range2" size="50" id="input-cena" placeholder="Cena maksymalna" />
          </div>
        </div>
      </form>
    </div>

    <div class="servlet-form-5">
      <form method="get" name="Servlet" action="Servlet">
        <div>
          <label>Nazwa produktu</label> <input type="text" name="name" size="50"
            placeholder="Wprowadź nazwę produktu" />
        </div>
        <div>
          <label>Ilość</label> <input type="text" name="count" size="50" placeholder="Wprowadź ilość" />
        </div>
        <div>
          <label>Cena</label>
          <div class="form-cena">
            <h5>Od</h5> <input type="text" name="range1" size="50" id="input-cena" placeholder="Cena minimalna" />
            <h5>Do</h5> <input type="text" name="range2" size="50" id="input-cena" placeholder="Cena maksymalna" />
          </div>
        </div>
      </form>
    </div>


  </div>

</body>

</html>