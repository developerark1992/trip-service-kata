package org.craftedsw.tripservicekata.trip;

import org.craftedsw.tripservicekata.user.User;

import java.util.List;

public class TripDaoWrapper {

    public List<Trip> findTripsByUser(User user) {
        return TripDAO.findTripsByUser(user);
    }

}
