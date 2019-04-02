package com.biqu.xin;

/**
 * TODO
 *
 * @author xichao
 * @version v1.0.0
 * @created 2018/12/20
 */
public class Bq_XinXpathUri {

  //小说章节内容
  public static final String Xpath_chapter_content = "//div[@id='content']/text()";
  //小说章节标题
  public static final String Xpath_chapter_title = "//div[@class='bookname']/h1/text()";
  //小说章节列表的url
  public static String Xpath_chapter_url = "//div[@id='list']/dl/dd/a/@href";
  //匹配小说章节列表
  public static String Xpath_chapter_list = "//div[@id='list']/dl/dd/a/text()";
  //匹配小说结果页
  public static final String Xpath_Search = "//div[@class='box_con']/div[@id='list']/dl/dd/a/text()";
  //匹配小说结果列表
  public static final String Xpath_Search_Item = "//div[@class='result-item result-game-item']";
  //匹配小说结果地址
  public static final String Xpath_Search_Path = "div[@class='result-game-item-detail']/h3/a[@class='result-game-item-title-link']/@href";
  //匹配小说结果地址
  public static final String Xpath_Search_Name = "div[@class='result-game-item-detail']/h3/a[@class='result-game-item-title-link']/span/text()";
  //匹配小说结果地址
  public static final String Xpath_Search_Author = "div[@class='result-game-item-detail']/div[@class='result-game-item-info']/p[1]/span[2]/text()";
  //匹配小说结果地址
  public static final String Xpath_Search_logo = "div[@class='result-game-item-pic']/a/img/@src";
  //匹配小说结果地址
  public static final String Xpath_Search_Last_Chapter = "div[@class='result-game-item-detail']/div[@class='result-game-item-info']/p/a/text()";
  //匹配小说结果地址
  public static final String Xpath_Search_Last_Modified = "div[@class='result-game-item-detail']/div[@class='result-game-item-info']/p[3]/span[2]/text()";
  //匹配小说结果地址
  public static final String Xpath_Search_Intro = "div[@class='result-game-item-detail']/p[@class='result-game-item-desc']/text()";
  //小说书名
  public static final String Xpath_novel_name = "//div[@id='info']/h1/text()";
  //小说作者
  public static final String Xpath_novel_author = "//div[@id='info']/p/text()";

  public static final String Xpath_novel_last_chapter = "//div[@id='info']/p[4]/a/text()";

  public static final String Xpath_novel_logo = "//div[@id='fmimg']/img/@src";
  //小说简介
  public static final String Xpath_novel_introduction = "//div[@id='intro']/p/text()";
  //小说更新时间
  public static final String Xpath_novel_last_modified="//div[@id='info']/p[3]/text()";
  //搜索结果标题列表
  public static final String Xpath_Novel_Search_Title_List = "//a[@cpos='title']/span/text()";
  //搜索结果链接列表
  public static final String Xpath_Novel_Search_Url_List = "//a[@cpos='title']/@href";
  //导航地址列表
  public static final String Xpath_Navigate_Url_List = "//div[@class='nav']/ul/li/a/@href";
  //导航名称列表
  public static final String Xpath_Navigate_Name_List = "//div[@class='nav']/ul/li/a/text()";
  //导航小说地址
  public static final String Xpath_Navigate_Novel_Path = "dl/dt/a/@href";
  //导航小说名称
  public static final String Xpath_Navigate_Novel_Name = "dl/dt/a/text()";
  //导航小说logo
  public static final String Xpath_Navigate_Novel_Logo = "div[@class='image']/a/img/@src";
  //导航小说作者
  public static final String Xpath_Navigate_Novel_Author = "dl/dt/span/text()";
  //导航小说简介
  public static final String Xpath_Navigate_Novel_Intro = "dl//dd/text()";
  //导航小说条目内容
  public static final String Xpath_Navigate_Novel_Item = "//div[@id='hotcontent']/div/div[@class='item']";
  //最近更新的小说列表
  public static final String Xpath_Navigate_Laster_Update_Novel_Item = "//div[@id='newscontent']/div[@class='l']/ul/li";
  //最近更新小说地址
  public static final String Xpath_Navigate_Laster_Update_Novel_Path = "span[@class='s2']/a/@href";
  //最近更新小说名称
  public static final String Xpath_Navigate_Laster_Update_Novel_Name = "span[@class='s2']/a/text()";
  //最近更新小说作者
  public static final String Xpath_Navigate_Laster_Update_Novel_Author = "span[@class='s5']/text()";
  //最近更新小说最新章节
  public static final String Xpath_Navigate_Laster_Update_Novel_Laster_Chapter = "span[@class='s3']/a/text()";
}
