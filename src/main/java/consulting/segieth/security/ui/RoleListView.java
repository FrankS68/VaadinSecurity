package consulting.segieth.security.ui;

import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import consulting.segieth.base.ui.ViewToolbar;
import consulting.segieth.security.Role;
import consulting.segieth.security.RoleService;
import jakarta.annotation.security.RolesAllowed;

@Route("/roles")
@PageTitle("Role List")
@Menu(order = 0, icon = "vaadin:clipboard-check", title = "Role List")
@RolesAllowed("SecurityManager")
class RoleListView extends VerticalLayout {

    private final RoleService roleService;

    final TextField name;
    final TextField description;
    final Button createBtn;
    final Grid<Role> roleGrid;

    RoleListView(RoleService roleService) {
        this.roleService = roleService;

        name = new TextField();
        name.setAriaLabel("Role name");
        
        description = new TextField();
        description.setPlaceholder("What is that role meant for?");
        description.setAriaLabel("Role description");
        description.setMaxLength(Role.DESCRIPTION_MAX_LENGTH);
        description.setMinWidth("20em");

        createBtn = new Button("Create", event -> createRole());
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        roleGrid = new Grid<>();
        roleGrid.setItems(query -> roleService.list(toSpringPageRequest(query)).stream());
        roleGrid.addColumn(Role::getName).setHeader("Name");
        roleGrid.addColumn(Role::getType).setHeader("Type");
        roleGrid.addColumn(Role::getDescription).setHeader("Description");

        roleGrid.setEmptyStateText("You have no tasks to complete");
        roleGrid.setSizeFull();
        roleGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        setSizeFull();
        setPadding(false);
        setSpacing(false);
        getStyle().setOverflow(Style.Overflow.HIDDEN);

        add(new ViewToolbar("Role List", ViewToolbar.group(name,description, createBtn)));
        add(roleGrid);
    }

    private void createRole() {
    	roleService.createRole(name.getValue(),description.getValue());
        roleGrid.getDataProvider().refreshAll();
        name.clear();
        description.clear();
        Notification.show("Role added", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

}
