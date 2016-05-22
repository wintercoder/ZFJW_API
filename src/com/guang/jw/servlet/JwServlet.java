package com.guang.jw.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.guang.jw.utils.JsonUtils;
import com.guang.jw.utils.JwUtils;

/**
 * 服务器端后台逻辑处理页
 */
@SuppressWarnings("serial")
public class JwServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Writer writer = response.getWriter();
		boolean done = false;
		
		String sno = request.getParameter("uname");
		String password = request.getParameter("upwd");
		String action = request.getParameter("action");
		String result = "";
		switch (action) {
		case "login":
			if(JwUtils.LoginToSystem(sno, password)){
				result = "登陆成功";
			}else{
				result = "登陆失败";
			}
			done = true;
			break;
		case "curSemesterScore":
			String curScoreHtml = JwUtils.getCurSemesterScore(sno);
			result = JsonUtils.getJsonFromScore(curScoreHtml);
			done = true;
			break;
		case "curSemesterKeBiao":
			String curKeBiaoHtml = JwUtils.getCurSemesterKeBiao(sno);
			result = JsonUtils.getJsonFromKebiao(curKeBiaoHtml);
			done = true;
			break;
		case "curSemesterJieShao":
			String infoHtml = JwUtils.getCurSemesterJieShao(sno);
			result = JsonUtils.getJsonFromKebiao(infoHtml);
			done = true;
			break;
		}
		if(done){
			writer.write(result);	
			return;
		}
		
		
		String xuenian = request.getParameter("xuenian");
		String  xueqi = request.getParameter("xueqi");
		result = "";
		switch (action) {
		case "keBiao":
			String keBiaoHtml = JwUtils.getKebiao(sno, xuenian, xueqi);
			result = JsonUtils.getJsonFromKebiao(keBiaoHtml);
			break;
		case "score":
			String scoreHtml = JwUtils.getScore(sno, xuenian, xueqi);
			result = JsonUtils.getJsonFromScore(scoreHtml);
			break;
		default:
			break;
		}
		writer.write(result);
	}
}
