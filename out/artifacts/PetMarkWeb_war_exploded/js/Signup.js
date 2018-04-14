
/**
 * Created by davej on 8/26/2017.
 */

$(document).ready(function () {
    if (getUser() != null){
      window.location = "homepage.html"
    }
});

$('#newUser').click(function () {
    $('#signin').fadeOut('100', function () {
        $('#signup').fadeIn('100');
    })
});

$('#oldUser').click(function () {
    $('#signup').fadeOut('100', function () {
        $('#signin').fadeIn('100');
    })
});



function signupUser(){
    var user = {}
    user.email = $('#emailUser').val();
    user.password = $('#passwordUser').val();
    user.Name = $('#nameUser').val();
    // checkSignupSumbition(user);

    var data = {
        action: "addUser",
        user: JSON.stringify(user)

    }

    sendAsyncAjax('../updateDB', data, "POST", function () {
        window.history.back();
    })
    return false;
}

function userExist(user) {
    var userExist = false;
    var data = {
        action: "checkUser",
        user: JSON.stringify(user)
    }
    sendAsyncAjax('../updateDB', data, "GET", function (user) {
        userExist = user != null;
    })
    return userExist
}
function signinUser(){
    var user = {}
    user.email = $('.userMail').val();
    user.password = $('.passwordUser').val();
    if (userExist(user)){
        // window.location = "homepage.html";
        window.history.back();
    }
    else{
        $('#errors').text("דואר אלקטרוני/סיסמא לא נכון/ה");
    }
    return false;
}