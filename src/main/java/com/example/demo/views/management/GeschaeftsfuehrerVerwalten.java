package com.example.demo.views.management;

//Author: Bilgesu Kara

import com.example.demo.model.entities.User;
import com.example.demo.security.AuthenticatedUser;
import com.example.demo.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@PageTitle("Geschäftsführer Verwalten")
@Route(value = "geschaeftsfuehrer-verwalten")
@RolesAllowed("ADMINISTRATOR")
public class GeschaeftsfuehrerVerwalten extends VerticalLayout {

    private final UserService userService;
    private final Grid<User> grid;
    private final AuthenticatedUser authenticatedUser;
    
    @Autowired
    public GeschaeftsfuehrerVerwalten(UserService userService, AuthenticatedUser
    authenticatedUser) {
        this.userService = userService;
        this.authenticatedUser = authenticatedUser;
        this.grid = new Grid<>(User.class);

        // Create a title for the view
        H1 title = new H1("Geschäftsführer Verwalten");
        title.getStyle().set("text-align" , "center");
        add(title);
        setAlignItems(Alignment.CENTER);
        
        // Create a "Hinzufügen" button
        Button addButton = new Button("Hinzufügen");
        addButton.addClickListener(event -> UI.getCurrent().navigate("register"));

        // Create Home Button
        Button homeButton = new Button("Home");
        homeButton.addClickListener(event -> navigateToDashboard());

        // Add buttons to a horizontal layout
        HorizontalLayout buttonLayout = new HorizontalLayout(addButton, homeButton);
        add(buttonLayout);

        // Load all employees with role 'geschaeftsfuehrer'
        List<User> geschaeftsfuehrerList = userService.findAllGeschaeftsfuehrer();

        // Create a grid to display the employees
        grid.setItems(geschaeftsfuehrerList);
        grid.setColumns("id" , "benutzername" , "vorname" , "nachname" , "rolle");

        // Add delete button
        grid.addComponentColumn(user -> {
            Button deleteButton = new Button("Löschen");
            deleteButton.addClickListener(event -> openDeleteConfirmationDialog(user));
            return deleteButton;
        }).setHeader("Aktionen");
        add(grid);
    }

    // Function to navigate to the dashboard according to the role of the user
        private void navigateToDashboard() {
            Optional<User> currentUser = authenticatedUser.get();
            if (currentUser.isPresent()) {
                String role = currentUser.get().getRolle();
                if ("administrator" .equals(role)) {
                    UI.getCurrent().navigate("admin-dashboard");
                } else if ("geschaeftsfuehrer" .equals(role)) {
                    UI.getCurrent().navigate("geschaeftsfuehrer-dashboard");
                }
            }
    }
    
    // Function for opening the delete confirmation dialog
    private void openDeleteConfirmationDialog(User user) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Geschäftsführer löschen");
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add("Möchten Sie diesen Geschäftsführer wirklich löschen?");
        Button confirmButton = new Button("Löschen" , event -> {
            userService.deleteUserById(user.getId());
            grid.setItems(userService.findAllGeschaeftsfuehrer());
            Notification.show("Geschäftsführer gelöscht" , 3000, Notification.Position.MIDDLE);
            dialog.close();
        });
        confirmButton.getStyle().set("background-color" , "red");
        confirmButton.getStyle().set("color" , "white");
        Button cancelButton = new Button("Abbrechen" , event -> dialog.close());
        cancelButton.getStyle().set("color" , "#007BFF");
        HorizontalLayout buttonLayout = new HorizontalLayout(confirmButton, cancelButton);
        dialogLayout.add(buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
    }
}
