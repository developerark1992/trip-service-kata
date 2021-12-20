package org.craftedsw.tripservicekata.user;

import java.util.Optional;

public class UserSessionWrapper {

    private UserSession userSession;

    public UserSessionWrapper(UserSession userSession) {
        this.userSession = userSession;
    }

    public Optional<User> getLoggedUser() {
        return Optional.ofNullable(userSession.getLoggedUser());
    }
}

