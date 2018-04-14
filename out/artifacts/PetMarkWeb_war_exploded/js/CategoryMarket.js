
/**
 * Created by davej on 8/7/2017.
 */


$(document).ready(function () {
    setLimitsOnSearchForm();
    showLoaderAndCallBack('.bubbles', initCategoryMarket, '.search-resultes');

    // initCategoryMarket();
});



function initPrices() {
    $('#prices a').click(function () {
        var PCF = getProductCompareFilter();
        PCF.Price = $(this).attr("value");
        updatePCF(PCF,  showLoaderAndCallBack('.bubbles', initCategoryMarket, '.search-resultes'));
    });
}
function initCategoryMarket() {
    var PCF = getProductCompareFilter();
    var PCFproducts = getPCFilterProducts();
    initPath(PCF);
    initCompanies(PCFproducts);
    initPrices();
    setProductsByPCF(PCFproducts);
}

function initPath(PCF) {
    $('.path').empty();
    $('.path').append($('<p class="badge"  />').text('השוואת מחירים'));
    for (var filterElement in PCF){
        var span = $('<p class="badge path-clickable"  />').text(PCF[filterElement]);
        var closer = $('<i class="fa fa-times"></i>');
        span.append(closer);
        $('.path').append(span);
        $('<i class="fa fa-arrow-left" aria-hidden="true"></i>').insertBefore(span);
        span.click(function () {
            console.log($(this).text());
            var PCF1 = getProductCompareFilter();
            PCF1[getKeyByValue(PCF1, $(this).text())] = null;
            updatePCF(PCF1, showLoaderAndCallBack('.bubbles', initCategoryMarket, '.search-resultes'));
        })
    }
}


function showProductsByCompany(a , comp) {

    a.click(function () {
        var PCF = getProductCompareFilter();
        PCF.Company = comp;
        // console.log("company clicked")
        updatePCF(PCF, showLoaderAndCallBack('.bubbles', initCategoryMarket, '.search-resultes'));

    });
}

function submitCategorySearchForm() {
    // clearSearchFilterValues();
    var searchWhere = $('.navbar .selectpicker').val();
    searchFilterValues.searchWord = $('.search-holder').val();
    var SF = {};
    if (searchWhere === 'all'){
        SF = getSearchFilter();
        SF.m_searchWord = $('.search-holder').val();

        updateSearchFilter(SF, function(){
            window.location = "Market.html";
        });
    }
    else{
        var PCF = getProductCompareFilter();
        PCF.searchWord = $('.search-holder').val();
        updatePCF(PCF, showLoaderAndCallBack('.bubbles', initCategoryMarket, '.search-resultes'));
    }

    return false;

}


function initCompanies(prodCompareList) {
    var companiesDiv = $('#companies');
    var compDict = {};
    companiesDiv.empty();
    prodCompareList.forEach(function(x) { compDict[x.Company] = (compDict[x.Company] || 0)+1; });
    for (var comp in compDict){
        var a = $('<a>').text(comp);
        showProductsByCompany(a, comp);
        // a.click()
        var span = $('<span>').text(' (' + compDict[comp] + ') ');
        a.append(span);
        var br = $('<br>');
        companiesDiv.append(a, br);

    }
}

function setProductsByPCF(prodList) {
    // initPath(json);
    var productCompareList = prodList;
    // initCompanies(json.m_FilteredProductCompareList);
    var prodCount = productCompareList.length;
    var index = 1;
    var pagesCount = prodCount/ITEMS_ON_PAGE;
    var itemsOnPageCounter = 0;
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
    for (var i in productCompareList){
        prodDividedObject[index].push(productCompareList[i]);
        itemsOnPageCounter++;
        if (itemsOnPageCounter == ITEMS_ON_PAGE){
            index++;
            itemsOnPageCounter = 0;
        }
        // $('.prod_list_div').append(buildProdSection(prodList[i]));
    }
    for (var i in prodDividedObject[1]){
        $('.prod_list_div').append(buildProductCompareSection(prodDividedObject[1][i]));
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
    // showReplacedElement('.search-resultes', '.bubbles');


}

function execProductCompareQuery(productsQuery) {

    var data = {
        action: "searchByQuery",
        queryStr: productsQuery
    };
    $('.search-resultes').fadeOut("slow", function () {
        $('.bubbles').fadeIn("slow", function () {
            $.ajax(
                {
                    url: '../SearchServlet',
                    data: {
                        action: "searchByQuery",
                        queryStr: productsQuery,
                        // contentType: "application/json; charset=UTF-8",
                    },
                    type: 'GET',
                    success: function (json) {
                        setProductsBySearchFilter(json);
                    }
                }
            );
        });
    });

    // console.log(productsQuery);
}

function buildProductCompareSection(item){
     var divItem = $('<div class="galleryItem hvr-grow well" />');
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
     var button = $('<label class="btn btn-primary hvr-backward"/>').text("להשוואת מחירים");
     // var button = $('<a href="#" class="btn btn-primary" role="button"/>').text("להשוואת מחירים");
     // button.attr('onclick', execProductCompareQuery(item.productsQuery));
    button.click(function(){
        setCurrentProductCompare(item, function () {

            window.location = "ProductPage.html";
        });
        // execProductCompareQuery(item.productsQuery);
    });
    divItem.click(function () {
        setCurrentProductCompare(item, function () {
            window.location = "ProductPage.html";
        });
        // execProductCompareQuery(item.productsQuery);
    })
    var pBottom = $('<p class="bottomItem text-center" style="position: absolute; bottom: 0; margin: 10px 30px;" />');
     pBottom.append(hr,pPrice,pShop, button);
     divCaption.append(img_element, h3);
     divItem.append(divCaption);
     divItem.append(pBottom);
     return divItem;
 }



