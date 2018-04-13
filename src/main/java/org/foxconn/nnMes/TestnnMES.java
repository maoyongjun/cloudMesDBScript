package org.foxconn.nnMes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.foxconn.MdDocument.DocumentUtil;
import org.foxconn.MdDocument.MdDocument;
import org.foxconn.dao.AllTableDao;
import org.foxconn.entity.AllTable;
import org.foxconn.table.AbstractTableBuilder;
import org.foxconn.table.Table;
import org.foxconn.table.TableBuilder;
import org.foxconn.table.TableFactory;

public class TestnnMES {
	
	public static void main(String[] args) {
		Logger logger = Logger.getLogger(TestnnMES.class);
		long l= new Date().getTime();
		TableFactory factory= new TableBuilder();
		
		AllTableDao allTableDao =((AbstractTableBuilder)factory).getAllTableDao();
		List<AllTable> list = allTableDao.findAll();
		List<Table> tables = new ArrayList<Table>();
		for(AllTable alltable:list ){
			Table table = factory.build(alltable.getTABLE_NAME());
			tables.add(table);
		}
		DocumentUtil util = new DocumentUtil();
		List<MdDocument> documents = util.getMdDocumentsByPart(10, tables);
		Collections.sort(documents);
		StringBuilder stringBuilderpart1 = new StringBuilder(
					"**數據庫建制**\n\n\t第一部份：表格匯總\n\t第二部份：各個表格的詳細信息\n\n"
			  		+ "**第一部份：表格匯總**\n\n| 序號 | 表名 | 描述 |\n"
					+"|---|---|---|\n");
		StringBuilder stringBuilderpart2 = new StringBuilder("\n\n**第二部份：各個表格的詳細信息**\n\n");
		StringBuilder sYNONYMSStr = new StringBuilder("");
		for(MdDocument document:documents){
			stringBuilderpart1.append(document.getDocument(MdDocument.SIMPLETOTAL));
			stringBuilderpart2.append(document.getDocument(MdDocument.WHOLEDETAIL));
			sYNONYMSStr.append(document.getDocument(MdDocument.SYNONYMS));
		}
		logger.info(stringBuilderpart1);
		logger.info(stringBuilderpart2);
		FileUtil.writeStringToDisk(stringBuilderpart1.toString()+stringBuilderpart2.toString());
		FileUtil.writeStringToDisk("sql\\all_SYNONYMS.sql", sYNONYMSStr.toString());
		logger.info("\n--------->taketime"+(new Date().getTime()-l));
	}
}
