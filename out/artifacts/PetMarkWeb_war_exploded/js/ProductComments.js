/**
 * Created by davej on 8/24/2017.
 */

/**
 * Created by davej on 8/24/2017.
 */
var currentProductCompare;
var commentDividedObject = {};

$(document).ready(function () {
    setLimitsOnSearchForm();
    initProductComments();

});

function initProductComments() {
    currentProductCompare = getCurrentProductCompare();
    initDescriptionSection(currentProductCompare);
    initComments();
    if (getUser() != null){
        $('#addCommentDiv').show();
    }
    else{
        $('#addCommentDiv').hide();
    }
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
    // var button = $('<button id="descButton" class="btn btn-primary hvr-backward"/>').text("הוסף חוות דעת ");
    // button.click(function(){
    //
    // });
    var pBottom = $('<p class="bottomItem text-center"/>');
    pBottom.append(hr,pPrice,pShop);
    divCaption.append(img_element, h3);
    divItem.append(divCaption);
    divItem.append(pBottom);
    return divItem;
}

function buildCommentSection(item) {
    var divItem = $('<div class="well galleryItem" />');
    var divCaption = $('<div style="font: bold 14px Arial; color:salmon;" class="caption text-center"/>');
    var pComment = $('<p />').text('" ' + item.Comment + ' "');
    var pUserName = $('<p class="price"  />').text(item.userName);
    var pDate = $('<p  />').text(item.Date);
    var img_element = $('<img src="" alt="../../common/non_image" />');
    img_element.attr('src', "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTmTi4q11Wjf_AcWSBnrzdXBeVTR7IUtUhPu68oYC-WD7PRxMzC");
    var hr = $('<hr/>');
    var pBottom = $('<p class="bottomItem text-center"/>');
    pBottom.append(hr,pUserName, pDate);
    divCaption.append(img_element, pComment);
    divItem.append(divCaption);
    divItem.append(pBottom);
    return divItem;

}
function setComments(commentList) {
        var commentCount = commentList.length;
        var index = 1;
        var itemsOnPageCounter = 0;
        var pagesCount = commentCount/ITEMS_ON_PAGE;
        if (pagesCount <= 1 ){
            pagesCount = 1;
        }
        else{
            pagesCount = Math.floor(commentCount/ITEMS_ON_PAGE);
            if (commentCount % ITEMS_ON_PAGE > 0){
                pagesCount++;
            }
        }
        $(function() {
            $('.pagination-holder').pagination({
                items: commentCount,
                itemsOnPage: ITEMS_ON_PAGE,
                cssStyle: 'light-theme'
            });
        });
        for (i = 1; i <= pagesCount; i++) {
            commentDividedObject[i] = new Array();
        }


        $('.comment_list_div').empty();
        for (var i in commentList){
            commentDividedObject[index].push(commentList[i]);
            itemsOnPageCounter++;
            if (itemsOnPageCounter == ITEMS_ON_PAGE){
                index++;
                itemsOnPageCounter = 0;
            }
        }
        for (var i in commentDividedObject[1]){
            $('.comment_list_div').append(buildCommentSection(commentDividedObject[1][i]));
        }
}
function initComments() {
    var data = {
        action: "getComments",
        id: currentProductCompare.id
    }
    sendAjax('../SearchServlet', data, 'GET', function (commentsList) {
        setComments(commentsList);
        // console.log(commentsList)
    });
}

function addComment(e) {
    e.preventDefault();
    var comment = $('#comment').val();
    var id = currentProductCompare.id;
    $.ajax
    ({
        url: '../updateDB',
        data:
        {
            action: "addComment",
            comment: comment,
            id: id
        },
        type: 'POST',
        success: function (json) {
            initProductComments();
        }

    });
}