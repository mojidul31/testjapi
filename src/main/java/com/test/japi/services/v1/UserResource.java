package com.test.japi.services.v1;

import javax.ws.rs.PathParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.test.japi.BusinessManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@Path("/v1/users")
@Api(value="/users", description="Manage Users")
public class UserResource {
	
	private static final Logger log = Logger.getLogger(UserResource.class.getName());

	@GET
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Find User", notes="Get User By Id"+"<p><u>Input Parameter</u><ul><li>User Id is Required</li></ul></p>")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Success:{user profile}"),
			@ApiResponse(code=400, message="Failed:{\"error\":\"error description\",\"status\":\"FAILED\"}")
	})
	public Response getUserById(@ApiParam(value="userId",required=true,defaultValue="23456",allowableValues="",allowMultiple=false) @PathParam("userId") String userId) {
		
		//log.info("UserResource :: getUserById - called successfully.");
		
		if(userId == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Empty user\",\"status\":\"FAILED\"}").build();
		}
		
		try {
			User user = BusinessManager.getInstance().getUserById(userId);
			return Response.status(Response.Status.OK).entity(user).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("UserResource :: getUserById - error found.");
		}
		
		return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Could not found user\",\"status\":\"FAILED\"}").build();
		
	
//		User user = new User();
//		user.setId("A-01");
//		user.setName("Mojidul");
//		
//		return Response.status(Response.Status.OK).entity(user).build();
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Find All Users", notes="This service find all users.")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Success:{users: []}"),
			@ApiResponse(code=400, message="Failed:{\"error\":\"error description\",\"status\":\"FAILED\"}")
	})
	public Response getUsers() {		
		
		try {
			List<User> users = BusinessManager.getInstance().findUsers();
			UserHolder userHolder = new UserHolder();
			userHolder.setUsers(users);
			
			return Response.status(Response.Status.OK).entity(userHolder).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("UserResource :: getUsers - error found.");
		}
		
		return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Could not found user\",\"status\":\"FAILED\"}").build();
	
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="create new user", notes="This API creates a new user if not exists "+"<p><u>Input Parameter</u><ul><li>User Object is Required</li></ul></p>")
	@ApiResponses(value= {
			@ApiResponse(code=201, message="Success:{user profile}"),
			@ApiResponse(code=400, message="Failed:{\"error\":\"error description\",\"status\":\"FAILED\"}")
	})
	public Response saveUser(@ApiParam(value="new user",required=true,defaultValue="\"{\"name\":\"Mojidul\"}\"",allowableValues="",allowMultiple=false) User user) {		
		
		try {
			User newUser = BusinessManager.getInstance().addUser(user);
			
			return Response.status(Response.Status.CREATED).entity(newUser).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("UserResource :: saveUser - error found.");
		}
		
		return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Could not create user\",\"status\":\"FAILED\"}").build();
	
	}
	
	@PUT
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Edit an existing user", notes="This API updates an existing user if exists "+"<p><u>Input Parameter</u><ul><li>User Object is Required</li></ul></p>")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Success:{user profile}"),
			@ApiResponse(code=400, message="Failed:{\"error\":\"error description\",\"status\":\"FAILED\"}")
	})
	public Response editUser(@PathParam("userId") String userId, String jsonString) {		
		
		String name;
		
		try {
			Object obj = JSONValue.parse(jsonString);
			JSONObject jsonObject = (JSONObject)obj;
			name = (String) jsonObject.get("name");
		} catch (Exception e1) {			
			//e1.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Invalid user\",\"status\":\"FAILED\"}").build();
		}
		
		try {
			User user = BusinessManager.getInstance().updateUser(userId, "name", name);
			
			return Response.status(Response.Status.OK).entity(user).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("UserResource :: editUser - error found.");
		}
		
		return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Could not update user\",\"status\":\"FAILED\"}").build();
	
	}
	
	
	@DELETE
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Delete an existing user", notes="This API delete an existing user if exists "+"<p><u>Input Parameter</u><ul><li>User Id is Required</li></ul></p>")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Success:{ }"),
			@ApiResponse(code=400, message="Failed:{\"error\":\"error description\",\"status\":\"FAILED\"}")
	})
	public Response deleteUser(@PathParam("userId") String userId) {		
		
		try {
			String result = BusinessManager.getInstance().deleteUser(userId);
			Map<String, String> map = new HashMap<String, String>();
			map.put("Stats", result);
			
			return Response.status(Response.Status.OK).entity(map).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("UserResource :: deleteUser - error found.");
		}
		
		return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Could not be deleted user\",\"status\":\"FAILED\"}").build();
	
	}
	

}
