package com.whhft.sysmanage.common.base;

import java.io.Serializable;

public abstract class BaseServiceImpl<E, PK extends Serializable> implements Serializable, BaseService<E, PK>  {
	
	public abstract BaseMapper<E, PK> mapper();
	
	
	public Integer insert(E entity) {
		return mapper().insert(entity);
	}

	
	public void update(E entity) {
		mapper().updateByPrimaryKey(entity);
	}

	
	public E findOne(PK id) {
		return mapper().selectByPrimaryKey(id);
	}

	
	public void delete(PK id) {
		mapper().deleteByPrimaryKey(id);
	}
}
