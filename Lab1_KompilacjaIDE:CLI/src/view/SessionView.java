package view;

import model.TrainingSession;
import model.Workout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SessionView extends JFrame {
    private Workout workout;
    private DefaultListModel<TrainingSession> sessionListModel;
    private JList<TrainingSession> sessionList;

    public SessionView(Workout workout) {
        super("Training Sessions for: " + workout.getName() + " (" + workout.getLocation() + ")");
        this.workout = workout;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Kolor tła okna
        getContentPane().setBackground(new Color(220, 240, 255));

        // Model listy sesji
        sessionListModel = new DefaultListModel<>();
        sessionList = new JList<>(sessionListModel);
        sessionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Kolor tła i tekstu listy
        sessionList.setBackground(new Color(200, 230, 255));
        sessionList.setForeground(Color.BLACK);

        JScrollPane sessionScrollPane = new JScrollPane(sessionList);
        sessionScrollPane.setPreferredSize(new Dimension(600, 400));
        add(sessionScrollPane, BorderLayout.CENTER);

        // Wczytanie istniejących sesji
        List<TrainingSession> existingSessions = workout.getSessions();
        for (TrainingSession session : existingSessions) {
            sessionListModel.addElement(session);
        }

        // Panel przycisków
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(180, 210, 255)); // Tło panelu przycisków

        JButton addSessionButton = new JButton("Add Session");
        styleButton(addSessionButton);
        addSessionButton.addActionListener(e -> addSession());

        JButton editSessionButton = new JButton("Edit Session");
        styleButton(editSessionButton);
        editSessionButton.addActionListener(e -> editSession());

        JButton deleteSessionButton = new JButton("Delete Session");
        styleButton(deleteSessionButton);
        deleteSessionButton.addActionListener(e -> deleteSession());

        JButton showChartButton = new JButton("Show Progress Chart");
        styleButton(showChartButton);
        showChartButton.addActionListener(e -> showProgressChart());

        buttonPanel.add(addSessionButton);
        buttonPanel.add(editSessionButton);
        buttonPanel.add(deleteSessionButton);
        buttonPanel.add(showChartButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(100, 150, 255)); // Tło przycisku
        button.setForeground(Color.WHITE); // Kolor tekstu
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }

    private void addSession() {
        try {
            String dateInput = JOptionPane.showInputDialog(this, "Enter session date (YYYY-MM-DD):");
            if (dateInput == null || dateInput.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Date cannot be empty.");
                return;
            }

            LocalDate date = LocalDate.parse(dateInput);

            String goal = JOptionPane.showInputDialog(this, "Enter session goal:");
            if (goal == null || goal.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Goal cannot be empty.");
                return;
            }

            String result = JOptionPane.showInputDialog(this, "Enter session result (numerical):");
            if (result == null || result.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Result cannot be empty.");
                return;
            }

            String mood = JOptionPane.showInputDialog(this, "Enter mood (e.g., Good, Average, Bad):");
            if (mood == null || mood.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mood cannot be empty.");
                return;
            }

            TrainingSession session = new TrainingSession(workout, date, goal, result, mood);
            sessionListModel.addElement(session);
            workout.addSession(session);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please ensure all fields are filled correctly.");
        }
    }

    private void editSession() {
        TrainingSession selectedSession = sessionList.getSelectedValue();
        if (selectedSession == null) {
            JOptionPane.showMessageDialog(this, "Please select a session to edit.");
            return;
        }

        try {
            String dateInput = JOptionPane.showInputDialog(this, "Edit session date (YYYY-MM-DD):", selectedSession.getDate());
            LocalDate date = LocalDate.parse(dateInput);

            String goal = JOptionPane.showInputDialog(this, "Edit session goal:", selectedSession.getGoal());
            String result = JOptionPane.showInputDialog(this, "Edit session result (numerical):", selectedSession.getResult());
            String mood = JOptionPane.showInputDialog(this, "Edit mood (e.g., Good, Average, Bad):", selectedSession.getMood());

            TrainingSession updatedSession = new TrainingSession(workout, date, goal, result, mood);
            sessionListModel.setElementAt(updatedSession, sessionList.getSelectedIndex());
            workout.getSessions().set(sessionList.getSelectedIndex(), updatedSession);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please try again.");
        }
    }

    private void deleteSession() {
        TrainingSession selectedSession = sessionList.getSelectedValue();
        if (selectedSession == null) {
            JOptionPane.showMessageDialog(this, "Please select a session to delete.");
            return;
        }
        sessionListModel.removeElement(selectedSession);
        workout.removeSession(selectedSession);
    }

    private void showProgressChart() {
        if (sessionListModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No sessions available to display progress.");
            return;
        }

        List<Integer> results = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < sessionListModel.getSize(); i++) {
            TrainingSession session = sessionListModel.getElementAt(i);
            try {
                results.add(Integer.parseInt(session.getResult()));
                dates.add(session.getDate().toString());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid result format in session: " + session);
                return;
            }
        }

        JFrame chartFrame = new JFrame("Progress Chart for: " + workout.getName());
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.setSize(800, 600);
        chartFrame.add(new ProgressChart(results, dates));
        chartFrame.setVisible(true);
    }
}
