package org.foxconn.MdDocument;

import java.util.Collections;
import java.util.List;

import org.foxconn.table.Table;

public class MdDocument  implements Comparable<MdDocument>{
	int sortno;
	public static final String SIMPLETOTAL="SIMPLETOTAL";
	public static final String DETAILHEADER="DETAILHEADER";
	public static final String DETAILBODY="DETAILBODY";
	public static final String WHOLEDETAIL="WHOLEDETAIL";
	public static final String SYNONYMS="SYNONYMS";
	
	private List<Table> tables;
	
	public int getSortno() {
		return sortno;
	}

	public void setSortno(int sortno) {
		this.sortno = sortno;
	}

	public MdDocument(List<Table> tables) {
		this.tables = tables;
		Collections.sort(tables);
		for(Table table:tables){
			table.init();
		}
	}

	
	
	public String getDocument(String documentType){
		StringBuilder stringBuilder = new StringBuilder("");
		for(Table table :tables){
			if(SIMPLETOTAL.equals(documentType)){
				stringBuilder.append(table.getSimpleTotal());
			}else if(DETAILHEADER.equals(documentType)){
				stringBuilder.append(table.getDetailHeader());
			}else if(DETAILBODY.equals(documentType)){
				stringBuilder.append(table.getDetailBody());
			}else if(WHOLEDETAIL.equals(documentType)){
				stringBuilder.append(table.getDetailHeader().toString()+table.getDetailBody());
			}else if(SYNONYMS.equals(documentType)){
				stringBuilder.append(table.getSynonyms());
			}else {
				System.out.println("不支持");
			}
		}
		return stringBuilder.toString();
	}

	@Override
	public int compareTo(MdDocument o) {
		return sortno - o.getSortno();
	}
}
