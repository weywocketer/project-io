function submitForms() {


    document.getElementById("form0").submit();
    document.getElementById("form1").submit();
    document.getElementById("form2").submit();
    document.getElementById("form3").submit();
    document.getElementById("form4").submit();
}

var submitButton = document.getElementById('submit-btn');
submitButton.addEventListener('click', submitForms);

