package org.foxconn.entity;

public class TableDesc {

	String column;
	String type;
	String isPrimaryKey;
	String isIndex;
	String desc;
	int sortno;
	public String getStringForTable(){
		String result ="|"+getColumn()+"|"+getType()+"|"+getIsPrimaryKey()+"|"+getIsIndex()+"|"+getDesc()+"|";
		return result;
	}
	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsPrimaryKey() {
		return isPrimaryKey;
	}
	public void setIsPrimaryKey(String isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public String getIsIndex() {
		return isIndex;
	}
	public void setIsIndex(String isIndex) {
		this.isIndex = isIndex;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getSortno() {
		return sortno;
	}

	public void setSortno(int sortno) {
		this.sortno = sortno;
	}

	
	
}
