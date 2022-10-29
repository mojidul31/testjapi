package com.test.japi;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.ArrayList;
import com.test.japi.services.v1.User;

public class BusinessManager{
	
	private static final Logger log = Logger.getLogger(BusinessManager.class.getName());

	
	private static BusinessManager INSTANCE = new BusinessManager();
	
	public static BusinessManager getInstance() {
		return INSTANCE;
	}
	
	private BusinessManager() {}
	
	public User getUserById(String userId) throws Exception {
		
		log.info("BusinessManager :: getUserById- Successfully Called From BusinessManager");
		//User user = new User();
		//user.setId("1001");
		//user.setName("Mojidul");
		
//		if(user.getId().equals(userId)) {
//			return user;
//		}else {
//			return new User();
//		}	
		
		User newUser = DataManager.getInstance().findUserById(userId);
		if(newUser == null) {
			throw new Exception("User Not Found.");
		}
		return newUser;
		
	}

	public List<User> findUsers() {

//		List<User> users = new ArrayList<User>();
//		User user1 = new User();
//		user1.setId("1001");
//		user1.setName("Mojidul");
//		
//		User user2 = new User();
//		user2.setId("1002");
//		user2.setName("Nayeem");
//		
//		users.add(user1);
//		users.add(user2);
		List<User> users = DataManager.getInstance().findAllUsers();
		
		return users;
	}

	public User addUser(User user) {
		//user.setId("1003");
		User newUser = DataManager.getInstance().insertUser(user);
		return newUser;
	}

	public User updateUser(String userId, String attribute, String name) throws Exception {
		
		User user = DataManager.getInstance().updateUser(userId, attribute, name);
		if(user == null) {
			throw new Exception("User Not Found.");
		}
		return user;
	}

	public String deleteUser(String userId) {
		return DataManager.getInstance().deleteUser(userId);
	}

}
