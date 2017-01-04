<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/common.jsp" %>
<div style="padding:5px;overflow:hidden;">
  <form id="aystExitForm" class="yxForm" method="post">
    <table class="tableForm_L" style="margin-top:3px" width="99%" heigth="auto"  border="0" cellpadding="0" cellspacing="1">
        <tr>
          <th>所属房间</th>
          <td>
              <input type="hidden" name="userIds" value="${userIds}"/>
	          <select name="chatUserRoomId" id="chatUserRoomId" style="width:160px;">
	              <c:forEach var="row" items="${chatGroupList}">
	                 <option value="${row.id}<c:if test="${empty row.groupType}">,</c:if>">${row.name}</option>
	              </c:forEach>
	          </select>
          </td>
        </tr>
      </table>
  </form>
</div>
  
