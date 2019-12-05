/**
 * 
 */
package com.example.CarPoolApp;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@EnableWebMvc
public class Handler {

	// Transaction classes go here. Autowire them.
	@Autowired
	RidePostTransaction ridePostTransaction;
	@Autowired
	PassengerRequestTransaction passengerRequestTransaction;
	@Autowired
	UserTransaction userTransaction;
	 
	String currentUserID; //This instantiation is for tests. This variable should be set by calling the login method. Can use this method to determine if a user is logged in(null = not logged in).

	@Autowired
	ObjectFactory objFactory;
	
	@GetMapping("/login")
	public String getlogin(Data data) {
		return "login";
	}

	@PostMapping("/login")
	public String postlogin(Data data) {
		if(userTransaction.verifyLogin(data.getUserid(), data.getPassword()) == true) {
			currentUserID = data.getUserid();
			return "home";
		} else {
			return "login";
		}
	}


	@RequestMapping("/") // The initial  of the website. Should have buttons for sign-up, login,						// forgot password.
	public String loadInitial() {
		return "welcome";
	}

	@GetMapping("/signup") // The sign-up  of the website.
	public String getSignUp(Data data) {
		// User enters in info to all boxes, clicks button. Button calls signup use case
		// method, then redirects to login .
		return "signUp";
	}
	@PostMapping("/signup") // The sign-up  of the website.
	public String postSignUp(Data data){
		userTransaction.saveUser(data);
		return "login";
	}

	@RequestMapping("/emailconfirmation") // The sign-up  of the website.
	public String emailConfirmation() throws ServletException, IOException {

		// User enters in info to all boxes, clicks button. Button calls signup use case
		// method, then redirects to login .
		return "emailconfirmation";// TODO Make html.
	}

	@RequestMapping("/login/forgotpassword") // The forgot password.
	public String loadForgottenPassword() throws ServletException, IOException {
		// This  will have a form where the user enters their email. Then a button.
		// Once button pressed, send email to that email.
		// In the email, the link should be to a specific  used to set a new
		// password for the account.
		// The email will also send a verification code to verify that user is who they
		// say they are on the website. //TODO where do we store this in backend? User
		// class?

		return "login";// Go back to login .
	}

	@RequestMapping("/login/changeforgotpassword/useraccount") // The change forgot password .
	public String loadChangeForgottenFassword(@RequestParam("useraccount") String useraccount){//This request param allows us to use this variable to confirm user.
		// This  will have a form where the user enters their verification code,
		// enters a form their new password, and clicks a button.
		// Once button pressed, if verification code is the same, call the change
		// forgotten password use case method, return to login.
		// If not, change verification code in User class to null, and redirect user
		// back to home. They will have to resend the verification email and repeat the
		// process.

		return "login";// Go back to login .
	}
	
	@RequestMapping("/home")//The home  of the website. Should have buttons for most use cases... EX: view all rides.
	public String loadHome(Model model) {
		if (currentUserID == null)// User isn't logged in. Shouldn't be able to access this method/.
		{
			return "login";
		}
		model.addAttribute("currentUserID", currentUserID); // This allows the html  to access the currentUserID
															// variable. Can put methods in this call too.

		return "home";
	}

	@RequestMapping("/home/viewallrides") // The viewallrides  of the website. Will show all rideposts.
	public String loadViewAllRides(Model model) {
		if (currentUserID == null)// User isn't logged in. Shouldn't be able to access this method/.
		{
			return "login";
		}
		model.addAttribute("allrideposts", viewAllRides()); // Puts arraylist of all ride posts in html .

		return "viewallrides";
	}

	@RequestMapping("/home/upcomingrides") // The viewallrides  of the website. Will show all rideposts.
	public String viewUpcomingRides(Model model) {
		if (currentUserID == null)// User isn't logged in. Shouldn't be able to access this method/.
		{
			return "login";
		}
		model.addAttribute("theUpcomingRides", viewUpcomingRides(currentUserID)); // Puts arraylist of all ride posts in
																					// html .

		return "upcomingrides";
	}

	@RequestMapping("/home/pastrides/feedback")//The feedback page for users.
	public String viewFeedback(Model model) {
		if(currentUserID == null)
		{
			return "login";
		}
		//model.addAttribute("feedback", viewUpcomingRides(currentUserID)); //Puts arraylist of all ride posts in html .
		
		return "feedback";
		}

	// ride in URL must be changed to the ridePostID
	@RequestMapping("/home/ride/passengerrequests")
	public String viewPassengerRequests(Model model) {
		if (currentUserID == null)// User isn't logged in. Shouldn't be able to access this method/.
		{
			return "login";
		}
		// model.addAttribute("passRequests", viewPassengerRequests(currentUserID,
		// ridepostid)); //Puts arraylist of all ride posts in html .

		return "passengerrequests";// TODO need to make html  for viewallrides.html.
	}

	@RequestMapping("/home/pendingrides") // The viewallrides  of the website. Will show all rideposts.
	public String viewPendingRides(Model model) {
		if (currentUserID == null)// User isn't logged in. Shouldn't be able to access this method/.
		{
			return "login";
		}
		model.addAttribute("pendingRides", viewPendingRides(currentUserID)); // Puts arraylist of all ride posts in html
																				// .

		return "pendingrides";// TODO need to make html  for viewallrides.html.
	}
	
	@RequestMapping("/home/pastrides") // The viewallrides  of the website. Will show all rideposts.
	public String viewPastRides(Model model) {
		if (currentUserID == null)// User isn't logged in. Shouldn't be able to access this method/.
		{
			return "login";
		}
		model.addAttribute("pastRides", viewPendingRides(currentUserID)); // Puts arraylist of all past ride posts in html.
		return "pastrides";// TODO need to make html  for viewallrides.html.
	}
	
	@RequestMapping("/home/pastrides/{rideostid}") //TODO need to add @param or something in parameters to access ridepostID.
	public String viewOnePastRidePost(Model model)
	{
		if (currentUserID == null)// User isn't logged in. Shouldn't be able to access this method/.
		{
			return "login";
		}
		// model.addAttribute("ridepost", ridePostTransaction.getRidePost(ridePostID)); //TODO method to view past ride from url param.
		return "viewonepastridepost"; //TODO make html.
	}

	@RequestMapping("/home/viewallrides/{ridepostid}") // A  for viewing a ridePost. DO WE WANT THIS? OR JUST BUTTON
	public String viewOneRidePost(@RequestParam("ridepostid") int ridePostID, Model model) {
														// ridePostID.
		if (currentUserID == null)// User isn't logged in. Shouldn't be able to access this method/.
		{
			return "login";
		}
		// model.addAttribute("ridepost", ridePostTransaction.getRidePost(ridePostID));
		// //TODO ^^^

		return "viewoneridepost";// TODO need to make html  for loadviewoneridepost.html.
	}

	@GetMapping("/home/viewallrides/makeridepost") // A  for making a ridePost.
	public String getMakeRidePost(Data data) {
		return "ridepost";// TODO need to make html  for making a ridepost.
	}
	
	@PostMapping("/home/viewallrides/makeridepost") // A  for making a ridePost.
	public String postMakeRidePost(Data data) {
		if (currentUserID == null)// User isn't logged in. Shouldn't be able to access this method/.
		{
			return "login";
		}
		// Have button on viewallrides  that when clicked, moves to this . User
		// will enter info into boxes. Pushes button that calls the make ridepost use
		// case method, then redirects to viewallrideposts OR viewoneridepost.
		return "ridepost"; // TODO need to make html  for making a ridepost.
	}

	@RequestMapping("/home/currentuseraccount") // The account/profile  for the currently logged in user.
	public String loadCurrentUserAccount(Model model) {
		if (currentUserID == null)// User isn't logged in. Shouldn't be able to access this method/.
		{
			return "login";
		}
		model.addAttribute("currentUser", userTransaction.getUser(currentUserID)); // This allows the html  to
																					// access the currentUserID
																					// variable. Can put methods in this
																					// call too.
		return "currentuseraccount";// TODO Make this html.
	}

	@RequestMapping("/home/currentuseraccount/deleteaccountprompt") // The "are you sure" prompt before a user deletes
																	// their account.
	public String loadDeleteAccountPrompt(Model model) {
		if (currentUserID == null)// User isn't logged in. Shouldn't be able to access this method/.
		{
			return "login";
		}
		// The user should see a prompt, and a button for yes and no.
		// If yes is clicked, then the delete account user case method is called, then
		// return to welcome .
		// If no is clicked, then return to home  OR user account .

		return "currentuseraccount";// TODO Make this html.
	}

	// TODO Make mappings/s for all use cases.

	// Mapping methods! ^^^^^
	// Logic methods! vvvvv
	// ---------Mike Devitt's method zone--------------
	public boolean signUp(String username, String password, String phoneNumber, String firstName, String lastName,
			String gender) {
		User temp = objFactory.createUser(username, password, phoneNumber, firstName, lastName, gender);
		return userTransaction.saveNewUser(temp);
	}

	public String deleteUser(String username) {

		return userTransaction.deleteAccount(username);
	}

	public void acceptedForRide(String username) {
		passengerRequestTransaction.passengerAcceptedForRide(username);
	}

	public void editProfile(String username, String phoneNumber, String firstName, String lastName, String gender) {
		userTransaction.updateProfile(username, phoneNumber, firstName, lastName, gender);
	}

	// ---------Mike Devitt's method zone--------------
	public ArrayList<RidePost> viewAllRides() {
		return ridePostTransaction.getAllPresentRidePosts();
	}

	public ArrayList<RidePost> viewAllRides(String driverGender, int driverRating, String carPreference, String cost,
			boolean luggageAllowance) {
		return ridePostTransaction.getAllPresentRidePosts(driverGender, driverRating, carPreference, cost, luggageAllowance);
	}

	public String removeRidePost(int ridePostID) // confirmation
	{
		return ridePostTransaction.removeRidePost(ridePostID);
	}

	public String cancelRide(int passengerRequestID) {
		return passengerRequestTransaction.cancelRide(passengerRequestID);
	}

	public String makePassengerRequest(int ridePostID, String passengerUsername) {
		return passengerRequestTransaction.savePassengerRequest(ridePostID, passengerUsername);
	}

	public ArrayList<RidePost> viewUpcomingRides(String username) {
		return ridePostTransaction.viewUpcomingRides(username);
	}
	
	public ArrayList<RidePost> viewPastRides(String username) 
	{
		return ridePostTransaction.viewPastRides(username);
	}

	public ArrayList<PassengerRequest> viewPendingRides(String username) {
		return passengerRequestTransaction.viewPendingRides(username);
	}

	public ArrayList<PassengerRequest> viewPassengerRequests(String username, int ridePostID) {
		return passengerRequestTransaction.viewPassengerRequests(username, ridePostID);
	}
	
	@RequestMapping("/favorites")
    public String getFavorites(Model model) {
		userTransaction.getUser(currentUserID).addToFavorites("userA");
		userTransaction.getUser(currentUserID).addToFavorites("userB");
		userTransaction.getUser(currentUserID).addToFavorites("userC");
        if(currentUserID == null)
        {
            return "loginpage";
        }
        model.addAttribute("firstName", userTransaction.getUser(currentUserID).getProfile().getfName());
        if(userTransaction.getUser(currentUserID).getFavorites().size() != 0) {
            model.addAttribute("favorites", userTransaction.getUser(currentUserID).getFavorites());
        }
        else {
        	model.addAttribute("favorites", "You have no favorites!");
        }

        return "favorites";
    }
	
	@RequestMapping("/profile")
    public String getProfile(Model model) {
        if(currentUserID == null)
        {
            return "loginpage";
        }
        
        // First Last Name
        model.addAttribute("fullName", userTransaction.getUser(currentUserID).getProfile().getfName()+" "+userTransaction.getUser(currentUserID).getProfile().getlName());
        
        // ULID
        model.addAttribute("ulid", userTransaction.getUser(currentUserID).getUserID());
        
        // Gender
        model.addAttribute("gender", userTransaction.getUser(currentUserID).getProfile().getGender());
        
        // Phone
        model.addAttribute("phone", userTransaction.getUser(currentUserID).getProfile().getPhoneNumber());
        
        // Rating
        if(userTransaction.getUser(currentUserID).getProfile().getRatings().size() != 0) {
            model.addAttribute("rating", userTransaction.getUser(currentUserID).getProfile().getRating());
        }
        else {
        	model.addAttribute("rating", "No Ratings");
        }
        
        // Comments
        if(userTransaction.getUser(currentUserID).getProfile().getComments().size() != 0) {
            model.addAttribute("comments", userTransaction.getUser(currentUserID).getProfile().getComments());
        }
        else {
        	model.addAttribute("comments", "No Comments");
        }
        
        return "profile";
    }
}
