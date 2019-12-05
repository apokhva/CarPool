package com.example.CarPoolApp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class CarPoolAppApplication {
 
	private static final Logger log = LoggerFactory.getLogger(CarPoolAppApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CarPoolAppApplication.class);
	}

	@Bean
	public CommandLineRunner loadData(UserRepository repository) {
		return (args) -> {
			// save a couple of userAccounts

			repository.save(new User("Nick", "Nose", null, null, null, null, 0));
			repository.save(new User("AdeptDave", "icryeverytim", null, null, null, null, 0));
			repository.save(new User("monkey", "Banyanyas!", null, null, null, null, 0));
			repository.save(new User("cookieMonsta", "cookies", null, null, null, null, 0));
			repository.save(new User("Merry", "christmas", null, null, null, null, 0));
			
			repository.save(new User("David", "PG", null, null, null, null, 0));
			repository.save(new User("Michelle", "Please", null, null, null, null, 0));

			// fetch all userAccounts
			log.info("All users listed");
			log.info("-------------------------------");
			for (User userAccount : repository.findAll()) {
				log.info(userAccount.toString());
			}
			log.info("");

			// fetch an individual userAccount by ID
			User userAccount = repository.findById("Nick").get();
			log.info("User found in database findOne(\"Nick\"):");
			log.info(userAccount.toString());
			log.info("");

			// fetch userAccounts by last name
//			log.info("Username found with adeptdave:");
//			for (User adeptdave : repository.findByUsernameStartsWithIgnoreCase("adeptdave")) {
//				log.info(adeptdave.toString());
//			}
//			log.info("");
			
//			// Testing Favorites
//			User user = new User("sclaus", "hohoho", "Santa", "Claus", "Male", "1234567890", 0);
//			repository.save(user);
//			User user2 = new User("sclaus2", "hohoho", "Santa2", "Claus2", "Male", "1234567890", 0);
//			repository.save(user2);
//			User user3 = new User("sclaus3", "hohoho", "Santa3", "Claus3", "Male", "1234567890", 0);
//			repository.save(user3);
//			log.info("TESTING FEATURE: Favorites");
//			log.info("--------------------------");
//			user.addToFavorites(user2.getUserID());
//			user.addToFavorites(user3.getUserID());
//			user.addToFavorites(user2.getUserID());
//			List<String> favorites = user.getFavorites();
//			log.info("User Favorites:");
//			for (String temp : favorites) {
//				System.out.println(temp);
//			}
//			
//			user.removeFromFavorites(user2.getUserID());
//			log.info("User Favorites Updated:");
//			for (String temp : favorites) {
//				System.out.println(temp);
//			}
//			log.info("--------------------------");
		};
	}
	


//	//Testing email notifications.
//	@EventListener(ApplicationReadyEvent.class)
//	public void doSomethingAfterStartup() {
//	    //User user = new User("ULID", "PASSWORD", "FIRSTNAME", "LASTNAME", "GENDER", "PHONENUMBER", INT STATUS);
//		// DEFAULT INT STATUS: 0 (ACTIVE ACCOUNT)
//		// 					   1 (INACTIVE ACCOUNT)
//	    User user = new User("sclaus", "hohoho", "Santa", "Claus", "Male", "1234567890", 0);
//		Email email = new Email();
//		email.emailReminder(user);
//		email.emailPassengerCancelled(user);
//		email.emailRideCancelled(user);
//		email.emailSignUp(user);
//	}


	// Testing hibernate stuff with logger.
//	private static final Logger log = LoggerFactory.getLogger(CarPoolAppApplication.class);

	// public User(String userID, String password, int status, String phoneNumber,
	// String fName, String lName)

//	@Bean
//	  public CommandLineRunner demo(UserTransaction repository) {
//	    return (args) -> {
//	      // save a few users
//	      repository.save(new User("Jack", "Bauer", 4));
//	      repository.save(new User("Chloe", "O'Brian", 7));
//	      repository.save(new User("Kim", "Bauer", 1));
//	      repository.save(new User("David", "Palmer", 3));
//	      repository.save(new User("Michelle", "Dessler", 1));
//
//	      // fetch all users
//	      log.info("Users found with findAll():");
//	      log.info("-------------------------------");
//	      for (User user : repository.findAll()) {
//	        log.info(user.toString());
//	      }
//	      log.info("");
//
//	      // fetch an individual user by ID
//	      User user = repository.findUserByID("Bauer");
//	      log.info("User found with findUserByID(\"Bauer\"):");
//	      log.info("--------------------------------");
//	      log.info(user.toString());
//	      log.info("");
//
//	      // fetch users by status
//	      log.info("Customer found with findListByStatus(3):");
//	      log.info("--------------------------------------------");
//	      repository.findListByStatus(3).forEach(bauer -> {
//	        log.info(bauer.toString());
//	      });
//	      log.info("");
//	    };
//	  }

}
