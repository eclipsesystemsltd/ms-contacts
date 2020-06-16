package uk.co.meridenspares.web.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

public class UrlUtil {
	
	public static String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
		String encoding = httpServletRequest.getCharacterEncoding();
		
		if (encoding == null) {
			encoding = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		
		try {
			pathSegment = UriUtils.encodePathSegment(pathSegment, encoding);
		}
		catch (UnsupportedEncodingException e) {
			//Ignore for now.
		}
		
		return pathSegment;
	}
}
