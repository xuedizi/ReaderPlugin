package com.zhuishu;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import com.reader.support.ISupport;
import com.reader.support.K;
import com.reader.support.XpathHelper;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * TODO
 *
 * @author xichao
 * @version v1.0.0
 * @created 2018/12/17
 */
public class Zs implements ISupport {

  @Override public JSONObject getNovelInfoByUrl(String searchResultUrl) {
    Document dom = XpathHelper.getDom(searchResultUrl);
    JSONObject resultObject = new JSONObject();
    List<String> chaptersTitle = XpathHelper.getN(dom, ZsXpathUri.Xpath_chapter_list);
    List<String> chaptersUrl = XpathHelper.getN(dom, ZsXpathUri.Xpath_chapter_url);
    if (null == chaptersTitle || null == chaptersUrl) {
      return null;
    }
    String novelName = XpathHelper.get(dom, ZsXpathUri.Xpath_novel_name);
    String novelAuthor = XpathHelper.get(dom, ZsXpathUri.Xpath_novel_author);
    String novelLogo = XpathHelper.get(dom, ZsXpathUri.Xpath_novel_logo);
    String novelIntroduction = XpathHelper.get(dom, ZsXpathUri.Xpath_novel_introduction);
    String lastModifiedStr = XpathHelper.get(dom, ZsXpathUri.Xpath_novel_last_modified);
    long lastModified = getLastModified(lastModifiedStr);
    JSONArray chapterCatalogues = new JSONArray();
    int len = chaptersTitle.size() > chaptersUrl.size() ? chaptersUrl.size() : chaptersTitle.size();
    for (int index = 0; index < len; index++) {
      JSONObject jsonObject = new JSONObject();
      try {
        jsonObject.put(K.Catalogue_Path, ZsUrl.Url_Base + chaptersUrl.get(index));
        jsonObject.put(K.Catalogue_Title, chaptersTitle.get(index));
        chapterCatalogues.put(jsonObject);
      } catch (JSONException e) {
        Log.e("<Reader>", e.getMessage());
      }
    }
    String newestChapter = null;
    try {
      newestChapter =
          chapterCatalogues.getJSONObject(chapterCatalogues.length() - 1)
              .optString(K.Catalogue_Title);
    } catch (JSONException e) {
    }
    try {
      resultObject.put(K.Novel_Path, searchResultUrl);
      resultObject.put(K.Novel_Name, novelName);
      resultObject.put(K.Novel_Catalogue, chapterCatalogues);
      resultObject.put(K.Novel_Author, novelAuthor);
      resultObject.put(K.Novel_last_chapter, newestChapter);
      resultObject.put(K.Novel_logo, novelLogo);
      resultObject.put(K.Novel_last_modified, lastModified);
      resultObject.put(K.Novel_intro, novelIntroduction);
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
    return resultObject;
  }

  private long getLastModified(String lastModifiedStr) {
    if (TextUtils.isEmpty(lastModifiedStr)) return 0;
    try {
      if (lastModifiedStr.endsWith("小时前更新")) {
        String hours = lastModifiedStr.substring(0, lastModifiedStr.indexOf("小时前更新"));
        return System.currentTimeMillis() - TimeUnit.HOURS.toMillis(Integer.valueOf(hours));
      } else if (lastModifiedStr.endsWith("天前更新")) {
        String day = lastModifiedStr.substring(0, lastModifiedStr.indexOf("天前更新"));
        return System.currentTimeMillis() - TimeUnit.DAYS.toMillis(Integer.valueOf(day));
      } else if (lastModifiedStr.endsWith("个月前更新")) {
        String mo = lastModifiedStr.substring(0, lastModifiedStr.indexOf("个月前更新"));
        return System.currentTimeMillis() - TimeUnit.DAYS.toMillis(Integer.valueOf(mo) * 30);
      } else if (lastModifiedStr.endsWith("月前更新")) {
        String mo = lastModifiedStr.substring(0, lastModifiedStr.indexOf("月前更新"));
        return System.currentTimeMillis() - TimeUnit.DAYS.toMillis(Integer.valueOf(mo) * 30);
      } else if (lastModifiedStr.endsWith("年前更新")) {
        String year = lastModifiedStr.substring(0, lastModifiedStr.indexOf("年前更新"));
        return System.currentTimeMillis() - TimeUnit.DAYS.toMillis(Integer.valueOf(year) * 365);
      }
    } catch (Exception e) {
      Log.e("<Zs>", e.getMessage());
    }
    return 0;
  }

  @Override public JSONObject getNovelInfoByName(String name) {
    String searchResultUrl =
        XpathHelper.get(String.format(ZsUrl.Url_Search, name), ZsXpathUri.Xpath_Search);
    return getNovelInfoByUrl(ZsUrl.Url_Base + searchResultUrl);
  }

  @TargetApi(Build.VERSION_CODES.N) @Override
  public JSONObject getChapterContent(String chapterUrl) {

    Document dom = XpathHelper.getDom(chapterUrl);
    String title = XpathHelper.get(dom, ZsXpathUri.Xpath_chapter_title);
    List<String> list = XpathHelper.getN(dom, ZsXpathUri.Xpath_chapter_content);
    StringBuilder buffer = new StringBuilder();
    if (null != list) {
      for (String chapter : list) {
        //html = Html.fromHtml(html).toString();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
          chapter = Html.fromHtml(chapter, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
          chapter = Html.fromHtml(chapter).toString();
        }
        buffer.append("      ").append(chapter).append("\n");
      }
    }
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put(K.Chapter_Title, title);
      jsonObject.put(K.Chapter_Path, chapterUrl);
      jsonObject.put(K.Chapter_Content, buffer.toString());
    } catch (Exception e) {
      Log.e("<Fy>", e.getMessage());
    }
    return jsonObject;
  }

  @Override public JSONArray getNovelInfoByAuthor(String author) {
    Document dom = XpathHelper.getDom(String.format(ZsUrl.Url_Search, author));
    NodeList nodeList = XpathHelper.getNodeList(dom, ZsXpathUri.Xpath_Search_Item);
    if (null == nodeList) return null;
    JSONArray jsonArray = new JSONArray();
    for (int index = 0; index < nodeList.getLength(); index++) {
      Node item = nodeList.item(index);
      String path = ZsUrl.Url_Base + XpathHelper.get(item, ZsXpathUri.Xpath_Search_Novel_Path);
      String name = XpathHelper.get(item, ZsXpathUri.Xpath_Search_Novel_Name);
      String logo = XpathHelper.get(item, ZsXpathUri.Xpath_Search_Novel_Logo);
      String authorName = XpathHelper.get(item, ZsXpathUri.Xpath_Search_Novel_Author);
      String intro = XpathHelper.get(item, ZsXpathUri.Xpath_Search_Novel_Intro);
      try {
        jsonArray.put(new JSONObject().put(K.Novel_Name, name)
            .put(K.Novel_Path, path)
            .put(K.Novel_Author, authorName)
            .put(K.Novel_logo, logo)
            .put(K.Novel_intro, intro));
      } catch (JSONException e) {
      }
    }
    return jsonArray;
  }

  @Override public JSONArray getNavigateList() {
    Document dom = XpathHelper.getDom(ZsUrl.Url_ranking);
    NodeList nodeList = XpathHelper.getNodeList(dom, ZsXpathUri.Xpath_Navigate_List);
    if (null != nodeList) {
      JSONArray jsonArray = new JSONArray();
      for (int index = 0; index < nodeList.getLength(); index++) {
        Node item = nodeList.item(index);
        String tag = XpathHelper.get(item, ZsXpathUri.Xpath_Navigate_Tag);
        NodeList childNodeList = XpathHelper.getNodeList(item, ZsXpathUri.Xpath_Navigate_Sub_List);
        if (null != childNodeList) {
          for (int n = 0; n < childNodeList.getLength(); n++) {
            Node node = childNodeList.item(n);
            String name = XpathHelper.get(node, ZsXpathUri.Xpath_Navigate_Name);
            String url = XpathHelper.get(node, ZsXpathUri.Xpath_Navigate_Url);
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(url)) continue;
            url = ZsUrl.Url_Base + url;
            try {
              jsonArray.put(new JSONObject()
                  .put(K.Navigate_Name, String.format("%s(%s)", name, tag))
                  .put(K.Navigate_Path, url));
            } catch (JSONException e) {
            }
          }
        }
      }
      return jsonArray;
    }
    return null;
  }

  @Override public JSONArray getRecommendNovels() {
    JSONArray navigateItem = getNavigateItem(ZsUrl.Url_ranking);
    return navigateItem;
  }

  @Override public JSONArray getNavigateItem(String url) {
    Document dom = XpathHelper.getDom(url);
    JSONArray jsonArray = getNavigateItem(dom);
    return jsonArray;
  }

  private JSONArray getNavigateItem(Document dom) {

    NodeList nodeList = XpathHelper.getNodeList(dom, ZsXpathUri.Xpath_Navigate_Novel_Item);
    if (null != nodeList) {
      JSONArray jsonArray = new JSONArray();
      for (int index = 0; index < nodeList.getLength(); index++) {
        Node item = nodeList.item(index);
        String path = ZsUrl.Url_Base + XpathHelper.get(item, ZsXpathUri.Xpath_Navigate_Novel_Path);
        String name = XpathHelper.get(item, ZsXpathUri.Xpath_Navigate_Novel_Name);
        String logo = XpathHelper.get(item, ZsXpathUri.Xpath_Navigate_Novel_Logo);
        String author = XpathHelper.get(item, ZsXpathUri.Xpath_Navigate_Novel_Author);
        String intro = XpathHelper.get(item, ZsXpathUri.Xpath_Navigate_Novel_Intro);

        try {
          jsonArray.put(new JSONObject()
              .put(K.Novel_Path, path)
              .put(K.Novel_Name, name)
              .put(K.Novel_logo, logo)
              .put(K.Novel_Author, author)
              .put(K.Novel_intro, intro));
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
      return jsonArray;
    }

    return null;
  }

  public static void main(String args[]) {

    //List<String> allChapterUrls = fy.getChaptersByNovelName("武动乾坤", 0, 2);
    //List<String> allChapterUrls = zs.getNovelInfoByUrl("http://www.zhuishushenqi.com/book/5816b415b06d1d32157790b1");
  }
}
