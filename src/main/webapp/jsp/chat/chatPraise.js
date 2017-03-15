/**
 * 点赞管理JS
 * Created by Jade.zhu on 2017/3/9.
 */
var chatPraise = {
  gridId : 'praise_datagrid',
  init: function(){
    this.initGrid();
    this.setEvent();
    this.setUserList();
  },
  initGrid:function(){
    goldOfficeUtils.dataGrid({
      gridId : chatPraise.gridId,
      idField:"id",
      sortName : 'startDate',
      sort:'desc',
      singleSelect : false,
      url : basePath+'/chatPraiseController/datagrid.do',
      columns : [[
        {title : 'id',field : 'id',checkbox : true, hidden: true},
        {title : $.i18n.prop("common.operate"),field : 'todo', formatter : function(value, rowData, rowIndex) {		/**操作*/
          $("#praise_datagrid_rowOperation a").each(function(){
            $(this).attr("id",rowData.id);
          });
          return $("#praise_datagrid_rowOperation").html();
        }},

        {title : '分析师',field : 'praiseId',sortable : true,formatter : function(value, rowData, rowIndex) {
          return chatPraise.getAnalystCNameByCode(value);
        }},
        {title : '房间组别',field : 'fromPlatform',formatter : function(value, rowData, rowIndex) {
          return chatPraise.getDictNameByCode("#praise_groupType_select",rowData.fromPlatform);
        }},
        {title : "点赞数", field : 'praiseNum'}

      ]],
      toolbar : '#praise_datagrid_toolbar'
    });
  },
  setEvent:function(){
    // 列表查询
    $("#praise_queryForm_search").on("click",function(){
      var queryParams = $('#'+chatPraise.gridId).datagrid('options').queryParams;
      var analystArr = $("#analystsSelectId").combo("getValues");
      var groupType = $('#praise_groupType_select').val();
      queryParams["groupType"] = groupType;
      queryParams["praiseId"] = analystArr && analystArr.length > 0 ? analystArr.join(",") : "";

      $('#'+chatPraise.gridId).datagrid({
        url : basePath+'/chatPraiseController/datagrid.do',
        pageNumber : 1
      });
    });
    // 重置
    $("#subscribe_queryForm_reset").on("click",function(){
      $("#subscribe_queryForm")[0].reset();
    });
  },
  //显示用户列表
  setUserList:function(){
    chatPraise.setAnalystList("analystsSelectId");
  },
  /**
   * 功能：查看
   * @param recordId   dataGrid行Id
   */
  view : function(recordId){
    $("#subscribe_datagrid").datagrid('unselectAll');
    var url = formatUrl(basePath + '/chatSubscribeController/'+recordId+'/view.do');
    goldOfficeUtils.openSimpleDialog({
      title : $.i18n.prop("common.operatetitle.view"),       /**查看记录*/
      height : 575 ,
      href : url ,
      iconCls : 'pag-view'
    });
  },
  /**
   * 提取名称
   */
  getDictNameByCode:function(id,code){
    return $(id).find("option[value='"+code+"']").text();
  },
  /**
   * 功能：刷新
   */
  refresh : function(){
    $('#'+chatPraise.gridId).datagrid('reload');
  },
  /**
   * 功能：修改
   * @param recordId   dataGrid行Id
   */
  edit : function(recordId){
    $("#praise_datagrid").datagrid('unselectAll');
    var url = formatUrl(basePath + '/chatPraiseController/'+recordId+'/edit.do');
    var submitUrl =  formatUrl(basePath + '/chatPraiseController/update.do');
    goldOfficeUtils.openEditorDialog({
      dialogId : "editWindow",
      title : $.i18n.prop("common.operatetitle.edit"),   /**修改记录*/
      width : 500,
      height : 300,
      href : url,
      iconCls : 'pag-edit',
      handler : function(){    //提交时处理
        if($("#praiseEditFrom").form('validate')){
          goldOfficeUtils.ajaxSubmitForm({
            url : submitUrl,
            formId : 'praiseEditFrom',
            onSuccess : function(data){   //提交成功后处理
              var d = $.parseJSON(data);
              if (d.success) {
                $("#editWindow").dialog("close");
                chatPraise.refresh();
                $.messager.alert($.i18n.prop("common.operate.tips"),$.i18n.prop("common.editsuccess"),'info');/**操作提示  修改成功!*/
              }else{
                $.messager.alert($.i18n.prop("common.operate.tips"),'修改失败','error');  /**操作提示  修改失败!*/
              }
            }
          });
        }
      }
    });
  },
  /**
   * 设置分析师选择列表
   * @param id
   */
  setAnalystList:function(id, analyst){
    $('#'+id).combotree({
      data:getJson(basePath+"/chatSubscribeTypeController/getMultipleCkAnalystList.do",{analysts:analyst})
    });
  },
  /**
   * 提取名称
   * @param value
   */
  getAnalystCNameByCode:function(value){
    return $('div[node-id="'+value+'"] span.tree-title:first').text();
  }
};

//初始化
$(function() {
  chatPraise.init();

});