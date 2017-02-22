package com.web.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;


/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author hz
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {
	
	private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" };

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * 根据时间获取当天对应时间的时间搓
	 * @param date
	 * @return
	 */
	public static long getDateTime(Date date){
		long dateTime=0;
		String yearStr = DateUtils.getDate("yyyy-MM-dd");
		String hourStr = DateUtils.formatDate(date,"HH:mm:ss");
		String newStr = yearStr+" "+hourStr;
		Date parseDate = DateUtils.parseDate(newStr);

		Calendar cale = Calendar.getInstance();
		cale.setTime(parseDate);
	    dateTime = cale.getTimeInMillis();
		;
		return dateTime;
	}
	
	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}
	
	/**
	 * 获取某一天距离2000-10-10的天数
	 * @param date
	 * @return
	 */
	public static long getDays(Date date) {
		Date d=new Date(100, 9, 10);
		long t = d.getTime()-date.getTime();
		return t/(24*60*60*1000);
	}
	/**
	 * 获取两个时间相差的天数
	 * @param date
	 * @return
	 */	
	public static int getDifferDays(Date date1,Date date2) {		
		long t = date2.getTime()-date1.getTime();
		int num=(int) (t/(24*60*60*1000));
		return num;
	}
	
	/**
	 * 根据时间获取当天的开始时间
	 * @param date
	 * @return
	 */
	public static Date getDateStart(Date date) {
		if(date==null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date= sdf.parse(formatDate(date, "yyyy-MM-dd")+" 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 根据时间获取当天的结束时间
	 * @param date
	 * @return
	 */
	public static Date getDateEnd(Date date) {
		if(date==null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date= sdf.parse(formatDate(date, "yyyy-MM-dd") +" 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
	}
	
	/**
	 * 给定日期的获取 小时
	 */
	public static int getHours(Date date){
		Calendar cal=Calendar.getInstance();  
		cal.setTime(date);  
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		return hour;
	}
	
	/**
	 * 给定日期获取它的后一天
	 */
	
	public static Date getDayAfter(Date date) {  
        Calendar c = Calendar.getInstance();   
        c.setTime(date);  
        int day = c.get(Calendar.DATE);  
        c.set(Calendar.DATE, day + 1);  
  
        String dayAfter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")  
                .format(c.getTime());
        Date d=null;
		try {
			d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dayAfter);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
        return d;  
    } 
	
	/**
	 * 给定日期 获取 分钟
	 */
	public static int getMin(Date date){
		Calendar cal=Calendar.getInstance();  
		cal.setTime(date);  
		int hour = cal.get(Calendar.MINUTE);
		return hour;
	}
	
	/**
	 * 给定日期 获取 时间：分钟 字符串
	 * @param date
	 * @return
	 */
	public static String getHourMin(Date date){
		Calendar cal=Calendar.getInstance();  
		cal.setTime(date);  
		String ohHour = cal.get(Calendar.HOUR_OF_DAY)+"";
		String ohMinute = cal.get(Calendar.MINUTE)+"";
		if(ohMinute.length()==1){
			ohMinute = "0"+ohMinute;
		}
		if(ohHour.length()==1){
			ohHour = "0"+ohHour;
		}
		String ohTime = ohHour+":"+ohMinute;
		return ohTime;
	}
	
	/**
	 * 获取时间是上午还是下午
	 * @return
	 */
	public static String getAMOrPM(Date date){
		String apStr = "";
		Calendar cal=Calendar.getInstance();  
		cal.setTime(date);  
		int ohHour = cal.get(Calendar.HOUR_OF_DAY);
		if(ohHour<12){
			apStr = "am";
		}else if(ohHour>=12){
			apStr = "pm";
		}
		return apStr;
	}
	
	/**
	 * 根据时间搓获取时间
	 * @param time
	 * @return
	 */
	public static Date getDateByTime(Long time){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		Date date = c.getTime();
		return date;
	}
	
	/**
	 * 根据时间搓获取时间 秒数为00
	 * @param time
	 * @return
	 */
	public static Date getDateByMinLow(Long time){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		Date date = c.getTime();
		date.setSeconds(0);
		return date;
	}
	
	/**
	 * 根据时间搓获取时间 秒数为59
	 * @param time
	 * @return
	 */
	public static Date getDateByMinUp(Long time){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		Date date = c.getTime();
		date.setSeconds(59);
		return date;
	}
	
	
	/**
	 * 获取当前时间的年月日和 指定日期的 时,分，秒
	 * @param endDate
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date getResTime(Date pointDate){
		Date date = new Date();
		if(pointDate!=null){
			int hours = getHours(pointDate);
			int minutes = getMin(pointDate);
			date.setHours(hours);
			date.setMinutes(minutes);
		}
		return date;
	}
	
	/**
	 * 获取前一天的年月日 和 指定日期的 时，分，秒
	 * @param date
	 * @return
	 */
	public static Date getNextDayTime(Date pointDate) {
		Date date = new Date();
		date = getNextDay(date);
		if(pointDate!=null){
			int hours = getHours(pointDate);
			int minutes = getMin(pointDate);
			date.setHours(hours);
			date.setMinutes(minutes);
		}
		return date;  
    }
	
	/**
	 * 获取前一天日期
	 * @param date
	 * @return
	 */
	public static Date getNextDay(Date date) {  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -1);  
        date = calendar.getTime();  
        return date;  
    }
	
	
	
	/**
	 * 获取给定时间的当月最后一天
	 * @param date
	 * @return 字符串
	 */
	public static String getLastTime(Date date){
		Calendar cal = Calendar.getInstance();
		   int year=date.getYear()+1900;
		   int month=date.getMonth()+1;	
		  cal.set(Calendar.YEAR,year);
		  cal.set(Calendar.MONTH, month);
		  cal.set(Calendar.DAY_OF_MONTH, 1);		  
		  cal.add(Calendar.DAY_OF_MONTH, -1);
		  Date lastDate = cal.getTime();
		  lastDate.setHours(23);
		  lastDate.setMinutes(59);
		  lastDate.setSeconds(59);
		  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  return sdf.format(lastDate);
	}
	/**
	 * 获取给定时间的当月第一天
	 * @param date
	 * @return 字符串
	 */
	public static String getFirstTime(Date date){
		Calendar cal = Calendar.getInstance();
		 int year=date.getYear()+1900;
		 int month=date.getMonth()+1;
		  cal.set(Calendar.YEAR,year);
		  cal.set(Calendar.MONTH, month);
		  cal.set(Calendar.DAY_OF_MONTH, 1);
		  cal.add(Calendar.DAY_OF_MONTH, -1);
		  cal.set(Calendar.DAY_OF_MONTH, 1);
		  Date firstDate = cal.getTime();	
		  firstDate.setHours(0);
		  firstDate.setMinutes(0);
		  firstDate.setSeconds(0);
		  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		  
		  return sdf.format(firstDate);
	}
	
	/**
	 * 获取给定时间的当月最后一天
	 * @param date
	 * @return 时间对象
	 */
	public static Date getLastTimeDate(Date date){
		Calendar cal = Calendar.getInstance();
		   int year=date.getYear()+1900;
		   int month=date.getMonth()+1;	
		  cal.set(Calendar.YEAR,year);
		  cal.set(Calendar.MONTH, month);
		  cal.set(Calendar.DAY_OF_MONTH, 1);		  
		  cal.add(Calendar.DAY_OF_MONTH, -1);
		  Date lastDate = cal.getTime();
		  lastDate.setHours(23);
		  lastDate.setMinutes(59);
		  lastDate.setSeconds(59);		  
		  return lastDate;
	}
	/**
	 * 获取给定时间的当月第一天
	 * @param date
	 * @return 时间对象
	 */
	public static Date getFirstTimeDate(Date date){
		Calendar cal = Calendar.getInstance();
		 int year=date.getYear()+1900;
		 int month=date.getMonth()+1;
		  cal.set(Calendar.YEAR,year);
		  cal.set(Calendar.MONTH, month);
		  cal.set(Calendar.DAY_OF_MONTH, 1);
		  cal.add(Calendar.DAY_OF_MONTH, -1);
		  cal.set(Calendar.DAY_OF_MONTH, 1);
		  Date firstDate = cal.getTime();	
		  firstDate.setHours(0);
		  firstDate.setMinutes(0);
		  firstDate.setSeconds(0);		  	  
		  return firstDate;
	}
	
	/**
	 * 获取给定时间的当月天数
	 * @param date
	 * @return 时间对象
	 */
	public static int getLastTimeNum(Date date){
		Calendar cal = Calendar.getInstance();
		   int year=date.getYear()+1900;
		   int month=date.getMonth()+1;	
		  cal.set(Calendar.YEAR,year);
		  cal.set(Calendar.MONTH, month);
		  cal.set(Calendar.DAY_OF_MONTH, 1);		  
		  cal.add(Calendar.DAY_OF_MONTH, -1);
		  Date lastDate = cal.getTime();
		  int sum=lastDate.getDate();		  	  
		  return sum;
	}
	/**
	 * 判断当前时间是否在两者之间
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	public static Boolean isBetweenTime(Date startTime,Date endTime) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
   	 	String current =  formatter.format(new Date()); //当前时间
   	 	Date start;
		Date end;
		Date currentTime;
		try {
			start = formatter.parse(formatter.format(startTime));
			end = formatter.parse(formatter.format(endTime));
			currentTime = formatter.parse(current);
			return ((currentTime.before(end) && currentTime.after(start)) 
					|| currentTime.equals(start) || currentTime.equals(end));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
		
	}
}

