<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="login.title" /></title>
<%
   String contextPath = request.getContextPath();
%>
<!-- easyui -->
<link id="yxuiTheme" type="text/css" rel="stylesheet" href="<%=contextPath%>/base/js/easyui/themes/yxui/easyui.css"/>
<link type="text/css" rel="stylesheet" href="<%=contextPath%>/base/js/easyui/themes/icon.css" />
<!-- yxui.css baseIcons.css -->
<link type="text/css" rel="stylesheet" href="<%=contextPath%>/base/css/yxui.css" />
<link type="text/css" rel="stylesheet" href="<%=contextPath%>/base/css/yxuiForm.css" />
<link type="text/css" rel="stylesheet" href="<%=contextPath%>/base/css/baseIcons.css" />
<!-- portal -->
<link type="text/css" rel="stylesheet" href="<%=contextPath%>/base/js/easyui-portal/portal.css" />
<!-- uploadify -->
<link rel="stylesheet" href="<%=contextPath%>/base/js/uploadify/css/uploadify.css" type="text/css">
<link rel="stylesheet" href="<%=contextPath%>/base/js/ueditor/themes/default/css/ueditor.min.css" type="text/css">
</head>
<body class="easyui-layout">
  <!-- north -->
  <div data-options="region:'north',border:false" class="top_body">
    <div id="top_logo"></div>
    <div id="top_rightbody">
      <div class="top_menu_left"></div>
      <div class="top_menu_bg">
        <div class="top_menu_user">
          [ ${userNo} ],<spring:message code="main.head" /><!-- 您好!今天是 --><span id="yxui_main_today">&nbsp;</span>
        </div>
        <div id="shortcut_menu">
          <ul>
             <li style="padding: 6px 3px;"> 
              <span>系统类别  
               <select id="yxui_main_sysfag_select" name="systemCategory" style="width: 70px;">
                 <c:if test="${sessionScope.SESSION_SUPER_FLAG==true}">
                   <option value="">-请选择-</option>
                 </c:if>
                 <c:forEach var="systemCategory" items="${systemCategoryList}">
		                   <option value="${systemCategory.code}">${systemCategory.name}</option>
		         </c:forEach>
			  </select>  
	        </span>  
            </li>
            <%-- <li><a id="homeLinkId" title="<spring:message code="main.homePage" />" href="http://www.24k.hk/" target="_blank" class="button_short03" onFocus="this.blur()"></a></li> --%>
            <li><a title="<spring:message code="main.editPwd" />" href="#" class="button_short02" onFocus="this.blur()" onclick="updatePwd()"></a></li>
            <%--  <li><a title="<spring:message code="main.feedback" />" href="#" class="button_short05" onFocus="this.blur()" onclick="feedbackOpen()"></a></li>
            <li><a title="<spring:message code="main.help" />" href="#" class="button_short04" onFocus="this.blur()" onclick="helpOpen()"></a></li>
            <li><a title="<spring:message code="main.version" />" href="#" class="button_short06" onFocus="this.blur()" onclick="versionOpen()"></a></li>
        	--%>
        	<li><a title="<spring:message code="main.loginout" />" href="#" class="button_short01" onFocus="this.blur()" onclick="loginOut()"></a></li>
          </ul>
        </div>
      </div>
    </div>
  </div>
  <!-- south -->
  <div data-options="region:'south',border:false" style="height:20px; padding:3px; background:#cee0e7; overflow:hidden;">
    <div style="text-align:center">
      <!-- Copyright © 2004-<span id="yxui_main_fullyear">&nbsp;</span> -->
    </div>
  </div>
  <!-- west -->
  <div data-options="region:'west'" style="width:210px;">
    <div id="yxui_main_accordion" class="easyui-accordion" data-options="fit:true,border:false,animate:false"></div>
  </div>
  <!-- center -->
  <div data-options="region:'center'">
    <div id="yxui_main_tabs" class="easyui-tabs" data-options="fit:true,border:false">
      <div title="<spring:message code="main.welcome" />" style="padding: 20px; overflow: hidden;">
        <div id="container">
          <div id="welcome"></div>
        </div>
      </div>
    </div>
  </div>
  <!-- centerMenu -->
  <div id="yxui_main_tabs_menu" style="width: 100px; display: none;">
    <div iconCls="ope-refresh" type="refresh"><spring:message code="main.refresh" /><!-- 刷新 --></div>
    <div class="menu-sep"></div>
    <div iconCls="ope-cancel" type="close"><spring:message code="main.close" /><!-- 关闭 --></div>
    <div iconCls="ope-close" type="closeOther"><spring:message code="main.closeother" /><!-- 关闭其他 --></div>
    <div iconCls="ope-cancelSelect" type="closeAll"><spring:message code="main.closeall" /><!-- 关闭所有 --></div>
  </div>
<script type="text/javascript" charset="UTF-8">
   var basePath = "<%=request.getContextPath()%>";
   var path = "<%=request.getContextPath()%>";
   var locale = "${locale}";                       //简体：zh_CN 繁体：zh_TW 英文：en_US
</script>

<script type="text/javascript" src="<%=contextPath%>/base/js/jquery/jquery.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath%>/base/js/jquery/jquery.cookie.js" charset="UTF-8"></script>

<script type="text/javascript" src="<%=contextPath%>/base/js/easyui/jquery.easyui.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath%>/base/js/easyui/locale/easyui-lang-zh_CN.js" charset="UTF-8"></script>

<script type="text/javascript" src="<%=contextPath%>/base/js/easyui-portal/jquery.portal.js" charset="UTF-8"></script>
<!-- jquery UI -->
<script type="text/javascript" src="<%=contextPath%>/base/js/jqueryui/jquery-ui-1.9.2.custom.min.js" charset="UTF-8"></script>
<!-- My97 -->
<script type="text/javascript" src="<%=contextPath%>/base/js/My97DatePicker/WdatePicker.js" charset="UTF-8"></script>
<!-- yxuiLang -->
<script type="text/javascript" src="<%=contextPath%>/base/js/locale/yxui-lang-zh_CN.js" charset="UTF-8"></script>
<!-- yxuiUtil -->
<script type="text/javascript" src="<%=contextPath%>/base/js/util/yxuiUtil.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath%>/base/js/util/dateUtil.js" charset="UTF-8"></script>
<!-- main -->
<script type="text/javascript" src="<%=contextPath%>/js/lib/jquery.i18n.properties-1.0.9.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/common.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath%>/jsp/main/main.js" charset="UTF-8"></script>
<!-- uploadify -->
<script type="text/javascript" src="<%=contextPath%>/base/js/uploadify/jquery.uploadify.min.js" charset="utf-8" ></script>

<!-- 工具类 -->
<script type="text/javascript" src="<%=contextPath%>/base/js/extends/goldOfficeUtils.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=contextPath%>/base/js/extends/Event.js" charset="UTF-8"></script>
<script type="text/javascript">
  if(window.location.href.indexOf('handanadmin')!=-1){
    $("#homeLinkId").attr("href","http://www.hx9999.com");
  }
  $("#yxui_main_sysfag_select").change(function(){
	  var $center = $('#yxui_main_tabs');
	  var closeTabsTitle = [];
	  $.each($center.tabs('tabs'), function() {
		var opt = $(this).panel('options');
		if (opt.closable) {
			closeTabsTitle.push(opt.title);
		}
	 });
	 for (var i = 0; i < closeTabsTitle.length; i++) {
		$center.tabs('close', closeTabsTitle[i]);
	 }
	 $.messager.alert($.i18n.prop("common.operate.tips"),"切换系统类别成功！",'info');
  });
</script>
</body>
</html>