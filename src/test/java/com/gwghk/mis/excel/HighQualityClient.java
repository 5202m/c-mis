package com.gwghk.mis.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.gwghk.mis.model.ChatUserGroup;
import com.gwghk.mis.model.Member;
import com.sdk.poi.POIExcelBuilder;

/**
 * 微信优质客户导出操作
 * @author alan.wu
 * @date 2015/10/10
 */
public class HighQualityClient {
	
	/**
	 * 主函数
	 * @param arg
	 */
    public static void main (String[] arg){
    	synchToTargetExcel();
    }
    
    /**
     * 导出数据
     */
    private static void synchToTargetExcel(){
		try {
			List<HQWxUserModel> sList=getUserListFromSrcExcel(); 
	    	List<HQWxUserModel> wList=getUserListFromExcel();
	    	List<HQWxUserModel> mList=getUserListFromMember();
	    	String accoutNo="";
	    	for(HQWxUserModel u:sList){
	    		accoutNo=StringUtils.isNotBlank(u.getGtsNo())?u.getGtsNo():(StringUtils.isNotBlank(u.getMt4No())?u.getMt4No():u.getMt5No());
	    		accoutNo=accoutNo.substring(1);
	    		boolean isHasW=false;
	    		for(HQWxUserModel w:wList){
	    			if(w.getAccountNo().contains(accoutNo)){
	    				isHasW=true;
	    				u.setOpenId(w.getOpenId());
	    				u.setNickname(w.getNickname());
	    				break;
	    			}
	    		}
	    		if(!isHasW){
	    			for(HQWxUserModel m:mList){
	        			if(StringUtils.isNotBlank(m.getAccountNo())&&m.getAccountNo().contains(accoutNo)){
	        				u.setOpenId(m.getOpenId());
	        				u.setNickname(m.getNickname());
	        				break;
	        			}
	        		}
	    		}
	    	}
	    	//写数据到excel
			POIExcelBuilder builder = new POIExcelBuilder(new File("E://GTS2_Work/hq_client/target_template.xls"));
			builder.put("sList",sList);
			builder.parse();
    		builder.write(new File("E://GTS2_Work/hq_client/优质客户名单.xls"));
    		System.out.println("export 优质客户名单.xls success!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 提取用户信息（从优质客户excel文件）
     * @return
     */
    private static List<HQWxUserModel> getUserListFromSrcExcel() {
    	HSSFWorkbook hssfworkbook=null;
		try {
			hssfworkbook = new HSSFWorkbook(new FileInputStream("E://GTS2_Work/hq_client/src_user.xls"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HSSFSheet hssfsheet = hssfworkbook.getSheetAt(0);
		String gtsNo="",mt4No="",mt5No="";
		List<HQWxUserModel> uList=new ArrayList<HQWxUserModel>();
		HQWxUserModel su=null;
		for (int j = 3; j <10000; j++) {
			gtsNo=getValueNoByCell(hssfsheet.getRow(j).getCell(0));
			if("end".equals(gtsNo.toLowerCase())){
				break;
			}
			mt4No=getValueNoByCell(hssfsheet.getRow(j).getCell(1));
			mt5No=getValueNoByCell(hssfsheet.getRow(j).getCell(2));
			//System.out.println("j:"+j+";gtsNo:"+gtsNo+";mt4No:"+mt4No+";mt5No:"+mt5No);
			su=new HQWxUserModel();
	    	su.setGtsNo(gtsNo.equals("0")?"":gtsNo);
	    	su.setMt4No(mt4No.equals("0")?"":mt4No);
	    	su.setMt5No(mt5No.equals("0")?"":mt5No);
	    	uList.add(su);
		}
		return uList;
    }
    
    /**
     * 提取单元格的账号
     * @param cell
     * @return
     */
    private static String getValueNoByCell(HSSFCell cell){
    	int type=cell.getCellType();
		if(type==HSSFCell.CELL_TYPE_STRING){
			return cell.getStringCellValue().trim();
		}else if(type==HSSFCell.CELL_TYPE_NUMERIC){
			return String.valueOf((int)cell.getNumericCellValue());
		}else{
			return "";
		}
    }
    
    /**
     * 提取用户信息（从微信后台数据库导出的excel文件）
     * @return
     */
    private static List<HQWxUserModel> getUserListFromExcel() {
    	HSSFWorkbook hssfworkbook=null;
		try {
			hssfworkbook = new HSSFWorkbook(new FileInputStream("E://GTS2_Work/hq_client/wxuser.xls"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HSSFSheet hssfsheet = hssfworkbook.getSheetAt(0);
		String openId="",accountNo="",nickname="";
		List<HQWxUserModel> uList=new ArrayList<HQWxUserModel>();
    	HQWxUserModel wu=null;
    	HSSFCell firstCell=null;
		for (int j = 1; j <10000; j++) {
			firstCell=hssfsheet.getRow(j).getCell(0);
			if(firstCell==null){
				continue;
			}
			openId=getValueNoByCell(hssfsheet.getRow(j).getCell(0));
            if("end".equals(openId.toLowerCase())){
				break;
			}
			accountNo=getValueNoByCell(hssfsheet.getRow(j).getCell(1));
			nickname=getValueNoByCell(hssfsheet.getRow(j).getCell(2));
			//System.out.println("j:"+j+";openId:"+openId+";accountNo:"+accountNo+";nickname:"+nickname);
			wu=new HQWxUserModel();
	    	wu.setOpenId(openId);
	    	wu.setAccountNo(accountNo);
	    	wu.setNickname(nickname);
	    	uList.add(wu);
		}
		return uList;
    }
    
    /**
     * 提取用户信息（从pm_mis数据库导出的json文件）
     * @return
     */
    private static List<HQWxUserModel> getUserListFromMember(){
    	String str=readFile("E://GTS2_Work/hq_client/pm_mis_member.json");
    	List<Member> memberList=JSONArray.parseArray("["+str.replaceAll("_id", "id")+"]", Member.class);
    	List<HQWxUserModel> uList=new ArrayList<HQWxUserModel>();
    	HQWxUserModel wu=null;
    	for(Member m:memberList){
    		List<ChatUserGroup> ff=null;
    		if(m.getLoginPlatform()!=null && (ff=m.getLoginPlatform().getChatUserGroup())!=null){
    			for(ChatUserGroup g:ff){
    	    		   if(g.getId().equals("wechat") && g.getUserType()==0){
    	    			   wu=new HQWxUserModel();
    	    	    	   wu.setOpenId(g.getUserId());
    	    	    	   wu.setAccountNo(g.getAccountNo());
    	    	    	   wu.setNickname(g.getNickname());
    	    	    	   uList.add(wu);
    	    		   }
    	    		}
    			//System.out.println("getUserListFromMember->has member!");
    		}
    	}
    	//System.out.println("uList:"+uList.size());
    	return uList;
    }
    
    /**
     * 读取文件方法
     * @param Path
     * @return
     */
	private static String readFile(String Path){
		BufferedReader reader = null;
		String laststr = "";
		try{
			FileInputStream fileInputStream = new FileInputStream(Path);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while((tempString = reader.readLine()) != null){
				laststr += tempString;
			}
			reader.close();
		 }catch(IOException e){
				e.printStackTrace();
		 }finally{
			if(reader != null){
				try {
				  reader.close();
				} catch (IOException e) {
				  e.printStackTrace();
				}
			}
		}
		return laststr;
	}
}
