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
  <script type="text/javascript" src="functions.js"></script>

</head>

<body>

  <h1>SkąpiecPRO - find the best products!</h1>


      <form id="form" method="get" name="Servlet" action="Servlet">
        <div class="product-forms">
          <div class="product-form">
            <div>
              <label>Nazwa produktu</label> <input type="text" name="name0" size="50"
                                                   placeholder="Wprowadź nazwę produktu" />
            </div>
            <div>
              <label>Ilość</label> <input type="text" name="count0" size="50" placeholder="Wprowadź ilość" />
            </div>
            <div>
              <label>Cena</label>
              <div class="form-cena">
                <h5>Od</h5> <input type="text" name="range10" size="50" id="input-cena" placeholder="Cena minimalna" />
                <h5>Do</h5> <input type="text" name="range20" size="50" id="input-cena" placeholder="Cena maksymalna" />
              </div>
            </div>
          </div>

          <div class="product-form-1">
            <div>
              <label>Nazwa produktu</label> <input type="text" name="name1" size="50"
                placeholder="Wprowadź nazwę produktu" />
            </div>
            <div>
              <label>Ilość</label> <input type="text" name="count1" size="50" placeholder="Wprowadź ilość" />
            </div>
            <div>
              <label>Cena</label>
              <div class="form-cena">
                <h5>Od</h5> <input type="text" name="range11" size="50" id="input-cena" placeholder="Cena minimalna" />
                <h5>Do</h5> <input type="text" name="range21" size="50" id="input-cena" placeholder="Cena maksymalna" />
              </div>
            </div>
          </div>

          <div class="product-form-2">
            <div>
              <label>Nazwa produktu</label> <input type="text" name="name2" size="50"
                placeholder="Wprowadź nazwę produktu" />
            </div>
            <div>
              <label>Ilość</label> <input type="text" name="count2" size="50" placeholder="Wprowadź ilość" />
            </div>
            <div>
              <label>Cena</label>
              <div class="form-cena">
                <h5>Od</h5> <input type="text" name="range12" size="50" id="input-cena" placeholder="Cena minimalna" />
                <h5>Do</h5> <input type="text" name="range22" size="50" id="input-cena" placeholder="Cena maksymalna" />
              </div>
            </div>
          </div>

          <div class="product-form-3">
            <div>
              <label>Nazwa produktu</label> <input type="text" name="name3" size="50"
                placeholder="Wprowadź nazwę produktu" />
            </div>
            <div>
              <label>Ilość</label> <input type="text" name="count3" size="50" placeholder="Wprowadź ilość" />
            </div>
            <div>
              <label>Cena</label>
              <div class="form-cena">
                <h5>Od</h5> <input type="text" name="range13" size="50" id="input-cena" placeholder="Cena minimalna" />
                <h5>Do</h5> <input type="text" name="range23" size="50" id="input-cena" placeholder="Cena maksymalna" />
              </div>
            </div>
          </div>

          <div class="product-form-4">
            <div>
              <label>Nazwa produktu</label> <input type="text" name="name4" size="50"
                placeholder="Wprowadź nazwę produktu" />
            </div>
            <div>
              <label>Ilość</label> <input type="text" name="count4" size="50" placeholder="Wprowadź ilość" />
            </div>
            <div>
              <label>Cena</label>
              <div class="form-cena">
                <h5>Od</h5> <input type="text" name="range14" size="50" id="input-cena" placeholder="Cena minimalna" />
                <h5>Do</h5> <input type="text" name="range24" size="50" id="input-cena" placeholder="Cena maksymalna" />
              </div>
            </div>
          </div>
        </div>
        <input id="submit-btn" type="submit" value="Szukaj" size="50" />
      </form>


</body>

</html>