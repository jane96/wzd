package com.web.common;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author hz
 * @category 日期格式化
 */
public class CustomDateDetailSerializer extends JsonSerializer<Date> implements Serializable{
	private static final long serialVersionUID = 1L;
	@Override
	public void serialize(Date value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
		jgen.writeString(DateUtils.formatDate(value, "yyyy-MM-dd HH:mm:ss")); 
		
	}

}
