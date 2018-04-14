package DataExtraction;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Guy on 26/08/2017.
 */
public class ExtractionInfo {
    private String m_baseURI;
    private List<String> m_CssSelectorsPath;
    private String m_CssProductNameSelector;
    private String m_CssProductPriceSelector;
    private String m_CssImageUrlSelector;
    private String m_CssProductUrlSelector;
    private String m_CssProductCategorySelector;
    private String m_CssAnimalCategorySelector;
   //private boolean m_IsSeparateToPages;
    private int m_DelayTime = 0;
    private int m_ParseWayNum;

    //-------> ParseWay 1 members
    private String m_CssFirstPageSelector;
    private String m_CssLastPageSelector;
    private int m_PageIndexPattern;
    private String m_PhraseToFormatInTheUrl;

    //-------> ParseWay 2 members
    private String m_CssPagesLinksSelector;

    private void setDataMembers(HttpServletRequest i_ObjectData){
        this.m_baseURI = i_ObjectData.getParameter("Url");
        this.m_CssProductNameSelector = i_ObjectData.getParameter("productName");
        this.m_CssProductPriceSelector = i_ObjectData.getParameter("productPrice");
        this.m_CssImageUrlSelector = i_ObjectData.getParameter("imageUrl");
        this.m_CssProductUrlSelector = i_ObjectData.getParameter("productUrl");
        this.m_CssProductCategorySelector = i_ObjectData.getParameter("productCategory");
        this.m_CssAnimalCategorySelector = i_ObjectData.getParameter("animalCategory");
        if(!i_ObjectData.getParameter("delayTime").equals("")) {
            this.m_DelayTime = Integer.valueOf(i_ObjectData.getParameter("delayTime"));
        }

        Gson gson = new Gson();
        this.m_CssSelectorsPath = gson.fromJson(i_ObjectData.getParameter("productPathElementsList"), List.class);
        //this.m_IsSeparateToPages = gson.fromJson(i_ObjectData.getParameter("isSeparateToPages"), boolean.class);
        this.m_ParseWayNum = gson.fromJson(i_ObjectData.getParameter("parseWay"), int.class);
        List<String> parseWayInfo = gson.fromJson(i_ObjectData.getParameter("parseWayInfoList"), List.class);

        if(m_ParseWayNum==1) {
            this.m_CssFirstPageSelector = parseWayInfo.get(0);
            this.m_CssLastPageSelector = parseWayInfo.get(1);
            this.m_PhraseToFormatInTheUrl = parseWayInfo.get(2);
            this.m_PageIndexPattern = Integer.valueOf(parseWayInfo.get(3));
        }
        else{
            this.m_CssPagesLinksSelector = parseWayInfo.get(0);
        }
    }

    public ExtractionInfo(HttpServletRequest i_ObjectData){
        setDataMembers(i_ObjectData);
    }


    public String getM_baseURI() {
        return m_baseURI;
    }

    public List<String> getM_CssSelectorsPath() {
        return m_CssSelectorsPath;
    }

    public String getM_CssProductNameSelector() {
        return m_CssProductNameSelector;
    }

    public String getM_CssProductPriceSelector() {
        return m_CssProductPriceSelector;
    }

    public String getM_CssImageUrlSelector() {
        return m_CssImageUrlSelector;
    }

    public String getM_CssProductUrlSelector() {
        return m_CssProductUrlSelector;
    }

    public String getM_CssProductCategorySelector() {
        return m_CssProductCategorySelector;
    }

    public String getM_CssAnimalCategorySelector() {
        return m_CssAnimalCategorySelector;
    }
//
//    //public boolean isM_IsSeparateToPages() {
//        return m_IsSeparateToPages;
//    }

    public int getM_DelayTime() {
        return m_DelayTime;
    }

    public int getM_ParseWayNum() {
        return m_ParseWayNum;
    }

    public String getM_CssFirstPageSelector() {
        return m_CssFirstPageSelector;
    }

    public String getM_CssLastPageSelector() {
        return m_CssLastPageSelector;
    }

    public int getM_PageIndexPattern() {
        return m_PageIndexPattern;
    }

    public String getM_PhraseToFormatInTheUrl() {
        return m_PhraseToFormatInTheUrl;
    }

    public String getM_CssPagesLinksSelector() {
        return m_CssPagesLinksSelector;
    }
}
