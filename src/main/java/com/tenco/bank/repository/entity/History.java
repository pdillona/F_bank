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
	private Integer wAccountId;
	private Integer dAccountId;
	private Long wBalance;
	private Long dBalance;
	private Timestamp createdAt;
	
}
