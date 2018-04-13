package org.foxconn.dao;

import java.util.List;

import org.foxconn.entity.AllIndex;

public interface AllIndexDao {
	public List<AllIndex> findAll();

	public List<AllIndex> findByName(String tableName);
}
