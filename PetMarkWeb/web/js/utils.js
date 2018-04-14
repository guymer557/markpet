/**
 * Created by davej on 8/7/2017.
 */

var productCompareFilterValues = { Company : null,  searchWord : null, lowPrice:null, highPrice:null, Shops:null, AnimalCategory : null, Category:null };
var searchFilterValues = { searchWord : null, animalCategoryVal : null, categoryVal : null, priceVal : null  };
var ITEMS_ON_PAGE = 12;
var prodDividedObject = {};
var abroadProdDividedObject = {};
var currentProductCompare;
var globalProductsList;

function getUser() {
    var userResult = null;
    sendAsyncAjax('../SearchServlet', {action : "getUser"}, "GET", function (user) {
        userResult = user;
    });
    return userResult;
}
function setupUserInfo() {
    var user = getUser();
    // sendAjax('../SearchServlet', {action : "getUser"}, "GET", function (user) {
        if (user != null){
            $('#signin a').remove();
            $("#userWelcome label").text('ברוך הבא ' + user.Name);
            $('#userWelcome').show();
        }
        else{
            $('#userWelcome').hide();
        }
    // });


}
$(document).ready(function () {
    setShoppingCartValue();
    setupUserInfo();
    navBarActions();

    // window.location.reload();
});

function navBarActions() {
    // Add smooth scrolling to all links in navbar + footer link
    $(".navbar a, footer a[href='#myPage']").on('click', function(event) {
        // Make sure this.hash has a value before overriding default behavior
        if (this.hash !== "") {
            // Prevent default anchor click behavior
            event.preventDefault();

            // Store hash
            var hash = this.hash;

            // Using jQuery's animate() method to add smooth page scroll
            // The optional number (900) specifies the number of milliseconds it takes to scroll to the specified area
            $('html, body').animate({
                scrollTop: $(hash).offset().top
            }, 900, function(){

                // Add hash (#) to URL when done scrolling (default click behavior)
                window.location.hash = hash;
            });
        } // End if
    });

    $(window).scroll(function() {
        $(".slideanim").each(function(){
            var pos = $(this).offset().top;

            var winTop = $(window).scrollTop();
            if (pos < winTop + 600) {
                $(this).addClass("slide");
            }
        });
    });
}

function setCurrentProductCompare(item, callBackFunction){
    currentProductCompare = item;
    var  data = {
        action: "setCurrentProductCompare",
        item: JSON.stringify(item)

    };
    sendAsyncAjax('../SearchServlet', data, 'POST', callBackFunction);
}

function getCurrentProductCompare() {
    var currentProduct;
    console.log("get current product compare");
    var  data = {
        action: "getCurrentProductCompare",

    };
    $.ajax({
        url: '../SearchServlet',
        data: data,
        type: 'GET',
        async: false,
        success: function (result) {
            currentProduct = result;
        }
    });
    return currentProduct;
}

function submitSearchForm() {
    // clearSearchFilterValues();
    var searchWhere = $('.navbar .selectpicker').val();
    searchFilterValues.searchWord = $('.search-holder').val();
    var SF = {};
    if (searchWhere === 'local'){
        SF = getSearchFilter();
    }
    SF.m_searchWord = $('.search-holder').val().toLowerCase();

    updateSearchFilter(SF, function(){
        window.location = "Market.html";
    });
    return false;

}




function setShoppingCartValue() {
    var data = {
        action: "getCartProducts"
    }
    sendAsyncAjax('../SearchServlet', data, 'GET', function (json) {
        $('#cartBadgeValue').text(json.length);
    });
}



// function goToMarket() {
//     // clearSearchFilterValues();
//     var SF = getSearchFilter();
//     updateSearchFilter(function () {
//         window.location = "Market.html";
//     })
// }

function updateSearchFilter(SF, callBackFunction){
    var  data = {
            action: "updateSearchFilter",
            SF: JSON.stringify(SF)

    };
    sendAsyncAjax('../SearchServlet', data, 'POST', callBackFunction);
}

/* */


function getProductCompareFilter() {
    var PCF = null;
    var data = {
        action: "getProductCompareFilter",
    }
    $.ajax({
        url: '../SearchServlet',
        data: data,
        type: 'GET',
        async: false,
        success: function (result) {
            PCF = result;
        }
    });
    return PCF;
}

function updatePCF(PCF, callBackFunction) {
    var  data = {
        action: "updateProductCompareFilter",
        PCF: JSON.stringify(PCF)
    }
    sendAjax('../SearchServlet', data, 'POST', callBackFunction);
}

function getSFilterProducts() {
    var products = null;
    var data = {
        action: "getSFilterProducts",
    }
    $.ajax({
        url: '../SearchServlet',
        data: data,
        type: 'GET',
        async: false,
        success: function (result) {
            products = result;
        }
    });
    return products;
}

function getPCFilterProducts() {
    var products = null;
    var data = {
        action: "getPCFilterProducts",
    }
    $.ajax({
        url: '../SearchServlet',
        data: data,
        type: 'GET',
        async: false,
        success: function (result) {
            products = result;
        }
    });
    return products;
}

function getSearchFilter() {
    var SF = null;
    var data = {
        action: "getSearchFilter",
    }
    $.ajax({
        url: '../SearchServlet',
        data: data,
        type: 'GET',
        async: false,
        success: function (result) {
            SF = result;
        }
    });
    return SF;
}

// function updateSF(SF, callBackFunction) {
//     var  data = {
//         action: "updateSearchFilter",
//         SF: JSON.stringify(SF)
//     }
//     $.ajax({
//         url: '../SearchServlet',
//         data: data,
//         type: 'POST',
//         success: callBackFunction
//     });
// }

function DogsCategoryClicked(a) {
    // var PCF = getProductCompareFilter();
    var PCF = {};
    PCF.Company = null;
    PCF.AnimalCategory = "כלב";
    PCF.Category = $(a).text();
    updatePCF(PCF, function () {
        window.location = "CategoryMarket.html";
    })
    return false;
}
function CategoryClicked(a, animalCategory) {
    // var PCF = getProductCompareFilter();
    var PCF = {};
    PCF.Company = null;
    PCF.AnimalCategory = animalCategory;
    PCF.Category = $(a).text();
    updatePCF(PCF, function () {
        window.location = "CategoryMarket.html";
    })
    return false;
}

function AnimalCategoryClicked(animalCategory){
    var SF = {}
    SF.m_AnimalCategory = animalCategory;

    updateSearchFilter(SF, function(){
        window.location = "Market.html";
    });
    return false;
}

function clearSearchFilterValues() {
    for (var key in searchFilterValues) {
        key = null;
    }
}

function clearProductCompareFilterValues() {
    for (var key in productCompareFilterValues) {
        key = null;
    }
}



function setProductsByAnimal(animal, SF) {
    searchFilterValues.animalCategoryVal = animal;
    searchFilterValues.searchWord = SF.m_searchWord;
    updateSearchFilter(function () {
        window.location = "Market.html";
    })
}





function addProductToCart(item) {
    var  data = {
        action: "addProductToCart",
        product: JSON.stringify(item)
    };
    sendAjax('../SearchServlet', data, 'POST', function () {
        setShoppingCartValue();
    });
};

function buildProdSection(item){
    var divItem = $('<div class="galleryItem hvr-grow well" />');
    var divCaption = $('<div style="font: bold 14px Arial; color:salmon;" class="caption text-center"/>');
    var h3 = $('<p />').text(item.m_Name);
    var paySign = '₪ ';
    var pPrice = $('<p class="price"  />').text(paySign + item.m_Price);
    // var pShop = $('<p  />').text("מ- " + item.m_Shop);
    var pShop = $('<img class="shopImage" src=""  />');
    pShop.attr('src' , item.Logo);
    var img_element = $('<img src="" alt="../../common/non_image" />');
    var hr = $('<hr/>');
    img_element.attr('src', item.m_Image);
    // img_a.append(img_element);
    var buyButton = $('<a href="#" class="btn btn-primary hvr-glow" role="button"/>').text("קנה עכשיו");
    var addButton = $('<label class="btn btn primary hvr-glow"/>').text("הוסף לעגלה");
    addButton.click(function () {
        addProductToCart(item);
    });
    buyButton.attr('href', item.m_URL);
    // var pBottom = $('<div class="bottomItem" />');
    var pBottom = $('<p class="bottomItem text-center" style="position: absolute; bottom: 0; margin-bottom: 10px;" />');
    pBottom.append(hr, pPrice,pShop, buyButton, addButton);
    divCaption.append(img_element, h3);
    divItem.append(divCaption);
    divItem.append(pBottom);
    return divItem;
}

function buildAbroadProdSection(item){
    var divItem = $('<div class="galleryItem hvr-grow well" />');
    var divCaption = $('<div style="font: bold 14px Arial; color:salmon;" class="caption text-center"/>');
    var h3 = $('<p />').text(item.m_Name);
    var paySign = '$ ';
    var pPrice = $('<p class="price"  />').text(paySign + item.m_Price);
    // var pShop = $('<p  />').text("מ- " + "eBay");
    var pShop = $('<img class="shopImage " src=""  />');
    pShop.attr('src' , item.m_Shop);
    // var img_a = $('<a href="#"></a>');
    var img_element = $('<img src="" alt="../../common/non_image" />');
    var hr = $('<hr/>');
    img_element.attr('src', item.m_Image);
    // img_a.append(img_element);
    var buyButton = $('<a href="#" class="btn btn-primary hvr-glow" role="button"/>').text("קנה עכשיו");
    var addButton = $('<label class="btn btn primary hvr-glow"/>').text("הוסף לעגלה");
    addButton.click(function () {
        // addProductToCart(item);
    });
    buyButton.attr('href', item.m_URL);
    // var pBottom = $('<div class="bottomItem" />');
    var pBottom = $('<p class="bottomItem text-center" style="position: absolute; bottom: 0; margin-bottom: 10px;" />');
    pBottom.append(hr, pPrice,pShop, buyButton, addButton);
    divCaption.append(img_element, h3);
    divItem.append(divCaption);
    divItem.append(pBottom);
    return divItem;
}

// function searchProductsFreely(searchWord) {
//     clearSearchFilterValues();
//     searchFilterValues.searchWord = searchWord;
//     var action = "updateSearchFilter";
//
//     sendAjax()
// }

function sendAjax(url, data, type, success) {
 $.ajax({
     url: url,
     data: data,
     type: type,
     success: success
 });
}

function sendAsyncAjax(url, data, type, success) {
    $.ajax({
        url: url,
        data: data,
        type: type,
        async: false,
        success: success
    });
}

function setLimitsOnSearchForm() {
    $('.submitSearchButton').attr('disabled','disabled');
    $('.search-holder').keyup(function(){
        if($('.search-holder').val() == ""){
            $('.submitSearchButton').attr('disabled','disabled');
        }
        else{
            $('.submitSearchButton').removeAttr('disabled');
        }
    })
}

function showLoaderAndCallBack(loaderClass, functionToExec, replacedElementClass){
    // $(replacedElementClass).fadeOut('100', function () {
        $(loaderClass).fadeIn('100', function () {
            execFunctionAndCallBack(functionToExec, function () {
                $(loaderClass).fadeOut('100', function () {
                    $(replacedElementClass).fadeIn('100');
                });
            });

        });
    // })

}

function execFunctionAndCallBack(func, callBack) {
    $.when(func()).then(callBack());
}

function showReplacedElement(replacedElementClass, loaderClass) {
    $(loaderClass).fadeOut("slow", function () {
        $(replacedElementClass).fadeIn("slow");
    });
}

function setItemsOnPage(pageNumber){
        $('.prod_list_div').empty();
        for (var i in prodDividedObject[pageNumber]){
            if (prodDividedObject[1][0].Shops != null){
                $('.prod_list_div').append(buildProductCompareSection(prodDividedObject[pageNumber][i]));
            }
            else{
                $('.prod_list_div').append(buildProdSection(prodDividedObject[pageNumber][i]));
            }

        }
}

function getKeyByValue(object, value) {
    for (var key in object){
        if (object[key] === value){
            return key;
        }
    }
    return null;
}

function setProductsBySearchFilter(prodList) {
    globalProductsList = prodList;
    var prodCount = prodList.length;
    var index = 1;
    var itemsOnPageCounter = 0;
    var pagesCount = prodCount/ITEMS_ON_PAGE;
    if (pagesCount <= 1 ){
        pagesCount = 1;
    }
    else{
        pagesCount = Math.floor(prodCount/ITEMS_ON_PAGE);
        if (prodCount % ITEMS_ON_PAGE > 0){
            pagesCount++;
        }
    }
    $(function() {
        $('.pagination-holder').pagination({
            items: prodCount,
            itemsOnPage: ITEMS_ON_PAGE,
            cssStyle: 'light-theme'
        });
    });
    for (i = 1; i <= pagesCount; i++) {
        prodDividedObject[i] = new Array();
    }


    $('.prod_list_div').empty();
    for (var i in prodList){
        prodDividedObject[index].push(prodList[i]);
        itemsOnPageCounter++;
        if (itemsOnPageCounter == ITEMS_ON_PAGE){
            index++;
            itemsOnPageCounter = 0;
        }
        // $('.prod_list_div').append(buildProdSection(prodList[i]));
    }
    for (var i in prodDividedObject[1]){
        $('.prod_list_div').append(buildProdSection(prodDividedObject[1][i]));
    }
    $('.prod_count_span').text(prodCount);
    if (prodCount == 0){
        $('.filter_div').hide();
        $('.prod_pager').hide();

    }
    else{
        $('.filter_div').show()
        $('.prod_pager').show()
    }
    // $('.bubbles').fadeOut("slow", function () {
    //     $('.search-resultes').fadeIn("slow");
    // });

}

function setProductsByAmazon(prodList) {

}

function setAbroadProducts(prodList) {
    var prodCount = prodList.length;
    var index = 1;
    var itemsOnPageCounter = 0;
    var pagesCount = prodCount/ITEMS_ON_PAGE;
    if (pagesCount <= 1 ){
        pagesCount = 1;
    }
    else{
        pagesCount = Math.floor(prodCount/ITEMS_ON_PAGE);
        if (prodCount % ITEMS_ON_PAGE > 0){
            pagesCount++;
        }
    }
    $(function() {
        $('.pagination-holder').pagination({
            items: prodCount,
            itemsOnPage: ITEMS_ON_PAGE,
            cssStyle: 'light-theme'
        });
    });
    for (i = 1; i <= pagesCount; i++) {
        abroadProdDividedObject[i] = new Array();
    }


    $('.abroad_prod_list_div').empty();
    for (var i in prodList){
        abroadProdDividedObject[index].push(prodList[i]);
        itemsOnPageCounter++;
        if (itemsOnPageCounter == ITEMS_ON_PAGE){
            index++;
            itemsOnPageCounter = 0;
        }
        // $('.prod_list_div').append(buildProdSection(prodList[i]));
    }
    for (var i in abroadProdDividedObject[1]){
        $('.abroad_prod_list_div').append(buildAbroadProdSection(abroadProdDividedObject[1][i]));
    }
    $('.abroad_prod_count_span').text(prodCount);
    if (prodCount == 0){
        $('.abroad_filter_div').hide();
        $('.abroad_prod_pager').hide();

    }
    else{
        $('.abroad_filter_div').show()
        $('.abroad_prod_pager').show()
    }
    // $('.bubbles').fadeOut("slow", function () {
    //     $('.search-resultes').fadeIn("slow");
    // });

}


/*function setProductsByEbay(prodList) {
    var prodCount = prodList.length;
    var index = 1;
    var itemsOnPageCounter = 0;
    var pagesCount = prodCount/ITEMS_ON_PAGE;
    if (pagesCount <= 1 ){
        pagesCount = 1;
    }
    else{
        pagesCount = Math.floor(prodCount/ITEMS_ON_PAGE);
        if (prodCount % ITEMS_ON_PAGE > 0){
            pagesCount++;
        }
    }
    $(function() {
        $('.pagination-holder').pagination({
            items: prodCount,
            itemsOnPage: ITEMS_ON_PAGE,
            cssStyle: 'light-theme'
        });
    });
    for (i = 1; i <= pagesCount; i++) {
        abroadProdDividedObject[i] = new Array();
    }


    $('.abroad_prod_list_div').empty();
    for (var i in prodList){
        abroadProdDividedObject[index].push(prodList[i]);
        itemsOnPageCounter++;
        if (itemsOnPageCounter == ITEMS_ON_PAGE){
            index++;
            itemsOnPageCounter = 0;
        }
        // $('.prod_list_div').append(buildProdSection(prodList[i]));
    }
    for (var i in abroadProdDividedObject[1]){
        $('.abroad_prod_list_div').append(buildEbayProdSection(abroadProdDividedObject[1][i]));
    }
    $('.abroad_prod_count_span').text(prodCount);
    if (prodCount == 0){
        $('.abroad_filter_div').hide();
        $('.abroad_prod_pager').hide();

    }
    else{
        $('.abroad_filter_div').show()
        $('.abroad_prod_pager').show()
    }
    // $('.bubbles').fadeOut("slow", function () {
    //     $('.search-resultes').fadeIn("slow");
    // });

}*/

function Logout() {

    sendAsyncAjax('../LoginServlet', {action : "logout"}, "POST", function () {
        window.location.reload();
    })

    return false;
}

function createEbayUrl(keywords, numberOfItems){
    var url = "http://svcs.ebay.com/services/search/FindingService/v1";
    url += "?OPERATION-NAME=findItemsAdvanced";
    url += "&SERVICE-VERSION=1.0.0";
    url += "&SECURITY-APPNAME=DaveBitt-MarkPet-PRD-78dfd86bc-04cbadd0";
    url += "&GLOBAL-ID=EBAY-US";
    url += "&RESPONSE-DATA-FORMAT=JSON";
    url += "&callback=_cb_findItemsByKeywords";
    url += "&REST-PAYLOAD";
    url += "&keywords=" + keywords;
    url += "&itemFilter.name=AvailableTo";
    url += "&itemFilter.value=IL";
    url += "&paginationInput.entriesPerPage=" + numberOfItems.toString();
    return url;
}

//callback from ebay items request
function addEbayItems(items) {
    var products = [];
    for (var i in items){
        var product = {};
        product.m_URL = items[i].viewItemURL[0];
        product.m_Name = items[i].title;
        product.m_Price = items[i].sellingStatus[0].currentPrice[0].__value__;
        product.m_Shop = "../common/ebay.jpg"
        product.m_Image =  items[i].galleryURL[0];
        products.push(product);
    }
    var  data = {
        action: "addEbayItems",
        items: JSON.stringify(products)

    };
    sendAjax('../SearchServlet', data, 'POST', function () {
        initAbroadStores();
    });

}
function _cb_findItemsByKeywords(root) {
    var items = root.findItemsAdvancedResponse[0].searchResult[0].item || [];
    addEbayItems(items);
    // setProductsByEbay(items);

}

function initEbayStores(keywords, numberOfItems) {
    var prod = getCurrentProductCompare();
    s=document.createElement('script'); // create script element
    s.src= createEbayUrl(prod.Name, numberOfItems);
    document.body.appendChild(s);
}

function SortProductsByPrice(asc) {
    if (asc){
        var prodSortedByPrice = globalProductsList.sort(function(a, b) {
            return parseFloat(a.m_Price) - parseFloat(b.m_Price);
        });
    }
    else{
        var prodSortedByPrice = globalProductsList.sort(function(a, b) {
            return parseFloat(b.m_Price) - parseFloat(a.m_Price);
        });
    }

    // setProductsBySortedList(prodSortedByPrice);
    setProductsBySearchFilter(prodSortedByPrice);
}

function setProductsBySortedList(sortedList) {
    globalProductsList = sortedList;
    $('.prod_list_div').empty();
    for (var i in sortedList){
        $('.prod_list_div').append(buildProdSection(sortedList[i]));
    }
}