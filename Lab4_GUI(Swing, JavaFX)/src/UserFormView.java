package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Widok formularza do tworzenia/edycji użytkownika.
 */
public class UserFormView extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JButton saveButton;

    public UserFormView() {
        setTitle("User Form");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel formularza
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        // Etykiety i pola
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        saveButton = new JButton("Save");
        formPanel.add(saveButton);

        // Dodanie formularza do okna
        add(formPanel, BorderLayout.CENTER);
    }

    // Gettery pól i przycisków
    public String getName() {
        return nameField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public void setName(String name) {
        nameField.setText(name);
    }

    public void setEmail(String email) {
        emailField.setText(email);
    }

    public void addSaveButtonListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }
}

