package com.sync.service;

import java.util.List;
import java.util.Map;

import com.sync.mysql.model.CuxOmsIPaymentHistoryMysql;

public interface SyncService {
	/**
	 *  根据状态值查询OMS中间表流水信息
	 */
	List<CuxOmsIPaymentHistoryMysql> selectOmsPayhistoryByParms(Map<String, Object> params);
	
	/**
	 * 	将待同步的流水同步至EBS
	 */
	Map<String, Object> asyncPayHistory(List<CuxOmsIPaymentHistoryMysql> payHistoryMList);

}
