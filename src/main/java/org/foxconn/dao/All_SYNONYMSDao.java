package org.foxconn.dao;

import java.util.List;

import org.foxconn.entity.ALL_SYNONYMS;


public interface All_SYNONYMSDao
{
    public  List<ALL_SYNONYMS> findAll();

	public ALL_SYNONYMS findByName(String tableName);
 
}