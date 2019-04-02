package com.fanqie;

/**
 * TODO
 *
 * @author xichao
 * @version v1.0.0
 * @created 2019/1/15
 */
public class FqXpathUri {

  //章节内容
  public static final String Xpath_Chapter_Content = "//div[@id='content']/text()";
  //章节标题
  public static final String Xpath_Chapter_Title= "//div[@class='bookname']/h1/text()";

  //标题列表
  public static final String Xpath_Chapter_Title_List = "//div[@id='list']/dl/dd/a/text()";
  //目录列表
  public static final String Xpath_Chapter_Url_List = "//div[@id='list']/dl/dd/a/@href";
  //搜索结果列表
  public static final String Xpath_Search_item = "//div[@id='main']/div[@class='novelslist2']/ul/li";
  //搜索结果小说名称
  public static final String Xpath_Search_Novel_Name = "span[@class='s2']/a/text()";
  //搜索结果小说名称
  public static final String Xpath_Search_Novel_Path = "span[@class='s2']/a/@href";
  //搜索结果小说最新章节
  public static final String Xpath_Search_Novel_Last_Chapter = "span[@class='s3']/a/text()";
  //搜索结果小说名称
  public static final String Xpath_Search_Novel_Last_Modified = "span[5]/text()";
  //搜索结果小说名称
  public static final String Xpath_Search_Novel_Author = "span[@class='s4']/b/text()";
  //小说书名
  public static final String Xpath_novel_name = "//div[@id='info']/h1/text()";
  //小说作者
  public static final String Xpath_novel_author = "//div[@id='info']/p[1]/text()";
  //最新章节
  public static final String Xpath_Novel_last_Chapter = "//div[@id='info']/p[4]/a/text()";
  //小说logo
  public static final String Xpath_Novel_Logo = "//div[@id='fmimg']/img/@src";
  //最后更新时间
  public static final String Xpath_Novel_Last_Modified = "//div[@id='info']/p[3]/text()";
  //简介
  public static final String Xpath_Novel_Intro = "//div[@id='intro']/p[1]/text()";
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
