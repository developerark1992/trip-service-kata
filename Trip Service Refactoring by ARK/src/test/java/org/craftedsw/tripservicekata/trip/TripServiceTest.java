package org.craftedsw.tripservicekata.trip;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.eclipse.collections.impl.test.Verify;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.craftedsw.tripservicekata.user.User.aNewUser;
import static org.craftedsw.tripservicekata.user.User.newUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class TripServiceTest {
    public static User a_random_user() { return newUser();}
    public static final User UNUSED_OTHER_USER = null;
    public static final User UNLOGGED_USER = null;

    @Mock
    private TripDaoWrapper tripDao;

    private TripService tripService;
    private User currentUser;
    private User otherUser;
    private List<Trip> retriedTrips;
    private List<Trip> expectedTrips;

    @BeforeEach
    void setUp() {
        tripService = new TripService(tripDao);
    }

    @Test
    void an_error_should_be_thrown_if_the_user_is_not_logged() {
        // GIVEN
        a_user_not_logged();
        // THEN
        an_error_is_thrown_when_we_try_to_retrieve_the_trips();
    }

    @Test
    void a_user_with_no_friend_will_have_no_trip() {
        // GIVEN
        a_user_with_no_friend();
        // WHEN
        his_trips_are_retrieved();
        // THEN
        the_trip_list_is_empty();
        trip_dao_is_never_used();
    }

    @Test
    void a_user_with_one_friend_that_is_not_the_logged_user_will_have_no_trip() {
        // GIVEN
        a_user_with_one_friend_that_is_not_the_logged_user();
        // WHEN
        his_trips_are_retrieved();
        // THEN
        the_trip_list_is_empty();
        trip_dao_is_never_used();
    }

    @Test
    void a_user_with_one_friend_that_is_the_logged_user_will_have_the_trips_returned_by_trip_dao() {
        // GIVEN
        a_user_with_a_friend_that_is_the_logged_user();
        trip_dao_will_return_the_trips_of_the_user();
        // WHEN
        his_trips_are_retrieved();
        // THEN
        the_trip_list_is_the_one_returned_by_trip_dao();
    }

    private void a_user_with_a_friend_that_is_the_logged_user() {
        this.currentUser = newUser();
        this.otherUser = aNewUser()
                .friend(currentUser)
                .create();
    }

    private void a_user_with_one_friend_that_is_not_the_logged_user() {
        this.currentUser = newUser();
        this.otherUser = aNewUser()
                .friend(a_random_user())
                .create();
    }

    private void a_user_with_no_friend() {
        this.currentUser = newUser();
        this.otherUser = newUser();
    }

    private void a_user_not_logged() {
        this.currentUser = UNLOGGED_USER;
        this.otherUser = UNUSED_OTHER_USER;
    }

    private void the_trip_list_is_the_one_returned_by_trip_dao() {
        assertEquals(expectedTrips, retriedTrips);
    }

    private void trip_dao_will_return_the_trips_of_the_user() {
        expectedTrips = List.of(new Trip());
        given(tripDao.findTripsByUser(otherUser)).willReturn(expectedTrips);
    }

    private void the_trip_list_is_empty() {
        Verify.assertEmpty(retriedTrips);
    }

    private void trip_dao_is_never_used() {
        then(tripDao).shouldHaveZeroInteractions();
    }

    private void his_trips_are_retrieved() {
        this.retriedTrips = tripService.getTripsByUser(currentUser, otherUser);
    }

    private void an_error_is_thrown_when_we_try_to_retrieve_the_trips() {
    assertThrows(UserNotLoggedInException.class, this::his_trips_are_retrieved);
    }
}
