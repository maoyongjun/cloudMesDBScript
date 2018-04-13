package org.foxconn.nnMes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.foxconn.dao.AllColumnsDao;
import org.foxconn.dao.AllIndexDao;
import org.foxconn.dao.AllTableDao;
import org.foxconn.dao.All_SYNONYMSDao;
import org.foxconn.entity.ALL_SYNONYMS;
import org.foxconn.entity.AllColumns;
import org.foxconn.entity.AllIndex;
import org.foxconn.entity.AllTable;
import org.foxconn.entity.TableDesc;
import org.foxconn.util.ContextUtil;

public class TestnnMESBackup {
	public static void main(String[] args) {
		AllColumnsDao allColumnsDao= ContextUtil.getContext().getBean("allColumnsDao",AllColumnsDao.class);
		AllIndexDao allIndexDao = ContextUtil.getContext().getBean("allIndexDao",AllIndexDao.class);
		AllTableDao allTableDao = ContextUtil.getContext().getBean("allTableDao",AllTableDao.class);
		All_SYNONYMSDao all_SYNONYMSDao = ContextUtil.getContext().getBean("all_SYNONYMSDao",All_SYNONYMSDao.class);
		TestnnMESBackup test = new TestnnMESBackup();
		test.testQuery(allColumnsDao,allIndexDao,allTableDao,all_SYNONYMSDao);
	}
	private void testQuery(final AllColumnsDao allColumnsDao,final AllIndexDao allIndexDao,final AllTableDao allTableDao,final All_SYNONYMSDao all_SYNONYMSDao){
		final CountDownLatch cd = new CountDownLatch(4);
		long totalTime = new Date().getTime();
		final List<AllTable> alltables = allTableDao.findAll();
		final Map<String,List<AllColumns>> allTableColumns = new HashMap<String, List<AllColumns>>();
		final Map<String,List<AllIndex>> allTableIndexs = new HashMap<String, List<AllIndex>>();
		final Map<String,Map<String,TableDesc>> allTableDesc = new HashMap<String, Map<String,TableDesc>>();
		for(AllTable  model:alltables){
			allTableColumns.put(model.getTABLE_NAME(), new ArrayList<AllColumns>());
			allTableIndexs.put(model.getTABLE_NAME(), new ArrayList<AllIndex>());
			allTableDesc.put(model.getTABLE_NAME(), new HashMap<String,TableDesc>());
		}
		//多线程
		
		//1、列名信息
		Runnable runnale1 = new Runnable() {
			public void run() {
				for(AllColumns model:allColumnsDao.findAll()){
//					System.out.println(model);
					List<AllColumns> thisTableColumns = allTableColumns.get(model.getTABLE_NAME());
					if(null==thisTableColumns)continue;
					thisTableColumns.add(model);	
					HashMap<String,TableDesc> tableDescMap = (HashMap<String, TableDesc>) allTableDesc.get(model.getTABLE_NAME());
					TableDesc tableDesc = new TableDesc();
					tableDesc.setColumn(model.getCOLUMN_NAME());
					tableDesc.setIsIndex("N");
					tableDesc.setIsPrimaryKey("N");
					if(model.getDATA_TYPE().equalsIgnoreCase("DATE")||model.getDATA_TYPE().equalsIgnoreCase("NUMBER")){
						tableDesc.setType(model.getDATA_TYPE());
					}else{
						tableDesc.setType(model.getDATA_TYPE()+"("+model.getDATA_LENGTH()+")");
					}
					tableDesc.setSortno(allTableDesc.get(model.getTABLE_NAME()).size()+1);
					tableDesc.setDesc(model.getCOMMENTS());
					tableDescMap.put(model.getCOLUMN_NAME(),tableDesc);
				}
				
				cd.countDown();
			}
		};
		//2、索引信息
		Runnable runnale2 = new Runnable() {
			public void run() {
				for(AllIndex  model:allIndexDao.findAll()){
//					System.out.println(model);
					List<AllIndex> thisTableIndexs = allTableIndexs.get(model.getTABLE_NAME());
					if(null==thisTableIndexs)continue;
					thisTableIndexs.add(model);	
					
					TableDesc tableDesc = allTableDesc.get(model.getTABLE_NAME()).get(model.getCOLUMN_NAME());
					
					if(null!=tableDesc){
						if(model.getINDEX_NAME().startsWith("PK_")){
							tableDesc.setIsPrimaryKey("Y");
						}else{
							tableDesc.setIsIndex("Y");
						}
					}
				}
				cd.countDown();
			}
		};
		
		 final StringBuilder totalTable = new StringBuilder("**數據庫建制**\n\n\t第一部份：表格匯總\n\t第二部份：各個表格的詳細信息\n\n"
			  		+ "**第一部份：表格匯總**\n\n| 序號 | 表名 | 描述 |\n"+
						"|---|---|---|\n");
		Runnable runnale3 = new Runnable() {
			
			public void run() {
				int index =1;
				
				 for(AllTable  model:allTableDao.findAllOrderbyTableName()){
					 totalTable.append("|"+String.valueOf(index) +"|"+model.getTABLE_NAME()+"|"+model.getDESCRIPITION()+"|\n");
					 index++;
				 }
				 cd.countDown();
			}
		};
		final StringBuilder all_SYNONYMSresult = new StringBuilder("");
		Runnable runnale4 = new Runnable() {
			
			public void run() {
				
		        for (ALL_SYNONYMS all_SYNONYMS:all_SYNONYMSDao.findAll())
		        {
		                all_SYNONYMSresult.append(" CREATE OR REPLACE PUBLIC SYNONYM "+ all_SYNONYMS.getSYNONYM_NAME()+ " FOR "+ all_SYNONYMS.getTABLE_OWNER()+ "."+ all_SYNONYMS.getSYNONYM_NAME()+ ";\n");

		        }
		        FileUtil.writeStringToDisk("sql\\all_SYNONYMS.sql", all_SYNONYMSresult.toString());
		        cd.countDown();
			}
		};
		ExecutorService pool = Executors.newFixedThreadPool(8);
		pool.execute(runnale1);
		pool.execute(runnale2);
		pool.execute(runnale3);
		pool.execute(runnale4);
		try {
			cd.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		final CountDownLatch cd1 = new CountDownLatch(2);
		final StringBuilder result1 = new StringBuilder("");
		Runnable runnale5 = new Runnable() {
			public void run() {
				result1.append(getDoucumentString(0, 4, alltables, allTableColumns, allTableIndexs, allTableDesc));
				cd1.countDown();
			}
		};
		final StringBuilder result2 = new StringBuilder("");
		Runnable runnale6 = new Runnable() {
			public void run() {
				result2.append(getDoucumentString(1, 4, alltables, allTableColumns, allTableIndexs, allTableDesc));		
				cd1.countDown();
			}
		};
		final StringBuilder result3 = new StringBuilder("");
		Runnable runnale7 = new Runnable() {
			public void run() {
				result3.append(getDoucumentString(2, 4, alltables, allTableColumns, allTableIndexs, allTableDesc));		
				 cd1.countDown();
			}
		};
		final StringBuilder result4 = new StringBuilder("");
		Runnable runnale8 = new Runnable() {
			public void run() {
				result4.append(getDoucumentString(3, 4, alltables, allTableColumns, allTableIndexs, allTableDesc));
				cd1.countDown();
			}
		};
		pool.execute(runnale5);
		pool.execute(runnale6);
		pool.execute(runnale7);
		pool.execute(runnale8);
		try {
			cd1.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		FileUtil.writeStringToDisk(totalTable.toString()+"\n\n**第二部份：各個表格的詳細信息**\n\n" +result1.toString()+result2.toString() +result3.toString()+result4.toString());
		
		totalTime = new Date().getTime() - totalTime;
		System.out.println(totalTime);
		pool.shutdown();

	}
	
	private String getDoucumentString(int i,int n,List<AllTable> alltables, Map<String,List<AllColumns>> allTableColumns,Map<String,List<AllIndex>> allTableIndexs,Map<String,Map<String,TableDesc>> allTableDesc){
		StringBuilder result = new StringBuilder("");
		int index =alltables.size()*i/n+1;
		for(int j=alltables.size()*i/n;j<alltables.size()*(i+1)/n;j++){
			AllTable  model = alltables.get(j);
			result.append("\n**"+String.valueOf(Integer.valueOf(index++))+"、"+model.getTABLE_NAME().trim()+"&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;"+model.getDESCRIPITION().trim()+"**\n");
			result.append(createTableDesc(allTableDesc.get(model.getTABLE_NAME())));
			result.append(createTableSql(allTableColumns.get(model.getTABLE_NAME()),model.getTABLESPACE_NAME()));
			result.append(createIndexSql(allTableIndexs.get(model.getTABLE_NAME())));
			FileUtil.writeStringToDisk("sql\\"+model.getTABLE_NAME()+".sql",createTableSql(allTableColumns.get(model.getTABLE_NAME()),model.getTABLESPACE_NAME())+createIndexSql(allTableIndexs.get(model.getTABLE_NAME())));
		}		
		return result.toString();
	}
	
	private String createTableSql(List<AllColumns> list,String nameSpace){
		StringBuilder sql =new StringBuilder( "\n\tcreate table "+ list.get(0).getOWNER()+"."+list.get(0).getTABLE_NAME()+"\n"+
				"\t("+"\n");
		
		for(AllColumns model:list){
			sql.append( model.getColumnSql()+"\n");
		}
		sql.deleteCharAt(sql.length()-1);
		sql.deleteCharAt(sql.length()-1);
		sql.append("\n");
		sql.append("\t) "+"\n");
		sql.append("\ttablespace "+nameSpace+";"+"\n");   
		sql.append("\tCREATE OR REPLACE PUBLIC SYNONYM "+list.get(0).getTABLE_NAME()+" FOR "+list.get(0).getOWNER()+"."+list.get(0).getTABLE_NAME()+";"+"\n");  
	    
		for(AllColumns model:list){
			sql.append( model.getCommentSql());
		}
//		alter table SFCBASE.C_OBA add constraint PK_C_OBA primary key (ID) using index tablespace MESBASE;
		return sql.toString();
	}
	
	private String createIndexSql(List<AllIndex> list){
		StringBuilder sql =new StringBuilder();
		String tempStr ="";
		for(AllIndex  model:list){
			
			if(model.getINDEX_NAME().startsWith("PK_")){
				tempStr = "alter table "+model.getOWNER()+"."+model.getTABLE_NAME()+" add constraint "+model.getINDEX_NAME()+" primary key(";
				if(sql.indexOf(tempStr)!=-1){
					int startIndex = sql.lastIndexOf(tempStr);
					int length = tempStr.length();
					sql.replace(startIndex, startIndex+length, tempStr+model.getCOLUMN_NAME()+",");
					
				}else{
					sql.append("\t"+tempStr+model.getCOLUMN_NAME()+") using index tablespace "+model.getTABLESPACE_NAME()+";"+"\n");
				}
			}
			else if(model.getINDEX_NAME().startsWith("UNQ_"))
            {
 
                    tempStr = "alter table " + model.getOWNER() + "." + model.getTABLE_NAME() + " Add constraint " + model.getINDEX_NAME() + " unique(";
                    if (sql.indexOf(tempStr) != -1)
                    {
                        int startIndex = sql.lastIndexOf(tempStr);
                        int length = tempStr.length();
                        sql = new StringBuilder(sql.replace(startIndex, startIndex+length, tempStr + model.getCOLUMN_NAME() + ","));

                    }
                    else
                    {
                        sql.append("\t" + tempStr + model.getCOLUMN_NAME() + ") using index tablespace " + model.getTABLESPACE_NAME() + ";" + "\n");
                    }
                }
			else if(model.getINDEX_NAME().startsWith("IDX_")){
				tempStr ="CREATE INDEX "+model.getOWNER()+"."+model.getINDEX_NAME()+" ON " +model.getOWNER()+"."+model.getTABLE_NAME()+"(";
				if(sql.indexOf(tempStr)!=-1){
					int startIndex = sql.lastIndexOf(tempStr);
					int length = tempStr.length();
					sql.replace(startIndex, startIndex+length, tempStr+model.getCOLUMN_NAME()+",");
					
				}else{
					sql.append("\t"+tempStr+model.getCOLUMN_NAME()+")"+" TABLESPACE "+model.getTABLESPACE_NAME()+";" +"\n");
				}
					
				
			}
			
		}
		return sql.toString();
	}
	private String createTableDesc(Map<String,TableDesc> map){
		StringBuilder result =new StringBuilder("\n| 列名 | 類型 | 主鍵  | 索引 | 描述\n|---|---|---|---|---|\n");
		Map<String,TableDesc> sortTableDesc = new HashMap<String,TableDesc>();
		
		for(Map.Entry<String, TableDesc> entry :map.entrySet()){
			sortTableDesc.put(String.valueOf(Integer.valueOf(entry.getValue().getSortno())), entry.getValue());
		}
				
		for(int i=0;i<sortTableDesc.size();i++){
			TableDesc tableDesc= sortTableDesc.get(String.valueOf(Integer.valueOf(i+1)));
			if(null!=tableDesc){
				result.append(tableDesc.getStringForTable()+"\n");
			}
		}
		
		return result.toString();
	}
}
