package org.foxconn.dao;

import java.util.List;

import org.foxconn.entity.AllTable;

public interface AllTableDao {
	public List<AllTable> findAll();
	public List<AllTable> findAllOrderbyTableName();
	public AllTable findByName(String tableName);
 }
