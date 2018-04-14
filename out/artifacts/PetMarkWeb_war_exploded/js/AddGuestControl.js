/**
 * Created by davej on 4/12/2017.
 */

function AddGuestClick()
{
    event.preventDefault();

    var userName = $('.userName').val();
    // // var computerFlag = $('.computerType').is(':checked');
    // var computerFlag = document.getElementById("CompRadioButton").checked;
    // // if (document.getElementById("CompRadioButton").checked == true ){
    //     computerFlag = true;
    // }

    //$.get('login ', loginCallback);
    $.ajax
    ({
        url: '../GuestServlet',
        data:
        {
            action: "GuestServlet",

            name: userName,
            // isComputer: computerFlag
        },
        type: 'Post',
        success: function () {
            console.log("Added");
        }

    });
}

function AddWebSiteClick()
{
    event.preventDefault();

    var URL = $('.url').val();
    // // var computerFlag = $('.computerType').is(':checked');
    // var computerFlag = document.getElementById("CompRadioButton").checked;
    // // if (document.getElementById("CompRadioButton").checked == true ){
    //     computerFlag = true;
    // }

    //$.get('login ', loginCallback);
    $.ajax
    ({
        url: '../GuestServlet',
        data:
        {
            action: "GuestServlet",

            name: URL,
            // isComputer: computerFlag
        },
        type: 'Post',
        success: function (json) {
            console.log(json.URL);
        }

    });
}