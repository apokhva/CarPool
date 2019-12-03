package com.example.CarPoolApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserTransaction {
	
	@Autowired
	UserRepository users;
	
	public User getUser(String userID)
	{
		return users.findById(userID).get();
	}
	public void verifyLogin(String userID, String password) {
		if(users.existsById(userID) && users.existsById(password)) {
			System.out.println("User: " + userID + " has been logged in.");
		} else {
			
		}
		
	}
	
	//private DatabaseQuery query;
	
//	List<User> findListByStatus(int status);
//	
//	User findUserByID(String userID);
}
