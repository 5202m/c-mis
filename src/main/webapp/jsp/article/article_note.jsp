<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/article/article_note.js" charset="UTF-8"></script>
<style type="text/css">
#langTd label{margin-right: 10px;}
#article_tab .tabs {margin-left: 8px;}
#aEditPanel.retrieve #articleBasePanel .combo{display: none;}
</style>
<!-- 文章基本信息 -->
<div style="height: 700px;" id="articleBasePanel">
	<form id="articleBaseInfoForm" class="yxForm" method="post">
		<input type="hidden" name="id" id="articleId"/>
		<table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
			<tr>
				<th width="15%">栏目<span class="red">*</span></th>
				<td width="35%">
					<input type="hidden" name="categoryId" id="categoryId" value="class_note" />
					<span>课堂笔记</span>
				</td>
				<th width="15%">所属房间</th>
		     	<td width="35%">
		     		<select id="tradeNodeRoomId" style="width: 160px;">
		     			<option>--请选择--</option>
					</select>
		     	</td>
			</tr>
			<tr>
				<th width="15%">发布时间<span class="red">*</span></th>
				<td width="35%">
					从 <input name="publishStartDateStr" id="publishStartDate" class="Wdate" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'publishEndDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width: 140px" />
					到 <input name="publishEndDateStr" id="publishEndDate" class="Wdate" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'publishStartDate\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width: 140px" />
				</td>
				<th width="15%">应用位置<span class="red">*</span></th>
				<td width="35%">
					<select class="easyui-combotree" style="width: 180px;" name="platformStr" id="platformStr" multiple></select>
					<span id="platformTxt"></span>
				</td>
			</tr>
			<tr>
				<th width="15%">状态</th>
				<td width="35%">
					<select id="articleStatus" name="status">
					</select>
				</td>
				<th width="15%">序号</th>
				<td width="35%">
					<input type="text" name="sequence" id="articleSeq" value="0" />
				</td>
			</tr>
		</table>
	</form>
	<form name="articleDetailForm" id="article_detail_zh" class="yxForm">
		<table class="tableForm_L" border="0" cellspacing="1" cellpadding="0">
				<tr>
					<th width="15%">标题</th>
					<td colspan="3">
						<input type="hidden" name="lang" value="zh"/>
						<input type="text" name="title" class="easyui-validatebox" data-options="required:true,missingMessage:'请输入标题'" style="width: 600px" />
					</td>
				</tr>
				<tr>
					<th width="15%">内容</th>
					<td colspan="3" tid="content">
					</td>
				</tr>
				<tr>
					<th>作者</th>
					<td>
				        <input type="hidden" name="userId" /> 
				        <input type="hidden" name="name" /> 
				        <input type="hidden" name="position" /> 
				        <input type="hidden" name="avatar" /> 
				        <input type="hidden" name="usertag" /> 
				        <select name="authorList_zh" id="authorList_zh" style="width: 180px;"></select>
					</td>
				</tr>
				<tr>
					<th>模板</th>
					<td>
						<select name="tag" id="tag">
							<option value="">请选择</option>
							<option value="trading_strategy">交易策略</option>
							<option value="shout_single">喊单</option>
							<option value="resting_order">挂单</option>
						</select>
						&nbsp;&nbsp;
						<input type="checkbox" id="sendSubscribe" checked="checked">
						<label for="sendSubscribe" id="sendSubscribeLabel">自动发送订阅通知</label>
						<input type="button" id="sendSubscribeBtn" value=" 发送订阅通知 ">
					</td>
				</tr>
		</table>
		<input type="hidden" name="remark" id="remark" />
	</form>
	<table id="trading_strategy" class="tableForm_L tag_tab" border="0" cellspacing="1" cellpadding="0" style="display:none;">
		<tr>
			<th>品种</th>
			<td>
				<select name="symbol">
		        	<option value="" >--请选择--</option>
	        		<option value="AUDUSD" >澳元美元</option>
					<option value="USDJPY" >美元日元</option>
					<option value="EURUSD" >欧元美元</option>
					<option value="GBPUSD" >英镑美元</option>
					<option value="NZDUSD" >纽元美元</option>
					<option value="USDCAD" >美元加元</option>
					<option value="USDCHF" >美元瑞郎</option>
					<option value="USDCNH" >美元人民币</option>
					<option value="AUDJPY" >澳元日元</option>
					<option value="AUDNZD" >澳元纽元</option>
					<option value="CADJPY" >加元日元</option>
					<option value="EURAUD" >欧元澳元</option>
					<option value="EURCHF" >欧元瑞郎</option>
					<option value="EURGBP" >欧元英镑</option>
					<option value="EURJPY" >欧元日元</option>
					<option value="GBPAUD" >英镑澳元</option>
					<option value="GBPCHF" >英镑瑞郎</option>
					<option value="GBPJPY" >英镑日元</option>
					<option value="NZDJPY" >纽元日元</option>
					<option value="HKDCNH" >港币人民币</option>
					<option value="XAG/USD" >白银美元</option>
					<option value="XAU/USD" >黄金美元</option>
					<option value="AUD" >澳元</option>
					<option value="USD" >美元指数</option>
					<option value="JPY" >日元</option>
					<option value="EUR" >欧元</option>
					<option value="GBP" >英镑</option>
					<option value="NZD" >纽元</option>
					<option value="CAD" >加元</option>
					<option value="CHF" >瑞郎</option>
					<option value="CNH" >人民币</option>
					<option value="HKD" >港币</option>
					<option value="XAU" >黄金</option>
					<option value="XAG" >白银</option>
					<option value="UKOil" >英国原油</option>
					<option value="USOil" >美国原油</option>
					<option value="CNHGOLD" >人民币黄金</option>
					<option value="CNHSILVER" >人民币白银</option>
		        </select>
		     </td>
		     <th>支撑位</th>
		     <td><input type="text" name="support_level" /></td>
		     <th>阻力位</th>
		     <td><input type="text" name="drag_level" /></td>
		     <td><a href="javascript:void(0)" class="ope-add"></a><a href="javascript:void(0)" class="ope-remove" style="display:none;"></a></td>
		</tr>
	</table>
	<table id="shout_single" class="tableForm_L tag_tab" border="0" cellspacing="1" cellpadding="0" style="display:none;">
		<tr>
			<th>品种</th>
			<td>
				<select name="symbol">
		        	<option value="" >--请选择--</option>
	        		<option value="AUDUSD" >澳元美元</option>
					<option value="USDJPY" >美元日元</option>
					<option value="EURUSD" >欧元美元</option>
					<option value="GBPUSD" >英镑美元</option>
					<option value="NZDUSD" >纽元美元</option>
					<option value="USDCAD" >美元加元</option>
					<option value="USDCHF" >美元瑞郎</option>
					<option value="USDCNH" >美元人民币</option>
					<option value="AUDJPY" >澳元日元</option>
					<option value="AUDNZD" >澳元纽元</option>
					<option value="CADJPY" >加元日元</option>
					<option value="EURAUD" >欧元澳元</option>
					<option value="EURCHF" >欧元瑞郎</option>
					<option value="EURGBP" >欧元英镑</option>
					<option value="EURJPY" >欧元日元</option>
					<option value="GBPAUD" >英镑澳元</option>
					<option value="GBPCHF" >英镑瑞郎</option>
					<option value="GBPJPY" >英镑日元</option>
					<option value="NZDJPY" >纽元日元</option>
					<option value="HKDCNH" >港币人民币</option>
					<option value="XAG/USD" >白银美元</option>
					<option value="XAU/USD" >黄金美元</option>
					<option value="AUD" >澳元</option>
					<option value="USD" >美元指数</option>
					<option value="JPY" >日元</option>
					<option value="EUR" >欧元</option>
					<option value="GBP" >英镑</option>
					<option value="NZD" >纽元</option>
					<option value="CAD" >加元</option>
					<option value="CHF" >瑞郎</option>
					<option value="CNH" >人民币</option>
					<option value="HKD" >港币</option>
					<option value="XAU" >黄金</option>
					<option value="XAG" >白银</option>
					<option value="UKOil" >英国原油</option>
					<option value="USOil" >美国原油</option>
					<option value="CNHGOLD" >人民币黄金</option>
					<option value="CNHSILVER" >人民币白银</option>
		        </select>
		     </td>
		     <th>方向</th>
		     <td>
			     <select name="longshort">
			     	<option value="" >--请选择--</option>
			     	<option value="long">看涨</option>
			     	<option value="short">看跌</option>
			     </select>
		     </td>
		     <th>进场点位</th>
		     <td><input type="text" name="point" style="width:100px;" /></td>
		     <th>止盈</th>
		     <td><input type="text" name="profit" style="width:100px;" /></td>
		     <th>止损</th>
		     <td><input type="text" name="loss" style="width:100px;" /></td>
		     <td><a href="javascript:void(0)" class="ope-add"></a><a href="javascript:void(0)" class="ope-remove" style="display:none;"></a></td>
		</tr>
	</table>
	<table id="resting_order" class="tableForm_L tag_tab" border="0" cellspacing="1" cellpadding="0" style="display:none;">
		<tr>
			<th>品种</th>
			<td>
				<select name="symbol">
		        	<option value="" >--请选择--</option>
	        		<option value="AUDUSD" >澳元美元</option>
					<option value="USDJPY" >美元日元</option>
					<option value="EURUSD" >欧元美元</option>
					<option value="GBPUSD" >英镑美元</option>
					<option value="NZDUSD" >纽元美元</option>
					<option value="USDCAD" >美元加元</option>
					<option value="USDCHF" >美元瑞郎</option>
					<option value="USDCNH" >美元人民币</option>
					<option value="AUDJPY" >澳元日元</option>
					<option value="AUDNZD" >澳元纽元</option>
					<option value="CADJPY" >加元日元</option>
					<option value="EURAUD" >欧元澳元</option>
					<option value="EURCHF" >欧元瑞郎</option>
					<option value="EURGBP" >欧元英镑</option>
					<option value="EURJPY" >欧元日元</option>
					<option value="GBPAUD" >英镑澳元</option>
					<option value="GBPCHF" >英镑瑞郎</option>
					<option value="GBPJPY" >英镑日元</option>
					<option value="NZDJPY" >纽元日元</option>
					<option value="HKDCNH" >港币人民币</option>
					<option value="XAG/USD" >白银美元</option>
					<option value="XAU/USD" >黄金美元</option>
					<option value="AUD" >澳元</option>
					<option value="USD" >美元指数</option>
					<option value="JPY" >日元</option>
					<option value="EUR" >欧元</option>
					<option value="GBP" >英镑</option>
					<option value="NZD" >纽元</option>
					<option value="CAD" >加元</option>
					<option value="CHF" >瑞郎</option>
					<option value="CNH" >人民币</option>
					<option value="HKD" >港币</option>
					<option value="XAU" >黄金</option>
					<option value="XAG" >白银</option>
					<option value="UKOil" >英国原油</option>
					<option value="USOil" >美国原油</option>
					<option value="CNHGOLD" >人民币黄金</option>
					<option value="CNHSILVER" >人民币白银</option>
		        </select>
		     </td>
		     <th>方向</th>
		     <td>
			     <select name="longshort">
			     	<option value="" >--请选择--</option>
			     	<option value="long">看涨</option>
			     	<option value="short">看跌</option>
			     </select>
		     </td>
		     <th>进场点位</th>
		     <td><input type="text" name="point" style="width:100px;" /></td>
		     <th>止盈</th>
		     <td><input type="text" name="profit" style="width:100px;" /></td>
		     <th>止损</th>
		     <td><input type="text" name="loss" style="width:100px;" /></td>
		     <td><a href="javascript:void(0)" class="ope-add"></a><a href="javascript:void(0)" class="ope-remove" style="display:none;"></a></td>
		</tr>
	</table>
</div>
<script type="text/javascript">
	ArticleTemplate.config = JSON.parse('${config}');
	ArticleEdit.initTemplate(ArticleTemplate);
</script>