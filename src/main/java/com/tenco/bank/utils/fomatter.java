package com.tenco.bank.utils;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import com.tenco.bank.repository.entity.Account;

public class fomatter implements Formatter<Account>{

	@Override
	public String print(Account object, Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account parse(String text, Locale locale) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

}
