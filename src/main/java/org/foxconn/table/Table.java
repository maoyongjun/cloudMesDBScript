package org.foxconn.table;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.foxconn.dao.AllColumnsDao;
import org.foxconn.dao.AllIndexDao;
import org.foxconn.dao.AllTableDao;
import org.foxconn.dao.All_SYNONYMSDao;
import org.foxconn.entity.ALL_SYNONYMS;
import org.foxconn.entity.AllColumns;
import org.foxconn.entity.AllIndex;
import org.foxconn.entity.AllTable;
import org.foxconn.nnMes.FileUtil;

public class Table implements Comparable<Table>{
	private String tableName;
	private int sortno;
	private String tableDesc;
	private String owner;
	private String tableSpace;

	private Map<String,AllColumns> allColumns;
	private List<AllColumns> allColumnsList;
	private List<AllIndex> allIndex;
	private ALL_SYNONYMS synonyms;

	private StringBuilder simpleTotal;
	private StringBuilder detailHeader;
	private StringBuilder detailBody;

	private AllColumnsDao allColumnsDao;
	private AllIndexDao allIndexDao;
	private All_SYNONYMSDao all_SYNONYMSDao;
	private AllTableDao allTableDao;
	
	Logger logger = Logger.getLogger(Table.class);
	
	public void setAllTableDao(AllTableDao allTableDao) {
		this.allTableDao = allTableDao;
	}

	public void setAllColumnsDao(AllColumnsDao allColumnsDao) {
		this.allColumnsDao = allColumnsDao;
	}

	public void setAllIndexDao(AllIndexDao allIndexDao) {
		this.allIndexDao = allIndexDao;
	}

	public void setAll_SYNONYMSDao(All_SYNONYMSDao all_SYNONYMSDao) {
		this.all_SYNONYMSDao = all_SYNONYMSDao;
	}

	public StringBuilder getSimpleTotal() {
		return simpleTotal;
	}

	public StringBuilder getDetailHeader() {
		return detailHeader;
	}

	public StringBuilder getDetailBody() {
		return detailBody;
	}

	public String getSynonyms() {
		return " CREATE OR REPLACE PUBLIC SYNONYM " + synonyms.getSYNONYM_NAME() + " FOR " + synonyms.getTABLE_OWNER()
				+ "." + synonyms.getSYNONYM_NAME() + ";\n";
	}

	private void setSynonymsInit() {
		this.synonyms = all_SYNONYMSDao.findByName(getTableName());
	}

	public void setSimpleTotalInit() {
		simpleTotal= new StringBuilder("|" + sortno + "|" + tableName + "|" + tableDesc + "|\n");
	}

	public void init(){
		setAllColumnsInit();
		setAllIndexInit();
		setSynonymsInit();
		setTableBaseInit();
		setDetailBodyInit();
		setDetailHeaderInit();
		setSimpleTotalInit();
		createSqlFile();
	}
	
	private void createSqlFile(){
		FileUtil.writeStringToDisk("sql\\"+tableName.trim()+".sql",detailBody.toString());
	}
	
	private void setTableBaseInit(){
		AllTable allTable= allTableDao.findByName(getTableName());
		setTableDesc(allTable.getDESCRIPITION());
		setSortno(Integer.valueOf(allTable.getSORTNO()));
		setOwner(allTable.getOWNER());
		setTableSpace(allTable.getTABLESPACE_NAME());
	}
	
	
	
	private void setDetailBodyInit() {
		StringBuilder result = new StringBuilder("\n\tcreate table " + owner + "." + tableName + "\n" + "\t(" + "\n");
		StringBuilder resultColumn = new StringBuilder("");
		
		for (AllColumns allColumn :allColumnsList) {
			result.append(allColumn.getColumnSql() + "\n");
			resultColumn.append(allColumn.getCommentSql());
		}
		result.deleteCharAt(result.length() - 2);
		result.append("\n\t) \n"+"\ttablespace " + tableSpace + ";" + "\n");
		result.append(
				"\tCREATE OR REPLACE PUBLIC SYNONYM " + tableName + " FOR " + owner + "." + tableName + ";" + "\n");
		result.append(createIndexSql(allIndex));
		result.append(resultColumn);
		detailBody= result;
	}

	private String createIndexSql(List<AllIndex> list){
		StringBuilder sql =new StringBuilder();
		String tempStr ="";
		for(AllIndex  model:list){
			
			String indexName = model.getINDEX_NAME();
			String owner = model.getOWNER();
			String tableName = model.getTABLE_NAME();
			String columnName = model.getCOLUMN_NAME();
			String tableSpaceName = model.getTABLESPACE_NAME();
			boolean isPk =indexName.startsWith("PK_");
			boolean isUnq=indexName.startsWith("UNQ_");
			boolean isIdx=indexName.startsWith("IDX_");
			
			if(isPk||isUnq){
				tempStr = "alter table "+owner+"."+tableName+" add constraint "+indexName;
				tempStr +=isPk?  " primary key(": " unique(";
			}else{
				tempStr ="CREATE INDEX "+owner+"."+indexName+" ON " +owner+"."+tableName+"(";
			}
			if(isPk){
				allColumns.get(columnName).setIsPK("Y");
			}else{
				allColumns.get(columnName).setIsIndex("Y");
			}
			
//			if(tableName.equals("R_WO_COLUMN_MAPPING")){
//				logger.error("--->indexname:"+indexName);
//				logger.error("--->tempStr:"+tempStr);
//			}
			if(sql.indexOf(tempStr)!=-1){
				int startIndex = sql.lastIndexOf(tempStr);
				int endIndex =  sql.indexOf(")", startIndex);//找到右括号
				//CREATE INDEX SFCRUNTIME.IDX_R_WO_TEXT0 ON SFCRUNTIME.R_WO_TEXT(AUFNR) using index tablespace RMES;
				//将右括号替换成 ，逗号 加列名 加右括号。
				sql.replace(endIndex, endIndex+1, ","+columnName+")");
				
				
			}else{
				sql.append("\t"+tempStr+columnName+")"+(isIdx?" using index":"")+" tablespace "+tableSpaceName+";"+"\n");
			}
			
		}
		return sql.toString();
	}
	
	private void setDetailHeaderInit() {
		StringBuilder stringBuilder = new StringBuilder("\n**"+sortno+"、"+tableName.trim()+"&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;"+tableDesc.trim()+"**\n");
		stringBuilder.append("| 列名 | 類型 | 主鍵  | 索引 | 描述\n"
				+ "|---|---|---|---|---|\n");
		for(AllColumns allColumn:allColumnsList){
			stringBuilder.append(allColumn.getStringForTable()+"\n");
		}
			 
		detailHeader =  stringBuilder;
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getTableSpace() {
		return tableSpace;
	}

	public void setTableSpace(String tableSpace) {
		this.tableSpace = tableSpace;
	}

	public Table(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public int getSortno() {
		return sortno;
	}

	public void setSortno(int sortno) {
		this.sortno = sortno;
	}

	public String getTableDesc() {
		return tableDesc;
	}

	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}

	public Collection<AllColumns> getAllColumns() {
		return allColumns.values();
	}

	private void setAllColumnsInit() {
		allColumnsList = allColumnsDao.findByName(getTableName());
//		Collections.sort(allColumnsList);
		Map<String,AllColumns> map = new HashMap<String,AllColumns>();
		for(AllColumns allColumn:allColumnsList){
			map.put(allColumn.getCOLUMN_NAME(), allColumn);
		}
		this.allColumns = map;
	}

	public List<AllIndex> getAllIndex() {
		return allIndex;
	}

	private void setAllIndexInit() {
		this.allIndex = allIndexDao.findByName(getTableName());
	}

	@Override
	public int compareTo(Table o) {
		return this.sortno-o.sortno;
	}

}
