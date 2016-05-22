package com.guang.jw.fliter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

@WebFilter(
	filterName="EncodeFliter",
	urlPatterns="*",
	initParams={
		@WebInitParam(name="input_encoding",value="UTF-8"),
		@WebInitParam(name="output_encoding",value="UTF-8")	
//		@WebInitParam(name="output_encoding",value="gb2312")	//教务系统返回的页面是gb2312
		
	}
)

public class EncodeFliter implements Filter {
	private String input_encoding ;
	private String output_encoding ;
	
    public EncodeFliter() {
    }
	public void init(FilterConfig fConfig) throws ServletException {
		input_encoding = fConfig.getInitParameter("input_encoding");
		output_encoding = fConfig.getInitParameter("output_encoding");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding(input_encoding);
		response.setCharacterEncoding(output_encoding);
		chain.doFilter(request, response);
	}


	@Override
	public void destroy() {
	}

}
