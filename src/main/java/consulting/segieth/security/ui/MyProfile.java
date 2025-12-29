package consulting.segieth.security.ui;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;

@Route(value = "")
@AnonymousAllowed
@Menu(order = 99, icon = "vaadin:clipboard-check", title = "my Profile")
public class MyProfile extends VerticalLayout {

    public MyProfile(AuthenticationContext authContext) {

        add(new H2("Everyone can see this"));

        // Ensure that the class used by getAuthenticatedUser() matches the object type created
        // by the authentication providers used in Spring Security.
        authContext.getAuthenticatedUser(OAuth2User.class).ifPresent(user -> {
            boolean isAdmin = user.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> "ROLE_ADMIN".equals(grantedAuthority.getAuthority()));
            if (isAdmin) {
                add(new Button("Admin button"));
            } else {
                add(new H1(String.format("Hi %s (%s)",user.getAttribute("name"),user.getAttribute("email"))));
                add(new H2("You are not an admin"));
                user.getAttributes().forEach((key,value) ->{
                	if (!( key.equalsIgnoreCase("name") || key.equalsIgnoreCase("email"))) {
	                    add(new H3(key));
	                    add(new Span(value == null ? "{null}" : value.toString()));
                	}
                });
            }
        });
    }
}
