package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProgressChart extends JPanel {
    private List<Integer> results; // Lista wyników
    private List<String> dates;    // Lista dat

    public ProgressChart(List<Integer> results, List<String> dates) {
        if (results == null || results.isEmpty() || dates == null || dates.isEmpty() || results.size() != dates.size()) {
            throw new IllegalArgumentException("Invalid data for chart.");
        }
        this.results = results;
        this.dates = dates;
        setPreferredSize(new Dimension(800, 600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Tło wykresu
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Oś X i Y
        g.setColor(Color.BLACK);
        g.drawLine(50, getHeight() - 50, getWidth() - 50, getHeight() - 50); // Oś X
        g.drawLine(50, getHeight() - 50, 50, 50); // Oś Y

        // Dodawanie danych do wykresu
        int chartWidth = getWidth() - 100;
        int chartHeight = getHeight() - 100;
        int maxResult = results.stream().max(Integer::compareTo).orElse(1);
        int xStep = chartWidth / (results.size() - 1);

        for (int i = 0; i < results.size() - 1; i++) {
            int x1 = 50 + i * xStep;
            int y1 = getHeight() - 50 - (results.get(i) * chartHeight / maxResult);
            int x2 = 50 + (i + 1) * xStep;
            int y2 = getHeight() - 50 - (results.get(i + 1) * chartHeight / maxResult);

            // Linie i punkty
            g.setColor(Color.BLUE);
            g.drawLine(x1, y1, x2, y2);
            g.fillOval(x1 - 3, y1 - 3, 6, 6);

            // Daty na osi X
            g.setColor(Color.BLACK);
            g.drawString(dates.get(i), x1 - 15, getHeight() - 30);
        }

        // Ostatni punkt
        int lastX = 50 + (results.size() - 1) * xStep;
        int lastY = getHeight() - 50 - (results.get(results.size() - 1) * chartHeight / maxResult);
        g.fillOval(lastX - 3, lastY - 3, 6, 6);
        g.drawString(dates.get(dates.size() - 1), lastX - 15, getHeight() - 30);
    }
}

