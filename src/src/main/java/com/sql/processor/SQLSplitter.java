package com.sql.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLSplitter {
	private Map<String,String> exportRowValues ; 
	
	public void processExcel(List<String> row){
		exportRowValues = new HashMap<String,String>();
		if(row!=null && !row.isEmpty()){
			exportRowValues.put(Constants.COLUMN1, row.get(0));
			splitSQL(row.get(1));
		}
	}
	public  void splitSQL(String sql){
		String s = sql.substring(0, 8).trim().toUpperCase();
		if(s.startsWith(Constants.DELETE)){
			exportRowValues.put(Constants.COLUMN2, Constants.DELETE);
		}
		System.out.println("The statement is "+s);
	}
}
