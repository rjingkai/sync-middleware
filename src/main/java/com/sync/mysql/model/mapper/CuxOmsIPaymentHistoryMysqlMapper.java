package com.sync.mysql.model.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sync.mysql.model.CuxOmsIPaymentHistoryMysql;

@Component
public interface CuxOmsIPaymentHistoryMysqlMapper {
	/**
	 *  根据状态值查询OMS中间表流水信息
	 */
	List<CuxOmsIPaymentHistoryMysql> selectOmsPayhistoryByParms(Map<String, Object> params);
	
	/**
	 *  批量更新OMS中间表流水的状态
	 */
	void batchUpdate(List<CuxOmsIPaymentHistoryMysql> cuxOmsIPaymentHistoryList);
}
