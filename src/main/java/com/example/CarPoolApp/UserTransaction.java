package com.example.CarPoolApp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserTransaction {

	@Autowired
	UserRepository users;
	
	Email emailSender = new Email();

	private static final Logger log = LoggerFactory.getLogger(UserTransaction.class);

	public User getUser(String userID) {

		return users.findById(userID).get();
	}
	public boolean verifyLogin(String userID, String password) {
		if(users.existsById(userID)) {
			if(users.findById(userID).get().getPassword().equals(password)) {
				return true;
			}
		}
		return false;
	}

	public String deleteAccount(String username) {

		users.deleteById(username);
		return "deleted Account " + username;
	}

	public void updateProfile(String username, String phoneNumber, String firstName, String lastName, String gender) {
		User temp = users.findById(username).get();
		String keep_pass = temp.getPassword();
		System.out.println("before:" + temp.getUserID());
		users.delete(temp);
		Profile new_profile = new Profile(firstName, lastName, phoneNumber, gender);
		User temp2 = new User(username, keep_pass, new_profile);
		users.save(temp2);
	}

	public boolean saveNewUser(User user_obj) {
		User temp = users.save(user_obj);
		if (temp.getUserID() == user_obj.getUserID()) {
			 //3
			//emailSender.emailSignUp(user_obj);
			return true;
		} else {
			return false;
		}
	}
	public void saveUser(Data data) {
		int status = 0;//Setting status to inactive. Awaiting email confirmation
		users.save(new User(data.getUserid(), data.getPassword(), data.getFirstname(), data.getLastname(), data.getGender(), data.getPhonenumber(), status));
	}
	

}
