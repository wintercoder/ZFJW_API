package com.guang.jw.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 从教务处网站 获取信息的工具类，返回html代码
 * 查成绩和课表需要先获取页面的ViewState，一切需先登录
 */
public class JwUtils {
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36";
	private static final String Base_Referer = "http://jwc.gdufe.edu.cn/";
	private static final String Jw_Host = "http://jwxt2.gdufe.edu.cn:8080";
	
	//下面这些URL要根据Base_URL刷新
	private static String Base_URL = "http://jwxt2.gdufe.edu.cn:8080/(S(100000000000000000000002))";
	private static String Login_URL = Base_URL + "/default2.aspx";
	private static String LOGIN_URL_NO_Verify = Base_URL + "/default_ysdx.aspx";
	private static String Referer = Base_URL + "/xs_main.aspx?xh=";// + sno;	 //登陆后的reference
	private static String Score_URL = Base_URL + "/xscjcx_dq.aspx?xh=";
	private static String KeBiao_URL = Base_URL + "/xskbcx.aspx?xh=";
	private static String KeChengJieShao_URL = Base_URL + "/tjkbcx.aspx?xh=";

	private static String VIEW_STATE = "";
	private static String EVENT_STATE = "";

	private static HttpClient httpclient;
	private static String encoding = "gb2312";
	
	static {
		httpclient = HttpClientBuilder.create().build();
	}
	
	/**
	 * 登陆教务系统，成功返回true，失败返回false
	 */
	public static boolean LoginToSystem(String sno, String password) {
		if(!getAndSetBaseUrl(Jw_Host)){//教务系统崩溃
			return false;
		}else{
			refreshUrlByBaseUrl();
			getAndSetViewState(Login_URL,Base_Referer);
			if(LoginToSystem(Login_URL, sno, password) ){
				return true;
			}else{
				return false;
			}
		}
	}
	public static String getCurSemesterScore(String sno) {
		return getCurSemesterInfo(Score_URL,sno);
	}
	public static String getCurSemesterKeBiao(String sno) {
		return getCurSemesterInfo(KeBiao_URL,sno);
	}
	public static String getCurSemesterJieShao(String sno) {
		return getCurSemesterInfo(KeChengJieShao_URL,sno);
	}
	/**
	 * 获取指定学期课表
	 * @param sno	学号
	 * @param xuenian 学年 example:2015-2016
	 * @param xueqi 学期 example:2
	 * @return
	 */
	public static String getKebiao(String sno, String xuenian, String xueqi) {
		getAndSetViewState(KeBiao_URL + sno ,Referer + sno);
		return getKebiao(KeBiao_URL, sno, xuenian, xueqi);
	}
	/**
	 * 查分，提供学号和学年、学期
	 * @param sno 学号
	 * @param xuenian 学年，2015-2016格式 或者 全部
	 * @param xueqi 学期，1 或 2 或者 全部
	 * @return html，<td>([^<>]+) 可以过滤无关html信息，再进一步过滤科目分数等。 <br/>
	 * \d{6}[\r\n]<td>(.*)[\r\n]<td>.*[\r\n]<td>.*[\r\n]<td>(\d\.\d)过滤科目名、学分，具体分数的没写 
	 */
	public static String getScore(String sno, String xuenian, String xueqi) {
		getAndSetViewState(Score_URL + sno ,Referer + sno);
		return getScore(Score_URL, sno, xuenian, xueqi);
	}

	
	
	/**
	 * 当BaseUrl改变后刷新相关的Url，但不附加学号
	 */
	private static void refreshUrlByBaseUrl() {
		Login_URL = Base_URL + "/default2.aspx";
		LOGIN_URL_NO_Verify = Base_URL + "/default_ysdx.aspx";
		Referer = Base_URL + "/xs_main.aspx?xh=";// + sno;	 //登陆后的reference
		Score_URL = Base_URL + "/xscjcx_dq.aspx?xh=";
		KeBiao_URL = Base_URL + "/xskbcx.aspx?xh=";
		KeChengJieShao_URL = Base_URL + "/tjkbcx.aspx?xh=";
	}
	/**
	 * 获取隐藏的__VIEWSTATE和__EVENTVALIDATION属性，用于获取当前页面的信息。<br/>
	 * 在 登陆 查课表 查分 前需要调用获取对应的默认视图__VIEWSTATE
	 */
	private static void getAndSetViewState(String url,String referer) {
		HttpGet httpGet = new HttpGet(url);
		try {
			httpGet.setHeader("User-Agent", USER_AGENT);
			httpGet.setHeader("Referer", referer);
			HttpResponse httpResponse = httpclient.execute(httpGet);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent(), encoding));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			String pattern1 = "__VIEWSTATE\" value=\"(.*?)\" />";
			String pattern2 = "__EVENTVALIDATION\" value=\"(.*?)\" />";
			Pattern r1 = Pattern.compile(pattern1);
			Pattern r2 = Pattern.compile(pattern2);
			Matcher m1 = r1.matcher(result);
			Matcher m2 = r2.matcher(result);
			if (m1.find()) {
				VIEW_STATE = m1.group(1);
			}
			if (m2.find()) {
				EVENT_STATE = m2.group(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 访问一次Jw_Host，根据返回信息获取当前正确sessionId，写回BaseUrl <br/> 
	 * 注意不能是访问带有具体sessionId的链接，否则第一次获取成功，之后一段时间就返回200登陆了 <br/> 
	 * 貌似是因为如果带有sessionId的话即使访问其他sessionId也被认为这个客户端ip登陆成功，新旧sessionId都有效 <br/> 
	 * @param url Jw_Host，直接填地址+端口，不能带sessionId
	 * @param sno
	 * @param password
	 */
	private static boolean getAndSetBaseUrl(String url) {
		CloseableHttpClient client = HttpClientBuilder.create().build();  //只用一次，选择可close的
		HttpGet get = new HttpGet(url);
	    RequestConfig requestConfig = RequestConfig.custom().setRedirectsEnabled(false).build();
	    get.setConfig(requestConfig);	//禁止自动跳转，方便获取header的跳转信息
	    HttpResponse response;
		try {
			response = client.execute(get);
			Header header = response.getFirstHeader("Location");//header => [Location: /(S(mabs01f0sjr3jo45gy4atk55))/default2.aspx] 
			int statuCode = response.getStatusLine().getStatusCode();
			if (statuCode == 302) {	//跳转情况，到我们要的url
				String sessionId = header.getValue().replace("/default2.aspx", "");	//sessionId => [/(S(mgg3q5fvspor3o2xlady0g45))]
			    Base_URL =  Jw_Host + sessionId;	//设置正确的Base_URL
			    client.close();
			    return true;
			}else{	//教务系统崩溃
				System.out.println("header="+header +" statu="+statuCode);
				client.close();
				return false;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 实际的登陆教务系统代码，主要是获取Cookie，之后httpClient自动管理Cookie，需在getViewState()后调用 <br/> 
	 */
	private static boolean LoginToSystem(String url, String sno, String password) {
		HttpPost httpost = new HttpPost(url);
		httpost.setHeader("Referer", Base_Referer);
		httpost.setHeader("User-Agent", USER_AGENT);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("__VIEWSTATE", VIEW_STATE));
		nvps.add(new BasicNameValuePair("__EVENTVALIDATION", EVENT_STATE));
		nvps.add(new BasicNameValuePair("txtUserName", sno));
		nvps.add(new BasicNameValuePair("TextBox2", password));
		nvps.add(new BasicNameValuePair("txtSecretCode", ""));
		nvps.add(new BasicNameValuePair("RadioButtonList1", "%D1%A7%C9%FA"));
		nvps.add(new BasicNameValuePair("Button1", ""));
		nvps.add(new BasicNameValuePair("lbLanguage", ""));
		httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

		HttpResponse response;
		try {
			response = httpclient.execute(httpost);
//			String result = EntityUtils.toString(response.getEntity());
			/* 登陆成功后会返回302跳转，登陆失败则是200返回错误信息 */
			int statuCode = response.getStatusLine().getStatusCode();
			if (statuCode == 302) {
				return true;
			} else {
				return false;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 无验证码的登陆链接，也可以用
	 * @param url LOGIN_URL_NO_Verify
	 * @param sno
	 * @param password
	 * @return
	 */
	private boolean LoginToSystemNoVerify(String url, String sno, String password) {
		HttpPost httpost = new HttpPost(url);
		httpost.setHeader("Referer", Base_Referer);
		httpost.setHeader("User-Agent", USER_AGENT);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("__VIEWSTATE", VIEW_STATE));
		nvps.add(new BasicNameValuePair("__EVENTVALIDATION", EVENT_STATE));
		nvps.add(new BasicNameValuePair("TextBox1", sno));
		nvps.add(new BasicNameValuePair("TextBox2", password));
		nvps.add(new BasicNameValuePair("RadioButtonList1", "%D1%A7%C9%FA"));
		nvps.add(new BasicNameValuePair("Button1", ""));
		httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

		HttpResponse response;
		try {
			response = httpclient.execute(httpost);
			int statuCode = response.getStatusLine().getStatusCode();
			if (statuCode == 302) {
				return true;
			} else {
				return false;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 直接GET方式访问带学号的链接 <br/> 
	 * 可获取当前学期课表、当前学期成绩、课程介绍 <br/> 
	 * 传入KeBiao_URL 获取当前学期课表 <br/> 
	 * 传入Score_UR 获取当前学期成绩 <br/> 
	 * 传入KeChengJieShao_URL 获取课程介绍 <br/> 
	 */
	private static String getCurSemesterInfo(String url, String sno) {
		HttpGet httpGet = new HttpGet(url + sno);
		httpGet.setHeader("User-Agent", USER_AGENT);
		httpGet.setHeader("Referer", Referer + sno);
		HttpResponse httpResponse;
		String result = "";
		try {
			httpResponse = httpclient.execute(httpGet);
			result = EntityUtils.toString(httpResponse.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	private static String getKebiao(String url,String sno, String xuenian, String xueqi) {
		HttpPost httpost = new HttpPost(url+sno);
		httpost.setHeader("User-Agent", USER_AGENT);
		httpost.setHeader("Referer", url+sno);	//当前学生查询课表的get链接，[&xm=%u97e9%u88d5%u5149&gnmkdm=N121603]这部分不需要
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		nvps.add(new BasicNameValuePair("__EVENTTARGET", "xqd"));
		nvps.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		nvps.add(new BasicNameValuePair("__LASTFOCUS", ""));
		nvps.add(new BasicNameValuePair("__VIEWSTATE", VIEW_STATE));
		nvps.add(new BasicNameValuePair("__EVENTVALIDATION", EVENT_STATE));

		nvps.add(new BasicNameValuePair("xnd", xuenian));
		nvps.add(new BasicNameValuePair("xqd", xueqi));
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps,  "GBK"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		HttpResponse response;
		try {
			response = httpclient.execute(httpost);
			String result = EntityUtils.toString(response.getEntity());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private static String getScore(String url,String sno, String xuenian, String xueqi) {
		HttpPost httpost = new HttpPost(url+sno);
		httpost.setHeader("User-Agent", USER_AGENT);
		httpost.setHeader("Referer", url+sno);	//当前学生查询成绩的get链接
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		nvps.add(new BasicNameValuePair("__EVENTTARGET", "ddlxn"));
		nvps.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		nvps.add(new BasicNameValuePair("__LASTFOCUS", ""));
		nvps.add(new BasicNameValuePair("__VIEWSTATE", VIEW_STATE));
		nvps.add(new BasicNameValuePair("__EVENTVALIDATION", EVENT_STATE));

		nvps.add(new BasicNameValuePair("ddlxn", xuenian));
		nvps.add(new BasicNameValuePair("ddlxq", xueqi));
		nvps.add(new BasicNameValuePair("btnCx", "+%B2%E9++%D1%AF+"));
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps,  "GBK"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		HttpResponse response;
		try {
			response = httpclient.execute(httpost);
			String result = EntityUtils.toString(response.getEntity());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
