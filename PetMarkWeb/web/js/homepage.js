// Limitations
$(document).ready(function(){

    // setShoppingCartValue();
    setLimitsOnSearchForm();
    $('#miniHeaderNav').click(function () {
        var SF = {};
        updateSearchFilter(SF, function () {
            window.location = "Market.html";
        })

    });


});

/**
 * Created by davej on 7/6/2017.
 */


function setShoppingCartValue() {
    var data = {
        action: "getCartProducts"
    }
    sendAjax('../SearchServlet', data, 'GET', function (json) {
        $('#cartBadgeValue').text(json.length);
    });
}

