package org.craftedsw.tripservicekata.trip;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class TripService {

	public static final List<Trip> NO_TRIPS = Collections.emptyList();
	private final TripDaoWrapper tripDao;

	public TripService(TripDaoWrapper tripDao) {
		this.tripDao = tripDao;
	}

	public TripService() {
		this(new TripDaoWrapper());
	}

	public List<Trip> getTripsByUser(User currentUser, User otherUser) throws UserNotLoggedInException {
		validateCurrentUser(currentUser);
		return tripsOfOtherUserIfCurrentUserIsAFriend(currentUser, otherUser)
				.orElse(NO_TRIPS);
	}

	private void validateCurrentUser(User currentUser) {
		if(currentUser == null) {
			throw new UserNotLoggedInException();
		}
	}

	private Optional<List<Trip>> tripsOfOtherUserIfCurrentUserIsAFriend(User currentUser, User otherUser) {
		if(otherUser.isAFriendOf(currentUser)) {
			return of(findUserTrips(otherUser));
		}
		return empty();
	}

	private List<Trip> findUserTrips(User user) {
		return tripDao.findTripsByUser(user);
	}

}
