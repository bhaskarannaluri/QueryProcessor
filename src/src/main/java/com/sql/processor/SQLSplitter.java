package com.sql.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.SystemOutLogger;

public class SQLSplitter {
	private Map<String,String> exportRowValues ; 
	
	public void processExcel(List<String> row){
		exportRowValues = new HashMap<String,String>();
		if(row!=null && !row.isEmpty()){
			exportRowValues.put(Constants.COLUMN2, row.get(0));
			splitSQL(row.get(1));
		}
		//Adding the values to the excelWriter insertRows
		if(!exportRowValues.isEmpty()){
			WriterHelper.getWriter().getInsertRows().put(row.get(0), exportRowValues);
			//System.out.println("Row value is "+writer.getInsertRows().get(row.get(0)));
		}
	}
	/**
	 * API which performs Scan operation 
	 * */
	public  void splitSQL(String sql){
		String s = sql.replaceAll("\"","").substring(0, 8).trim().toUpperCase();
		String st  = sql.replaceAll("\"","").trim().toUpperCase();

		if(s.startsWith(Constants.DELETE)){
			exportRowValues.put(Constants.COLUMN1, Constants.DELETE);
			splitDeleteSQL(st);
		}else if(s.startsWith(Constants.INSERT)){
			exportRowValues.put(Constants.COLUMN1, Constants.INSERT);
			splitInsertSQL(st);
		}else if(s.startsWith(Constants.UPDATE)){
			exportRowValues.put(Constants.COLUMN1, Constants.UPDATE);
			splitUpdateSQL(st);
		}
	}
	/**
	 * API to perform scan on DELETE operation
	 */
	public  void splitDeleteSQL(String s){
		//Transformation findings
		exportRowValues.put(Constants.COLUMN9, findTransformations(s));
		
		String q= s.substring(s.indexOf("FROM"));
		StringBuilder query =  new StringBuilder(q.replace("FROM",""));
		if(query.indexOf("WHERE")> 0){
			query = new StringBuilder(query.substring(0, query.indexOf("WHERE")));
		}
		System.out.println("The from statement is "+query);
		String schema ="";
		String table ="";
		String[] tables = query.toString().trim().split(",");
		
		if(tables.length>0){
			for(String st:tables){
				System.out.println("The split is "+st);
				if(st.contains(".")){
					schema = schema.concat(st.substring(0, st.indexOf("."))+"\n");
					table = table.concat(st.substring(st.indexOf(".")+1)+"\n");
				}else{
					table = table.concat(st+"\n");
				}
				System.out.println("The split is "+st.substring(0, st.indexOf(".")));
				System.out.println("The split is "+st.substring(st.indexOf(".")+1));
			}
		}
		if(StringUtils.isNotBlank(schema)){
			exportRowValues.put(Constants.COLUMN3, schema);
			System.out.println("Schema is "+schema);
		}
		if(StringUtils.isNotBlank(table)){
			exportRowValues.put(Constants.COLUMN4, table);
			System.out.println("table is "+table);

		}
	}
	public String findTransformations(String s){
		String transformation ="";
		if(s.contains("UNION ALL")){
			transformation += "UNION ALL \n"; 
		}else if(s.contains("UNION")){
			transformation += "UNION \n"; 
		}
		if(s.contains("JOIN")){
			transformation += "JOIN \n"; 
		}
		return transformation;
	}
	/**
	 * API to perform scan on Update operation
	 */
	public  void splitUpdateSQL(String sql){
		//Transformation findings
		exportRowValues.put(Constants.COLUMN9, findTransformations(sql));
		//Tables adn Schema Details 
		String st = sql.substring(0, sql.indexOf("SET"));
		st = st.replace("UPDATE", "");
		//Target columns 
		/**
		 * The Logic used is - Between SET and WHERE clause
		 */
		if(sql.contains("SET ")){
			String targetColumns = sql.substring(sql.indexOf("SET "));
			if(targetColumns.contains("WHERE ")){
				targetColumns = targetColumns.substring(0,targetColumns.indexOf("WHERE "));
			}
			System.out.println("SET statement is   "+targetColumns);

			targetColumns = targetColumns.replace("SET", "").trim();
			String targetColumn = "";
			//If simple UPdate statement without having select statement 
			if(!targetColumns.contains("SELECT")){
				
				String[] target1 = targetColumns.split(",");
				if(target1.length>0){
					for(String sp:target1){
						String[] tar = sp.split("=");
						//System.out.println("Thetarget column is "+tar[0].substring(tar[0].indexOf(".")+1));
						targetColumn = targetColumn.concat(tar[0].substring(tar[0].indexOf(".")+1)+"\n");
					}
				}
				
			}
			if(StringUtils.isNotBlank(targetColumn)){
				System.out.println("Thetarget column is "+targetColumn);
				exportRowValues.put(Constants.COLUMN5, targetColumn);
			}
		}
		
		
	}
	/**
	 * API to perform scan on Insert operation
	 */
	public  void splitInsertSQL(String sql){
		//Transformation findings
		exportRowValues.put(Constants.COLUMN9, findTransformations(sql));
		/**
		 * Finding the target Schema, Table
		 */
		String st = sql.substring(sql.indexOf("INSERT INTO"), sql.indexOf("("));
		st = st.replace("INSERT INTO", "");
		String[] schema = seperateSchema(st);
		if(schema.length>0){
			exportRowValues.put(Constants.COLUMN3, schema[0]);
			exportRowValues.put(Constants.COLUMN4, schema[1]);
		}
		/**
		 * Finding the target columns
		 */
		if(sql.contains("(") && sql.contains(")")){
			st =  sql.substring(sql.indexOf("(")+1, sql.indexOf(")"));
		}
		//st = st.replace("(", "").trim();
		String[] tarCols = seperateSchema(st);
		exportRowValues.put(Constants.COLUMN5, tarCols[1]);
		/**
		 * Finding the Source columns
		 */
		if(sql.contains("SELECT")){
			String srcCol = sql.substring(sql.indexOf("SELECT"));
			//Source Table will be defined between FROm and WHERE
			String srcTbl = srcCol.substring(srcCol.indexOf("FROM"));
			srcCol = srcCol.substring(srcCol.indexOf("SELECT"),srcCol.indexOf("FROM"));
			srcCol = srcCol.replace("SELECT", "").trim();
			String[] cols = seperateSchema(srcCol);
			exportRowValues.put(Constants.COLUMN8, cols[1]);
			
			/**
			 * Finding Source Table
			 */
			if(srcTbl.contains("WHERE")){
				srcTbl = srcTbl.substring(srcTbl.indexOf("FROM"), srcTbl.indexOf("WHERE"));
				srcTbl = srcTbl.replace("FROM", "");
				if(!srcTbl.contains("JOIN")){
					String[] src = seperateSchema(srcTbl);
					exportRowValues.put(Constants.COLUMN6, cols[0]);
					exportRowValues.put(Constants.COLUMN7, cols[1]);
				}else{
					String table1 = srcTbl.substring(0,srcTbl.indexOf("JOIN"));
					String table2 = srcTbl.substring(srcTbl.indexOf("JOIN"),srcTbl.indexOf("ON"));
					table2 = table2.replace("JOIN", "");
					String t="";
					String sc="";
					if(table1.contains(".")){
						sc = table1.substring(0, table1.indexOf("."))+"\n";
						t = table1.substring(table1.indexOf(".")+1)+"\n";
					}else{
						t = table1+"\n";
					}
					if(table2.contains(".")){
						sc = table2.substring(0, table2.indexOf("."))+"\n";
						t = table2.substring(table2.indexOf(".")+1)+"\n";
					}else{
						t = table2;
					}
					exportRowValues.put(Constants.COLUMN6, sc);
					exportRowValues.put(Constants.COLUMN7, t);
				}
			}
		}

	}
	/**
	 * If the sql contains statement like a1.tab1,a2.tab2 then it gives output as String[]{{a1 a2},{tab1 tab2}}
	 * @param sql
	 * @return
	 */
	public String[] seperateSchema(String sql){
		String[] tables = sql.toString().trim().split(",");
		String schema ="";
		//Can be used for table / Column
		String table = "";
		if(tables.length>0){
			for(String st:tables){
				System.out.println("The split is "+st);
				if(st.contains(".")){
					schema = schema.concat(st.substring(0, st.indexOf("."))+"\n");
					table = table.concat(st.substring(st.indexOf(".")+1)+"\n");
				}else{
					table = table.concat(st+"\n");
				}
			}
		}
		return new String[] {schema,table};
	}
}
