package com.test.japi;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import io.swagger.jaxrs.config.BeanConfig;

public class SwaggerDocumentSetup extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void init(ServletConfig config) throws ServletException{
		
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion("1.0.0");
		beanConfig.setTitle("General API");
		beanConfig.setDescription("Mojidul Islam created API");
		beanConfig.setSchemes(new String[] {"http"});
		beanConfig.setHost("localhost:8080");
		//for production use change here
		//beanConfig.setSchemes(new String[] {"https"});
		//beanConfig.setHost("www.moriom-technologies.com:8080");
		beanConfig.setBasePath("/testjapi/services");
		beanConfig.setResourcePackage("io.swagger.resources");
		beanConfig.setResourcePackage("com.test.japi.services");
		beanConfig.setScan(true);
	}
}
