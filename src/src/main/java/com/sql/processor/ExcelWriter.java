package com.sql.processor;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {
	private List<String> headerRow;
	private Map<String,Map<String,String>> insertRows;
	FileOutputStream fileOut;
	HSSFWorkbook workbook ;
	HSSFSheet worksheet;
	int rowNum;
	
	public Map<String, Map<String, String>> getInsertRows() {
		return insertRows;
	}
	public void setInsertRows(Map<String, Map<String, String>> insertRows) {
		this.insertRows = insertRows;
	}
	public void initHeader(){
		headerRow = new ArrayList<String>();
		headerRow.add(Constants.COLUMN1);
		headerRow.add(Constants.COLUMN2);
	}
	public void writeExcel(){
		if(insertRows!=null && !insertRows.isEmpty()){
			Set<String> rows = insertRows.keySet();

			for(String s:rows){
				Map<String,String> row = insertRows.get(s);
			}
		}
	}
	public void initXls(){
		try{
		 fileOut = new FileOutputStream("D:\\test.xls");
		 workbook = new HSSFWorkbook();
		 worksheet = workbook.createSheet("SQL Analysis");
		 //Header Row inserting
		 initHeader();
		 HSSFRow row = worksheet.createRow(0);
		 int cellnum=0;
		 for(String s:headerRow){
			 Cell cell =row.createCell(cellnum++);
			 cell.setCellValue((String)s);
		 }
		 workbook.write(fileOut);
		 fileOut.flush();
		 fileOut.close();

		}catch(Exception e ){
			
		}
	}
}
