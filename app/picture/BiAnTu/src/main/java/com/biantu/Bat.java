package com.biantu;

import android.text.TextUtils;
import com.reader.support.XpathHelper;
import com.support.picture.IPictureSupport;
import com.support.picture.PK;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by jiaomengfei on 19-04-03.
 */
public class Bat implements IPictureSupport {

  @Override public JSONArray getRecommendPictures() {
      Document dom = XpathHelper.getDom(BatUrl.Url_Recommend_Newest);
      JSONArray jsonArray = getNavigateItem(dom);
      return jsonArray;
  }

  @Override public JSONArray getNavigateList() {
    Document dom = XpathHelper.getDom(BatUrl.Url_Base);
    NodeList nodeList = XpathHelper.getNodeList(dom,BatXpathUri.Xpath_navigate_list);
    if (null != nodeList) {
      JSONArray jsonArray = new JSONArray();
      for (int index = 0; index < nodeList.getLength(); index++) {
        Node item = nodeList.item(index);

        String navigateName = XpathHelper.get(item, BatXpathUri.Xpath_Navigate_Name);
        String navigatePath = XpathHelper.get(item,BatXpathUri.Xpath_Navigate_Url);
        if (null != navigatePath && !navigatePath.startsWith(BatUrl.Url_Base)) {
          navigatePath = BatUrl.Url_Base + navigatePath;
        }

        try {
          JSONObject navigatePic = new JSONObject();
          navigatePic.put(PK.Navigate_Name, navigateName);
          navigatePic.put(PK.Navigate_Path, navigatePath);
          jsonArray.put(navigatePic);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
      return jsonArray;
    }
    return null;
  }

  @Override public JSONArray getNavigateItem(String url, int page) {
    if(TextUtils.isEmpty(url)){
      return null;
    }
    ++page;
    //String pageUrl  = url.substring(0,url.length() - 1);
    String pageUrl = url+"index_"+(++page)+".html";
    Document dom = XpathHelper.getDom(pageUrl);
    JSONArray navigateItem = getNavigateItem(dom);
    return navigateItem;
  }

  public JSONArray getNavigateItem(Document dom) {
    NodeList nodeList =
        XpathHelper.getNodeList(dom, BatXpathUri.Xpath_recommend_newest_item);
    if (null == nodeList) return null;
    JSONArray pictures = new JSONArray();
    for (int m = 0; m < nodeList.getLength(); m++) {
      Node item = nodeList.item(m);
      String pictureUrl = XpathHelper.get(item, BatXpathUri.Xpath_recommend_newest_picture_url);
      String pictureTitle = XpathHelper.get(item,BatXpathUri.Xpath_recommend_newest_picture_title);
      String pictureHref = XpathHelper.get(item,BatXpathUri.Xpath_recommend_newest_picture_href);
      if (null != pictureUrl && !pictureUrl.startsWith(BatUrl.Url_Base)) {
        pictureUrl = BatUrl.Url_Base + pictureUrl;
      }
      if(null != pictureHref && ! pictureHref.startsWith(BatUrl.Url_Base)){
        pictureHref = BatUrl.Url_Base + pictureHref;
      }
      try {
        JSONObject navigatePic = new JSONObject();
        navigatePic.put(PK.Picture_Title, pictureTitle);
        navigatePic.put(PK.Picture_Url, pictureUrl);
        navigatePic.put(PK.Picture_Href,pictureHref);
        pictures.put(navigatePic);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return pictures;
  }

  public static void main(String args[]) {
  }
}
