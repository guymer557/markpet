package DataExtraction;

import Products.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static java.lang.Float.parseFloat;


/**
 * Created by Guy on 16/02/2017.
 */


public class ExtractProductsV2{

    /********Constants********/
    private static final String k_RegexpToSplitPhrase = "[^a-zA-Z0-9]";
    private static final String k_ProductCategoryTag = "<div class='ProductCategory'>%s</div>";
    private static final String k_AnimalCategoryTag = "<div class='AnimalCategory'>%s</div>";
    private static final String k_CssSelectorProductCategoryTag = "div.ProductCategory";
    private static final String k_CssSelectorAnimalCategoryTag = "div.AnimalCategory";
    private static final String k_CssSelectorInvalidMsg = "-> Something wrong with Css: ";

    /********Info members********/
    private String m_baseURI;
    private List<String> m_CssSelectorsPath;
    private String m_CssProductNameSelector;
    private String m_CssProductPriceSelector;
    private String m_CssImageUrlSelector;
    private String m_CssProductUrlSelector;
    private String m_CssProductCategorySelector;
    private String m_CssAnimalCategorySelector;
    private int m_NumOfCssSelectors;
    private boolean m_IsParsed = false;
    private int m_ParseWayNum;
    private int m_DelayTime = 2000;

    /********Process collect members********/
    private HashSet<String> m_BufferOfPagesFromThreads = new HashSet<>();
    private Elements m_Products = new Elements();
    private Elements m_BufferOfElementsFromThreads = new Elements();
    private String m_BufferOfErrorMessages="";
    private List<String> m_StringsOfProducts = new ArrayList<>();
    private ExtractProgressInfo m_ProgressBarInfo;

    /********Parse way I members********/
    private String m_CssFirstPageSelector;
    private String m_CssLastPageSelector;
    private String m_PhraseToFormatInTheUrl;
    private int m_PageIndexPattern;

    /**********Parse way II members**********/
    private String m_CssPagesLinksSelector;

    public List<Product> ScanSiteForProducts() throws Exception {
        Document CurrentPageToScan=null;
        m_ProgressBarInfo = new ExtractProgressInfo(m_CssSelectorsPath.size()+1);
        m_NumOfCssSelectors = m_CssSelectorsPath.size();

        Elements currentElementsThatWereFound = new Elements();
        try {
            CurrentPageToScan = Jsoup.connect(m_baseURI).get();
        }
        catch(IOException e){
            e.printStackTrace();
            throw new IOException("-> Failed to connect the site " + m_baseURI);
        }

        currentElementsThatWereFound.addAll(CurrentPageToScan.select(m_CssSelectorsPath.get(0)));

        if(currentElementsThatWereFound.isEmpty()){
            throw new IOException(k_CssSelectorInvalidMsg + m_CssSelectorsPath.get(0));
        }

        // This method gathers all the links of the FIRST page in every category of products
        gatherElementsByListOfCssSelectors(currentElementsThatWereFound);

        // This method gathers all the links of the pages in every product's category and extract the products elements from them.
        connectToProductsPages(m_CssSelectorsPath.size() - 1);

        // This method extracts the name and the price of the product from all Elements were found
        return makeListOfProducts();
    }

    private void gatherElementsByListOfCssSelectors(Elements i_currentElementsThatWereFound)throws Exception{

        for(int cssSelectorIndex = 1; cssSelectorIndex< m_CssSelectorsPath.size(); cssSelectorIndex++){
            ExecutorService ExecuteNewConnect = Executors.newFixedThreadPool(i_currentElementsThatWereFound.size());
           for(Element ElementByCssSelector : i_currentElementsThatWereFound){
            //for(int i=0; i<1; i++){Element ElementByCssSelector = i_currentElementsThatWereFound.get(0);
                ExecuteNewConnect.execute(new ConnectToLinkThread(ElementByCssSelector.absUrl("href"),cssSelectorIndex));
                try {
                    Thread.sleep(m_DelayTime);
                } catch (InterruptedException e) {
                    m_BufferOfErrorMessages = m_BufferOfErrorMessages.concat("Delay failed");
                }

               updateProgressInfoForMultiThreading(((ThreadPoolExecutor) ExecuteNewConnect).getMaximumPoolSize(), (int)((ThreadPoolExecutor) ExecuteNewConnect).getCompletedTaskCount());
            }

            ExecuteNewConnect.shutdown();
            while(!ExecuteNewConnect.isTerminated()){
                //Do Nothing! waits for all threads process end
                Thread.sleep(500);
                updateProgressInfoForMultiThreading(((ThreadPoolExecutor) ExecuteNewConnect).getMaximumPoolSize(), (int)((ThreadPoolExecutor) ExecuteNewConnect).getCompletedTaskCount());
            }

            if(m_BufferOfElementsFromThreads.isEmpty() && m_CssSelectorsPath.size() - 1 != cssSelectorIndex
                    || m_BufferOfPagesFromThreads.isEmpty() && m_CssSelectorsPath.size() -1 == cssSelectorIndex){
                throw new IOException(k_CssSelectorInvalidMsg + m_CssSelectorsPath.get(cssSelectorIndex));
            }

            m_ProgressBarInfo.CalculateAndUpdate(((ThreadPoolExecutor)ExecuteNewConnect).getMaximumPoolSize(),(int)((ThreadPoolExecutor)ExecuteNewConnect).getCompletedTaskCount());
            System.out.println("Finish all Threads of CssSelector "+ cssSelectorIndex);
            i_currentElementsThatWereFound.clear();
            i_currentElementsThatWereFound.addAll(m_BufferOfElementsFromThreads);
            m_BufferOfElementsFromThreads.clear();
        }
    }

    private void connectToProductsPages(int i_CssSelectorIndex) throws Exception{

        ExecutorService ExecuteNewConnect = Executors.newFixedThreadPool(m_BufferOfPagesFromThreads.size());
        m_IsParsed = true;
        for(String pageUrl: m_BufferOfPagesFromThreads){
           //for(int i=0; i<1; i++){String pageUrl = m_BufferOfPagesFromThreads.iterator().next();
            Thread.sleep(m_DelayTime);
            ExecuteNewConnect.execute(new ConnectToLinkThread(pageUrl,i_CssSelectorIndex));
            updateProgressInfoForMultiThreading(((ThreadPoolExecutor) ExecuteNewConnect).getMaximumPoolSize(), (int)((ThreadPoolExecutor) ExecuteNewConnect).getCompletedTaskCount());
        }

        ExecuteNewConnect.shutdown();
        while(!ExecuteNewConnect.isTerminated()){
            //Do Nothing! waits for all threads process end
            Thread.sleep(500);
            updateProgressInfoForMultiThreading(((ThreadPoolExecutor) ExecuteNewConnect).getMaximumPoolSize(), (int)((ThreadPoolExecutor) ExecuteNewConnect).getCompletedTaskCount());
        }

        if(m_BufferOfElementsFromThreads.isEmpty()){
            throw new IOException(k_CssSelectorInvalidMsg + m_CssSelectorsPath.get(i_CssSelectorIndex));
        }
        m_ProgressBarInfo.CalculateAndUpdate(((ThreadPoolExecutor)ExecuteNewConnect).getMaximumPoolSize(),(int)((ThreadPoolExecutor)ExecuteNewConnect).getCompletedTaskCount());
        m_Products.addAll(m_BufferOfElementsFromThreads);
    }

    private List<Product> makeListOfProducts(){
        List<Product> m_ListOfProducts = new ArrayList<>();
        int indexOfIteration = 1;
        for(Element product: m_Products){
            Elements auxElementForHoldingNameTag;
            Elements auxElementForHoldingPriceTag;
            Elements auxElementForHoldingImageUrlTag;
            Elements auxElementForHoldingProductUrlTag;
            Elements auxElementForHoldingCategoryProductTag;
            Elements auxElementForAnimalCategoryProductTag;
            auxElementForHoldingNameTag = product.select(m_CssProductNameSelector);
            auxElementForHoldingPriceTag = product.select(m_CssProductPriceSelector);
            auxElementForHoldingImageUrlTag = product.select(m_CssImageUrlSelector);
            auxElementForHoldingProductUrlTag = product.select(m_CssProductUrlSelector);
            auxElementForHoldingCategoryProductTag = product.select(k_CssSelectorProductCategoryTag);
            auxElementForAnimalCategoryProductTag = product.select(k_CssSelectorAnimalCategoryTag);

            String prod_shop = m_baseURI.split("\\.")[1];
            // NEED TO CHANGE //
            String prod_image_url;
            prod_image_url = auxElementForHoldingImageUrlTag.attr("abs:src");
            /////////////////////////
            float priceTag;
            try{
                priceTag = parseFloat(auxElementForHoldingPriceTag.text().split("[^0-9. ]")[1]);
            }catch (Exception e){
                priceTag = 0f;
            }
           // if(auxElementForHoldingPriceTag.text() != null && auxElementForHoldingPriceTag.text().length() > 0) {
//                priceTag = parseFloat(auxElementForHoldingPriceTag.text());
//            }
//            else{
//                priceTag = 0f;
//            }
            m_ListOfProducts.add(new Product(auxElementForHoldingProductUrlTag.attr("abs:href"), auxElementForHoldingNameTag.text(), priceTag, prod_shop, prod_image_url, auxElementForHoldingCategoryProductTag.text(), auxElementForAnimalCategoryProductTag.text()));
            m_StringsOfProducts.add(String.format("Name: %s, ImageUrl: %s, Price: %s, Url: %s, Category: %s, Animal: %s", auxElementForHoldingNameTag.text(), auxElementForHoldingImageUrlTag.attr("src"), auxElementForHoldingPriceTag.text(), auxElementForHoldingProductUrlTag.attr("abs:href"),
                    auxElementForHoldingCategoryProductTag.text(), auxElementForAnimalCategoryProductTag.text()));
//            m_StreamOfProducts = m_StreamOfProducts.concat(String.format("Name: %s, ImageUrl: %s, Price: %s, Url: %s, Category: %s, Animal: %s <br>", auxElementForHoldingNameTag.text(), auxElementForHoldingImageUrlTag.attr("src"), auxElementForHoldingPriceTag.text(), auxElementForHoldingProductUrlTag.attr("abs:href"),
//                    auxElementForHoldingCategoryProductTag.text(), auxElementForAnimalCategoryProductTag.text()));
            m_ProgressBarInfo.CalculateAndUpdate(m_Products.size(), indexOfIteration);
            indexOfIteration++;
        }

        m_ProgressBarInfo.RoundUpTotalPercentsOfRunningTask();
        return m_ListOfProducts;
    }

    private void updateProgressInfoForMultiThreading(int i_CurrentTotal, int i_CurrentPart){
        if(i_CurrentPart < i_CurrentTotal) {
            m_ProgressBarInfo.CalculateAndUpdate(i_CurrentTotal, i_CurrentPart);
        }
    }

    private void ParseWay1(Document i_page,String i_UrlToConnect)throws IOException{
        String urlToFormat;
        String urlOfLastPage = null;
        String newUrlToAdd;
        try {
            urlOfLastPage = i_page.select(m_CssLastPageSelector).first().absUrl("href");
            i_page = Jsoup.connect(urlOfLastPage).get();
        }// Catch exception where there is no another pages in category page.
        catch(NullPointerException e){
            System.out.println(i_UrlToConnect);
            m_BufferOfPagesFromThreads.add(i_UrlToConnect);
            return;
        }
        catch(Exception e){
            m_BufferOfErrorMessages = m_BufferOfErrorMessages.concat(String.format("-> Missing Category, Failed to connect: %s", i_UrlToConnect));
        }

        try {
            urlToFormat = i_page.select(m_CssFirstPageSelector).first().absUrl("href");
        }
        catch (NullPointerException e){
            throw new IOException(k_CssSelectorInvalidMsg + m_CssFirstPageSelector);
        }

        newUrlToAdd = urlToFormat;
        String currentPhrase;
        String[] splitString = m_PhraseToFormatInTheUrl.split(k_RegexpToSplitPhrase);
        Integer currentPageNum = Integer.valueOf(splitString[1]) + m_PageIndexPattern;
        m_BufferOfPagesFromThreads.add(urlOfLastPage);
        while(m_BufferOfPagesFromThreads.add(newUrlToAdd)) {
            currentPhrase = m_PhraseToFormatInTheUrl.replace(splitString[1], currentPageNum.toString());
            newUrlToAdd = urlToFormat.replace(m_PhraseToFormatInTheUrl, currentPhrase);
            currentPageNum = currentPageNum + m_PageIndexPattern;
        }
    }

    private void ParseWay2(Document i_page,String i_UrlToConnect){
            Elements AuxPagesBuffer = i_page.select(m_CssPagesLinksSelector);
            for (Element PageElement : AuxPagesBuffer) {
                m_BufferOfPagesFromThreads.add(PageElement.absUrl("href"));
            }
            m_BufferOfPagesFromThreads.add(i_UrlToConnect);
    }

    private class ConnectToLinkThread implements Runnable{
        private String m_UrlToConnect;
        private int m_CssSelectorIndex;
        private Document m_PageToScan;
        public void run(){
            try {
                m_PageToScan = Jsoup.connect(m_UrlToConnect).get();
            }
            catch(IOException e){
                e.printStackTrace();
                m_BufferOfErrorMessages = m_BufferOfErrorMessages.concat(String.format("-> Failed to connect: %s \n", m_UrlToConnect));
                return;
            }

            try {
                if (m_CssSelectorIndex == m_NumOfCssSelectors - 1 && !m_IsParsed) {
                    switch (m_ParseWayNum) {
                        case 1: {
                            ParseWay1(m_PageToScan, m_UrlToConnect);
                            break;
                        }
                        case 2: {
                            ParseWay2(m_PageToScan, m_UrlToConnect);
                            break;
                        }
                    }
                } else if (m_CssSelectorIndex == m_NumOfCssSelectors - 1) {
                    m_BufferOfElementsFromThreads.addAll(m_PageToScan.select(m_CssSelectorsPath.get(m_CssSelectorIndex)).append(String.format(k_ProductCategoryTag, m_PageToScan.select(m_CssProductCategorySelector).toString())).append(String.format(k_AnimalCategoryTag, m_PageToScan.select(m_CssAnimalCategorySelector).toString())));
                } else {
                    m_BufferOfElementsFromThreads.addAll(m_PageToScan.select(m_CssSelectorsPath.get(m_CssSelectorIndex)));
                }
            }
            catch(IOException e){
                m_BufferOfErrorMessages = m_BufferOfErrorMessages.concat(e.getMessage() + '\n');
            }

        }

        ConnectToLinkThread(String i_url, int i_CssSelectorIndex){
            this.m_UrlToConnect = i_url;
            this.m_CssSelectorIndex = i_CssSelectorIndex;
        }
    }

    public ExtractProductsV2(ExtractionInfo i_DataMembersInfo){
        ExtractProgressInfo.ResetProgressCounter();
        this.m_baseURI = i_DataMembersInfo.getM_baseURI();
        this.m_CssSelectorsPath = i_DataMembersInfo.getM_CssSelectorsPath();
        this.m_CssProductNameSelector = i_DataMembersInfo.getM_CssProductNameSelector();
        this.m_CssProductPriceSelector = i_DataMembersInfo.getM_CssProductPriceSelector();
        this.m_CssImageUrlSelector = i_DataMembersInfo.getM_CssImageUrlSelector();
        this.m_CssProductUrlSelector = i_DataMembersInfo.getM_CssProductUrlSelector();
        this.m_CssAnimalCategorySelector = i_DataMembersInfo.getM_CssAnimalCategorySelector();
        this.m_CssProductCategorySelector = i_DataMembersInfo.getM_CssProductCategorySelector();
        this.m_DelayTime = i_DataMembersInfo.getM_DelayTime();
        this.m_ParseWayNum = i_DataMembersInfo.getM_ParseWayNum();
        if(m_ParseWayNum==1){
            this.m_CssFirstPageSelector = i_DataMembersInfo.getM_CssFirstPageSelector();
            this.m_CssLastPageSelector = i_DataMembersInfo.getM_CssLastPageSelector();
            this.m_PageIndexPattern = i_DataMembersInfo.getM_PageIndexPattern();
            this.m_PhraseToFormatInTheUrl = i_DataMembersInfo.getM_PhraseToFormatInTheUrl();
        }
        else{
            this.m_CssPagesLinksSelector = i_DataMembersInfo.getM_CssPagesLinksSelector();
        }
    }

    public String GetErrorMessagesBuffer(){
        return m_BufferOfErrorMessages;
    }

    public List<String> GetStringsOfProducts(){
        return m_StringsOfProducts;
    }



}








