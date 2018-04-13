package org.foxconn.nnMes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
		public static boolean writeStringToDisk(String xmlString){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String filename = "數據庫建制.md";
			return writeStringToDisk(filename,xmlString);
		}
		public static  boolean writeStringToDisk(String filename,String xmlString){
			if(null==xmlString||"".equals(xmlString)){
				return false;
			}
			FileWriter fileWriter=null;
			try {
//				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
				String baseLocalDir = "C:\\Users\\nnfoxconn\\Desktop\\cloudMesPerson\\MES_Project.document\\數據庫建制";
				File file = new File(baseLocalDir+"\\sql");
				if(!file.exists()){
					file.mkdirs();
				}
				fileWriter = new FileWriter(baseLocalDir+"\\"+filename);
				fileWriter.write(xmlString);
				fileWriter.flush();
				fileWriter.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
			} finally{
				try {
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} 
			return false;
		}
}
