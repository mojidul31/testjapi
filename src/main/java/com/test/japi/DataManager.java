package com.test.japi;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.test.japi.services.v1.User;

public class DataManager {
	
	private static final Logger log = Logger.getLogger(DataManager.class.getName());
	
	private static DB apiDB;
	private static DBCollection userCollection;
	
	private static DataManager INSTANCE;
	
	public static DataManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new DataManager();
		}
		return INSTANCE;
	}
	
	private DataManager() {
		
		try {
			MongoClient mongoClient = new MongoClient(new ServerAddress("localhost",27017));
			apiDB = mongoClient.getDB("api");
			userCollection = apiDB.getCollection("users");
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			log.info("DataManager - Database Not Found");
		}
		
	}
	
	public User insertUser(User user) {
		
		BasicDBObject doc = new BasicDBObject();
		doc.put("name", user.getName());
		//insert into collection
		userCollection.insert(doc);
		//put new user id into user object
		user.setId(doc.get("_id").toString());
		return user;
	}
	
	public User findUserById(String userIdString) {
		if(userIdString == null)
			return null;
		try {
			DBObject searchById = new BasicDBObject("_id", new ObjectId(userIdString));
			
			DBObject userObject = userCollection.findOne(searchById);
			if(userObject != null) {
				return mapUserFromDBObject(userObject);
			}else{
				return null;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.info("DataManager - Data Not Found");
		}
		
		return null;
	}
	
	public List<User> findAllUsers(){
		List<User> users = new ArrayList<User>();
		
		try {
			DBCursor cursor = userCollection.find();
			if(cursor != null) {
				while(cursor.hasNext()) {
					BasicDBObject doc = (BasicDBObject) cursor.next();
					User item = mapUserFromDBObject(doc);
					users.add(item);					
				}
				return users;
			}
		} catch (Exception e) {			
			//e.printStackTrace();
			log.info("DataManager :: findAllUsers - All Users Data Not Found");
		}
		return null;
		
	}
	
	public User updateUser(String userId, String attribute, String name) {
		
		String updateValue = name;
		BasicDBObject doc = new BasicDBObject();
		doc.append("$set", new BasicDBObject().append(attribute, updateValue));
		DBObject searchById = new BasicDBObject("_id", new ObjectId(userId));
		userCollection.update(searchById, doc);
		
		return findUserById(userId);		
	}
	
	public String deleteUser(String userId) {
		String msg = "Failed to delete.";
		DBObject searchById = new BasicDBObject("_id", new ObjectId(userId));
		if(searchById != null) {
			userCollection.remove(searchById);
			msg = "Entity deleted successfully.";
		}
		
		return msg;		
	}
	

	private User mapUserFromDBObject(DBObject userObject) {
		User user = new User();
		
		user.setId(userObject.get("_id").toString());
		user.setName((String)userObject.get("name"));
		
		return user;
	}
}
