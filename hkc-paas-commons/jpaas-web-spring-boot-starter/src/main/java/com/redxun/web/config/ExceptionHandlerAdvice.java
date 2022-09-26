package com.redxun.web.config;

import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.exception.BusinessException;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.MessageUtil;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.web.log.LogUtil;
import com.redxun.web.mq.ErrLogOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * 全局异常捕获。
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

	@Autowired
	private ErrLogOutput errLogOutput;

	@Value("${spring.application.name}")
	private String applicationName;

	/**
	 * IllegalArgumentException异常处理返回json 状态码:400
	 *
	 * @param exception
	 * @return
	 */
	@ExceptionHandler({ IllegalArgumentException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public JsonResult badRequestException(IllegalArgumentException exception) {
		JsonResult result= JsonResult.Fail("方法的参数错误");
		result.setCode(HttpStatus.BAD_REQUEST.value());

		handResult(result,exception);

		return result;
	}


	/**
	 * AccessDeniedException异常处理返回json 状态码:403
	 *
	 * @param exception
	 * @return
	 */
	@ExceptionHandler({ AccessDeniedException.class })
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public JsonResult badMethodExpressException(AccessDeniedException exception) {
		JsonResult result= JsonResult.Fail("无权限访问");
		result.setCode(HttpStatus.FORBIDDEN.value());

		handResult(result,exception);

		return result;

	}





	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public JsonResult handleError(MissingServletRequestParameterException exception) {
		JsonResult result= JsonResult.Fail("缺少请求参数");
		result.setCode(HttpStatus.BAD_REQUEST.value());

		handResult(result,exception);
		return result;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public JsonResult handleError(MethodArgumentNotValidException exception) {
		JsonResult result= JsonResult.Fail("方法参数无效");
		result.setCode(HttpStatus.BAD_REQUEST.value());

		handResult(result,exception);
		return result;
	}

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public JsonResult handleError(BindException exception) {
		JsonResult result= JsonResult.Fail("SPRING数据绑定异常");
		result.setCode(HttpStatus.BAD_REQUEST.value());

		handResult(result,exception);
		return result;

	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public JsonResult handleError(ConstraintViolationException exception) {
		JsonResult result= JsonResult.Fail("数据校验异常");
		result.setCode(HttpStatus.BAD_REQUEST.value());

		handResult(result,exception);
		return result;
	}



	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public JsonResult handleError(HttpMessageNotReadableException exception) {
		JsonResult result= JsonResult.Fail("参数类型不符合，请检查出错类型");
		result.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

		handResult(result,exception);
		return result;

	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public JsonResult handleError(HttpRequestMethodNotSupportedException exception) {
		JsonResult result= JsonResult.Fail("调用的方法不支持!");
		result.setCode(HttpStatus.METHOD_NOT_ALLOWED.value());

		handResult(result,exception);

		return result;

	}

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public JsonResult handleError(HttpMediaTypeNotSupportedException exception) {
		JsonResult result= JsonResult.Fail("内容类型不符合!");

		result.setCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());

		handResult(result,exception);
		return result;
    }

	@ExceptionHandler(BusinessException.class)
	public JsonResult handleError(BusinessException exception) {

		JsonResult result= JsonResult.Fail("系统执行出错!");

		handResult(result,exception);

		return result;
	}

	private void handResult(JsonResult result,Throwable exception){
		if(StringUtils.isNotEmpty(MessageUtil.getTitle())){
			result.setMessage(MessageUtil.getTitle());
		}
		String errorMessage="";
		if(exception instanceof  BusinessException){
			errorMessage= MessageUtil.getMessage();
		}
		else{
			Throwable cause = exception.getCause();
			if(cause instanceof BusinessException){
				errorMessage= MessageUtil.getMessage();
			}
			else{
				errorMessage=ExceptionUtil.getExceptionMessage(exception);
			}
		}

		//记录日志。
		LogUtil.handLog(applicationName, errorMessage);

		boolean showErr=MessageUtil.getShowError();

		result.setShow(showErr);
		//是否显示错误信息。
		String errorShow="true";
		String  showError=SysPropertiesUtil.getString("errorShow");
		if(StringUtils.isNotEmpty(showError) ){
			errorShow=showError;
		}

		if(MBoolean.TRUE_LOWER.val.equals(errorShow)) {
			result.setData(errorMessage);
		}
		else{
			result.setData("系统执行出错了,错误日志已记录!");
		}
	}


	@ExceptionHandler(Throwable.class)
	public JsonResult handleError(Throwable exception) {
		JsonResult result= JsonResult.Fail("系统执行出错!");
		handResult(result,exception);
		return result;
	}

	public static void main(String[] args) {
		System.err.println(MBoolean.TRUE_LOWER.name());
	}
}
