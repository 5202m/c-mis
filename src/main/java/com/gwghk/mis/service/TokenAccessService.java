package com.gwghk.mis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.model.TokenAccess;

/**
 * 摘要：Token设置Service实现
 * @author Gavin.guo
 * @date   2015年5月11日
 */
@Service
public class TokenAccessService{
	
	@Autowired
	private PmApiService pmApiService;
	
	/**
	 * 功能：tokenAccess 分页查询
	 */
	public Page<TokenAccess> getTokenAccessPage(DetachedCriteria<TokenAccess> dCriteria) {
		TokenAccess tokenAccess = dCriteria.getSearchModel();
		Page<TokenAccess> page=new Page<TokenAccess>();
		List<TokenAccess> list = pmApiService.getTokenAccessList(tokenAccess);
		if(list!=null){
			page.addAll(list);
		}
		return page;
	}

	/**
	 * 功能：根据平台-->获取token设置信息
	 */
	public TokenAccess getByPlatform(String platform){
		return pmApiService.getTokenAccessByPlatform(platform);
	}

	/**
	 * 功能：根据id-->获取token设置信息
	 */
	public TokenAccess getById(String id){
		return pmApiService.getTokenAccessById(id);
	}
	/**
	 * 功能：保存token设置信息
	 */
	public ApiResult saveTokenAccess(TokenAccess tokenAccessparam,boolean isUpdate) {
		ApiResult result=new ApiResult();
		boolean isOk=pmApiService.setTokenAccess(tokenAccessparam, isUpdate);
		return result.setCode(isOk?ResultCode.OK:ResultCode.FAIL);
	}
	
	/**
	 * 功能：删除token设置信息
	 */
	public ApiResult deleteTokenAccess(String tokenAccessIds) {
		ApiResult result=new ApiResult();
		boolean isOk=pmApiService.deleteTokenAccess(tokenAccessIds);
		return result.setCode(isOk?ResultCode.OK:ResultCode.FAIL);
	}
}
