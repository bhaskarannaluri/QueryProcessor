package com.sql.processor;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.SystemUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {
	private List<String> headerRow;
	private Map<String,Map<String,String>> insertRows = new HashMap<String,Map<String,String>>();
	FileOutputStream fileOut;
	HSSFWorkbook workbook ;
	HSSFSheet worksheet;
	
	
	protected ExcelWriter(){
		
	}
	
	public Map<String, Map<String, String>> getInsertRows() {
		return insertRows;
	}
	public void setInsertRows(Map<String, Map<String, String>> insertRows) {
		this.insertRows = insertRows;
	}
	public void initHeader(){
		headerRow = new ArrayList<String>();
		headerRow.add(Constants.COLUMN2);
		headerRow.add(Constants.COLUMN1);
		headerRow.add(Constants.COLUMN3);
		headerRow.add(Constants.COLUMN4);
		headerRow.add(Constants.COLUMN5);
		headerRow.add(Constants.COLUMN6);
		headerRow.add(Constants.COLUMN7);
		headerRow.add(Constants.COLUMN8);
		headerRow.add(Constants.COLUMN9);
	}
	
	public void initXls(){
		try{
		 fileOut = new FileOutputStream("D:\\test"+Calendar.getInstance().getTimeInMillis()+".xls");
		 workbook = new HSSFWorkbook();
		 worksheet = workbook.createSheet("SQL Analysis");
		 //Font Bold style
		 CellStyle style = workbook.createCellStyle();//Create style
		 Font font = workbook.createFont();//Create font
		 font.setBoldweight(Font.BOLDWEIGHT_BOLD);//Make font bold
		 style.setFont(font);//set it to bold

		 //Header Row inserting
		 initHeader();
		 HSSFRow row = worksheet.createRow(0);
		 int cellnum=0;
		 for(String s:headerRow){
			 Cell cell =row.createCell(cellnum++);
			 cell.setCellValue((String)s);
			 cell.setCellStyle(style);
		 }
		 
		 int rownum =1;
		 if(insertRows!=null && !insertRows.isEmpty()){
				Set<String> rows = insertRows.keySet();
				System.out.print("the rows is "+rows);
				for(String s:rows){
					 HSSFRow tempRow = worksheet.createRow(rownum++);
					Map<String,String> rowItem = insertRows.get(s);
					//Reset Cellnumber
					 cellnum=0;
					 for(String str:headerRow){
						 Cell cell =tempRow.createCell(cellnum++);
						 if(rowItem.containsKey(str)){
							 cell.setCellValue((String)rowItem.get(str));
						 }else{
							 cell.setCellValue((String)"NA");
						 }
					 }
				}
			}
		 workbook.write(fileOut);
		 fileOut.flush();
		 fileOut.close();

		}catch(Exception e ){
			
		}
	}
}
