package com.qidian;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import com.reader.support.ISupport;
import com.reader.support.K;
import com.reader.support.XpathHelper;
import com.reader.utils.DataUtils;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
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
public class Qd implements ISupport {

  @Override public JSONObject getNovelInfoByUrl(String url) {
    Connection.Response novelResponse = XpathHelper.getResponse(url);
    if (null == novelResponse) return null;
    //获取目录章节列表
    JSONArray chapterCatalogues = getChapters(novelResponse, url);
    if (null == chapterCatalogues) return null;
    //解析目录信息
    Document dom = XpathHelper.getDom(novelResponse);
    String novelName = XpathHelper.get(dom, QdXpathUri.Xpath_novel_name);
    String novelAuthor = XpathHelper.get(dom, QdXpathUri.Xpath_novel_author);
    String novelLogo = XpathHelper.get(dom, QdXpathUri.Xpath_novel_logo);
    String novelIntroduction = XpathHelper.get(dom, QdXpathUri.Xpath_novel_introduction);
    //最新时间
    String lastModifiedStr = XpathHelper.get(dom, QdXpathUri.Xpath_novel_last_modified);
    long lastModified = getLastModified(lastModifiedStr);
    String newestChapter = XpathHelper.get(dom, QdXpathUri.Xpath_novel_last_chapter);
    try {
      if (TextUtils.isEmpty(newestChapter)) {
        newestChapter = chapterCatalogues.getJSONObject(chapterCatalogues.length() - 1)
            .optString(K.Catalogue_Title);
      }
    } catch (Exception e) {
    }
    //整合小说基本信息
    JSONObject resultObject = new JSONObject();
    try {
      resultObject.put(K.Novel_Name, novelName);
      resultObject.put(K.Novel_Path, url);
      resultObject.put(K.Novel_Catalogue, chapterCatalogues);
      resultObject.put(K.Novel_Author, novelAuthor);
      resultObject.put(K.Novel_last_chapter, newestChapter);
      resultObject.put(K.Novel_logo, QdUrl.Url_Base_Http + novelLogo);
      resultObject.put(K.Novel_intro, novelIntroduction);
      resultObject.put(K.Novel_last_modified, lastModified);
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
    return resultObject;
  }

  private long getLastModified(String lastModifiedStr) {
    if (TextUtils.isEmpty(lastModifiedStr)) return 0;
    try {
      if (lastModifiedStr.contains("小时前")) {
        String time = lastModifiedStr.substring(0, lastModifiedStr.lastIndexOf("小时前"));
        Integer integer = Integer.valueOf(time);
        return System.currentTimeMillis() - TimeUnit.HOURS.toMillis(integer);
      } else if (lastModifiedStr.contains("分钟前")) {
        String time = lastModifiedStr.substring(0, lastModifiedStr.lastIndexOf("分钟前"));
        Integer integer = Integer.valueOf(time);
        return System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(integer);
      } else if (lastModifiedStr.contains("昨日")) {
        String time = lastModifiedStr.substring(0, lastModifiedStr.lastIndexOf("昨日"));
        return System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1);
      } else if (lastModifiedStr.contains(":")) {
        return DataUtils.getString2Date(lastModifiedStr, "yyyy-MM-dd HH:mm");
      } else {
        return DataUtils.getString2Date(lastModifiedStr, "yyyy-MM-dd");
      }
    } catch (Exception e) {
      Log.e("<Qd>", e.getMessage());
    }
    return 0;
  }

  public JSONArray getChapters(Connection.Response novelResponse, String searchResultUrl) {
    Map<String, String> cookie = novelResponse.cookies();
    String _csrfToken = cookie.get("_csrfToken");
    org.jsoup.Connection.Response response =
        XpathHelper.getResponse(String.format(QdUrl.Url_Catalog_Page, _csrfToken,
            searchResultUrl.substring(searchResultUrl.lastIndexOf("/") + 1)));
    if (null == response) {
      return null;
    }
    String body = response.body();
    if (TextUtils.isEmpty(body)) {
      return null;
    }
    try {
      JSONObject result = new JSONObject(body);
      String dataJson = result.optString("data");
      JSONObject data = new JSONObject(dataJson);

      JSONArray jsonArray = new JSONArray();
      JSONArray vsArray = data.optJSONArray("vs");
      for (int array = 0; array < vsArray.length(); array++) {
        JSONObject vs = vsArray.getJSONObject(array);
        JSONArray cs = vs.optJSONArray("cs");
        for (int index = 0; index < cs.length(); index++) {
          JSONObject jsonObject = new JSONObject();
          JSONObject csJSONObject = cs.getJSONObject(index);
          try {
            jsonObject.put(K.Catalogue_Path, QdUrl.Url_Chapter + csJSONObject.optString("cU"));
            jsonObject.put(K.Catalogue_Title, csJSONObject.optString("cN"));
            jsonArray.put(jsonObject);
          } catch (JSONException e) {
            Log.e("<Reader>", e.getMessage());
          }
        }
      }
      return jsonArray;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 示例链接:https://book.qidian.com/ajax/book/category?_csrfToken=3HSMPRqboAVR4UwEvcDTvBaRIEPmD2BXVvpFxIJ8&bookId=1004608738
   */
  @Override public JSONObject getNovelInfoByName(String name) {
    //获取详情页信息
    String searchResultUrl =
        QdUrl.Url_Base_Http + XpathHelper.get(String.format(QdUrl.Url_Search, name),
            QdXpathUri.Xpath_Search);
    return getNovelInfoByUrl(searchResultUrl);
  }

  @Override public JSONObject getChapterContent(String chapterUrl) {

    Document dom = XpathHelper.getDom(chapterUrl);
    String title = XpathHelper.get(dom, QdXpathUri.Xpath_chapter_title);
    List<String> list = XpathHelper.getN(dom, QdXpathUri.Xpath_chapter_content);
    StringBuilder buffer = new StringBuilder();
    if (null != list) {
      for (String chapter : list) {
        //html = Html.fromHtml(html).toString();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
          chapter = Html
              .fromHtml(chapter, Html.FROM_HTML_MODE_LEGACY).toString();
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
    Document dom = XpathHelper.getDom(String.format(QdUrl.Url_Search, author));
    JSONArray jsonArray = getNavigateItem(dom);
    return jsonArray;
  }

  @Override public JSONArray getNavigateList() {
    Document dom = XpathHelper.getDom(QdUrl.Url_Rank);
    NodeList nodeList = XpathHelper.getNodeList(dom, QdXpathUri.Xpath_Navigate_List);
    if (null != nodeList) {
      JSONArray jsonArray = new JSONArray();
      for (int index = 0; index < nodeList.getLength(); index++) {
        Node item = nodeList.item(index);
        String name = XpathHelper.get(item, QdXpathUri.Xpath_Navigate_Name);
        String url = XpathHelper.get(item, QdXpathUri.Xpath_Navigate_Url);
        if (!TextUtils.isEmpty(url) && !url.startsWith(QdUrl.Url_Base_Http)) {
          url = QdUrl.Url_Base_Http + url;
        }
        JSONObject jsonObject = new JSONObject();
        try {
          jsonObject.put(K.Navigate_Name, name).put(K.Navigate_Path, url);
          jsonArray.put(jsonObject);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
      return jsonArray;
    }
    return null;
  }

  @Override public JSONArray getRecommendNovels() {
    Document dom = XpathHelper.getDom(QdUrl.Url_Recommend);
    JSONArray navigateItem = getNavigateItem(dom);
    return navigateItem;
  }

  @Override public JSONArray getNavigateItem(String url) {
    Document dom = XpathHelper.getDom(url);
    JSONArray navigateItem = getNavigateItem(dom);
    return navigateItem;
  }

  public JSONArray getNavigateItem(Document dom) {
    NodeList nodeList = XpathHelper.getNodeList(dom, QdXpathUri.Xpath_Navigate_Novel_Item);
    if (null != nodeList) {
      JSONArray jsonArray = new JSONArray();
      for (int index = 0; index < nodeList.getLength(); index++) {
        Node item = nodeList.item(index);
        String logo =
            QdUrl.Url_Base_Http + XpathHelper.get(item, QdXpathUri.Xpath_Navigate_Novel_Logo);
        String name = XpathHelper.get(item, QdXpathUri.Xpath_Navigate_Novel_Name);
        String author = XpathHelper.get(item, QdXpathUri.Xpath_Navigate_Novel_Author);
        String intro = XpathHelper.get(item, QdXpathUri.Xpath_Navigate_Novel_Intro);
        String lastChapter = XpathHelper.get(item, QdXpathUri.Xpath_Navigate_Novel_Last_Chapter);
        if (!TextUtils.isEmpty(lastChapter) && lastChapter.contains(" ")) {
          lastChapter = lastChapter.substring(lastChapter.indexOf(" ")).trim();
        }
        String lastModifedStr =
            XpathHelper.get(item, QdXpathUri.Xpath_Navigate_Novel_Last_Modified);
        long lastModified = getLastModified(lastModifedStr);
        String path =
            QdUrl.Url_Base_Http + XpathHelper.get(item, QdXpathUri.Xpath_Navigate_Novel_Path);

        JSONObject jsonObject = new JSONObject();
        try {
          jsonObject.put(K.Novel_Path, path);
          jsonObject.put(K.Novel_Name, name);
          jsonObject.put(K.Novel_logo, logo);
          jsonObject.put(K.Novel_last_chapter, lastChapter);
          jsonObject.put(K.Novel_last_modified, lastModified);
          jsonObject.put(K.Novel_intro, intro);
          jsonObject.put(K.Novel_Author, author);
          jsonArray.put(jsonObject);
        } catch (JSONException e) {

        }
      }
      return jsonArray;
    }
    return null;
  }

  public static void main(String args[]) {
    Qd fy = new Qd();
    String con = fy.getNovelInfoByName("武动乾坤").toString();
    //List<String> allChapterUrls = fy.getChaptersByNovelName("武动乾坤", 0, 2);
    //List<String> allChapterUrls = fy.getNovelInfoByUrl("http://www.baoliny.com/1/index.html");
  }
}
