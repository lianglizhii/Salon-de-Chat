


function valid()
{
    //liste caractère spéciaux
    var format = /[ `!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?~]/;


    var name = document.getElementById("name");
    var firstname = document.getElementById("firstname");


    if(format.test(name.value))
    {
        alert("Le nom contient des caractères spéciaux");
    }
    else
    {
        alert("Le nom est correct");
    }



}
