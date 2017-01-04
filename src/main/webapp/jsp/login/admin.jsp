<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta name="description" content="overview & stats" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>直播室管理后台-登录</title>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/base/css/login.css?timestamp=<%=(int)(Math.random() *100)%>" />
<style>
	#container.hx{width:620px;}
	#container.hx #login_center{background:url(base/images/login/gwhx_login_bg.jpg) no-repeat 0 0;border : 1px solid #fbf3f3;}
</style>
</head>
<body style="text-align: center;">
   <div id="loading" style="width:120px;z-index:10000;position:absolute;margin-top:180px;left:52%;display:none;">
	<p><img src="<%=request.getContextPath()%>/images/loading_2.gif" /></p>
  </div>
  <form id="loginForm" method="post">
  	<input type="hidden" name="localSelect" value="zh_TW" />
    <div id="container">
      <div id="login_center">
        <div id="input_body">
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="login_table">
            <tr>
              <td><input id="userNo" name="userNo" type="text" class="login_input" placeholder="账号"/></td>
            </tr>
            <tr>
              <td><input id="password" name="password" type="password"  class="login_input" placeholder="密码"/></td>
            </tr>
            <tr>
            	<td>
            		<input type="text"  name="code" id="code" title="请输入验证码" class="login_input login_input-wid" style="width:90px;" placeholder="验证码"/>
            		<img src="<%=request.getContextPath()%>/captchaController/get.do" id="p_captcha_img" title="点击刷新" style="cursor: pointer;" onclick="login.refreshCaptcha();" width="148" height="30" class="yxmimg">
            	</td>
            </tr>
            <tr>
              <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td style="padding:0">
                    	<input name="loginBtn" id="loginBtn" type="button" value='<spring:message code="login.loginbtn" />' class="button_login" onclick="login.doLogin();"/>
                    	<input name="btnReset" id="btnReset" type="button" value='<spring:message code="login.resetbtn" />' class="button_help"/>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td style="padding:0"><span id="errorInfo" style="display:none" class="error"><spring:message code="1006" /><!-- 密码输入错误，请重新输入！ --></span></td>
            </tr>
            <tr>
              <td class="error">&nbsp;</td>
            </tr>
          </table>
        </div>
      </div>
      <p class="gw_browser" style="color: red;"><spring:message code="login.broswer" /></p>
      <p class="gw_copyright">Copyright &copy; 2009-2015 <span id="compName">金道</span>贵金属有限公司</p>
    </div>
  </form>
<script type="text/javascript" charset="UTF-8">
    var basePath = "<%=request.getContextPath()%>";
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/base/js/jquery/jquery.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/login/admin.js?timestamp=<%=(int)(Math.random() *100)%>" charset="UTF-8"></script>
</body>
</html>