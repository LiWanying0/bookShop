package com.nit.book.shop.exception;


import com.nit.book.shop.common.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


@Order(1)
@RestControllerAdvice(basePackages = "com.nit.book.shop")
@Slf4j
public class ExceptionHandler<T> implements ResponseBodyAdvice<T> {

	@org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
	public JsonResult<?> baseException(Exception exception) {
		log.error("",exception);
		return JsonResult.error("系统繁忙，请稍后再试");
	}

//	@org.springframework.web.bind.annotation.ExceptionHandler(value = JSONException.class)
//	public JsonResult<?> jsonException(JSONException exception) {
//		log.error("",exception);
//		return JsonResult.error("Json格式异常");
//	}

	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
		return false;
	}

	@Override
	public T beforeBodyWrite(T body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
		return null;
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(value = IllegalParamException.class)
	public JsonResult<?> handleIllegalParamException(IllegalParamException exception) {
		log.error("",exception);
		return JsonResult.error(exception.getMessage());
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(value = ResourceNotFoundException.class)
	public JsonResult<?> handleResourceNotFoundException(ResourceNotFoundException exception) {
		log.error("",exception);
		return JsonResult.error(exception.getMessage());
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(value = AuthenticationCredentialsNotFoundException.class)
	public JsonResult<?> handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException exception) {
		log.error("",exception);
		return JsonResult.error(exception.getMessage());
	}
}
