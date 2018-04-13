package org.foxconn.dao;

import java.util.List;

import org.foxconn.entity.AllColumns;



public interface AllColumnsDao {
	public List<AllColumns> findAll();

	public List<AllColumns> findByName(String tableName);
}
