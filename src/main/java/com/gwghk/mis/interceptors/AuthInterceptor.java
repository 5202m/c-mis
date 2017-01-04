package com.gwghk.mis.interceptors;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.gwghk.mis.authority.ActionVerification;
import com.gwghk.mis.model.BoMenu;
import com.gwghk.mis.model.MenuResult;
import com.gwghk.mis.util.ContextHolderUtils;
import com.gwghk.mis.util.PropertiesUtil;
import com.gwghk.mis.util.ResourceBundleUtil;
import com.gwghk.mis.util.ResourceUtil;

/**
 * 摘要：权限拦截器
 * @author Gavin
 * @date   2014-10-29
 */
public class AuthInterceptor implements HandlerInterceptor {
	 
	private List<String> excludeUrls;   //排除不拦截的URL

	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response
			, Object object, Exception exception) throws Exception {
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response
			, Object object, ModelAndView modelAndView) throws Exception {
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response
			 , Object handler) throws Exception {
		String requestPath = ResourceUtil.getRequestPath(request);
		if (StringUtils.isBlank(requestPath) || excludeUrls.contains(requestPath)) {
			return true;
		} else {
			if (ContextHolderUtils.getSession().getAttribute(ContextHolderUtils.getSessionId()) != null
			           || (requestPath.contains("uploadController/upload.do") && ContextHolderUtils.getSessionId() != null)) {
				//System.out.println("request:"+request.getRequestURI()+";systemCategory:"+request.getParameter("systemCategory"));
				return actionAuth(request,response,handler);
			} else {
				//response.sendRedirect(request.getContextPath()+"/jsp/login/timeout.jsp");
				jumpLoginPage(request,response);
				return false;
			}
		}
	}
	
	/**
	 * 功能：跳转到登录页
	 * @param request
	 * @param response
	 */
	private void jumpLoginPage(HttpServletRequest request,HttpServletResponse response){
		try {
			 response.setCharacterEncoding("utf-8");  
			 response.setContentType("text/html; charset=utf-8");
			 PrintWriter out = response.getWriter();
	         StringBuilder builder = new StringBuilder();
	         builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">"); 
	         builder.append("alert(\""+ResourceBundleUtil.getByMessage("common.session.timeout")+"\");");
	         builder.append("window.top.location.href=\"");
	         builder.append(request.getContextPath());
	         builder.append("/login.do\";</script>");
	         out.print(builder.toString());
	         out.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 动作拦截
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 */
	private boolean actionAuth(HttpServletRequest request, HttpServletResponse response
			 , Object handler) throws Exception{
		if("false".equals(PropertiesUtil.getInstance().getProperty("authority"))){
			return true;
		}
		//监听注解方法
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		ActionVerification annotation = method.getAnnotation(ActionVerification.class);
		// 判断是是否存在对应注解
		if (annotation != null) {
			//提取注解的key值
			String key = annotation.key(),menuId=request.getParameter("menuId");
			if (StringUtils.isNotBlank(key)&&StringUtils.isNotBlank(menuId)) {
				MenuResult result = ResourceUtil.getSessionMenu();
				List<BoMenu> funList=result.getFunMap().get(menuId);
				if(funList!=null&&funList.size()>0){
					boolean hasRecord=false;
				    for(BoMenu row:funList){//判断是否存在对应的功能权限
				       if(key.equals(row.getCode())){
				    	   hasRecord=true;
				    	   break;
				       }
				    }
				    if(!hasRecord){
				    	response.sendRedirect(request.getContextPath()+"/jsp/common/noAuth.jsp");
				        return false;
				    }
				}
			}
		}
		return true;
	}
	
}
