package com.tenco.bank.repository.entity;

import java.sql.Timestamp;
import java.text.DecimalFormat;

import com.tenco.bank.utils.TimeUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomHistoryEntity {

	private Integer id;
	private Long amount;
	private Long balance;
	private String sender;
	private String receiver;
	private Timestamp createdAt;
	
	
	public String formatCreatedAt() {
		
		return TimeUtils.timestampToString(createdAt);
	}
	
	public String formatBalance() {
		DecimalFormat df = new DecimalFormat("#,###");
		String formatNumber = df.format(balance);
		
		return formatNumber + "원";
	}
	
}
