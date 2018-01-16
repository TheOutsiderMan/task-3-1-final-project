package by.tr.web.kinorating.controller.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class EncodingFilter implements Filter {
	
	private static final String ENCODING_FILTER_INIT_PARAM = "encoding";
	private String charset;
	
	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding(charset);
		response.setCharacterEncoding(charset);
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		charset = fConfig.getInitParameter(ENCODING_FILTER_INIT_PARAM);
	}

}
