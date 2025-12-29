package consulting.segieth.security.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("mylogin")
@PageTitle("login")
@AnonymousAllowed
@Menu(order = 42, icon = "vaadin:clipboard-check", title = "my Logout")
public class MyLoginView extends VerticalLayout {

    public MyLoginView() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Standard Spring Security OAuth2 Pfad
        Anchor googleLoginLink = new Anchor("/oauth2/authorization/google", "Mit Google anmelden");
        googleLoginLink.getElement().setAttribute("router-ignore", true);

        add(new H1("Willkommen"),  googleLoginLink);
        
        Anchor ghLoginLink = new Anchor("/oauth2/authorization/github", "Mit GitHub anmelden");
        // Wichtig: Router ignorieren, damit die Anfrage direkt an Spring Security geht
        ghLoginLink.getElement().setAttribute("router-ignore", true);
     	add(ghLoginLink);

     	Anchor bskyLoginLink = new Anchor("/oauth2/authorization/bluesky", "Mit Blue Sky anmelden");
        // Wichtig: Router ignorieren, damit die Anfrage direkt an Spring Security geht
     	bskyLoginLink.getElement().setAttribute("router-ignore", true);
     	add(new Span("Bluesky kann nicht auf localhost zugreifen! Da müssen wir nochmal ran."),bskyLoginLink);

     	Button loginButton = new Button("Mit Microsoft anmelden", e -> {
     	    // Leitet zum Standard-Spring-Security-Endpunkt für Azure weiter
     	    UI.getCurrent().getPage().setLocation("/oauth2/authorization/azure");
     	});
     	add(loginButton);

    }
}
