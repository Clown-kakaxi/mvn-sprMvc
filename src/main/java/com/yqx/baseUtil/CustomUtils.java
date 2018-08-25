package com.yqx.baseUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CustomUtils {

	// 分拆list
	public static <X> List<List<X>> splitToMultiList(List<X> list) {
		List<List<X>> multiList = new ArrayList<List<X>>();
		if (list.size() > 1000) {
			int splitCount = 0;
			if (list.size() % 1000 == 0) {
				splitCount = list.size() / 1000;
			} else {
				splitCount = list.size() / 1000 + 1;
			}
			for (int i = 0; i < splitCount; i++) {
				List<X> subList = new ArrayList<X>();
				if (i == (splitCount - 1)) {
					subList = list.subList(i * 1000, list.size());
				} else {
					subList = list.subList(i * 1000, ((i + 1) * 1000));
				}
				multiList.add(subList);
			}
		} else {
			multiList.add(list);
		}
		return multiList;
	}

	/**
	 * java 实现类似 linux 上 tail -n 倒序读取文件功能
	 * 
	 * @param count
	 *            读取的行数
	 * @param file
	 *            目标文件
	 * @param charset
	 *            字符集 默认UTF8
	 */
	public static List<String> getFileMsg(int count, File file, String charset) {
		RandomAccessFile rf = null;
		List<String> textList = new ArrayList<String>();
		try {
			rf = new RandomAccessFile(file, "r");
			long len = rf.length();
			long start = rf.getFilePointer();
			long nextend = start + len - 1;
			String line;
			rf.seek(nextend);
			int c = -1;
			while (nextend > start) {
				if (count <= 0)
					break;
				c = rf.read();
				if (c == '\n' || c == '\r') {
					line = rf.readLine();
					if (line != null) {
						textList.add(new String(line.getBytes("ISO-8859-1"),
								charset));
						count--;
					} else {
						textList.add("");
						count--;
					}
					nextend--;
				}
				nextend--;
				rf.seek(nextend);
				if (nextend == 0) {// 当文件指针退至文件开始处，输出第一行
					textList.add(new String(rf.readLine()
							.getBytes("ISO-8859-1"), charset));
					count--;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rf != null)
					rf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return textList;
	}

	/**
	 * 得到本周周一
	 * 
	 * @return Date
	 */
	public static Date getMondayOfThisWeek() {
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;// 获取当前是星期的第几天
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 1);
		return c.getTime();
	}

	/**
	 * 得到本周周日
	 * 
	 * @return Date
	 */
	public static Date getSundayOfThisWeek() {
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 7);
		return c.getTime();
	}

	/**
	 * 得到下周周五
	 * 
	 * @return Date
	 */
	public static Date getNextWeekMonday() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getSundayOfThisWeek());
		cal.add(Calendar.DATE, 5);
		return cal.getTime();
	}

	/**
	 * 获取某段时这里写代码片间内的所有日期
	 * 
	 * @param dBegin
	 * @param dEnd
	 * @return
	 */
	public static List<Date> findDates(Date dBegin, Date dEnd) {
		List<Date> lDate = new ArrayList<Date>();
		lDate.add(dBegin);
		Calendar calBegin = Calendar.getInstance();
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(calBegin.getTime());
		}
		return lDate;
	}
	
	/**
	 * date转星期几
	 * @param datetime
	 * @return
	 */
	public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
}
