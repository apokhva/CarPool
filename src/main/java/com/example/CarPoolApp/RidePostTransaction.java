package com.example.CarPoolApp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RidePostTransaction {
	
	@Autowired
	PassengerRequestTransaction passengerRequestTransaction;
	 
	@Autowired
	RidePostRepository ridePosts;
	
	@Autowired
	UserTransaction userTransaction;
	
	public boolean isRidePresent(String date) {
        //Date is in format yyyy/mm/dd
        //Tokenize the date to be checked
        StringTokenizer dateToCheck = new StringTokenizer(date, "/");

        //Get the current date
        LocalDateTime currentDate = LocalDateTime.now();

        //format the current date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String theCurrentDate = dtf.format(currentDate);

        //tokenize the current date
        StringTokenizer current = new StringTokenizer(theCurrentDate, "/");

        //check if date to check is in the past
        for(int i = 0; i < 3; i++) {
            int dateCheck = Integer.parseInt(dateToCheck.nextToken());
            int currentInt = Integer.parseInt(current.nextToken());
            if(dateCheck < currentInt) {
                return false;
            }
        }
        return true;
    }
	
	public ArrayList<RidePost> getConvertedPresentRidePosts(ArrayList<RidePost> rides)
	{
		ArrayList<RidePost> result = new ArrayList<RidePost>();
		for(RidePost ride : rides)
		{
			if(isRidePresent(ride.getTime()))
			{
				result.add(ride);
			}
		}
		return result;
	}
	
	public ArrayList<RidePost> getConvertedPastRidePosts(ArrayList<RidePost> rides)
	{
		ArrayList<RidePost> result = new ArrayList<RidePost>();
		for(RidePost ride : rides)
		{
			if(!isRidePresent(ride.getTime()))
			{
				result.add(ride);
			}
		}
		return result;
	}
	
	public RidePost getRidePost(int ridePostID)
	{
		return ridePosts.findById(ridePostID).get();
	}
	
	public ArrayList<RidePost> getAllRidePosts()
	{
		return ridePosts.findAll();
		
		//DONT REMOVE. TEST THIS METHOD WORKS FIRST.
//		Iterable<RidePost> iterable = ridePosts.findAll();
//		ArrayList<RidePost> result = new ArrayList<RidePost>();
//		iterable.forEach(result::add);
//		return result;
	}
	
	public ArrayList<RidePost> getAllPresentRidePosts()
	{
		return getConvertedPresentRidePosts(getAllRidePosts());
	}
	
	public ArrayList<RidePost> getAllRidePosts(String driverGender, int driverRating, String carPreference, String costPreference, boolean luggageAllowance)
	{
		//Get all rideposts OR find way to get very specific ridePosts from database.
		ArrayList<RidePost> rides = getAllRidePosts();
		
		//Loop through each ride post and compare to restrictions. If they match, add to result list.
		ArrayList<RidePost> result = new ArrayList<RidePost>();
		User driver;
		for( RidePost ride : rides)
		{
			driver = userTransaction.getUser(ride.getDriverUsername());
			//Compare restrictions. //1. driver gender. //2. driver rating. //3. ridePost car and carPreference. //4. ridePost cost and cost. //5. ridePost luggageAllowance and luggageAllowance.
			if((driver.getProfile().getGender().equals(driverGender) || driverGender.isEmpty() || driverGender.equals("")) && 
					(driver.getProfile().getRating()==driverRating || driverRating==-1) && 
					(ride.getCar().equals(carPreference) || carPreference.isEmpty() || carPreference.equals("")) && 
					(ride.getCost().equals(costPreference) || costPreference.isEmpty() || costPreference.equals("")) && 
					(ride.getHasLuggageAllowance()==luggageAllowance)) 
			result.add(ride);
		}
		
		return result;
	}
	
	public ArrayList<RidePost> getAllPresentRidePosts(String driverGender, int driverRating, String carPreference, String costPreference, boolean luggageAllowance)
	{
		return getConvertedPresentRidePosts(getAllRidePosts(driverGender, driverRating, carPreference, costPreference, luggageAllowance));
	}
	
	public ArrayList<RidePost> viewRelatedRides(String driverUsername){
		
		//Stores the ride posts the user has posted themselves
		ArrayList<RidePost> rides = ridePosts.findByDriverUsername(driverUsername);
		
		ArrayList<PassengerRequest> rides2 = passengerRequestTransaction.getAcceptedRequests(driverUsername);
		
		//filter out the passengerRequests to only those accepted
		for(int i = 0; i<rides2.size(); i++) {
			if(!(rides2.get(i).getWaitingAcceptedDeclined() == 2)) {
				rides2.remove(i);
			}
		}
		
		//find the corresponding ridePosts of the passengerRequests. based on ridePostID. add them to original list
		for(int i = 0; i<rides2.size(); i++) {
			Integer ID = rides2.get(i).getRidePostID();
			RidePost ridePost = ridePosts.findById(ID).get();
			rides.add(ridePost);
			
		}
		return rides;
	}
	
	public ArrayList<RidePost> viewUpcomingRides(String currentUsername)
	{
		return getConvertedPresentRidePosts(viewRelatedRides(currentUsername));
	}
	
	public ArrayList<RidePost> viewPastRides(String currentUsername){
		return getConvertedPastRidePosts(viewRelatedRides(currentUsername));		
	}
	
	public String removeRidePost(int ridePostID)
	{
		//Prompt are you sure? If yes, delete. If no, end.
		//TODO How do? Similar prompt method as other use case. 
				
		//Get all passengerRequests for ridePostID. Loop through and send emails to passengerRequestOwners.
		ArrayList<PassengerRequest> passengerRequests = passengerRequestTransaction.getAllPassengerRequests(ridePostID);
		User requestOwner;
		Email email;
		for(PassengerRequest request : passengerRequests)
		{
			requestOwner = userTransaction.getUser(request.getPassengerUsername());
			//Send email with requestOwner.
			email = new Email();
			email.emailRideCancelled(requestOwner);
			//Delete passengerRequest.
			passengerRequestTransaction.deletePassengerRequest(request.getPassengerRequestID());
		}
		
				
		//Delete ridepost.
		ridePosts.deleteById(ridePostID);
				 
		//Print confirmation.
		return "RidePost " + ridePostID + " was deleted.";
	}

	public void createRidePost(Data data) {
	ridePosts.save(new RidePost(data.getTime(),0, data.getCar(),data.getStart(),data.getEnd(),data.getCost(),data.getMaxpass(), false));
		
	}
}
