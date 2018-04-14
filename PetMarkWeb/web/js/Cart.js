
/**
 * Created by davej on 8/14/2017.
 */

$(document).ready(function(){


    setLimitsOnSearchForm();
    initCart();

});


function    initCart() {
    var data = {
        action: "getCartProducts"
    }
    sendAjax('../SearchServlet', data, 'GET',  setProductsByCart);
    // showLoader('.bubbles', sendAjax('../SearchServlet', data, 'GET',  setProductsByCart), '.search-resultes');
}

function deleteProductFromCart(item) {
    var  data = {
        action: "deleteProductFromCart",
        product: JSON.stringify(item)
    };
    sendAjax('../SearchServlet', data, 'POST', function () {
        initCart();
    });
}

function buildCartProdSection(item) {
    var divItem = $('<div class="galleryItem hvr-grow well" />');
    var divCaption = $('<div style="font: bold 14px Arial; color:salmon;" class="caption text-center"/>');
    var h3 = $('<p />').text(item.m_Name);
    var paySign = '₪ ';
    var pPrice = $('<p class="price"  />').text(paySign + item.m_Price);
    var pShop = $('<p  />').text("מ- " + item.m_Shop);
    // var img_a = $('<a href="#"></a>');
    var img_element = $('<img src="" alt="../../common/non_image" />');
    var hr = $('<hr/>');
    img_element.attr('src', item.m_Image);
    // img_a.append(img_element);
    var buyButton = $('<a href="#" class="btn btn-primary hvr-glow" role="button"/>').text("קנה עכשיו");
    var deleteButton = $('<label class="btn btn primary hvr-glow"/>').text("הסר");
    deleteButton.click(function () {
        deleteProductFromCart(item);
    });
    // addButton.attr('onclick', function(){
    //     addProductToCart(item);
    // });
    buyButton.attr('href', item.m_URL);
    // var pBottom = $('<div class="bottomItem" />');
    var pBottom = $('<div class="bottomItem text-center" style="position: absolute; bottom: 0; margin-bottom: 10px;" />');
    pBottom.append(hr, pPrice,pShop, buyButton, deleteButton);
    divCaption.append(img_element, h3);
    divItem.append(divCaption);
    divItem.append(pBottom);
    return divItem;
}

function setProductsByCart(json) {
    var totalPrice = 0;
    var prodList = json;
    var prodCount = prodList.length;
    $('.amount').text(prodCount);

    // globalProductsList = prodList;
    var pageCount = Math.floor(prodCount / 10);
    var reminder = prodCount % 10;
    if (reminder > 0){
        pageCount++;
    }

        $('.prod_list_div').empty();
    for (var i in prodList){
        totalPrice += prodList[i].m_Price;
        $('.prod_list_div').append(buildCartProdSection(prodList[i]));
    }
    $('.totalPrice').text(totalPrice);
    $('.prod_count_span').text(prodCount);
    if (prodCount == 0){
        $('.filter_div').hide();
        $('.prod_pager').hide();

    }
    else{
        $('.filter_div').show()
        $('.prod_pager').show()
    }
    $('.bubbles').fadeOut("slow", function () {
        $('.search-resultes').fadeIn("slow");
    });

}