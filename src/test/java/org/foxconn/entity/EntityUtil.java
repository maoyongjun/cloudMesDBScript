package org.foxconn.entity;

import org.junit.Test;

public class EntityUtil {
	
	

	public String getColumnType(String type){
		if ("NUMBER".equalsIgnoreCase(type)){
			return "Double";
		}else if("Date".equalsIgnoreCase(type)){
			return "Date";
		}else{
			return "String";
		}
	}
	

	public String getFunction(String type,String columnName){
		String funtionName = String.valueOf(columnName.charAt(0)).toUpperCase()+columnName.substring(1, columnName.length());
		String getFunction="Public "+type+" get"+funtionName+"("+type+" "+columnName+"){\n"
				+ "\treturn "+columnName+";\n"
				+ "}\n";
		return getFunction;
		
	}
	@Test
	public  void test() {
		System.out.println("abc123".substring(1, "abc123".length()));
		System.out.println(getFunction("Integer","name"));
	}
	
	
	
}
