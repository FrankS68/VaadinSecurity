package consulting.segieth.base.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility;

import consulting.segieth.security.SecurityService;

@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout {

	   private SecurityService securityService;

	    public MainLayout(
	    		@Autowired SecurityService securityService) {
	        this.securityService = securityService;
        // 1. Navbar Bereich (oben)
        createHeader();

        // 2. Drawer Bereich (seitlich / mobil einklappbar)
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Moin " + securityService.getUserProfile().getName());
        logo.addClassNames(
            LumoUtility.FontSize.LARGE, 
            LumoUtility.Margin.MEDIUM);

        // Der DrawerToggle ist der "Hamburger"-Button für Mobilgeräte
        // addToNavbar(true, ...) bedeutet: optimiert für Touch/Mobil
        addToNavbar(true, new DrawerToggle(), logo);
    }

    private void createDrawer() {
        // Die Navigation als SideNav (neue Komponente in Vaadin 24)
        SideNav nav = createSideNav();

        // Scroller sorgt dafür, dass das Menü scrollbar ist, wenn es zu viele Items sind
        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
    }
    
	private SideNav createSideNav() {
	    var nav = new SideNav();
	    // nav.addClassNames(Margin.Horizontal.MEDIUM);
	    MenuConfiguration.getMenuEntries().forEach(entry -> nav.addItem(createSideNavItem(entry)));
	    return nav;
	}
	
	private SideNavItem createSideNavItem(MenuEntry menuEntry) {
	    if (menuEntry.icon() != null) {
	        return new SideNavItem(menuEntry.title(), menuEntry.path(), new Icon(menuEntry.icon()));
	    } else {
	        return new SideNavItem(menuEntry.title(), menuEntry.path());
	    }
	}
}