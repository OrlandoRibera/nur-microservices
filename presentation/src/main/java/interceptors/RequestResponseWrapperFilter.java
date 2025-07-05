package interceptors;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
public class RequestResponseWrapperFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		ContentCachingRequestWrapper wrappedRequest =
			new ContentCachingRequestWrapper((HttpServletRequest) request);
		ContentCachingResponseWrapper wrappedResponse =
			new ContentCachingResponseWrapper((HttpServletResponse) response);

		chain.doFilter(wrappedRequest, wrappedResponse);

		wrappedResponse.copyBodyToResponse();
	}
}
