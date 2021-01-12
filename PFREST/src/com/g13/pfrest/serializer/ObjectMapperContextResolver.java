package com.g13.pfrest.serializer;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public ObjectMapperContextResolver() {
        this.mapper = createObjectMapper();
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }

    private ObjectMapper createObjectMapper() {
    	
    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(df);
//        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
        mapper.registerModule(new Hibernate5Module());

//        SimpleModule module = new SimpleModule();
//      	module.addSerializer(Geometry.class, new GeometrySerializer());
//      	module.addDeserializer(Geometry.class, new GeometryDeserializer());
//      	mapper.registerModule(module);
      	
//      	mapper.registerModule(new Jdk8Module());
//      	mapper.registerModule(new JavaTimeModule());
//      	mapper.registerModule(new ParameterNamesModule());
		
      	mapper.registerModule(new JtsModule());
		return mapper;
    }
}

