package interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.applicationinsights.TelemetryClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

	private static final String TRACE_ID = "traceId";
	private final ObjectMapper objectMapper;
	private TelemetryClient telemetryClient;

	public LoggingInterceptor(ObjectMapper objectMapper, TelemetryClient telemetryClient) {
		this.objectMapper = objectMapper;
		this.telemetryClient = telemetryClient;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		MDC.put(TRACE_ID, request.getHeader("request-id"));
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		String traceId = MDC.get(TRACE_ID);

		String method = request.getMethod();
		String uri = request.getRequestURI();

		String headers = getHeadersAsJson(request);

		String requestBody = extractRequestBody(request);
		String responseBody = extractResponseBody(response);

		int status = response.getStatus();

		String logEntry = String.format("[TraceID: %s] [Method: %s] [URI: %s] [Status: %d] [Headers] %s [Request] %s [Response] %s", traceId, method, uri, status, headers, requestBody, responseBody);

		if (status >= 200 && status < 300) {
			org.slf4j.LoggerFactory.getLogger("CentralLogger").info(logEntry);
			telemetryClient.trackTrace("T > " + logEntry);
		} else {
			org.slf4j.LoggerFactory.getLogger("CentralLogger").error(logEntry);
			telemetryClient.trackTrace("T > " + logEntry);
		}

		MDC.remove(TRACE_ID);
	}

	private String extractRequestBody(HttpServletRequest request) {
		if (request instanceof ContentCachingRequestWrapper wrapper) {
			byte[] buf = wrapper.getContentAsByteArray();
			if (buf.length > 0) {
				return new String(buf, StandardCharsets.UTF_8);
			}
		}
		return "";
	}

	private String extractResponseBody(HttpServletResponse response) {
		if (response instanceof ContentCachingResponseWrapper wrapper) {
			byte[] buf = wrapper.getContentAsByteArray();
			if (buf.length > 0) {
				try {
					String content = new String(buf, StandardCharsets.UTF_8);
					wrapper.copyBodyToResponse(); // important to allow Spring to return body
					return content;
				} catch (Exception ignored) {
					return "{}";
				}
			}
		}
		return "";
	}

	private String getHeadersAsJson(HttpServletRequest request) {
		try {
			return objectMapper.writeValueAsString(Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, h -> Collections.list(request.getHeaders(h)))));
		} catch (Exception e) {
			return "{}";
		}
	}
}
