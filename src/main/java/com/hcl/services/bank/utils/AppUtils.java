package com.hcl.services.bank.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

public class AppUtils {
	private static AppUtils instance;
	
	private AppUtils() {}

	public static AppUtils getInstance() {
		return instance==null ? new AppUtils() : instance;
	}
	
	public List<String> getBindingResultToStrings(BindingResult errors){
		return errors.getAllErrors().stream().map(e -> e.getCode() + ":" + e.getDefaultMessage()).collect(Collectors.toList());
	}
	
}
