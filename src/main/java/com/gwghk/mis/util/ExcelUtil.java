package com.gwghk.mis.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 摘要：Excel导入、导出工具类(单例类)
 * @author  Gavin
 * @date 2014-12-08
 */
public final class ExcelUtil {
	public static final int MAX_ROW=1000;
	
	private ExcelUtil() { 
	}
	private static class ExcelUtilInstance {
		private static final ExcelUtil instance = new ExcelUtil();
	}
	public static ExcelUtil getInstance() { 
		return ExcelUtilInstance.instance; 
	}
	
	/**
	 * 功能:转换列类型
	 */
	public static String convertCellType(HSSFCell HhSSFCell){
		String  result =  "";
		int cellType = HhSSFCell.getCellType();
		switch(cellType) {
			case HSSFCell.CELL_TYPE_NUMERIC :
		         result = String.valueOf(HhSSFCell.getNumericCellValue());
		         break;
		     case HSSFCell.CELL_TYPE_STRING :
		         result = HhSSFCell.getRichStringCellValue().getString();
		         break;
		     case HSSFCell.CELL_TYPE_FORMULA :
		         result = HhSSFCell.getCellFormula();
		         break;
		}
		return result;
	}
	
	/**
	 * 功能：导出为excel(参数以字符串数组)
	 * @param excel_name    生成的Excel文件路径+名称
	 * @param headList		Excel文件Head标题集合
	 * @param field_list	Excel文件Field标题集合
	 * @param dataList		Excel文件数据内容部分
	 * @throws Exception
	 */
	public static void createExcel(String excel_name, String[] headList,String[] fieldList, List<Map<String, Object>> dataList)
		throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headList.length; i++) {
			HSSFCell cell = row.createCell(i);								// 在索引0的位置创建单元格(左上端)
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);					// 定义单元格为字符串类型
			cell.setCellValue(headList[i]);									// 在单元格中输入一些内容
		}
		for (int n = 0; n < dataList.size(); n++) {
			HSSFRow row_value = sheet.createRow(n + 1);						// 在索引1的位置创建行(最顶端的行)
			Map<String, Object> dataMap = dataList.get(n);
			for (int i = 0; i < fieldList.length; i++) {
				HSSFCell cell = row_value.createCell(i);					// 在索引0的位置创建单元格(左上端)
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);				// 定义单元格为字符串类型
				cell.setCellValue(StringUtil.objToString(dataMap.get(fieldList[i])));	// 在单元格中输入一些内容
			}
		}
		FileOutputStream fOut = new FileOutputStream(excel_name);
		workbook.write(fOut);
		fOut.flush();
		fOut.close();
	}

	/**
	 * 功能：导出为excel(参数以list)
	 * @param excel_name	生成的Excel文件路径+名称
	 * @param headList		Excel文件Head标题集合
	 * @param field_list	Excel文件Field标题集合
	 * @param dataList		Excel文件数据内容部分
	 */
	public static void createExcel(String excel_name, List<String> headList,List<String> fieldList, List<Map<String, Object>> dataList)
			throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headList.size(); i++) {
			HSSFCell cell = row.createCell(i);					// 在索引0的位置创建单元格（左上端）
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);		// 定义单元格为字符串类型
			cell.setCellValue(headList.get(i));					// 在单元格中输入一些内容	
		}
		for (int n = 0; n < dataList.size(); n++) {
			HSSFRow row_value = sheet.createRow(n + 1);  	 	// 在索引1的位置创建行（最顶端的行）
			Map<String, Object> dataMap = dataList.get(n);
			for (int i = 0; i < fieldList.size(); i++) {
				HSSFCell cell = row_value.createCell(i);		// 在索引0的位置创建单元格（左上端）
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);	// 定义单元格为字符串类型
				cell.setCellValue(StringUtil.objToString(dataMap.get(fieldList.get(i))));	// 在单元格中输入一些内容
			}
		}
		FileOutputStream fOut = new FileOutputStream(excel_name);
		workbook.write(fOut);
		fOut.flush();
		fOut.close();
	}

	/**
	 * 功能：导出为excel(参数以list)
	 * @param excel_name 	生成的Excel文件路径+名称
	 * @param headList	 	Excel文件Head标题集合
	 * @param field_list	Excel文件Field标题集合
	 * @param dataList		Excel文件数据内容部分
	 */
	public static HSSFWorkbook createExcel(List<String> headList,List<String> fieldList, List<Map<String, Object>> dataList) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row = sheet.createRow(0);								// 在索引0的位置创建行（最顶端的行）
		for (int i = 0; i < headList.size(); i++) {
			HSSFCell cell = row.createCell(i);							// 在索引0的位置创建单元格（左上端）
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);				// 定义单元格为字符串类型
			cell.setCellValue(headList.get(i));							// 在单元格中输入一些内容
		}
		for (int n = 0; n < dataList.size(); n++) {
			HSSFRow row_value = sheet.createRow(n + 1);					// 在索引1的位置创建行（最顶端的行）
			Map<String, Object> dataMap = dataList.get(n);
			for (int i = 0; i < fieldList.size(); i++) {
				HSSFCell cell = row_value.createCell(i);				// 在索引0的位置创建单元格（左上端）
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);			// 定义单元格为字符串类型
				cell.setCellValue(StringUtil.objToString(dataMap.get(fieldList.get(i))));	// 在单元格中输入一些内容
			}
		}
		return workbook;
	}
	
	/**
	 * 功能：
	 * @param excel_name
	 *            生成的Excel文件路径+名称
	 * @param headList
	 *            Excel文件Head标题部分
	 * @param valueList
	 *            Excel文件数据内容部分
	 * @throws Exception
	 */
	public static void bulidExcel(String excel_name, String[] headList,	List<String[]> valueList) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row = sheet.createRow(0);						// 在索引0的位置创建行（最顶端的行）
		for (int i = 0; i < headList.length; i++) {
			HSSFCell cell = row.createCell(i);					// 在索引0的位置创建单元格（左上端）
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);		// 定义单元格为字符串类型
			cell.setCellValue(headList[i]);						// 在单元格中输入一些内容
		}
		for (int n = 0; n < valueList.size(); n++) {
			HSSFRow row_value = sheet.createRow(n + 1);			// 在索引1的位置创建行（最顶端的行）
			String[] valueArray = valueList.get(n);
			for (int i = 0; i < valueArray.length; i++) {
				HSSFCell cell = row_value.createCell(i);		// 在索引0的位置创建单元格（左上端）
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);	// 定义单元格为字符串类型	
				cell.setCellValue(valueArray[i]);				// 在单元格中输入一些内容
			}
		}
		FileOutputStream fOut = new FileOutputStream(excel_name);
		workbook.write(fOut);
		fOut.flush();
		fOut.close();
	}

	/**
	 * 功能：读取 Excel文件内容
	 * @param excel_name 生成的Excel文件路径+名称
	 * @throws Exception
	 */
	public static List<String[]> readExcel(String excel_name) throws Exception {
		List<String[]> list = new ArrayList<String[]>();					// 结果集
		HSSFWorkbook hssfworkbook = new HSSFWorkbook(new FileInputStream(excel_name));
		HSSFSheet hssfsheet = hssfworkbook.getSheetAt(0);					// 遍历该表格中所有的工作表，i表示工作表的数量 getNumberOfSheets表示工作表的总数
		for (int j = 0; j < hssfsheet.getPhysicalNumberOfRows(); j++) {
			HSSFRow hssfrow = hssfsheet.getRow(j);
			if(hssfrow!=null){
				int col = hssfrow.getPhysicalNumberOfCells();
				String[] arrayString = new String[col];							// 单行数据
				for (int i = 0; i < col; i++) {
					HSSFCell cell = hssfrow.getCell(i);
					if (cell == null) {
						arrayString[i] = "";
					} else if (cell.getCellType() == 0) {
						if (HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()) { 
							  if (HSSFDateUtil.isCellDateFormatted(cell)) {    
								  Date d = cell.getDateCellValue();    
								  DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								  arrayString[i] = formater.format(d);   
							   } else {    
							      arrayString[i] = new BigDecimal(cell.getNumericCellValue()).longValue()+"";    
							   }
						}
					} else {													// 如果EXCEL表格中的数据类型为字符串型
						arrayString[i] = cell.getStringCellValue().trim();
					}
				}
				list.add(arrayString);
			}
		}
		return list;
	}

	/**
	 * 功能： 读取 Excel文件内容(以文件方式)
	 * @param excel_name    生成的Excel文件路径+名称
	 * @throws Exception
	 */
	public static List<List<Object>> readExcelByList(String excel_name)	throws Exception {
		List<List<Object>> list = new ArrayList<List<Object>>();			 	// 结果集
		HSSFWorkbook hssfworkbook = new HSSFWorkbook(new FileInputStream(excel_name));
		HSSFSheet hssfsheet = hssfworkbook.getSheetAt(0);				  		// 遍历该表格中所有的工作表，i表示工作表的数量 getNumberOfSheets表示工作表的总数
		for (int j = 0; j < hssfsheet.getPhysicalNumberOfRows(); j++) {   		// 遍历该行所有的行,j表示行数 getPhysicalNumberOfRows行的总数
			HSSFRow hssfrow = hssfsheet.getRow(j);
			if (hssfrow != null) {
				int col = hssfrow.getPhysicalNumberOfCells();
				List<Object> arrayString = new ArrayList<Object>();				// 单行数据
				for (int i = 0; i < col; i++) {
					HSSFCell cell = hssfrow.getCell(i);
					if (cell == null) {
						arrayString.add("");
					} else if (cell.getCellType() == 0) {
						arrayString.add(new Double(cell.getNumericCellValue()).toString());
					} else {													// 如果EXCEL表格中的数据类型为字符串型
						arrayString.add(cell.getStringCellValue().trim());
					}
				}
				list.add(arrayString);
			}
		}
		return list;
	}

	/**
	 * 功能：读取 Excel文件内容(以InputStream方式)
	 * @param 	inputstream   文件流
	 * @throws  Exception
	 */
	public static List<List<Object>> readExcelByInputStream(InputStream inputstream) throws Exception {
		List<List<Object>> list = new ArrayList<List<Object>>();			// 结果集
		HSSFWorkbook hssfworkbook = new HSSFWorkbook(inputstream);
		HSSFSheet hssfsheet = hssfworkbook.getSheetAt(0);					// 遍历该表格中所有的工作表，i表示工作表的数量 getNumberOfSheets表示工作表的总数
		for (int j = 0; j < hssfsheet.getPhysicalNumberOfRows(); j++) {		// 遍历该行所有的行,j表示行数 getPhysicalNumberOfRows行的总数
			HSSFRow hssfrow = hssfsheet.getRow(j);
			if (hssfrow != null) {
				int col = hssfrow.getPhysicalNumberOfCells();
				List<Object> arrayString = new ArrayList<Object>();          // 单行数据
				for (int i = 0; i < col; i++) {
					HSSFCell cell = hssfrow.getCell(i);
					if (cell == null) {
						arrayString.add("");
					} else if (cell.getCellType() == 0) {
						arrayString.add(new Double(cell.getNumericCellValue())
								.toString());
					} else {													// 如果EXCEL表格中的数据类型为字符串型
						arrayString.add(cell.getStringCellValue().trim());
					}
				}
				list.add(arrayString);
			}
		}
		return list;
	}
	
	/**
	 * 功能：包装excel导出response，使其支持excel输出
	 * @param  codedFileName 文件名
	 * @param  request   request请求对象
	 * @param  response  response请求对象
	 */
	public static String wrapExcelExportResponse(String codedFileName,HttpServletRequest request
											  ,HttpServletResponse response) throws Exception {
		response.setContentType("application/vnd.ms-excel");
		String browse = BrowserUtils.checkBrowse(request),newtitle="",dms=DateUtil.toYyyymmddHhmmss();
		if ("MSIE".equalsIgnoreCase(browse.substring(0, 4))) {  // 根据浏览器进行转码，使其支持中文文件名
		    newtitle=java.net.URLEncoder.encode(codedFileName,"UTF-8")+dms + ".xls";
			response.setHeader("content-disposition","attachment;filename="+newtitle);
		} else {
			newtitle = new String(codedFileName.getBytes("UTF-8"),"ISO8859-1")+dms + ".xls";
			response.setHeader("content-disposition","attachment;filename=" + newtitle);
		}
		return codedFileName+dms + ".xls";
	}
	
	/**
	 * 功能：测试
	 */
	public void  test(){
		/*
		try{
			POIExcelBuilder builder = new POIExcelBuilder(new File(request.getServletContext().getRealPath(WebConstant.DAILY_REPORT_TEMPLATE_PATH)));
			DataRowSet dataRowSet = new DataRowSet();
			Page<ViewReportCompanyDailyParam> page = ManagerFactory.getInstance().getReportManager().getReportCompanyDailyPage(0,0
													  ,new DetachedCriteria().where(crateCriteriaCondition(request,reportCompanyDailyParam)).orderByDesc("id"));
			List<ViewReportCompanyDailyParam>  dailyList = page.getCollection();
			if(dailyList != null && dailyList.size() > 0){
				int size = dailyList.size();
				for(int i=0;i<size;i++){
					ViewReportCompanyDailyParam rd = dailyList.get(i);
					IRow iRow = dataRowSet.append();
					iRow.set("no",i+1);
					iRow.set("accountNo", rd.getAccountNo());
					iRow.set("chineseName",rd.getChineseName());
					iRow.set("nationality",RegionUtil.getRegionById(rd.getNationality()));
					iRow.set("createAccountDate",rd.getCreateAccountDate());
					iRow.set("mdeposit",rd.getMdeposit());
					iRow.set("withdraw",rd.getWithdraw());
					iRow.set("tranin",rd.getTranin());
					iRow.set("tranout",rd.getTranout());
					iRow.set("commission",rd.getCommission());
					iRow.set("profit",rd.getProfit());
					iRow.set("balance",rd.getBalance());
				}
				ViewReportCompanyDailyParam total = page.getTotal();
				builder.put("rowSet", dataRowSet);
				builder.put("totalMdeposit", total.getMdeposit());
				builder.put("totalWithdraw", total.getWithdraw());
				builder.put("totalTranin", total.getTranin());
				builder.put("totalTranout", total.getTranout());
				builder.put("totalProfit", total.getProfit());
				builder.put("totalCommission", total.getCommission());
				builder.put("totalBalance", total.getBalance());
			}else{
				builder.put("rowSet", null);
				builder.put("totalMdeposit", "0.00");
				builder.put("totalWithdraw", "0.00");
				builder.put("totalTranin", "0.00");
				builder.put("totalTranout", "0.00");
				builder.put("totalProfit", "0.00");
				builder.put("totalCommission", "0.00");
				builder.put("totalBalance", "0.00");
			}
			builder.parse();
			//对response处理使其支持Excel
			ExcelUtil.wrapExcelExportResponse("日报表", request, response);
			builder.write(response.getOutputStream());
		}catch(Exception e){
			logger.error("<<method:exportExcelDailyReport()|daily report export error!",e);
		}
		*/
	}
}
