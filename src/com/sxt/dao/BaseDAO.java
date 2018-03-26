package com.sxt.dao;

import java.util.List;

/**
 * 基础DAO的封装接口
 * @author Administrator
 *
 * @param <T>
 */
public interface BaseDAO<T> {
	
	void add(T bean);
	void update(T bean);
	void delete(T bean);
	List<T> query();
	T queryById(T bean);
}
