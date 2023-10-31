package org.ifsul.carhired.api.user;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Base class for user-related services.
 */
public abstract class UserService {
    /**
     * Gets the email address of the currently authenticated user.
     *
     * @return The email address of the authenticated user.
     */
    protected static String getAuthenticatedUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Checks access for a user entity by comparing the authenticated user's email
     * with the email of the provided user entity. If the emails do not match,
     * an AccessDeniedException is thrown to indicate access denial.
     *
     * @param entity The User entity to check access for.
     * @throws AccessDeniedException If access is denied.
     */
    protected static void checkAccessForUser(@NotNull User entity) {
        var authEmail = getAuthenticatedUserEmail();
        if (!authEmail.equals(entity.getEmail())) throw new AccessDeniedException("access denied");
    }

}
