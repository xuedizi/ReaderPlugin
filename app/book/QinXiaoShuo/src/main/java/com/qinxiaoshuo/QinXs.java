package com.qinxiaoshuo;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import com.reader.support.IBookSupport;
import com.reader.support.XpathHelper;
import com.reader.utils.DataUtils;
import com.support.book.BK;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
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
public class QinXs implements IBookSupport {


  @Override public JSONObject getNovelInfoByUrl(String url) {
    Document dom = XpathHelper.getDom(url);
    return getNovelInfo(dom, url);
  }

  public JSONObject getNovelInfo(Document dom, String url) {
    JSONObject resultObject = new JSONObject();
    List<String> chaptersTitle = XpathHelper.getN(dom, QinXsXpathUri.Xpath_chapter_list);
    List<String> chaptersUrl = XpathHelper.getN(dom, QinXsXpathUri.Xpath_chapter_url);
    if (null == chaptersTitle || null == chaptersUrl) {
      return null;
    }
    String novelName = XpathHelper.get(dom, QinXsXpathUri.Xpath_novel_name);
    //获取作者名称
    String novelAuthor = null;
    List<String> ps = XpathHelper.getN(dom, QinXsXpathUri.Xpath_novel_author);
    //if (null != ps && !ps.isEmpty() && ps.get(0).split("：").length > 1) {
    //  novelAuthor = ps.get(0).split("：")[1];
    //}
    if(null != ps && !ps.isEmpty()){
      novelAuthor = ps.get(0);
    }
    //获取最后更新时间
    String lastModifiedStr = XpathHelper.get(dom, QinXsXpathUri.Xpath_novel_last_modified);
    long lastModified = getLastModified(lastModifiedStr);
    String novelLogo = XpathHelper.get(dom, QinXsXpathUri.Xpath_novel_logo);
    String novelIntroduction = XpathHelper.get(dom, QinXsXpathUri.Xpath_novel_introduction);
    JSONArray chapterCatalogues = new JSONArray();
    int len = chaptersTitle.size() > chaptersUrl.size() ? chaptersUrl.size() : chaptersTitle.size();
    for (int index = 0; index < len; index++) {
      JSONObject jsonObject = new JSONObject();
      try {
        String chapterUrl = chaptersUrl.get(index);
        chapterUrl = chapterUrl.substring(0,chapterUrl.lastIndexOf("/"));
        jsonObject.put(BK.Catalogue_Path, QinXsUrl.Url_Base + chapterUrl+"/#"+(index+1));
        jsonObject.put(BK.Catalogue_Title, chaptersTitle.get(index));
        chapterCatalogues.put(jsonObject);
      } catch (JSONException e) {
        Log.e("<Reader>", e.getMessage());
      }
    }
    //获取最新章节
    String newestChapter = XpathHelper.get(dom, QinXsXpathUri.Xpath_novel_last_chapter);
    try {
      if (TextUtils.isEmpty(newestChapter)) {
        newestChapter =
            chapterCatalogues.getJSONObject(chapterCatalogues.length() - 1)
                .optString(BK.Catalogue_Title);
      }
    } catch (JSONException e) {
    }
    try {
      resultObject.put(BK.Novel_Path, url);
      resultObject.put(BK.Novel_Name, novelName);
      resultObject.put(BK.Novel_Catalogue, chapterCatalogues);
      resultObject.put(BK.Novel_Author, novelAuthor);
      resultObject.put(BK.Novel_last_chapter, newestChapter);
      resultObject.put(BK.Novel_logo, novelLogo);
      resultObject.put(BK.Novel_intro, novelIntroduction);
      resultObject.put(BK.Novel_last_modified, lastModified);
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
    return resultObject;
  }

  private long getLastModified(String lastModifiedStr) {
    if (!TextUtils.isEmpty(lastModifiedStr)) {
      String[] split = lastModifiedStr.split("：");
      if (split.length > 1) {
        return DataUtils.getString2Date(split[1], "yyyy-MM-dd");
      }
    }
    return 0;
  }

  @Override public JSONObject getNovelInfoByName(String novelname) {
    String novelnameEdcode = novelname;
    //try {
    //  novelnameEdcode  = URLEncoder.encode(novelname, "GBK");
    //} catch (UnsupportedEncodingException e) {
    //  e.printStackTrace();
    //}

    Document dom = XpathHelper.getDom(QinXsUrl.Url_Search+novelnameEdcode);
    String url = null;
    //判断是搜索列表页或小说结果页
    String titles = XpathHelper.get(dom, QinXsXpathUri.Xpath_Novel_Name_Search);
    //List<String> urls = XpathHelper.getN(dom, QinXsXpathUri.Xpath_Novel_Search_Url_List);

    if (null != titles ) {

      //搜索结果列表类型
      url = QinXsUrl.Url_Search+novelname;
      dom = XpathHelper.getDom(url);
    } else {
      //搜索结果小说详情
      String title = XpathHelper.get(dom, QinXsXpathUri.Xpath_novel_name);
      if (!TextUtils.isEmpty(title)) {
        String chapterUrl = XpathHelper.get(dom, QinXsXpathUri.Xpath_chapter_url);
        assert chapterUrl != null;
        url = QinXsUrl.Url_Base + chapterUrl.substring(0, chapterUrl.lastIndexOf("/"));
      }
    }
    //没有获取到链接直接返回
    if (TextUtils.isEmpty(url)) {
      return null;
    }

    return getNovelInfo(dom, url);
  }

  @Override public JSONObject getChapterContent(String chapterUrl) {
    Document chapterDom = XpathHelper.getDom(chapterUrl);
    String title = XpathHelper.get(chapterDom, QinXsXpathUri.Xpath_chapter_title);
    String novelCatalogUrl = QinXsUrl.Url_Base+XpathHelper.get(chapterDom, QinXsXpathUri.Xpath_catalog_url_by_chapter_url);
    Document catalogDom = XpathHelper.getDom(novelCatalogUrl);
    String novelLogoUrl = XpathHelper.get(catalogDom,QinXsXpathUri.Xpath_novel_logo);
    String novelId = null;
    if(!TextUtils.isEmpty(novelLogoUrl)){
      Pattern pattern = Pattern.compile("\\d+");
      Matcher matcher = pattern.matcher(novelLogoUrl);
      while (matcher.find()) {
        novelId= matcher.group(0);
      }
    }
    String chapterNum = null;
    if(!TextUtils.isEmpty(chapterUrl)){
      Pattern pattern = Pattern.compile("\\d+");
      Matcher matcher = pattern.matcher(chapterUrl);
      while (matcher.find()) {
        chapterNum= matcher.group(0);
      }
    }
    String chapterContentUrl = null;
    if(!TextUtils.isEmpty(novelId) && !TextUtils.isEmpty(chapterNum)){
      // "https://static.qinxiaoshuo.com/book/bookdata/901/14.txt"
      chapterContentUrl = "https://static.qinxiaoshuo.com/book/bookdata/"+novelId+"/"+chapterNum+".txt";
    }
    String chapterContent =null;
    try {
      if(!TextUtils.isEmpty(chapterContentUrl)){
        Connection connect = Jsoup.connect(chapterContentUrl).validateTLSCertificates(false);
        HttpConnection.Response response = (HttpConnection.Response) connect.execute();
        chapterContent = response.body();
        //HtmlCleaner htmlCleaner = new HtmlCleaner();
        //TagNode clean = htmlCleaner.clean(chapterContent);
        //Document dom = new DomSerializer(new CleanerProperties()).createDOM(clean);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
          chapterContent = Html
              .fromHtml(chapterContent, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
          chapterContent = Html.fromHtml(chapterContent).toString();
        }
      }
    }catch (Exception e){

    }


    //Document dom = XpathHelper.getDom(chapterUrl);
    ////String title = XpathHelper.get(dom, QinXsXpathUri.Xpath_chapter_title);
    //List<String> list = XpathHelper.getN(dom, QinXsXpathUri.Xpath_chapter_content);
    //
    //StringBuilder buffer = new StringBuilder();
    //if (null != list) {
    //  for (String chapter : list) {
    //    //html = Html.fromHtml(html).toString();
    //    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
    //      chapter = Html
    //          .fromHtml(chapter, Html.FROM_HTML_MODE_LEGACY).toString();
    //    } else {
    //      chapter = Html.fromHtml(chapter).toString();
    //    }
    //    buffer.append("      ").append(chapter).append("\n");
    //  }
    //}
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put(BK.Chapter_Title, title.trim());
      jsonObject.put(BK.Chapter_Path, chapterUrl);
      jsonObject.put(BK.Chapter_Content, chapterContent);
    } catch (Exception e) {
      Log.e("<Fy>", e.getMessage());
    }
    return jsonObject;
  }

  @Override public JSONArray getNovelInfoByAuthor(String author) {
    Document dom = XpathHelper.getDom(QinXsUrl.Url_Search+"search/?keyword="+author);
    NodeList nodeList = XpathHelper.getNodeList(dom, QinXsXpathUri.Xpath_Search_Item);
    if (null == nodeList) return null;
    JSONArray jsonArray = new JSONArray();
    for (int index = 0; index < nodeList.getLength(); index++) {
      Node item = nodeList.item(index);
      String path = QinXsUrl.Url_Base + XpathHelper.get(item, QinXsXpathUri.Xpath_Search_Path);
      String name = XpathHelper.get(item, QinXsXpathUri.Xpath_Search_Name);
      String logo = XpathHelper.get(item, QinXsXpathUri.Xpath_Search_Logo);
      String authorName = XpathHelper.get(item, QinXsXpathUri.Xpath_Search_Author);
      String intro = XpathHelper.get(item, QinXsXpathUri.Xpath_Search_Intro);
      try {
        jsonArray.put(new JSONObject().put(BK.Novel_Path, path)
            .put(BK.Novel_Name, name)
            .put(BK.Novel_logo, logo)
            .put(BK.Novel_Author, authorName)
            .put(BK.Novel_intro, intro));
      } catch (JSONException e) {
      }
    }
    return jsonArray;
  }

  @Override public JSONArray getNavigateList() {
    Document dom = XpathHelper.getDom(QinXsUrl.Url_Base);
    List<String> navUrls = XpathHelper.getN(dom, QinXsXpathUri.Xpath_Navigate_Url_List);
    List<String> navNames = XpathHelper.getN(dom, QinXsXpathUri.Xpath_Navigate_Name_List);
    if (null == navNames || navUrls == null) return null;
    JSONArray jsonArray = new JSONArray();
    //删除需要登录的导航栏
    for (int index = 0; index < navUrls.size(); index++) {
      //过滤无用的导航
      if (index == 0) continue;
      if(index == 1)continue;
      if(index ==navUrls.size()-1)continue;
      JSONObject jsonObject = new JSONObject();
      //首页单独处理
      try {
        jsonObject.put(BK.Navigate_Name, navNames.get(index));
        String path = navUrls.get(index);
        if (null != path && !path.startsWith(QinXsUrl.Url_Base)) {
          path = QinXsUrl.Url_Base + path;
        }
        jsonObject.put(BK.Navigate_Path, path);
        jsonArray.put(jsonObject);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return jsonArray;
  }

  @Override public JSONArray getRecommendNovels() {
    Document dom = XpathHelper.getDom(QinXsUrl.Url_Base);
    JSONArray jsonArray = getNavigateItem(dom);
    return jsonArray;
  }

  @Override public JSONArray getRecommendNovelsByName(String novelName) {
    return null;
  }

  public JSONArray getNavigateItem(Document dom) {
    NodeList nodeList =
        XpathHelper.getNodeList(dom, QinXsXpathUri.Xpath_Navigate_Novel_Item);
    if (null == nodeList) return null;
    JSONArray novels = new JSONArray();
    for (int m = 0; m < nodeList.getLength(); m++) {
      Node item = nodeList.item(m);
      List<String> introList = XpathHelper.getN(item, QinXsXpathUri.Xpath_Navigate_Novel_Intro);
      String intro = null;
      if (null != introList) {
        StringBuilder buffer = new StringBuilder();
        for (String in : introList) {
          buffer.append(in.replace("\n", ""));
        }
        intro = buffer.toString();
      }
      String author = XpathHelper.get(item, QinXsXpathUri.Xpath_Navigate_Novel_Author);
      String logo = XpathHelper.get(item, QinXsXpathUri.Xpath_Navigate_Novel_Logo);
      //if (null != logo && !logo.startsWith(QinXsUrl.Url_Base)) {
      //  logo = logo;
      //}
      String path = XpathHelper.get(item, QinXsXpathUri.Xpath_Navigate_Novel_Path);
      if (null != path && !path.startsWith(QinXsUrl.Url_Base)) {
        path = QinXsUrl.Url_Base + path;
      }
      String name = XpathHelper.get(item, QinXsXpathUri.Xpath_Navigate_Novel_Name);

      try {
        JSONObject novelJ = new JSONObject();
        novelJ.put(BK.Novel_Name, name);
        novelJ.put(BK.Novel_Author, author);
        novelJ.put(BK.Novel_intro, intro);
        novelJ.put(BK.Novel_logo, logo);
        novelJ.put(BK.Novel_Path, path);
        novels.put(novelJ);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return novels;
  }

  @Override public JSONArray getNavigateItem(String url) {
    Document dom = XpathHelper.getDom(url);
    JSONArray navigateItem = getNavigateItem(dom);
    //JSONArray lasterUpdateItem = getLasterUpdateItem(dom);
    JSONArray json = new JSONArray();
    if (null != navigateItem) {
      for (int index = 0; index < navigateItem.length(); index++) {
        try {
          json.put(navigateItem.getJSONObject(index));
        } catch (JSONException e) {
        }
      }
    }
    return json;
  }

  @Override public JSONArray getNavigateItem(String url, int page) {
    if(TextUtils.isEmpty(url)){
      return null;
    }
    //String pageUrl  = url.substring(0,url.length() - 1);
    String pageUrl = url+"/"+(++page)+".html";
    Document dom = XpathHelper.getDom(pageUrl);
    JSONArray navigateItem = getNavigateItem(dom);
    return navigateItem;
  }

  public static void main(String args[]) {
    String allChapterUrls = new QinXs().getNovelInfoByName("武动乾坤").toString();
    System.out.println("Chapter --> " + allChapterUrls);
    //List<String> allChapterUrls = fy.getNovelInfoByUrl("http://www.baoliny.com/1/index.html");
    new Thread(new Runnable() {
      @Override public void run() {
        QinXs fy = new QinXs();
      }
    }).start();
  }
}
