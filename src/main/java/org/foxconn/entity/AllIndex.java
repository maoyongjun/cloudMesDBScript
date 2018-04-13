package org.foxconn.entity;

public class AllIndex {
	String	TABLESPACE_NAME	;
	String	OWNER	;
	String	TABLE_OWNER	;
	String	TABLE_NAME	;
	String	COLUMN_NAME	;
	String	INDEX_NAME	;
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
	public String getTABLE_OWNER() {
		return TABLE_OWNER;
	}
	public void setTABLE_OWNER(String tABLE_OWNER) {
		TABLE_OWNER = tABLE_OWNER;
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
	public String getINDEX_NAME() {
		return INDEX_NAME;
	}
	public void setINDEX_NAME(String iNDEX_NAME) {
		INDEX_NAME = iNDEX_NAME;
	}
	@Override
	public String toString() {
		return "AllIndex [TABLESPACE_NAME=" + TABLESPACE_NAME + ", OWNER=" + OWNER + ", TABLE_OWNER=" + TABLE_OWNER
				+ ", TABLE_NAME=" + TABLE_NAME + ", COLUMN_NAME=" + COLUMN_NAME + ", INDEX_NAME=" + INDEX_NAME + "]";
	}
	
}
