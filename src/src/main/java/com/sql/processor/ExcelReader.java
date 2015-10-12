package com.sql.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
	static void readXlsx(File inputFile) {

		System.out.println("Hello maven World");
		try 
		{
		        // Get the workbook instance for XLSX file
		        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(inputFile));
		        // Get first sheet from the workbook
		        XSSFSheet sheet = wb.getSheetAt(0);
		        Row row;
		        Cell cell;

		        // Iterate through each rows from first sheet
		        Iterator<Row> rowIterator = sheet.iterator();

		        while (rowIterator.hasNext()) 
		        {
		                row = rowIterator.next();

		                // For each row, iterate through each columns
		                Iterator<Cell> cellIterator = row.cellIterator();
		                
		                while (cellIterator.hasNext()) 
		                {
		                cell = cellIterator.next();

		                switch (cell.getCellType()) 
		                {

		                case Cell.CELL_TYPE_BOOLEAN:
		                        System.out.println(cell.getBooleanCellValue());
		                        break;

		                case Cell.CELL_TYPE_NUMERIC:
		                        System.out.println(cell.getNumericCellValue());
		                        break;

		                case Cell.CELL_TYPE_STRING:
		                        System.out.println(cell.getStringCellValue());
		                        break;

		                case Cell.CELL_TYPE_BLANK:
		                        System.out.println(" ");
		                        break;

		                default:
		                        System.out.println(cell);

		                }
		                }
		        }
		}
		catch (Exception e) 
		{
		        System.err.println("Exception :" + e.getMessage());
		}

	}
	
	static void readXls(File inputFile) 
	{
		
	try 
	{
	        // Get the workbook instance for XLS file
	        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(inputFile));
	        // Get first sheet from the workbook
	        HSSFSheet sheet = workbook.getSheetAt(0);
	        Cell cell;
	        Row row;

	        // Iterate through each rows from first sheet
	        Iterator<Row> rowIterator = sheet.iterator();
	        
	        while (rowIterator.hasNext()) 
	        {
	                row = rowIterator.next();

	                // For each row, iterate through each columns
	                Iterator<Cell> cellIterator = row.cellIterator();
	                List<String> cellValues = new ArrayList<String>();
	                while (cellIterator.hasNext()) 
	                {
			                cell = cellIterator.next();
			                String cellValue ="";
		
			                switch (cell.getCellType()) 
			                {
		
			                case Cell.CELL_TYPE_BOOLEAN:
			                        System.out.println(cell.getBooleanCellValue());
			                        cellValue = ""+cell.getBooleanCellValue();
			                        break;
		
			                case Cell.CELL_TYPE_NUMERIC:
			                        System.out.println(cell.getNumericCellValue());
			                        cellValue  = ""+cell.getNumericCellValue();
			                        break;
		
			                case Cell.CELL_TYPE_STRING:
			                        System.out.println(cell.getStringCellValue());
			                        cellValue = ""+cell.getStringCellValue();
			                        break;
		
			                case Cell.CELL_TYPE_BLANK:
			                        System.out.println(" ");
			                        cellValue = " ";
			                        break;
		
			                default:
			                        System.out.println(cell);
			                }
	                
	                cellValues.add(cellValue);
	                }
	                System.out.println("The List is "+cellValues);
	                new SQLSplitter().processExcel(cellValues);
	        }

	} 

	catch (FileNotFoundException e) 
	{
	        System.err.println("Exception" + e.getMessage());
	}
	catch (IOException e) 
	{
	        System.err.println("Exception" + e.getMessage());
	}
	}
}


