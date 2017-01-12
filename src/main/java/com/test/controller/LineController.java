package com.test.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.pojo.Line;
import com.test.pojo.ResoultData;
import com.test.service.LineService;
import com.test.util.JsonUtil;

@Controller
@RequestMapping("/line")
public class LineController {

	@Resource
	LineService lineService;
	
	@RequestMapping("/lineList")
	@ResponseBody
	public String getLineList(){
		ResoultData data=lineService.getLineList();
		return JsonUtil.getStringByObject(data);
	}
	
	@RequestMapping("/linePo")
	@ResponseBody
	public String getLinePo(@RequestBody Line line){
		ResoultData data=lineService.getLinePo(line);
		return JsonUtil.getStringByObject(data);
	}
	
}
