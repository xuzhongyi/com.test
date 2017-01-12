package com.test.service;

import com.test.pojo.Line;
import com.test.pojo.ResoultData;

public interface LineService {

	ResoultData getLineList();

	ResoultData getLinePo(Line line);

}
