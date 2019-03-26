package com.reader.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * TODO
 *
 * @author xichao
 * @version v1.0.0
 * @created 2019/1/16
 */
public class DataUtils {

  /**
   * yyyy-MM-dd HH:mm:ss
   * @param dateString
   * @param format
   * @return
   */
  public static long getString2Date(String dateString, String format) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
    try {
      return dateFormat.parse(dateString).getTime();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return 0;
  }
}
