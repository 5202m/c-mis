package com.gwghk.cms.service;

import java.sql.Timestamp;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwghk.cms.SpringJunitTest;
import com.gwghk.mis.service.NodeAPIService;

public class NodeAPIServiceTest extends SpringJunitTest {

	
	@Autowired
    public NodeAPIService nodeAPIService;
	
	@Test
	public void test(){
		//nodeAPIService.getChatOnlineUserPage(null);
		System.out.println(new Timestamp(1428489304218210l));
	}
}