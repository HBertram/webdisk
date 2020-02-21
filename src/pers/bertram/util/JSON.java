package pers.bertram.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSON {

	private static ObjectMapper mapper = new ObjectMapper();
	
	public static String stringify(Object object){
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{error: '×ª»»Ê§°Ü'}";
		}
	}
}
