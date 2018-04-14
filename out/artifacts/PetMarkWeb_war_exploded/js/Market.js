/**
 * Created by davej on 7/3/2017.
 */



$(document).ready(function () {
    initPrices();
    setLimitsOnSearchForm();
    initMarket();
});


function initMarket() {
    var SF = getSearchFilter();
    var SFproducts = getSFilterProducts();
    initCategories(SF ,SFproducts);
    initPath(SF);
    initAnimals(SFproducts);
    setProductsBySearchFilter(SFproducts);

}

function initPrices() {
    $('#prices a').click(function () {
        console.log("price clicked");
        var SF = getSearchFilter();
        SF.m_Price = $(this).attr("value");
        updateSearchFilter(SF,  showLoaderAndCallBack('.bubbles', initMarket, '.search-resultes'));
    });
}

function initPath(SF) {
    $('.path').empty();
    $('.path').append($('<p class="badge"  />').text('שוק מוצרי בעלי חיים'));
    for (var filterElement in SF){


        var span = $('<p class="badge path-clickable"  />').text(SF[filterElement]);
        var closer = $('<i class="fa fa-times"></i>');
        span.append(closer);
        $('.path').append(span);
        $('<i class="fa fa-arrow-left" aria-hidden="true"></i>').insertBefore(span);
        span.click(function () {
            console.log($(this).text());
            var SF1 = getSearchFilter();
            SF1[getKeyByValue(SF1, $(this).text())] = null;
            updateSearchFilter(SF1, showLoaderAndCallBack('.bubbles', initMarket, '.search-resultes'));
        })
    }

}

function initAnimals(prodList) {
    var animalsDiv = $('#animals');
    animalsDiv.empty();
    var animalDict = {};
    prodList.forEach(function(x) { animalDict[x.m_AnimalCategory] = (animalDict[x.m_AnimalCategory] || 0)+1; });
    for (var animal in animalDict){
        var a = $('<a>').text(animal);
        showProductsByAnimal(a, animal);
        // a.click()
        var span = $('<span class="filterAmount">').text(' (' + animalDict[animal] + ') ');
        a.append(span);
        var br = $('<br>');
        animalsDiv.append(a, br);

    }

}

function initCategories(SF, prodList) {
    var categoriesDiv = $('#categories');
    if (SF.m_AnimalCategory != null){
        categoriesDiv.empty();
        var categoriesDict = {};
        prodList.forEach(function(x) { categoriesDict[x.m_Category] = (categoriesDict[x.m_Category] || 0)+1; });
        for (var category in categoriesDict){
            var a = $('<a>').text(category);
            showProductsByCategory(a, category);
            var span = $('<span class="filterAmount">').text(' (' + categoriesDict[category] + ') ');
            a.append(span);
            var br = $('<br>');
            categoriesDiv.append(a, br);
        }
        $('#categoriesDiv').show();
    }
    else{
        $('#categoriesDiv').hide();
    }


}

function showProductsByAnimal(a, animal) {
    a.click(function () {
        var SF = getSearchFilter();
        SF.m_AnimalCategory = animal;
        updateSearchFilter(SF, initMarket);
    });
}

function showProductsByCategory(a, category) {
    a.click(function () {
        var SF = getSearchFilter();
        SF.m_Category = category;
        updateSearchFilter(SF, initMarket);
    });
}



// function submitSearchNavbar() {
//     if ($('.search-holder').val().length > 0){
//         $('.search-resultes').fadeOut("slow", function () {
//             searchFilterValues.searchWord = $('.search-holder').val();
//             $('.loader').fadeIn("slow", updateSearchFilter('../SearchServlet', searchFilterValues, searchProducts));
//         })
//     }
//     else{
//         alert("מה נחפש אם לא אמרת לנו מה לחפש??")
//     }
//     return false;
// }

function submitSearchDiv(){
    var animalVal = $('#animal_selection').val();
    var priceVal = $('#price_selection').val();
    $('.search-resultes').fadeOut("slow", function () {
        $('.loader').fadeIn("slow", function(){
            $.ajax(
                {
                    url: '../SearchServlet',
                    data: {
                        action: "updateSearchFilter",
                        animalVal: animalVal,
                        priceVal: priceVal,
                    },
                    type: 'POST',
                    success: searchProducts
                }
            );
        });
    })

}

function searchProducts() {
    $.ajax(
        {
            url: '../SearchServlet',
            data: {
                action: "getSearchFilter",
            },
            type: 'GET',
            success: setProductsBySearchFilter
        }
    );


}







function runQuery() {
    var queryStr = $('#query').val();
    $('.search-resultes').fadeOut("slow", function () {
        $('.loader').fadeIn("slow", function () {
            $.ajax(
                {
                    url: '../SearchServlet',
                    data: {
                        action: "searchByQuery",
                        queryStr: queryStr,
                        // contentType: "application/json; charset=UTF-8",
                    },
                    type: 'GET',
                    success: function (json) {
                        var isStr = typeof json;
                        if (isStr != "string"){
                            setProductsBySearchFilter(json)
                        }
                        else{
                            alert(json);
                            $('.loader').fadeOut("slow", function () {
                                $('.search-resultes').fadeIn("slow");
                            });
                        }
                        console.log("success");
                    }
                }
            );
        });
    });

}