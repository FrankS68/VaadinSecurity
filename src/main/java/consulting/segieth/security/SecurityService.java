package consulting.segieth.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;

@Component
public class SecurityService {
	@Autowired
	ProfileService profileService;

    private static final String LOGOUT_SUCCESS_URL = "/";

    public OAuth2User getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        return getAuthenticatedUser(context);
    }
    
    public UserProfile getUserProfile() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserProfile userProfile = getUserProfile(context);
        if (userProfile == null) {
        	return new UserProfile("local", "anonymous", "Jane Doe");
        }
        if (userProfile.getName().equals("Frank Segieth")) {
        	addRoleToCurrentUser("TaskManager");
        }
        if (userProfile.getEmail().equals("fse418@gmail.com")) {
        	addRoleToCurrentUser("SecurityManager");
        }
		return userProfile;
    	
    }
    
    public void addRoleToCurrentUser(String newRole) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
	        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
        	updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + newRole));
        	// updatedAuthorities.add(new SimpleGrantedAuthority(newRole));
	        
	        Authentication newAuth = new UsernamePasswordAuthenticationToken(
	            auth.getPrincipal(), 
	            auth.getCredentials(), 
	            updatedAuthorities
	        );
	        
	        SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
    }

	private UserProfile getUserProfile(SecurityContext context) {
		OAuth2User user = getAuthenticatedUser(context);
		// if (context != null) throw new RuntimeException("hier authorities zuweisen");		
		return profileService.getProfileByEmail(
				context.getAuthentication().getName(),
				user == null ? null : user.getAttributes().get("email").toString(),
				user == null ? null : user.getAttributes().get("name").toString());
    }

	private OAuth2User getAuthenticatedUser(SecurityContext context) {
		Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof OAuth2User) {
            OAuth2User userInfo = (OAuth2User) context.getAuthentication().getPrincipal();
			return userInfo;
        }
        // Anonymous or no authentication.
        return null;
	}

    public void logout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                null);
    }
}
