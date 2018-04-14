// /**
//  * Created by davej on 8/16/2017.
//  */
//
// // Construct the request
// // Replace MyAppID with your Production AppID
//
//
// function createEbayUrl(keywords, numberOfItems){
//     var url = "http://svcs.ebay.com/services/search/FindingService/v1";
//     url += "?OPERATION-NAME=findItemsAdvanced";
//     url += "&SERVICE-VERSION=1.0.0";
//     url += "&SECURITY-APPNAME=DaveBitt-MarkPet-PRD-78dfd86bc-04cbadd0";
//     url += "&GLOBAL-ID=EBAY-US";
//     url += "&RESPONSE-DATA-FORMAT=JSON";
//     url += "&callback=_cb_findItemsByKeywords";
//     url += "&REST-PAYLOAD";
//     url += "&keywords=" + keywords;
//     url += "&itemFilter.name=AvailableTo";
//     url += "&itemFilter.value=IL";
//     url += "&paginationInput.entriesPerPage=" + numberOfItems.toString();
//     return url;
// }
//
//
// // Submit the request
// s=document.createElement('script'); // create script element
// s.src= createEbayUrl("orijen adult dog", 1);
// document.body.appendChild(s);
//
// function addItemsToDB(items) {
//     items.forEach(function (item) {
//         // var shipToIsrael = item.shippingInfo[0].shipToLocations[0].toLowerCase().includes("worldwide" || "israel");
//         // if (shipToIsrael == true){
//         //     console.log("ships to israel ==== " + item.viewItemURL);
//         // }
//       var img = item.galleryURL;
//         // var shipToIsrael = item.shippingInfo[0].shipToLocations[0].toLowerCase().includes("worldwide" || "israel");
//         var name = item.title;
//         var paymentMethod = item.paymentMethod[0].toLowerCase();
//         var category = items[0].primaryCategory[0].categoryName[0];
//         var returnsAccepted = item.returnsAccepted[0];
//         var url = item.viewItemURL[0];
//         var condition = item.condition[0].conditionDisplayName[0];
//         var location = item.location[0];
//         var topRated = item.topRatedListing[0];
//
//
//     });
// }
// function _cb_findItemsByKeywords(root) {
//     var items = root.findItemsAdvancedResponse[0].searchResult[0].item || [];
//     addItemsToDB(items);
//
//     // var html = [];
//     // html.push('<table width="100%" border="0" cellspacing="0" cellpadding="3"><tbody>');
//     // for (var i = 0; i < items.length; ++i) {
//     //     var item     = items[i];
//     //     var title    = item.title;
//     //     var pic      = item.galleryURL;
//     //     var viewitem = item.viewItemURL;
//     //     if (null != title && null != viewitem) {
//     //         html.push('<tr><td>' + '<img src="' + pic + '" border="0">' + '</td>' +
//     //             '<td><a href="' + viewitem + '" target="_blank">' + title + '</a></td></tr>');
//     //     }
//     // }
//     // html.push('</tbody></table>');
//     // document.getElementById("results").innerHTML = html.join("");
// }  // End _cb_findItemsByKeywords() function