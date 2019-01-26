package com.pantanal.data.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author gudong
 *
 */
public class StringUtil {
  /**
   * 字符串 转成boolean 数组
   * 
   * @param s
   *          like:true,1,false,0,true
   * @param split
   *          like:,
   * @return [true,true,false,false,true]
   */
  public static boolean[] split2Boolean(String s, String split) {
    String[] strArray = StringUtils.split(s, split);
    boolean[] result = new boolean[strArray.length];
    for (int i = 0; i < strArray.length; i++) {
      result[i] = BooleanUtils.toBoolean(strArray[i]);
    }
    return result;
  }

  /**
   * Upper substring assigned length.
   * 
   * @param s
   * @param start
   * @param end
   * @return
   */
  public static String toUpperCase(String s, int start, int end) {
    int length = s.length();
    if (length >= end) {
      s = s.substring(0, start) + s.substring(start, end).toUpperCase() + s.substring(end, length);
    }
    return s;
  }

  /**
   * Lower substring assigned length.
   * 
   * @param s
   * @param start
   * @param end
   * @return
   */
  public static String toLowerCase(String s, int start, int end) {
    int length = s.length();
    if (length >= end) {
      s = s.substring(0, start) + s.substring(start, end).toLowerCase() + s.substring(end, length);
    }
    return s;
  }

  /**
   * Lower the first char.
   * 
   * @param s
   * @return
   */
  public static String toLowerCaseFirst(String s) {
    return toLowerCase(StringUtils.defaultString(s), 0, 1);
  }

  /**
   * Upper the first char.
   * 
   * @param s
   * @return
   */
  public static String toUpperCaseFirst(String s) {
    return toUpperCase(StringUtils.defaultString(s), 0, 1);
  }

  /**
   * 转化成数据库表名，如：LotteryOrder转成LOTTERY_ORDER
   * 
   * @param name
   * @return
   */
  public static String toDBTableName(String name) {
    return toDBColumnName(name);
  }

  /**
   * 转化成数据库列表，如：isCurrentPeriod转成IS_CURRENT_PERIOD
   * 
   * @param fieldName
   * @return
   */
  public static String toDBColumnName(String fieldName) {
    if (StringUtils.isBlank(fieldName))
      return "";
    StringBuffer columnNameBuffer = new StringBuffer();
    char[] charArray = StringUtils.defaultString(fieldName).toCharArray();
    columnNameBuffer.append(Character.toUpperCase(charArray[0]));
    for (int i = 1; i < charArray.length; i++) {
      if (Character.isUpperCase(charArray[i])) {
        columnNameBuffer.append("_");
      }
      columnNameBuffer.append(Character.toUpperCase(charArray[i]));
    }
    return columnNameBuffer.toString();
  }

  /**
   * 
   * @param o
   * @return
   */
  public static String toString(Object o) {
    if (o == null) {
      return "";
    } else {
      return o.toString();
    }
  }

  /**
   * json字符串的格式化，友好格式
   * 
   * @param json
   *          json串
   * @param fillStringUnit
   *          填充字符，比如四个空格
   * @return
   */
  public static String formatJson(String json, String fillStringUnit) {
    if (json == null || json.trim().length() == 0) {
      return null;
    }

    int fixedLenth = 0;
    ArrayList<String> tokenList = new ArrayList<String>();
    {
      String jsonTemp = json;
      // 预读取
      while (jsonTemp.length() > 0) {
        String token = getToken(jsonTemp);
        jsonTemp = jsonTemp.substring(token.length());
        token = token.trim();
        tokenList.add(token);
      }
    }

    for (int i = 0; i < tokenList.size(); i++) {
      String token = tokenList.get(i);
      int length = token.getBytes().length;
      if (length > fixedLenth && i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":")) {
        fixedLenth = length;
      }
    }

    StringBuilder buf = new StringBuilder();
    int count = 0;
    for (int i = 0; i < tokenList.size(); i++) {

      String token = tokenList.get(i);

      if (token.equals(",")) {
        buf.append(token);
        doFill(buf, count, fillStringUnit);
        continue;
      }
      if (token.equals(":")) {
        buf.append(" ").append(token).append(" ");
        continue;
      }
      if (token.equals("{")) {
        String nextToken = tokenList.get(i + 1);
        if (nextToken.equals("}")) {
          i++;
          buf.append("{ }");
        } else {
          count++;
          buf.append(token);
          doFill(buf, count, fillStringUnit);
        }
        continue;
      }
      if (token.equals("}")) {
        count--;
        doFill(buf, count, fillStringUnit);
        buf.append(token);
        continue;
      }
      if (token.equals("[")) {
        String nextToken = tokenList.get(i + 1);
        if (nextToken.equals("]")) {
          i++;
          buf.append("[ ]");
        } else {
          count++;
          buf.append(token);
          doFill(buf, count, fillStringUnit);
        }
        continue;
      }
      if (token.equals("]")) {
        count--;
        doFill(buf, count, fillStringUnit);
        buf.append(token);
        continue;
      }

      buf.append(token);
      // 左对齐
      if (i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":")) {
        int fillLength = fixedLenth - token.getBytes().length;
        if (fillLength > 0) {
          for (int j = 0; j < fillLength; j++) {
            buf.append(" ");
          }
        }
      }
    }
    return buf.toString();
  }

  private static String getToken(String json) {
    StringBuilder buf = new StringBuilder();
    boolean isInYinHao = false;
    while (json.length() > 0) {
      String token = json.substring(0, 1);
      json = json.substring(1);

      if (!isInYinHao
          && (token.equals(":") || token.equals("{") || token.equals("}") || token.equals("[") || token.equals("]") || token.equals(","))) {
        if (buf.toString().trim().length() == 0) {
          buf.append(token);
        }

        break;
      }

      if (token.equals("\\")) {
        buf.append(token);
        buf.append(json.substring(0, 1));
        json = json.substring(1);
        continue;
      }
      if (token.equals("\"")) {
        buf.append(token);
        if (isInYinHao) {
          break;
        } else {
          isInYinHao = true;
          continue;
        }
      }
      buf.append(token);
    }
    return buf.toString();
  }

  private static void doFill(StringBuilder buf, int count, String fillStringUnit) {
    buf.append("\n");
    for (int i = 0; i < count; i++) {
      buf.append(fillStringUnit);
    }
  }

  /**
   * 
   * @param params
   * @return
   */
  public static String generateRowKey(Object... params) {
    if (params != null) {
      String s = "";
      for (int i = 0; i < params.length; i++) {
        s += params[i] == null ? "" : DigestUtils.md5Hex(params[i].toString());
        if (i < params.length - 1) {
          s += "_";
        }
      }
      return s;
    } else {
      return "";
    }
  }

  /**
   * 解析字符串获得双精度型数值，
   * 
   * @param str
   * @return
   */
  public static Double getDouble(String str) {
    Double d = null;
    if (StringUtils.isBlank(str)) {
      return d;
    }
    try {
      d = Double.parseDouble(str);
    } catch (Exception e) {
      if (str.length() > 1) {
        Pattern p = Pattern.compile("(\\-|\\+)?\\d+(\\.)?\\d*");
        Matcher m = p.matcher(str);
        if (m.find()) {
          try {
            d = Double.parseDouble(str.substring(m.start(), m.end()));
          } catch (Exception e1) {
          }
        }
      }
    }
    return d;
  }

  /**
   * 解析字符串获得双精度型数值，
   * 
   * @param str
   * @return
   */
  public static Double[] getDoubleArray(String str) {
    if (StringUtils.isBlank(str)) {
      return new Double[0];
    }
    if (str.length() == 1) {
      try {
        return new Double[] { Double.parseDouble(str) };
      } catch (Exception e) {
        return new Double[0];
      }
    } else {
      Pattern p = Pattern.compile("(\\-|\\+)?\\d+(\\.)?\\d*");
      Matcher m = p.matcher(str);
      List<Double> list = new ArrayList<Double>();
      while (m.find()) {
        try {
          list.add(Double.parseDouble(str.substring(m.start(), m.end())));
        } catch (Exception e1) {
        }
      }
      return list.toArray(new Double[0]);
    }
  }

  /**
   * 解析字符串获得双精度型数值，
   * 
   * @param str
   * @return
   */
  public static Integer getInt(String str) {
    Double d = getDouble(str);
    return d == null ? null : d.intValue();
  }

  /**
   * 
   * @param s
   * @return
   */
  public static String md5(String s) {
    return DigestUtils.md5Hex(s);
  }

  public static void main(String[] args) {
    String str = "{\"content\":\"'this,is the msg content.'\",\"tousers\":\"user1|user2\",\"msgtype\":\"texturl\",\"appkey\":\"test\",\"domain\":\"test\","
        + "\"system\":{\"wechat\":[{\"safe\":\"1\"}]},\"texturl\":{\"urltype\":\"0\",\"user1\":{\"spStatus\":\"user01\",\"workid\":\"work01\"},\"user2\":{\"spStatus\":\"user02\",\"workid\":\"work02\"}}}";
    System.out.println(formatJson(str, "  "));

    System.out.println(DigestUtils.md5Hex("1"));

    String ss = "dfafds-3.53453sdfs";
    Pattern p = Pattern.compile("(\\-|\\+)?\\d+(\\.)?\\d*");
    Matcher m = p.matcher(ss);
    if (m.find()) {
      System.out.println(ss.substring(m.start(), m.end()));
      System.out.println(Double.valueOf(ss.substring(m.start(), m.end())).intValue());
    }

    ss = "dfafds-3.53453sdfs456asd23";
    Double[] ds = getDoubleArray(ss);
    for (Double d : ds) {
      System.out.println(d);
    }
    
    System.out.println(md5("北京-小区"));
  }
}
