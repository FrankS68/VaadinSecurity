package consulting.segieth.security;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.security.RolesAllowed;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
        printCurrentRoles();
    }
    
    public void printCurrentRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            List<String> roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            System.out.println("Aktuelle Rollen des Users: " + roles);
        } else {
            System.out.println("Kein User eingeloggt.");
        }
    }
    
    @Transactional
    public UserProfile createProfile(String provider,String email,String name) {
        var userProfile = new UserProfile(provider,email,name);
        profileRepository.saveAndFlush(userProfile);
        return userProfile;
    }
    
    public List<UserProfile> findByEmail(String email){
    	return profileRepository.findByEmail(email);
    };

    public List<UserProfile> findByProviderAndEmail(String provider,String email){
    	return profileRepository.findByProviderAndEmail(provider,email);
    };

    @Transactional(readOnly = true)
    public List<UserProfile> list(Pageable pageable) {
        return profileRepository.findAllBy(pageable).toList();
    }

	public UserProfile getProfileByEmail(String provider,String email,String name) {
		if (email == null) {
			return new UserProfile("local", "Anonymous");
		}
		List<UserProfile> profiles = findByProviderAndEmail(provider,email);
		if (profiles == null || profiles.size() == 0) {
			return createProfile(provider, email, name);
		}
		return profiles.get(0);
	}

}
