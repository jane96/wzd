package com.web.common;

import java.io.File;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

/**
 * MD5加密类
 * @author hz
 */
public class Md5Util
{
  public static final String COLON = ":";
  public static final String COMMA = ",";
  public static final String EMPTY = "";
  public static final String UNDER_LINE = "_";
  public static final String ENDL = "\n";
  public static final String SLASH = "/";
  public static final String BLANK = " ";
  public static final String DOT = ".";
  public static final String FILE_SEPARATOR = File.separator;
  private static Random getR = new Random();  
  /**
   * 
   * @param str 加密的字符串
   * @return
   */
  public static String encodeRule(String str)
  {
	  if(str == null || str.equals("")){
		  return "";
	  }
		StringBuffer strB = new StringBuffer();
		//把第一位字符提到最后以为
		strB.append(str.substring(1,str.length()));
		strB.append(str.substring(0,1));
		//倒序
		strB.reverse();
		return strB.toString();
  }
  /**
   * 
   * @param paramString
   * @return
   */
  public static String md5Encoder(String paramString)
  {
    String str = null;
    try
    {
      str = encodePassword(paramString, "MD5");
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      localNoSuchAlgorithmException.printStackTrace();
    }
    return str;
  }
  /**
   * 
   * @param value
   * @return
   * @throws NoSuchAlgorithmException
   */
  public static String encoder(String value) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.reset();
		messageDigest.update(value.getBytes());
		byte[] bytes = messageDigest.digest();
		StringBuffer localStringBuffer = new StringBuffer();
		for (int i = 0; i < bytes.length; ++i) {
			if ((bytes[i] & 0xFF) < 16)
				localStringBuffer.append("0");
			localStringBuffer.append(Long.toString(bytes[i] & 0xFF, 16));
		}
		return localStringBuffer.toString();
	}

  /**
   * 
   * @param paramList
   * @return
   */
  public static String changeList2String(List<String> paramList)
  {
    String str = paramList.toString();
    return str.substring(1, str.length() - 1);
  }
  /**
   * 
   * @param paramString
   * @return
   */
  public static boolean isBlank(String paramString)
  {
    int i = 0;
    if ((paramString == null) || ("".equals(paramString.trim())))
      i = 1;
    if(i==1)
    {
    	return true;
    }else return false;
  }
  /**
   * 
   * @param paramString
   * @return
   */
  public static boolean isNotBlank(String paramString)
  {
    int i = 0;
    if ((paramString != null) && (!("".equals(paramString))))
      i = 1;
    if(i==1)
    {
    	return true;
    }else return false;
  }
  /**
   * 
   * @param paramString1
   * @param paramString2
   * @return
   * @throws NoSuchAlgorithmException
   */
  public static String encodePassword(String paramString1, String paramString2)
    throws NoSuchAlgorithmException
  {
    byte[] arrayOfByte1 = paramString1.getBytes();
    MessageDigest localMessageDigest = null;
    localMessageDigest = MessageDigest.getInstance(paramString2);
    localMessageDigest.reset();
    localMessageDigest.update(arrayOfByte1);
    byte[] arrayOfByte2 = localMessageDigest.digest();
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < arrayOfByte2.length; ++i)
    {
      if ((arrayOfByte2[i] & 0xFF) < 16)
        localStringBuffer.append("0");
      localStringBuffer.append(Long.toString(arrayOfByte2[i] & 0xFF, 16));
    }
    return localStringBuffer.toString();
  }
  /**
   * 
   * @param paramString
   * @return
   */
  public static boolean isEmpty(String paramString)
  {
    if (paramString == null)
      return true;
    return ("".equals(paramString));
  }
  /**
   * 
   * @param paramInt
   * @return
   */
  public static String getRandomString(int paramInt)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Random localRandom = new Random();
    for (int i = 0; i < paramInt; ++i)
    {
      int j;
      do
        if ((j = localRandom.nextInt(90)) > 64)
          break;
      while ((j = localRandom.nextInt(122)) <= 97);
      localStringBuffer.append((char)j);
    }
    return localStringBuffer.toString();
  }
  /**
   * 
   * @param paramArrayOfString
   * @param paramString
   * @return
   */
  public static boolean contains(String[] paramArrayOfString, String paramString)
  {
    if (paramArrayOfString != null)
      for (int i = 0; i < paramArrayOfString.length; ++i)
        if (paramString.equals(paramArrayOfString[i]))
          return true;
    return false;
  }
  /**
   * 
   * @param paramString
   * @param paramInt
   * @return
   */
  public static String ljustZero(String paramString, int paramInt)
  {
    String str = "";
    for (int i = 0; i < paramInt - paramString.length(); ++i)
      str = str + "0";
    str = str + paramString;
    return str;
  }
  /**
   * 
   * @param paramString
   * @return
   */
  public static int getWordLength(String paramString)
  {
    int i = 0;
    if (isBlank(paramString))
      return i;
    char[] arrayOfChar = paramString.toCharArray();
    for (int j = 0; j < arrayOfChar.length; ++j)
      if (isChinese(arrayOfChar[j]))
        i += 2;
      else
        i += 1;
    return i;
  }
  /**
   * 
   * @param paramString
   * @param paramInt
   * @return
   */
  public static String getWord(String paramString, int paramInt)
  {
    char[] arrayOfChar = paramString.toCharArray();
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 0;
    for (int j = 0; j < arrayOfChar.length; ++j)
    {
      if (isChinese(arrayOfChar[j]))
        i += 2;
      else
        i += 1;
      if (i > paramInt)
        break;
      localStringBuffer.append(arrayOfChar[j]);
    }
    return localStringBuffer.toString();
  }
  /**
   * 
   * @param paramString
   * @return
   */
  public static boolean hasChinese(String paramString)
  {
    if (paramString == null)
      return true;
    char[] arrayOfChar = paramString.toCharArray();
    for (int i = 0; i < arrayOfChar.length; ++i)
      if (isChinese(arrayOfChar[i]))
        return true;
    return false;
  }
  /**
   * 
   * @param paramChar
   * @return
   */
  public static boolean isChinese(char paramChar)
  {
    Character.UnicodeBlock localUnicodeBlock = Character.UnicodeBlock.of(paramChar);
    return ((localUnicodeBlock != Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) && (localUnicodeBlock != Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS) && (localUnicodeBlock != Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) && (localUnicodeBlock != Character.UnicodeBlock.GENERAL_PUNCTUATION) && (localUnicodeBlock != Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION) && (localUnicodeBlock != Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS));
  }
  /**
   * 
   * @param paramString
   * @return
   */
  public static String[] splitIntoArr(String paramString)
  {
    if (isEmpty(paramString))
      return new String[0];
    return paramString.split(",");
  }
  /**
   * 
   * @param paramArrayOfString
   * @return
   */
  public static String join(String[] paramArrayOfString)
  {
    return join(paramArrayOfString, ",", "'");
  }
  /**
   * 
   * @param paramArrayOfString
   * @param paramString
   * @return
   */
  public static String join(String[] paramArrayOfString, String paramString)
  {
    return join(paramArrayOfString, ",", "");
  }
  /**
   * 
   * @param paramArrayOfString
   * @param paramString1
   * @param paramString2
   * @return
   */
  public static String join(String[] paramArrayOfString, String paramString1, String paramString2)
  {
    String str = "";
    for (int i = 0; i < paramArrayOfString.length; ++i)
    {
      str = str + paramString2 + paramArrayOfString[i] + paramString2;
      if (i == paramArrayOfString.length - 1)
        continue;
      str = str + paramString1;
    }
    return str;
  }
  /**
   * 
   * @param paramString1
   * @param paramString2
   * @param paramString3
   * @return
   */
  public static String convertCode(String paramString1, String paramString2, String paramString3)
  {
    try
    {
      if (paramString1 == null)
        return null;
      if (paramString1.equals(""))
        return "";
      return new String(paramString1.getBytes(paramString2), paramString3);
    }
    catch (Exception localException)
    {
    	return localException.toString();
    }
    
  }
  /**
   * 
   * @param paramString
   * @return
   */
  public static boolean isUpperCase(String paramString)
  {
    int i = 1;
    for (int k = 0; k < paramString.length(); ++k)
    {
      int j = paramString.charAt(k);
      if ((j >= 65) && (j <= 90))
        continue;
      i = 0;
    }
    if(i==1)
    {
    	return true;
    }else return false;
  }
  /**
   * 
   * @param paramString
   * @return
   */
  public static boolean isLowerCase(String paramString)
  {
    int i = 1;
    for (int k = 0; k < paramString.length(); ++k)
    {
      int j = paramString.charAt(k);
      if ((j >= 97) && (j <= 122))
        continue;
      i = 0;
    }
    if(i==1)
    {
    	return true;
    }else return false;
  }
  public static String toTrimString(String str){
	  if(str != null){
		  return str.trim();
	  }else{
		  return "";
	  }
  } 
  /**
   * 随机数字的字符串  
   * @return
   */
  private static String getSz() {  
      int getI = getR.nextInt(10) + 48;// 数字48--57=0---9  
      String sI = String.valueOf((char) getI);  
      return sI;  
  }  
  /**
   * 随机数字的字母，区分大小写  
   * @return
   */
  private static String getZm() {  
          char sSS = (char) (getR.nextInt(26) + 97);// 小写字母97--122=a---z  
          char sBs = (char) (getR.nextInt(26) + 65);// 大写65--90=A----Z  
          char[] stemp = { sSS, sBs };  
          int i = getR.nextInt(2);  
          String sS = String.valueOf(stemp[i]);  
          return sS;  
   }  
  /**
   * 
   * @return
   */
  public static String getSz6(){
	  String s = "";
	  
	  for (int i = 0; i < 6; i++) {
		  s += getSz(); 
	  }
	  return s;
  }

  /**
   * 
   * @return
   */
  public static String getRandomS() {  
      String s = "";  
      int n = 0;  
      int m = 0;  
      for (int i = 0; i < 8; i++) {  
          if (n == 4) {  
              s += getSz();  
              m++;  
          } else if (m == 4) {  
              s += getZm();  
              n++;  
          } else {  
              int ri = getR.nextInt(2);  
              int temp = ri == 0 ? m++ : n++;  
              s += ri == 0 ? getSz() : getZm();  
         }  
      }  
     return s;  
  }
  /**
   * 将字符串首字母转为小写
   * @param str
   * @return
   */
public static String initialSmall(String str){
	  String str2 = str;
	  str2 = str.substring(0,1).toLowerCase()+str.substring(1);
	  return str2;
  }
public static void  main(String[] agts) {
	System.out.println(Md5Util.md5Encoder("admin"));

}


}
