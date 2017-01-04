<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<div style="padding:5px;overflow:hidden;">
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
      <tr>
        <th width="15%"><spring:message code="user.no" /><!-- 账号 --></th>
        <td width="35%"><span>${mngUser.userNo}</span></td>
        <th width="15%"><spring:message code="user.name" /><!-- 姓名 --></th>
        <td width="35%"><span>${mngUser.userName}</span></td>
      </tr>
      <tr>
        <th width="15%"><spring:message code="user.phone" /><!-- 手机号 --></th>
        <td width="35%"><span>${mngUser.telephone}</span></td>
        <th width="15%"><spring:message code="user.email" /><!-- Email --></th>
        <td width="35%"><span>${mngUser.email}</span></td>
      </tr>
      <tr>
        <th width="15%"><spring:message code="user.role" /><!-- 所属角色 --></th>
        <td width="35%"><span>${mngUser.role.roleName}</span></td>
        <th width="15%"><spring:message code="common.status" /><!-- 状态 --></th>
        <td width="35%">
        	<span>
	            <c:choose>
	            	<c:when test="${mngUser.status == 0}"><spring:message code="common.enabled" /><!-- 启用 --></c:when>
	            	<c:otherwise><spring:message code="common.disabled" /><!-- 禁用 --></c:otherwise>
	            </c:choose>
            </span>
        </td>
      </tr>
      <tr>
        <th width="15%">微信号</th>
        <td width="35%"><span>${mngUser.wechatCode}</span></td>
        <th width="15%">胜率</th>
        <td width="35%"><span>${mngUser.winRate}</span></td>
      </tr>
      <tr>
        <th><spring:message code="user.position" /><!-- 职位 --></th>
        <td><span>${mngUser.position}</span></td>
        <th>月收益</th>
        <td><span>${mngUser.earningsM}</span></td>
      </tr>
      <tr>
        <th>头像</th>
        <td colspan="3"><img src="${mngUser.avatar}"/></td>
      </tr>
      <tr>
        <th>简介</th>
        <td colspan="3"><span>${mngUser.introduction}</span></td>
      </tr>
      
      <tr>
        <th>微信二维码</th>
        <td colspan="3">
        <c:choose>
         	<c:when test="${empty mngUser.wechatCodeImg}">未上传</c:when>
         	<c:otherwise><a href="${mngUser.wechatCodeImg}" target="_blank"><img src="${mngUser.wechatCodeImg}" style="max-width:80px;" alt="二维码"/></a></c:otherwise>
		</c:choose>
	            
        </td>
      </tr>
    </table>
</div>
  
