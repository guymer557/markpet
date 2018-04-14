package DataExtraction;

import Products.Product;
import com.ECS.client.jax.AWSECommerceService;
import com.ECS.client.jax.AwsHandlerResolver;
import com.ECS.client.jax.Item;
import com.ECS.client.jax.Items;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guy on 23/08/2017.
 */
public class AmazonExtraction {
    private static final String k_AWSSecretKey = "49b4wuLEujQk4TXhg+pc8zLkI+jqzgghFCcVyRPe"; //taken from AWS site
    private static final String k_AWSAccessKey = "AKIAJMTSUFDTGGVDES3Q"; // taken from AWS site
    private static final String k_AssociateTag = "guymer557-20"; // taken from Amazon Associate site
    private List<Product> products = new ArrayList<>();

    public List<Product> SearchItems(String name) {
        try {
            AWSECommerceService service = new AWSECommerceService();
            service.setHandlerResolver(new AwsHandlerResolver(k_AWSSecretKey));

            //Set the service port:
            com.ECS.client.jax.AWSECommerceServicePortType port = service.getAWSECommerceServicePort();

            //Get the operation object:
            com.ECS.client.jax.ItemSearchRequest itemRequest = new com.ECS.client.jax.ItemSearchRequest();

            itemRequest.setSearchIndex("PetSupplies"); // should be a function param.
            itemRequest.setBrowseNode("16225013011");
            itemRequest.setKeywords(name); // should be a function param.
            List<String> responseGroupList = itemRequest.getResponseGroup();
            responseGroupList.add("ItemAttributes"); //extract extended details on the product such as price
            responseGroupList.add("Images"); //extract Urls to the product images
            responseGroupList.add("Reviews"); //extract Url to the page of customer reviews, url valid for 24 hours
            com.ECS.client.jax.ItemSearch ItemElement = new com.ECS.client.jax.ItemSearch();
            ItemElement.setAWSAccessKeyId(k_AWSAccessKey);
            ItemElement.setAssociateTag(k_AssociateTag);
            ItemElement.getRequest().add(itemRequest);

            //Call the Web service operation and store the response
            //in the response object:
            com.ECS.client.jax.ItemSearchResponse response = port.itemSearch(ItemElement);
            for(Items list: response.getItems()) {
                for (Item item : list.getItem()) {
                    try {
                        String price = item.getItemAttributes().getListPrice().getFormattedPrice().replace('$', ' ');
                        products.add(new Product(item.getDetailPageURL(), item.getItemAttributes().getTitle(), Float.parseFloat(price), "../common/amazon.jpg", item.getMediumImage().getURL()));
//                        System.out.println(item.getItemAttributes().getTitle() + " " + item.getDetailPageURL() + " " + item.getItemAttributes().getListPrice().getFormattedPrice());
                    }
                    catch(Exception e){

                    }
                }
            }
            return products;
        }
        catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

//   public static void main(String[] args){
//        AmazonExtraction yoyo = new AmazonExtraction();
//        yoyo.SearchItems();
//    }

}




