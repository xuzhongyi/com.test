package com.test.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.test.persistence.line.LineMapper;
import com.test.persistence.linepo.LinePoMapper;
import com.test.pojo.Line;
import com.test.pojo.LinePo;
import com.test.pojo.ResoultData;
import com.test.service.LineService;

@Service("lineService")
public class LineServiceImpl implements LineService{

	@Resource
	LineMapper lineMapper;
	
	@Resource
	LinePoMapper linePoMapper;
	
	@Override
	public ResoultData getLineList() {
		ResoultData data=new ResoultData();
		List<Line> line=lineMapper.getLineList();
		data.setCore("true");
		data.setData(line);
		return data;
	}

	@Override
	public ResoultData getLinePo(Line line) {
		ResoultData data=new ResoultData();
		LinePo linePo=linePoMapper.getLinePo(line);
		data.setCore("true");
		data.setData(linePo);
		return data;
	}

}