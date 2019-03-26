package com.qinxiaoshuo;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import com.reader.support.ISupport;
import com.reader.support.K;
import com.reader.support.XpathHelper;
import com.reader.utils.DataUtils;
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

/**
 * TODO
 *
 * @author xichao
 * @version v1.0.0
 * @created 2018/12/17
 */
public class QinXs implements ISupport {


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
        jsonObject.put(K.Catalogue_Path, QinXsUrl.Url_Base + chaptersUrl.get(index));
        jsonObject.put(K.Catalogue_Title, chaptersTitle.get(index));
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
                .optString(K.Catalogue_Title);
      }
    } catch (JSONException e) {
    }
    try {
      resultObject.put(K.Novel_Path, url);
      resultObject.put(K.Novel_Name, novelName);
      resultObject.put(K.Novel_Catalogue, chapterCatalogues);
      resultObject.put(K.Novel_Author, novelAuthor);
      resultObject.put(K.Novel_last_chapter, newestChapter);
      resultObject.put(K.Novel_logo, novelLogo);
      resultObject.put(K.Novel_intro, novelIntroduction);
      resultObject.put(K.Novel_last_modified, lastModified);
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
    List<String> titles = XpathHelper.getN(dom, QinXsXpathUri.Xpath_Novel_Search_Title_List);
    List<String> urls = XpathHelper.getN(dom, QinXsXpathUri.Xpath_Novel_Search_Url_List);

    if (null != titles && !titles.isEmpty()) {
      //搜索结果列表类型
      if (titles.contains(novelname)) {
          url = QinXsUrl.Url_Search+novelnameEdcode;
          dom = XpathHelper.getDom(url);
      }
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
      jsonObject.put(K.Chapter_Title, title.trim());
      jsonObject.put(K.Chapter_Path, chapterUrl);
      jsonObject.put(K.Chapter_Content, chapterContent);
    } catch (Exception e) {
      Log.e("<Fy>", e.getMessage());
    }
    return jsonObject;
  }

  @Override public JSONArray getNovelInfoByAuthor(String author) {
    return null;
  }

  @Override public JSONArray getNavigateList() {
    return null;
  }

  @Override public JSONArray getRecommendNovels() {
    return null;
  }

  @Override public JSONArray getNavigateItem(String url) {
    return null;
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
