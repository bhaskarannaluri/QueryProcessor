package com.sql.processor;

import java.io.File;

import org.apache.commons.lang.StringUtils;


public class SqlProcessor {
	public static void main(String[] args) 
	{
	        File inputFile = new File("D:\\input.xls");
		/*if(args.length<=0){
			System.out.println("Use Correct Fomat : java -jar <jar-file-name>.jar <input-file>.xls ");
			System.exit(0);
		}
			File inputFile = new File(args[0]);*/
	        ExcelReader. readXls(inputFile);
	        WriterHelper.getWriter().initXls();
	}

}
