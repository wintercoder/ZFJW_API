package com.guang.jw.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.guang.jw.bean.Course;
import com.guang.jw.bean.Score;

/**
 * 提取html页面中有用信息并转成json格式，用于返回给客户端
 * @author xiaoguang
 */
public class JsonUtils {
	private static  Gson gson ;
	static{
		gson = new Gson();
	}
	
	/**
	 * 解析课表页面，返回Json格式的核心数据，每个子项为bean/Course格式
	 * @param kebiaoHtml
	 * @return
	 */
	public static String getJsonFromKebiao(String kebiaoHtml) {
		List<Course> courses = new ArrayList<>();
		String regex = "<td align=\"center\"[> ].*?(.*?)(<br>.*?)</td>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher  = pattern.matcher(kebiaoHtml);
		while(matcher.find()){
			Course course = new Course();
			String gg[] = matcher.group(1).split("\">");
			course.setName(gg[gg.length-1]);	
			
			String content = matcher.group(2);
			String arg[] = content.split("<br>");
			if(arg.length == 5){
				course.setType(arg[1]);
				course.setTime(arg[2]);
				course.setTeacher(arg[3]);
				course.setLocation(arg[4]);
			}else{
				course.setType("");
				course.setTime(arg[1]);
				course.setTeacher(arg[2]);
				course.setLocation(arg[3]);
			}
			courses.add(course);
		}
		return gson.toJson(courses);
	}
	
	/**
	 * 解析查分页面，返回Json格式的核心数据，每个子项为bean/Socre格式
	 * @param scoreHtml
	 * @return
	 */
	public static String getJsonFromScore(String scoreHtml) {
		List<Score> scores = new ArrayList<>();
		String regex = "\\d{6}</td><td>(.+?)</td>.+?<td>(\\d\\.\\d)</td><td>(\\d+)</td>.+?</td><td>(\\d+)</td>.+?<td>(\\d+)</td>";
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher  = pattern.matcher(scoreHtml);
		while(matcher.find()){
			Score score = new Score();
            score.setCourseName(matcher.group(1));
            score.setCredit(matcher.group(2));
            score.setRegularScore(matcher.group(3));
            score.setPaperScore(matcher.group(4));
            score.setTotalScore(matcher.group(5));
            scores.add(score);
		}
		return gson.toJson(scores);
	}
}
