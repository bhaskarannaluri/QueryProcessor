package com.sql.processor;

public class WriterHelper extends ExcelWriter{
	private static ExcelWriter writer;
	public static ExcelWriter getWriter(){
		if(writer==null){
			writer= new ExcelWriter();
		}
		return writer;
	}
}
