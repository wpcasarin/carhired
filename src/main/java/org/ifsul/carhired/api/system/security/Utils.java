package org.ifsul.carhired.api.system.security;

import org.ifsul.carhired.api.user.User;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {
    /**
     * Retrieves the authenticated user from the security context.
     *
     * @return The authenticated User object.
     * @throws AuthenticationException If the authenticated user cannot be retrieved.
     */
    public static User getAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        throw new AuthenticationException("Unable to retrieve authenticated user") {
        };
    }
}
