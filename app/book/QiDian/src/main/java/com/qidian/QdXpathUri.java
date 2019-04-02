package com.qidian;

/**
 * TODO
 *
 * @author xichao
 * @version v1.0.0
 * @created 2018/12/20
 */
public class QdXpathUri {

  //小说章节内容
  public static final String Xpath_chapter_content =
      "//div[@class='read-content j_readContent']/p/text()";
  //小说章节标题
  public static final String Xpath_chapter_title = "//h3[@class='j_chapterName']/text()";
  //小说章节列表的url
  public static String Xpath_chapter_url = "//ul[@class='cf']/li/a/@href";
  //匹配小说章节列表
  public static String Xpath_chapter_list = "//ul[@class='cf']/li/a/text()";
  //匹配小说结果页
  public static final String Xpath_Search = "//div[@class='book-mid-info']/h4/a/@href";
  //匹配小说结果页通过小说作者
  public static final String Xpath_Search_By_Author =
      "//div[@class='book-img-text']/ul/li[@class='res-book-item']";
  //小说书名
  public static final String Xpath_novel_name = "//div[@class='book-info']/h1/em/text()";
  //小说作者
  public static final String Xpath_novel_author = "//a[@class='writer']/text()";
  //小说最新更新时间
  public static final String Xpath_novel_last_modified =
      "//div[@class='detail']/p[@class='cf']/em/text()";
  //小说最新章节
  public static final String Xpath_novel_last_chapter =
      "//div[@class='detail']/p[@class='cf']/a/text()";
  //小说logo
  public static final String Xpath_novel_logo = "//a[@id='bookImg']/img/@src";
  //bookcover.yuewen.com/qdbimg/349573/1013726956/180
  //小说简介
  public static final String Xpath_novel_introduction = "//p[@class='intro']/text()";
  //导航列表
  public static final String Xpath_Navigate_List = "//div/ul[@class='list_type_detective']/li/a";
  //导航地址列表
  public static final String Xpath_Navigate_Url = "@href";
  //导航名称列表
  public static final String Xpath_Navigate_Name = "text()";
  //导航小说地址
  public static final String Xpath_Navigate_Novel_Path = "div[@class='book-mid-info']/h4/a/@href";
  //导航小说名称
  public static final String Xpath_Navigate_Novel_Name = "div[@class='book-mid-info']/h4/a/text()";
  //导航小说logo
  public static final String Xpath_Navigate_Novel_Logo = "div[@class='book-img-box']/a/img/@src";
  //导航小说作者
  public static final String Xpath_Navigate_Novel_Author =
      "div[@class='book-mid-info']/p[@class='author']/a[@class='name']/text()";
  //导航小说简介
  public static final String Xpath_Navigate_Novel_Intro =
      "div[@class='book-mid-info']/p[@class='intro']/text()";
  //导航小说最新章节
  public static final String Xpath_Navigate_Novel_Last_Chapter =
      "div[@class='book-mid-info']/p[@class='update']/a/text()";
  //导航小说最后更新时间
  public static final String Xpath_Navigate_Novel_Last_Modified =
      "div[@class='book-mid-info']/p[@class='update']/span/text()";
  //导航小说条目内容
  public static final String Xpath_Navigate_Novel_Item = "//div[@class='book-img-text']/ul/li";
}
