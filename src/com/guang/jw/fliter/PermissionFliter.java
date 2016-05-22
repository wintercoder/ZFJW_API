package com.guang.jw.fliter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.apache.http.util.TextUtils;

@WebFilter(
	filterName="PermissionFliter",
	servletNames="JwServlet"
)
/**
 * 空学号、密码
 */
public class PermissionFliter implements Filter {
    public PermissionFliter() {
    }
	public void init(FilterConfig fConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String uName = request.getParameter("uname");
		String upwd = request.getParameter("upwd");
		
		if (TextUtils.isEmpty(uName) || TextUtils.isEmpty(upwd)) {
			RequestDispatcher rdDispatcher = request
					.getRequestDispatcher("/index.jsp");
			rdDispatcher.forward(request, response);
		}
		chain.doFilter(request, response);
	}


	@Override
	public void destroy() {
	}

}
