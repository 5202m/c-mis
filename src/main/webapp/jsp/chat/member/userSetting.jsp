<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/common/common.jsp"%>
<div style="padding:5px;overflow:hidden;width:300px;">
  <form id="userSettingForm" class="yxForm" method="post">
    <input type="hidden" name="memberId" value="${memberId}"/>
    <input type="hidden" name="groupType" value="${groupType}"/>
    <input type="hidden" name="type" value="${type}"/>
    <table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
	      <tbody>
		      <c:choose>
				    <c:when test="${type==1}">
				       <tr>
				          <th width="30%">价值用户</th>
				          <td width="70%"> 
				               <input type="radio" name="value" value="true" <c:if test="${valueUser==true}"> checked="checked"</c:if> />是 &nbsp;&nbsp;
						       <input type="radio" name="value" value="false" <c:if test="${valueUser==false}"> checked="checked"</c:if>/>否
				  		  </td>
		               </tr>
				       <tr>
		                   <th width="30%">备注</th>
		          		   <td width="70%"><input type="text" name="remark" value="${valueUserRemark}" style="width:200px;"/></td>
		                </tr>             
				    </c:when>
				     <c:when test="${type==2}">
				       <tr>
				          <th width="30%">VIP用户</th>
				          <td width="70%"> 
					     	<input type="radio" name="value" value="true" <c:if test="${vipUser==true}"> checked="checked"</c:if> />是 &nbsp;&nbsp;
				            <input type="radio" name="value" value="false" <c:if test="${vipUser==false}"> checked="checked"</c:if>/>否
				  		  </td>
		               </tr>
		                <tr>
		                   <th width="30%">备注</th>
		          		   <td width="70%"><input type="text" name="remark" value="${vipUserRemark}" style="width:200px;"/></td>
		                </tr>
				    </c:when>
				     <c:when test="${type==3}">
				       <tr>
				          <th width="30%">用户级别</th>
				          <td width="70%"> 
					     	<select name="clientGroup" id="userSetting_clientGroup" style="width: 160px;">
									<option value="">--请选择--</option>
									<c:forEach var="row" items="${clientGroupList}">
										<c:if test="${row.clientGroupId != 'visitor' && row.clientGroupId != 'vip' }">
											<option value="${row.clientGroupId}" <c:if test="${row.clientGroupId == clientGroup }"> selected="selected"</c:if>>${row.name}</option>
										</c:if>
									</c:forEach>
							</select>
						  </td>
		               </tr>
		                <tr>
		                   <th>账号</th>
		          		   <td><input type="text" name="accountNo" value="${accountNo}" style="width:200px;"/></td>
		                </tr>
				    </c:when>
				    <c:otherwise>
				    </c:otherwise>   
				  </c:choose>
	      </tbody>
    </table>
  </form>
</div>
  
