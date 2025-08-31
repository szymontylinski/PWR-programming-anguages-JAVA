package controller;

import model.User;
import view.UserFormView;

import javax.swing.*;

/**
 * Kontroler zarządzający interakcjami między widokiem UserFormView a modelem User.
 */
public class UserController {
    private User user;
    private UserFormView userFormView;

    public UserController(User user, UserFormView userFormView) {
        this.user = user;
        this.userFormView = userFormView;

        // Dodanie nasłuchiwania akcji dla przycisku zapisu
        this.userFormView.addSaveButtonListener(e -> saveUserData());
    }

    // Metoda zapisująca dane użytkownika
    private void saveUserData() {
        try {
            String name = userFormView.getName();
            String email = userFormView.getEmail();

            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(userFormView, "Fields cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            user.setName(name);
            user.setEmail(email);

            JOptionPane.showMessageDialog(userFormView, "User saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(userFormView, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
