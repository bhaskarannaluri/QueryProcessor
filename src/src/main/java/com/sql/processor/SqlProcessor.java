package com.sql.processor;

import java.io.File;

import org.apache.commons.lang.StringUtils;


public class SqlProcessor {

	
	public static void main(String[] args) 
	{
	        File inputFile = new File("D:\\input.xls");
	        ExcelReader. readXls(inputFile);
	        WriterHelper.getWriter().initXls();
	}

}
