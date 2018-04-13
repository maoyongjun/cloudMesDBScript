package org.foxconn.table;

import org.foxconn.dao.AllColumnsDao;
import org.foxconn.dao.AllIndexDao;
import org.foxconn.dao.AllTableDao;
import org.foxconn.dao.All_SYNONYMSDao;
import org.foxconn.util.ContextUtil;
import org.springframework.context.ApplicationContext;

public abstract class AbstractTableBuilder {
	All_SYNONYMSDao all_SYNONYMSDao;
	AllColumnsDao allColumnsDao;
	AllIndexDao allIndexDao;
	AllTableDao allTableDao;
	private ApplicationContext ac;

	public AbstractTableBuilder() {
		ac =  ContextUtil.getContext();
		allColumnsDao =ac.getBean("allColumnsDao", AllColumnsDao.class);
		allIndexDao = ac.getBean("allIndexDao", AllIndexDao.class);
		allTableDao = ac.getBean("allTableDao", AllTableDao.class);
		all_SYNONYMSDao = ac.getBean("all_SYNONYMSDao", All_SYNONYMSDao.class);

	}
	public AllTableDao getAllTableDao(){
		return allTableDao;
	}
	public Table build(String tableName){
		Table table = new Table(tableName);
		table.setAll_SYNONYMSDao(all_SYNONYMSDao);
		table.setAllColumnsDao(allColumnsDao);
		table.setAllTableDao(allTableDao);
		table.setAllIndexDao(allIndexDao);
		return table;
		
	}


}