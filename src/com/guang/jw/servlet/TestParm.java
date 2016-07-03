package com.guang.jw.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * 测试发请求用，相当于客户端
 */
public class TestParm extends HttpServlet {
	private final String url = "http://localhost:8080/jw/main.do";
	private final String testSno = "1325110...";	
	private final String testPwd = "";	//	TODO 用test.jsp测试的话改这里
	private final String testXuenian = "2014-2015";
	private final String testXueqi = "1";
	
	private static HttpClient httpclient;
	static {
		httpclient = HttpClientBuilder.create().build();
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		String result = "";
		switch (action) {
		case "login":
			result = testLogin();
			break;
		case "curSemesterScore":
			result = testCurrentScore();
			break;
		case "curSemesterKeBiao":
			result = testCurrentKebiao();
			break;
		case "curSemesterJieShao":
			result = testCurrentJieShao();
			break;
		case "keBiao":
			result = testKeBiao();
			break;
		case "score":
			result = testScore();
			break;
		default:
			break;
		}
		request.setAttribute("result", result);
		RequestDispatcher rdDispatcher = request
				.getRequestDispatcher("/test.jsp");
		rdDispatcher.forward(request, response);
	}
	

	private String testLogin(){
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("action", "login"));
		nvps.add(new BasicNameValuePair("uname", testSno));
		nvps.add(new BasicNameValuePair("upwd", testPwd));
		httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		try {
			String result = EntityUtils.toString( httpclient.execute(httpost).getEntity(),HTTP.UTF_8);
			return result;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String testCurrentScore(){
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("action", "curSemesterScore"));
		nvps.add(new BasicNameValuePair("uname", testSno));
		httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		try {
			String result = EntityUtils.toString( httpclient.execute(httpost).getEntity(),HTTP.UTF_8 );
			return result;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String testCurrentKebiao(){
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("action", "curSemesterKeBiao"));
		nvps.add(new BasicNameValuePair("uname", testSno));
		httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		try {
			String result = EntityUtils.toString( httpclient.execute(httpost).getEntity(),HTTP.UTF_8);
			return result;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String testCurrentJieShao() {
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("action", "curSemesterJieShao"));
		nvps.add(new BasicNameValuePair("uname", testSno));
		httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		try {
			String result = EntityUtils.toString( httpclient.execute(httpost).getEntity(),HTTP.UTF_8);
			return result;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String testScore(){
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("action", "score"));
		nvps.add(new BasicNameValuePair("uname", testSno));
		nvps.add(new BasicNameValuePair("xuenian", testXuenian));
		nvps.add(new BasicNameValuePair("xueqi", testXueqi));
		httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		try {
			String result = EntityUtils.toString( httpclient.execute(httpost).getEntity(),HTTP.UTF_8);
			return result;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	private String testKeBiao(){
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("action", "keBiao"));
		nvps.add(new BasicNameValuePair("uname", testSno));
		nvps.add(new BasicNameValuePair("xuenian", testXuenian));
		nvps.add(new BasicNameValuePair("xueqi", testXueqi));
		httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		try {
			String result = EntityUtils.toString( httpclient.execute(httpost).getEntity(),HTTP.UTF_8);
			return result;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
