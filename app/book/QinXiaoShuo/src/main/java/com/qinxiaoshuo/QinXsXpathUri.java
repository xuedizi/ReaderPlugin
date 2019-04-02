package com.qinxiaoshuo;

/**
 * TODO
 *
 * @author xichao
 * @version v1.0.0
 * @created 2018/12/20
 */
public class QinXsXpathUri {

  //小说章节内容
  public static final String Xpath_chapter_content = "//div[@class='chaptercontent']/text()";
  //小说章节标题
  public static final String Xpath_chapter_title = "//p[@id='BookTitle']/text()";
  //小说章节列表的url
  public static String Xpath_chapter_url = "//div[@class='chapter cpt-828586']/a/@href";
  //匹配小说章节列表
  public static String Xpath_chapter_list = "//div[@class='chapter cpt-828586']/a/@alt";

  public static String Xpath_catalog_url_by_chapter_url = "//a[@class='btn btn-primary btn-block btn-outlined']/@href";

  //匹配小说结果页
  public static final String Xpath_Search = "//div[@class='box_con']/div[@id='list']/dl/dd/a/text()";
  //小说书名
  public static final String Xpath_novel_name = "//div[@class='d_title']/h1/text()";
  //小说作者
  public static final String Xpath_novel_author = "//span[@class='p_author']/a/text()";

  public static final String Xpath_novel_last_chapter = "//div[@id='info']/p[4]/a/text()";

  public static final String Xpath_novel_logo = "//div[@id='bookimg']/mip-img/@src";
  //小说简介
  public static final String Xpath_novel_introduction = "//div[@id='bookintro']/text()";
  //小说更新时间
  public static final String Xpath_novel_last_modified="//li[@id='uptime']/span/text()";
  //搜索结果标题列表
  public static final String Xpath_Novel_Search_Title_List = "//div[@class='d_title']/h1/text()";
  //搜索结果链接列表
  public static final String Xpath_Novel_Search_Url_List = "//span[@class='s2']/a/@href";

  //书名搜索结果
  public static final String Xpath_Novel_Name_Search = "//div[@class='d_title']/h1/text()";


  //匹配小说结果列表
  public static final String Xpath_Search_Item = "//div[@id='sitebox']/dl";
  //匹配小说结果小说名称
  public static final String Xpath_Search_Name = "dt/a/mip-img/@alt";
  //匹配小说结果小说地址
  public static final String Xpath_Search_Path = "dt/a/@href";
  //匹配小说结果小说logo
  public static final String Xpath_Search_Logo = "dt/a/mip-img/@src";
  //匹配小说结果小说作者
  public static final String Xpath_Search_Author = "dd[@class='book_other']/span/text()";
  //匹配小说结果小说介绍
  public static final String Xpath_Search_Intro = "dd[@class='book_des']/text()";

  //导航地址列表1
  public static final String Xpath_Navigate_Url_List = "//div[@class='nav']/ul/li/a/@href";
  //导航名称列表1
  public static final String Xpath_Navigate_Name_List = "//div[@class='nav']/ul/li/a/text()";
  //导航小说地址12
  public static final String Xpath_Navigate_Novel_Path = "dt/a/@href";
  //导航小说名称12
  public static final String Xpath_Navigate_Novel_Name = "dt/a/mip-img/@alt";
  //导航小说logo12
  public static final String Xpath_Navigate_Novel_Logo = "dt/a/mip-img/@src";
  //导航小说作者12
  public static final String Xpath_Navigate_Novel_Author = "dd[@class='book_other']/span/text()";
  //导航小说简介12
  public static final String Xpath_Navigate_Novel_Intro = "dd[@class='book_des']/text()";
  //导航小说条目内容1
  public static final String Xpath_Navigate_Novel_Item = "//div[@id='sitebox']/dl";
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
