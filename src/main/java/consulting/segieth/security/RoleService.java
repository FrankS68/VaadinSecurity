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

import consulting.segieth.security.Role.RoleType;
import jakarta.annotation.security.RolesAllowed;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
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
    public Role createRole(String name,String description) {
        var role = new Role(name,RoleType.Group,description);
        roleRepository.saveAndFlush(role);
        return role;
    }
    
    @Transactional
    public Role createUserRole(String name) {
        var role = new Role(name,RoleType.User);
        roleRepository.saveAndFlush(role);
        return role;
    }
    
    @Transactional
    public Role createPermissionRole(String name) {
        var role = new Role(name,RoleType.Permission);
        roleRepository.saveAndFlush(role);
        return role;
    }
    
    @Transactional(readOnly = true)
    public List<Role> list(Pageable pageable) {
        return roleRepository.findAllBy(pageable).toList();
    }

    public List<Role> findByName(String name){
    	return roleRepository.findByName(name);
    };

    public List<Role> findByGroups(Role.RoleType type){
    	return roleRepository.findByGroups(type);
    };

}
