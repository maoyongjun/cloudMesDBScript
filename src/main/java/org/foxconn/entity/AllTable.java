package org.foxconn.entity;

public class AllTable {
	String	TABLESPACE_NAME	;
	String	OWNER	;
	String	TABLE_NAME	;
	String DESCRIPITION;
	String SORTNO;
	
	
	
	public String getSORTNO() {
		return SORTNO;
	}
	public void setSORTNO(String sORTNO) {
		SORTNO = sORTNO;
	}
	public String getDESCRIPITION() {
		return DESCRIPITION;
	}
	public void setDESCRIPITION(String dESCRIPITION) {
		DESCRIPITION = dESCRIPITION;
	}
	public String getTABLESPACE_NAME() {
		return TABLESPACE_NAME;
	}
	public void setTABLESPACE_NAME(String tABLESPACE_NAME) {
		TABLESPACE_NAME = tABLESPACE_NAME;
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
	@Override
	public String toString() {
		return "AllTable [TABLESPACE_NAME=" + TABLESPACE_NAME + ", OWNER=" + OWNER + ", TABLE_NAME=" + TABLE_NAME + "]";
	}
	
}
