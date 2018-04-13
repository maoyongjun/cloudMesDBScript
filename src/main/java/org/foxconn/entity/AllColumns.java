package org.foxconn.entity;


public class AllColumns {
	String	OWNER	;
	String	TABLE_NAME	;
	String	COLUMN_NAME	;
	String	NULLABLE	;
	String	DATA_TYPE	;
	String	DATA_LENGTH	;
	String	COMMENTS	;
	String  DATA_DEFAULT;
	String  isPK="N";
	String  isIndex="N";
	
	public String getIsPK() {
		return isPK;
	}
	


	public void setIsPK(String isPK) {
		this.isPK = isPK;
	}

	public String getIsIndex() {
		return isIndex;
	}

	public void setIsIndex(String isIndex) {
		this.isIndex = isIndex;
	}

	public String getDATA_DEFAULT() {
		return DATA_DEFAULT;
	}

	public void setDATA_DEFAULT(String dATA_DEFAULT) {
		DATA_DEFAULT = dATA_DEFAULT;
	}
	public String getStringForTable(){
		String result ="|"+COLUMN_NAME+"|"+DATA_TYPE+"("+DATA_LENGTH+")|"+isPK+"|"+isIndex+"|"+COMMENTS+"|";
		return result;
	}
	public String getColumnSql(){
		String sql="";
				
		if(getDATA_TYPE().equalsIgnoreCase("DATE")||getDATA_TYPE().equalsIgnoreCase("NUMBER")){
			sql ="\t"+getCOLUMN_NAME()+" "+getDATA_TYPE();
		}else{
			sql ="\t"+getCOLUMN_NAME()+" "+getDATA_TYPE()+"("+getDATA_LENGTH()+")" ;
		}
		if("null".equals( getDATA_DEFAULT())||null== getDATA_DEFAULT()) {
			sql +=",";
		}else if(!"".equals(getDATA_DEFAULT())){
			sql +=" default "+getDATA_DEFAULT().trim()+",";
		}else{
			sql +=",";
		}
		return sql;
	}
	
	public String getCommentSql(){
		String sql ="\tcomment on column "+getOWNER()+"."+getTABLE_NAME()+"."+getCOLUMN_NAME()+" is '"+getCOMMENTS()+"';"+"\n";
		return sql;
	}
	private String isnull(String str){
		if(null==str){
			return "";
		}else{
			return str;
		}
		
	}
	
	public String getOWNER() {
		return OWNER;
	}
	public void setOWNER(String oWNER) {
		OWNER = oWNER;
	}
	public String getTABLE_NAME() {
		return TABLE_NAME;
	}
	public void setTABLE_NAME(String tABLE_NAME) {
		TABLE_NAME = tABLE_NAME;
	}
	public String getCOLUMN_NAME() {
		return COLUMN_NAME;
	}
	public void setCOLUMN_NAME(String cOLUMN_NAME) {
		COLUMN_NAME = cOLUMN_NAME;
	}
	public String getNULLABLE() {
		return NULLABLE;
	}
	public void setNULLABLE(String nULLABLE) {
		NULLABLE = nULLABLE;
	}
	public String getDATA_TYPE() {
		return DATA_TYPE;
	}
	public void setDATA_TYPE(String dATA_TYPE) {
		DATA_TYPE = dATA_TYPE;
	}
	public String getDATA_LENGTH() {
		return DATA_LENGTH;
	}
	public void setDATA_LENGTH(String dATA_LENGTH) {
		DATA_LENGTH = dATA_LENGTH;
	}
	public String getCOMMENTS() {
		return isnull(COMMENTS).replaceAll("\\n", " ").trim();
	}
	public void setCOMMENTS(String cOMMENTS) {
		COMMENTS = cOMMENTS;
	}
	@Override
	public String toString() {
		return "AllColumns [OWNER=" + OWNER + ", TABLE_NAME=" + TABLE_NAME + ", COLUMN_NAME=" + COLUMN_NAME + ", NULLABLE="
				+ NULLABLE + ", DATA_TYPE=" + DATA_TYPE + ", DATA_LENGTH=" + DATA_LENGTH + ", COMMENTS=" + COMMENTS
				+ "]";
	}

	
}
