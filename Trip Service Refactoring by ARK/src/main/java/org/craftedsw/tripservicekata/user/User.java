package org.craftedsw.tripservicekata.user;

import lombok.*;
import org.craftedsw.tripservicekata.trip.Trip;

import java.util.ArrayList;
import java.util.List;

@Builder(builderMethodName = "aNewUser", buildMethodName = "create")
@NoArgsConstructor(staticName = "newUser")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

	@Singular
	private List<Trip> trips = new ArrayList<Trip>();
	@Singular
	private List<User> friends = new ArrayList<User>();
	
	public List<User> getFriends() {
		return friends;
	}
	
	public void addFriend(User user) {
		friends.add(user);
	}

	public void addTrip(Trip trip) {
		trips.add(trip);
	}
	
	public List<Trip> trips() {
		return trips;
	}

	public boolean isAFriendOf(User user) {
		return getFriends().stream()
				.anyMatch(user::equals);
	}
}
