package com.sync.oraclel.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.sync.oraclel.model.CuxOmsIPaymentHistoryOracle;

@Component
public interface CuxOmsIPaymentHistoryOracleMapper {
	
	/**
	 *	根据souceId查询EBS流水信息
	 */
	List<CuxOmsIPaymentHistoryOracle> selectBySouceIds(@Param("sourceIds") String sourceIds);

	void batchInsert(List<CuxOmsIPaymentHistoryOracle> cuxOmsIPaymentHistoryOInsertList);
	
}
