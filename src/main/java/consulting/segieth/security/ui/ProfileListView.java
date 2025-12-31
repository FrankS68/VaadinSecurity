package consulting.segieth.security.ui;

import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import consulting.segieth.security.ProfileService;
import consulting.segieth.security.UserProfile;
import jakarta.annotation.security.RolesAllowed;

@Route("/profiles")
@PageTitle("Profile List")
@Menu(order = 0, icon = "vaadin:clipboard-check", title = "Profile List")
@RolesAllowed("SecurityManager")
class ProfileListView extends VerticalLayout {

    private final ProfileService profileService;

    final TextField description;
    final DatePicker dueDate;
    final Button createBtn;
    final Grid<UserProfile> profileGrid;

    ProfileListView(ProfileService profileService) {
        this.profileService = profileService;

        description = new TextField();
        description.setPlaceholder("What do you want to do?");
        description.setAriaLabel("Task description");
        description.setMaxLength(UserProfile.DESCRIPTION_MAX_LENGTH);
        description.setMinWidth("20em");

        dueDate = new DatePicker();
        dueDate.setPlaceholder("Due date");
        dueDate.setAriaLabel("Due date");

        createBtn = new Button("Create", event -> createTask());
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        var dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(getLocale())
                .withZone(ZoneId.systemDefault());
        var dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(getLocale());

        profileGrid = new Grid<>();
        profileGrid.setItems(query -> profileService.list(toSpringPageRequest(query)).stream());
        profileGrid.addColumn(UserProfile::getProvider).setHeader("Provider");
        profileGrid.addColumn(UserProfile::getEmail).setHeader("Email");
        profileGrid.addColumn(UserProfile::getName).setHeader("Name");
        /*
        taskGrid.addColumn(task -> Optional.ofNullable(task.getDueDate()).map(dateFormatter::format).orElse("Never"))
                .setHeader("Due Date");
        taskGrid.addColumn(task -> dateTimeFormatter.format(task.getCreationDate())).setHeader("Creation Date");
        */
        profileGrid.setEmptyStateText("You have no tasks to complete");
        profileGrid.setSizeFull();
        profileGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        setSizeFull();
        setPadding(false);
        setSpacing(false);
        getStyle().setOverflow(Style.Overflow.HIDDEN);

        add(new ViewToolbar("Profile List", ViewToolbar.group(description, dueDate, createBtn)));
        add(profileGrid);
    }

    private void createTask() {
    	profileService.createProfile("","","");
        profileGrid.getDataProvider().refreshAll();
        description.clear();
        dueDate.clear();
        Notification.show("Task added", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

}
