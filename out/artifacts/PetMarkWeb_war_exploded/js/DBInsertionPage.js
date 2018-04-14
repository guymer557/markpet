/**
 * Created by Guy on 24/08/2017.
 */




var PathElementsNumber = 1;
var ListOfProductsToConfirm;
var isScanClickedAgain = false;


function AddProductPathElement(e) {
    e.preventDefault()
    PathElementsNumber++;
    var inputTag = $('<input>', {'type' : 'text', 'class' : 'ProductPath', 'name' : 'e' + PathElementsNumber, 'placeholder' : 'Enter Path Element  ' + PathElementsNumber, 'required' : 'required'});
    $("#ProductPathElementsTextBoxes").append("<br>").append(inputTag);

    if(PathElementsNumber==2) {
        $(".fa-fa-Disabled").prop("disabled", false).prop("class", "fa-fa-Enabled");
    }
}

function RemoveProductPathElement(e){
    e.preventDefault()
    $("input[name='e" + PathElementsNumber + "']").prev().remove();
    $("input[name='e" + PathElementsNumber + "']").remove();
    PathElementsNumber--;
    if(PathElementsNumber==1){
        $(".fa-fa-Enabled").prop("disabled", true).prop("class", "fa-fa-Disabled");
    }
}

function ParseWaySelected(value){
    var br = $('<br>');

    if(value==1){
        $('div.parseWay2').remove();
        var divParseWay1 = $('<div/>', {'class':'parseWay1'});
        $('<label>דף ראשון:</label>', {'for':'firstPageSelector'}).appendTo(divParseWay1);
        $('<input>', {'type':'text','id':'firstPageSelector','class':'parseWay1info', 'name':'firstPageSelector', 'required' : 'required'}).appendTo(divParseWay1);
        $('<label>דף אחרון:</label>', {'for':'lastPageSelector'}).appendTo(divParseWay1);
        $('<input>', {'type':'text','id':'lastPageSelector','class':'parseWay1info', 'name':'lastPageSelector', 'required' : 'required'}).appendTo(divParseWay1);
        $('<label>ביטוי בקישור העמוד לערוך</label>', {'for':'phraseToFormat'}).appendTo(divParseWay1);
        $('<input>', {'type':'text', 'id':'phraseToFormat', 'class':'parseWay1info', 'name':'phraseToFormat', 'required' : 'required'}).appendTo(divParseWay1);
        $('<label>מרווחי המיספור:</label>', {'for':'pageIndexPattern'}).appendTo(divParseWay1);
        $('<input>', {'type':'text','id':'pageIndexPattern', 'class':'parseWay1info', 'name':'pageIndexPattern', 'required' : 'required'}).appendTo(divParseWay1);
        $('select.parseWay').after(divParseWay1);
        $('input.parseWay1info').after(br);
    }
    else if(value==2){
        $('div.parseWay1').remove();
        var divParseWay2 = $('<div/>', {'class':'parseWay2'});
        $('<label>קישורי העמודים:</label>', {'for':'PagesSelector'}).appendTo(divParseWay2);
        $('<input>', {'type':'text','id':'PagesSelector','class':'parseWay2info', 'name':'PagesSelector', 'required' : 'required'}).appendTo(divParseWay2);
        $('select.parseWay').after(divParseWay2);
        $('input.parseWay2info').after(br);
    }
    else{
        $('div.parseWay1').remove();
        $('div.parseWay2').remove();
    }
}

function SubmitWebsiteToDB(e){
        e.preventDefault();
        if(isScanClickedAgain) {
            $('.messageBoxBtns').empty();
            $('.messageBox').val("");
        }
        isScanClickedAgain = true;
        var Url = $('.WebSiteURL')[0].value;
        var productPathElementsList = [];
        $( ".ProductPath" ).each(function() {
           productPathElementsList.push(( this ).value);
        });
        var productName = $('.CSSProductNameSelector')[0].value;
        var productPrice = $('.CSSProductPriceSelector')[0].value;
        var imageUrl = $('.CSSImageUrlSelector')[0].value;
        var productUrl = $('.CSSProductUrlSelector')[0].value;
        var productCategory = $('.CSSProductCategorySelector')[0].value;
        var animalCategory = $('.CSSAnimalCategorySelector')[0].value;
        var delayTime = $('.DelayTime')[0].value;
        // var isSeparateToPages = $("input[class='pageSeparate']:checked", '#WebSiteSubmit').val();
        var parseWay = $('.parseWay')[0].value;
        if(parseWay == 0){
            alert("יש לבחור שיטת ניתוח / Parse Way");
            return;
        }

        var parseWayInfoList = [];
        $("input[class^='parseWay']").each(function(){
            parseWayInfoList.push((this.value));
        });

        $('#scanSitebtn').attr('disabled', true);
        productPathElementsList = JSON.stringify(productPathElementsList);
        parseWayInfoList = JSON.stringify(parseWayInfoList);
        $.ajax
        ({
            url: '../SubmitWebsiteServlet',
            data:
            {
                action: "SubmitWebsiteServlet",
                Url : Url,
                productPathElementsList : productPathElementsList,
                productName : productName,
                productPrice : productPrice,
                imageUrl : imageUrl,
                productUrl : productUrl,
                productCategory : productCategory,
                animalCategory : animalCategory,
                delayTime : delayTime,
                parseWay : parseWay,
                parseWayInfoList : parseWayInfoList
            },
            type: 'post',
            success: SubmitWebsiteToDBCallBack

        });
     InitialProgressBar();
}

function SubmitWebsiteToDBCallBack(json){
    ListOfProductsToConfirm = json.m_ListOfProducts;
    var processLog = json.m_ErrorMessages;
    var text = "";
    var exception = json.m_ExceptionMsg;
    var isExceptionOccurred = json.m_isExceptionOccurred;

    if(!isExceptionOccurred) {
        text += "Errors:\n";
        text += processLog + '\n';
        text += "Found " + json.m_StringsOfProducts.length + " products:\n";
        for (i = 0; i < json.m_StringsOfProducts.length; i++) {
            text += ( i + 1) + ". " + json.m_StringsOfProducts[i] + '\n';
        }

        $('.messageBox').val(text);
        $($('<button>', {
            'type': 'button',
            'class': 'btn btn-primary',
            'id': 'approveReviewbtn',
            'onclick': 'approveReviewbtnClick(event)'
        }).text("הכנס את המוצרים לבסיס הנתונים")).appendTo(".messageBoxBtns");
        $($('<button>', {
            'type': 'button',
            'class': 'btn btn-primary',
            'id': 'resetbtn',
            'onclick': 'resetFormbtnClick(event)'
        }).text("נקה טופס")).appendTo(".messageBoxBtns");

    }
    else {
        $('.messageBox').val(exception);
        ExceptionOnSubmitOccurred();
    }

    $('#scanSitebtn').attr('disabled', false);
}

function ExceptionOnSubmitOccurred(){
    resetAndHideProgressBarWidget();
}

function approveReviewbtnClick(e){
    var logo = $('#LogoImageUrl').val();
    var shipment = $('#ShippingCost').val();
 $.ajax({
     url: '../updateDB',
     data:{
         action: 'updateDB',
         // listOfProductToInsert: JSON.stringify(ListOfProductsToConfirm),
         logo: logo,
         shipment: shipment
     },
     type: 'post',
     success:function(){

     }
});
}

function resetFormbtnClick(e){
e.preventDefault();
    window.location.reload();
}

function InitialProgressBar() {
     var progressbar = $("#progressbar");
     progressbar.show();
     // $('<div>', {'class' : 'progress-label'}).text("בטעינה...").appendTo(progressbar);
     var progressLabel = $(".progress-label");

     progressbar.progressbar({
         value: false,
         change: function () {
             progressLabel.text(progressbar.progressbar("value") + "%");
         },
         complete: function () {
             resetAndHideProgressBarWidget();
         }
     });

     function progress() {
         $.ajax
         ({
             url: '../SubmitWebsiteServlet',
             data:
             {
                 action: "UpdateProgressBar",
             },
             type: 'Post',
             success: function(json){
                 var val2 = json;
                 var val = progressbar.progressbar("value") || 0;
                 progressbar.progressbar("value", val2);

                 if (val < 100) {
                     setTimeout(progress, 150);
                 }
             }
         });
     }

     setTimeout(progress, 3000);
}

function resetAndHideProgressBarWidget(){
    var progressbar = $("#progressbar");
    progressbar.hide();
    progressbar.progressbar("value", 0);
}
