package com.tenco.bank.repository.entity;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class History {

	
	private Integer id;
	private Long amount;
	private Integer w_account_id;
	private Integer d_account_id;
	private Long w_balance;
	private Long d_balance;
	private Timestamp createdAt;
	
}
