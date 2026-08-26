package com.centoria.jobmaroc.app;

import com.centoria.jobmaroc.dto.InputSearchAdDTO;

public interface IQueryBuilder {

	String getQuery(InputSearchAdDTO dto);
	
}
