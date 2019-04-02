package com.fengyun;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import com.reader.support.ISupport;
import com.reader.support.K;
import com.reader.support.XpathHelper;
import com.reader.utils.DataUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class Fy implements ISupport {

  @Override public JSONObject getNovelInfoByName(String novelname) {

    Map<String, String> map = new HashMap<>();
    map.put("searchtype", "articlename");
    map.put("searchkey", novelname);
    Document domcument = XpathHelper.getDomByPost(Url.Url_Search_V2, map);
    String url = null;
    //获取小说详情
    //判断是搜索列表页或小说结果页
    List<String> titles = XpathHelper.getN(domcument, XpathUri.Xpath_Novel_Search_Title_List);
    List<String> urls = XpathHelper.getN(domcument, XpathUri.Xpath_Novel_Search_Url_List);

    if (null != titles && !titles.isEmpty()) {
      //搜索结果列表类型
      if (titles.contains(novelname)) {
        if (null != urls) {
          url = urls.get(titles.indexOf(novelname));
          domcument = XpathHelper.getDom(url);
        }
      }
    } else {
      //搜索结果小说详情
      String title = XpathHelper.get(domcument, XpathUri.Xpath_novel_name);
      if (!TextUtils.isEmpty(title)) {
        String chapterUrl = XpathHelper.get(domcument, XpathUri.Xpath_chapter_url);
        assert chapterUrl != null;
        url = String.format(Url.Url_Chapter_list,
            chapterUrl.substring(1, chapterUrl.lastIndexOf("/")));
      }
    }
    //没有获取到链接直接返回
    if (TextUtils.isEmpty(url)) {
      return null;
    }

    return getNovelInfo(domcument, url);
  }

  public String trim(String title) {
    return title.replaceAll("\\s", "").trim();
  }

  public JSONObject getNovelInfo(Document domcument, String url) {
    JSONObject resultObject = new JSONObject();
    List<String> chaptersTitle = XpathHelper.getN(domcument, XpathUri.Xpath_chapter_list);
    List<String> chaptersUrl = XpathHelper.getN(domcument, XpathUri.Xpath_chapter_url);
    if (null == chaptersUrl || chaptersUrl.isEmpty()) {
      return null;
    }
    String novelName = XpathHelper.get(domcument, XpathUri.Xpath_novel_name);

    JSONArray jsonArray = new JSONArray();
    int len = chaptersTitle.size() > chaptersUrl.size() ? chaptersUrl.size() : chaptersTitle.size();
    for (int index = 0; index < len; index++) {
      JSONObject jsonObject = new JSONObject();
      try {
        jsonObject.put(K.Catalogue_Path, Url.Url_Base + chaptersUrl.get(index));
        jsonObject.put(K.Catalogue_Title, chaptersTitle.get(index));
        jsonArray.put(jsonObject);
      } catch (JSONException e) {
        Log.e("<Reader>", e.getMessage());
      }
    }
    String newestChapter =
        jsonArray.optJSONObject(jsonArray.length() - 1).optString(K.Catalogue_Title);
    try {
      resultObject.put(K.Novel_Name, trim(novelName));
      resultObject.put(K.Novel_Path, url);
      resultObject.put(K.Novel_Catalogue, jsonArray);
      resultObject.put(K.Novel_Author, null);
      resultObject.put(K.Novel_last_chapter, newestChapter);
      resultObject.put(K.Novel_logo, null);
      resultObject.put(K.Novel_intro, null);
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
    return resultObject;
  }

  @Override public JSONObject getNovelInfoByUrl(String url) {
    Document domcument = XpathHelper.getDom(url);
    return getNovelInfo(domcument, url);
  }

  @Override public JSONObject getChapterContent(String chapterUrl) {
    Document dom = XpathHelper.getDom(chapterUrl);
    String title = XpathHelper.get(dom, XpathUri.Xpath_chapter_title);
    List<String> list = XpathHelper.getN(dom, XpathUri.Xpath_chapter_content);
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
      jsonObject.put(K.Chapter_Path, Url.Url_Base + chapterUrl);
      jsonObject.put(K.Chapter_Content, buffer.toString());
    } catch (Exception e) {
      Log.e("<Fy>", e.getMessage());
    }
    return jsonObject;
  }

  @Override public JSONArray getNovelInfoByAuthor(String author) {
    Map<String, String> map = new HashMap<>();
    map.put("searchtype", "author");
    map.put("searchkey", author);
    Document domcument = XpathHelper.getDomByPost(Url.Url_Search_V2, map);
    NodeList nodeList = XpathHelper.getNodeList(domcument, XpathUri.Xpath_Search_Item);
    if (null == nodeList) return null;
    JSONArray jsonArray = new JSONArray();
    for (int index = 0; index < nodeList.getLength(); index++) {
      Node item = nodeList.item(index);
      String path = XpathHelper.get(item, XpathUri.Xpath_Search_Novel_Path);
      String name = XpathHelper.get(item, XpathUri.Xpath_Search_Novel_Name);
      String authorName = XpathHelper.get(item, XpathUri.Xpath_Search_Novel_Author);
      String lastChapter = XpathHelper.get(item, XpathUri.Xpath_Search_Novel_Last_Chapter);
      String lastModified = XpathHelper.get(item, XpathUri.Xpath_Search_Novel_Last_Modified);
      long lastModifiedL = getLasterModifiedTime(lastModified);
      try {
        jsonArray.put(new JSONObject().put(K.Novel_Path, path)
            .put(K.Novel_Name, name)
            .put(K.Novel_Author, authorName)
            .put(K.Novel_last_chapter, lastChapter)
            .put(K.Novel_last_modified, lastModifiedL));
      } catch (JSONException e) {
      }
    }
    return jsonArray;
  }

  private long getLasterModifiedTime(String lastModified) {
    lastModified = "20" + lastModified;
    return DataUtils.getString2Date(lastModified, "yyyy-MM-dd");
  }

  @Override public JSONArray getNavigateList() {
    Document dom = XpathHelper.getDom(Url.Url_Base);
    List<String> navUrls = XpathHelper.getN(dom, XpathUri.Xpath_Navigate_Url_List);
    List<String> navNames = XpathHelper.getN(dom, XpathUri.Xpath_Navigate_Name_List);
    if (null == navNames || navUrls == null) return null;
    JSONArray jsonArray = new JSONArray();
    //删除需要登录的导航栏
    for (int index = 0; index < navUrls.size(); index++) {
      //过滤无用的导航
      if (index == 0 || index == 1 || index == 9) continue;
      JSONObject jsonObject = new JSONObject();
      //首页单独处理
      try {
        jsonObject.put(K.Navigate_Name, navNames.get(index));
        String path = navUrls.get(index);
        //if (null != path && !path.startsWith(Url.Url_Base)) {
        //  path = Url.Url_Base + path;
        //}
        jsonObject.put(K.Navigate_Path, path);
        jsonArray.put(jsonObject);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return jsonArray;
  }

  @Override public JSONArray getRecommendNovels() {
    Document dom = XpathHelper.getDom(Url.Url_Base);
    JSONArray jsonArray = getNavigateItem(dom);
    return jsonArray;
  }

  @Override public JSONArray getRecommendNovelsByName(String novelName) {
    return null;
  }

  @Override public JSONArray getNavigateItem(String url) {
    Document dom = XpathHelper.getDom(url);
    JSONArray navigateItem = getNavigateItem(dom);
    JSONArray lasterUpdateItem = getLasterUpdateItem(dom);
    JSONArray json = new JSONArray();
    if (null != navigateItem) {
      for (int index = 0; index < navigateItem.length(); index++) {
        try {
          json.put(navigateItem.getJSONObject(index));
        } catch (JSONException e) {
        }
      }
    }
    if (null != lasterUpdateItem) {
      for (int index = 0; index < lasterUpdateItem.length(); index++) {
        try {
          json.put(lasterUpdateItem.getJSONObject(index));
        } catch (JSONException e) {
        }
      }
    }
    return json;
  }

  @Override public JSONArray getNavigateItem(String url, int page) {
    return null;
  }

  private JSONArray getLasterUpdateItem(Document dom) {
    NodeList nodeList =
        XpathHelper.getNodeList(dom, XpathUri.Xpath_Navigate_Laster_Update_Novel_Item);
    if (null != nodeList) {
      JSONArray jsonArray = new JSONArray();
      for (int index = 0; index < nodeList.getLength(); index++) {
        Node node = nodeList.item(index);
        String path = XpathHelper.get(node, XpathUri.Xpath_Navigate_Laster_Update_Novel_Path);
        String author =
            XpathHelper.get(node, XpathUri.Xpath_Navigate_Laster_Update_Novel_Author);
        String name = XpathHelper.get(node, XpathUri.Xpath_Navigate_Laster_Update_Novel_Name);
        String lastChapter =
            XpathHelper.get(node, XpathUri.Xpath_Navigate_Laster_Update_Novel_Laster_Chapter);
        JSONObject jsonObject = new JSONObject();
        try {
          jsonObject.put(K.Novel_Path, Url.Url_Base + path);
          jsonObject.put(K.Novel_Name, name);
          jsonObject.put(K.Novel_Author, author);
          jsonObject.put(K.Novel_last_chapter, lastChapter);
          jsonArray.put(jsonObject);
        } catch (JSONException e) {
        }
      }
      return jsonArray;
    }
    return null;
  }

  public JSONArray getNavigateItem(Document dom) {
    NodeList nodeList =
        XpathHelper.getNodeList(dom, XpathUri.Xpath_Navigate_Novel_Item);
    if (null == nodeList) return null;
    JSONArray novels = new JSONArray();
    for (int m = 0; m < nodeList.getLength(); m++) {
      Node item = nodeList.item(m);
      List<String> introList = XpathHelper.getN(item, XpathUri.Xpath_Navigate_Novel_Intro);
      String intro = null;
      if (null != introList) {
        StringBuilder buffer = new StringBuilder();
        for (String in : introList) {
          buffer.append(in.replace("\n", ""));
        }
        intro = buffer.toString();
      }
      String author = XpathHelper.get(item, XpathUri.Xpath_Navigate_Novel_Author);
      String logo = XpathHelper.get(item, XpathUri.Xpath_Navigate_Novel_Logo);
      if (null != logo && !logo.startsWith(Url.Url_Base)) {
        logo = Url.Url_Base + logo;
      }
      String path = XpathHelper.get(item, XpathUri.Xpath_Navigate_Novel_Path);
      if (null != path && !path.startsWith(Url.Url_Base)) {
        path = Url.Url_Base + path;
      }
      String name = XpathHelper.get(item, XpathUri.Xpath_Navigate_Novel_Name);

      try {
        JSONObject novelJ = new JSONObject();
        novelJ.put(K.Novel_Name, name);
        novelJ.put(K.Novel_Author, author);
        novelJ.put(K.Novel_intro, intro);
        novelJ.put(K.Novel_logo, logo);
        novelJ.put(K.Novel_Path, path);
        novels.put(novelJ);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return novels;
  }

  public static void main(String args[]) throws IOException {
    Fy fy = new Fy();
    fy.getChapterContent("https://www.fengyunok.com/2/1264.html");
    //System.out.println(URLEncoder.encode("遮天", "GBK"));
    //Connection.Response response =
    //    Jsoup.connect("https://www.fengyunok.com/modules/article/search.php")
    //        .method(Connection.Method.POST)
    //        .validateTLSCertificates(false)
    //        .data("searchtype", "articlename")
    //        .data("searchkey", "三寸人间")
    //        .postDataCharset("GBK")
    //        .execute();
    //String body = response.body();
    //System.out.println(body);
  }
}
