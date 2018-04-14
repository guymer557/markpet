
/**
 * Created by davej on 8/24/2017.
 */
$(document).ready(function () {
    setLimitsOnSearchForm();
    showLoaderAndCallBack('.fa-spinner', initProductPage, '.search-resultes');
});

function initCommentsCount(productCompare) {
    var data = {
        action: "getComments",
        id: productCompare.id
    }
    sendAjax('../SearchServlet', data, 'GET', function (commentsList) {
        $('#descButton span').text('(' + commentsList.length + ')');
        // console.log(commentsList)
    });
}



function initProductPage() {
    var productCompare = getCurrentProductCompare();
    initDescriptionSection(productCompare);
    initStores();

    initAmazonStores();
    initEbayStores("harry", 1);
    // initEbayStores(productCompare.Name, 1);
    // initAbroadStores();
    initCommentsCount(productCompare);


}


function initDescriptionSection(productCompare) {
    var productDiv = buildProductCompareSection(productCompare);
    $('#productDescription').empty();
    $('#productDescription').append(productDiv);

}

function buildProductCompareSection(item){
    var divItem = $('<div class="well" />');
    var divCaption = $('<div style="font: bold 14px Arial; color:salmon;" class="caption text-center"/>');
    var h3 = $('<p />').text(item.Name);
    var paySign = '₪ ';
    var pPrice = $('<p class="price"  />').text(paySign + item.highPrice + " - " + item.lowPrice);
    var pShop = $('<p  />').text("מתוך " + item.Shops + " חנויות");
    // var img_a = $('<a href="#"></a>');
    var img_element = $('<img src="" alt="../../common/non_image" />');
    img_element.attr('src', item.Image);
    // img_a.append(img_element);
    var hr = $('<hr/>');
    // var button = $('<a href="#" class="btn btn-primary hvr-backward" role="button"/>').text("לחוות דעת " + '(' + 123 + ')');
    var button = $('<button id="descButton" class="btn btn-primary hvr-backward"/>').text("לחוות דעת ");
    var span = $('<span />');/*.text('(' + 123 + ')');*/
    button.append(span);
    button.click(function(){
        window.location = "ProductComments.html";
    });
    var pBottom = $('<p class="bottomItem text-center"/>');
    pBottom.append(hr,pPrice,pShop, button);
    divCaption.append(img_element, h3);
    divItem.append(divCaption);
    divItem.append(pBottom);
    return divItem;
}

function getProductsByQueryStr(queryStr) {
    var data = {
        action: "searchByQuery",
        // queryStr: productCompare.productsQuery,
        queryStr: queryStr
    }
    sendAjax('../SearchServlet', data, 'GET', function (prodList) {
      setProductsBySearchFilter(prodList);
    });
}

function initStores() {
    var data = {
        action: "initStores",
    }
    sendAjax('../SearchServlet', data, 'GET', function (prodList) {
        setProductsBySearchFilter(prodList);
    });
}

function initAbroadStores() {
    var data = {
        action: "getAbroadStores",
    }
    sendAjax('../SearchServlet', data, 'GET', function (prodList) {
        setAbroadProducts(prodList);
    });
}

function initAmazonStores() {
    var data = {
        action: "initAmazonStores",
    }
    sendAsyncAjax('../SearchServlet', data, 'POST', function () {
        // setProductsByAmazon(prodList);
    });
}

// function initEbayStores(keywords, numberOfItems) {
//     s=document.createElement('script'); // create script element
//     s.src= createEbayUrl(keywords, numberOfItems);
//     document.body.appendChild(s);
// }