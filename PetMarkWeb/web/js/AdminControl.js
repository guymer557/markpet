/**
 * Created by davej on 4/14/2017.
 */


var identifiersNumber = 1;

function RefreshWebSites() {
    $.ajax(
        {
            url: '../WebsiteServlet',
            data: {
                action: "websitesList"
            },
            type: 'GET',
            success: refreshWebSitesCallback()
        }
    );
}

function getProductID() {

}
function addComment(e) {
    e.preventDefault();
    var comment = $('#comment').val();
    var productID = getProductID();
    $.ajax
    ({
        url: '../updateDB',
        data:
        {
            action: "addComment",
            comment: comment,
            id: 8000
        },
        type: 'Post',
        success: function () {
            console.log("comment added");
        }

    });
}

function initProductModel(e) {
    e.preventDefault();
    $.ajax
    ({
        url: '../updateDB',
        data:
        {
            action: "initProductModel",
        },
        type: 'POST',
        success: function () {
            alert("INIT producr model done");
        }

    });
}

function refreshWebSitesCallback(json) {
    // var usersTable = $('.usersTable tbody');
//     // var usersPanel = $('.panel-users');
//     // usersTable.empty();
//     // usersPanel.empty();
    var websitesList = json;
    var websiteHtmlSection = $('.WebsitesList');
    websiteHtmlSection.empty();
    // websitesList.forEach(function (website) {
    //     var li = $(document.createElement('li'));
    //     // div.addClass("panel-footer");
    //     li.text(website.URL);
    //     li.appendTo(websiteHtmlSection);
    //     // var tr = $(document.createElement('tr'));
    //     // //
    //     // var td = $(document.createElement('td')).text(user.name);
    //     // //
    //     // td.appendTo(tr);
    //     // //
    //     // tr.appendTo(usersTable);
    // });
//
}
function OnLoad() {
    RefreshWebSites();
    // setInterval(RefreshWebSites, 3000);
}

function UpdateDataBase() {
    $.ajax
    ({
        url: '../updateDB',
        data:
        {
            action: "updateDB",
        },
        type: 'Post',
        success: function () {
            console.log("DB UPDATED");
        }

    });
}

function AddProductsIdentifier(e) {
    e.preventDefault()

    identifiersNumber++;
    var divIdentifiers = document.getElementById('ProductsIdentifiersTextBoxes');
    var input1 = document.createElement("input");
    input1.setAttribute("type","text");
    input1.setAttribute("name","i" + identifiersNumber );
    divIdentifiers.innerHTML += '<input type="text" class="ProductsIdentifier" placeholder="Enter products identifier..."/> <br />';

}


function SubmitWebsite() {
     var prodIdentifiersList = [];
    $( ".ProductsIdentifier" ).each(function() {
       prodIdentifiersList.push(( this ).value);
    });
    var webAdress = $('.URL')[0].value;
    prodIdentifiersList = JSON.stringify({ 'prodIdentifiersList': prodIdentifiersList });
    $.ajax
    ({
        url: '../SubmitWebsiteServlet',
        data:
        {
            action: "SubmitWebsiteServlet",
            webAdress : webAdress,
            prodIdentifierList: prodIdentifiersList,
            // isComputer: computerFlag
        },
        type: 'Post',
        success: function () {
            console.log("Added");
        }

    });
}

function SubmitProductModel(){
    var englishName, hebrewName, Company, Image, Category, AnimalCategory;
    englishName = $('.englishName')[0].value;
    hebrewName = $('.hebrewName')[0].value;
    Company = $('.Company')[0].value;
    Image = $('.Image')[0].value;
    Category = $('.Category')[0].value;
    AnimalCategory = $('.AnimalCategory')[0].value;
    $.ajax
    ({
        url: '../SubmitWebsiteServlet',
        data:
        {
            action: "SubmitProductModel",
            englishName: englishName,
            hebrewName: hebrewName,
            Company: Company,
            Image: Image,
            Category: Category,
            AnimalCategory: AnimalCategory,
        },
        type: 'Post',
        success: function () {
            console.log("Product Model Added");
        }

    });
}

function updateCategory(a, animalCategory) {
    var categoryVal = $(a).text();
    var animalCategoryVal = animalCategory;
    $.ajax(
        {
            url: '../updateDB',
            data: {
                action: "updateProductCompare",
                animalCategoryVal: animalCategoryVal,
                categoryVal: categoryVal,

            },
            type: 'POST',
            success: function (json) {
                // console.log("finished");
                alert(json);
            }
        }
    );

}