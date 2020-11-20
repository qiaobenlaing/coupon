package com.whhft.sysmanage.common.base;

import java.io.Serializable;

public interface BaseService <S, PK extends Serializable>{
	Integer insert(S entity);
	void update(S entity);
	S findOne(PK id);
	void delete(PK id);
}