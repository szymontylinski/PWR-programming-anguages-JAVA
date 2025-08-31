import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;
import java.awt.*;

public class GameOfLifeConcurrent {

    private static int[][] currentBoard;
    private static int[][] nextBoard;
    private static int rows, cols, iterations, numThreads;

    private static JFrame frame;
    private static JPanel gridPanel;
    private static JLabel[][] cellLabels;
    private static Color[] threadColors;
    private static boolean restartSimulation = false;

    public static void main(String[] args) throws IOException, InterruptedException {
        // Provide default values for arguments if not provided
        if (args.length < 2) {
            System.out.println("No arguments provided. Using default config and threads.");
            args = new String[]{"config.txt", "1"}; // Default config and thread count
        }

        String configFile = args[0];
        numThreads = Integer.parseInt(args[1]);

        ensureConfigFileExists(configFile);
        loadConfig(configFile);
        setupGUI();
        initializeThreadColors();

        while (true) {
            if (restartSimulation) {
                restartSimulation = false;
                continue;
            }

            for (int i = 0; i < iterations; i++) {
                System.out.println("Iteration " + (i + 1) + " started.");
                CountDownLatch latch = new CountDownLatch(numThreads);
                ExecutorService executor = Executors.newFixedThreadPool(numThreads);

                int partitionSize = rows / numThreads;
                for (int t = 0; t < numThreads; t++) {
                    int startRow = t * partitionSize;
                    int endRow = (t == numThreads - 1) ? rows : startRow + partitionSize;
                    executor.execute(new Worker(startRow, endRow, latch, t));
                }

                latch.await();
                swapBoards();

                executor.shutdown();
                updateGUI();
                Thread.sleep(500); // Pause for visualization

                if (restartSimulation) {
                    break;
                }
                System.out.println("Iteration " + (i + 1) + " completed.\n");
            }
        }
    }

    private static void ensureConfigFileExists(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Config file not found. Creating default config.txt...");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("30\n30\n100\n5\n");
                writer.write("29 1\n28 2\n27 0\n27 1\n27 2\n");
            }
        }
    }

    private static void loadConfig(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        try {
            rows = Integer.parseInt(br.readLine());
            cols = Integer.parseInt(br.readLine());
            iterations = Integer.parseInt(br.readLine());
            int liveCells = Integer.parseInt(br.readLine());

            if (rows <= 0 || cols <= 0 || iterations <= 0 || liveCells < 0) {
                throw new IllegalArgumentException("Invalid configuration values. Dimensions and iterations must be positive.");
            }

            currentBoard = new int[rows][cols];
            nextBoard = new int[rows][cols];

            for (int i = 0; i < liveCells; i++) {
                String[] parts = br.readLine().split(" ");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid cell coordinates format.");
                }
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                if (x < 0 || x >= rows || y < 0 || y >= cols) {
                    throw new IllegalArgumentException("Cell coordinates out of bounds.");
                }
                currentBoard[x][y] = 1;
            }
        } catch (Exception e) {
            throw new IOException("Failed to load configuration: " + e.getMessage());
        } finally {
            br.close();
        }
    }

    private static void setupGUI() {
        frame = new JFrame("Game of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        gridPanel = new JPanel(new GridLayout(rows, cols));
        cellLabels = new JLabel[rows][cols];

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadConfigItem = new JMenuItem("Load Configuration");
        loadConfigItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(frame);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    loadConfig(selectedFile.getAbsolutePath());
                    restartSimulation = true;
                    setupGUI();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Failed to load configuration: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        fileMenu.add(loadConfigItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JLabel cell = new JLabel();
                cell.setOpaque(true);
                cell.setBackground(currentBoard[i][j] == 1 ? Color.BLACK : Color.WHITE);
                cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                gridPanel.add(cell);
                cellLabels[i][j] = cell;
            }
        }

        frame.add(gridPanel);
        frame.setVisible(true);
    }

    private static void initializeThreadColors() {
        threadColors = new Color[numThreads];
        Random random = new Random();
        for (int i = 0; i < numThreads; i++) {
            threadColors[i] = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }
    }

    private static void updateGUI() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cellLabels[i][j].setBackground(currentBoard[i][j] == 1 ? Color.BLACK : Color.WHITE);
            }
        }
    }

    private static void swapBoards() {
        int[][] temp = currentBoard;
        currentBoard = nextBoard;
        nextBoard = temp;
    }

    private static class Worker implements Runnable {
        private final int startRow;
        private final int endRow;
        private final CountDownLatch latch;
        private final int threadId;

        public Worker(int startRow, int endRow, CountDownLatch latch, int threadId) {
            this.startRow = startRow;
            this.endRow = endRow;
            this.latch = latch;
            this.threadId = threadId;
        }

        @Override
        public void run() {
            System.out.println("Thread " + threadId + " processing rows " + startRow + " to " + (endRow - 1));
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < cols; j++) {
                    int liveNeighbors = countLiveNeighbors(i, j);
                    if (currentBoard[i][j] == 1) {
                        nextBoard[i][j] = (liveNeighbors == 2 || liveNeighbors == 3) ? 1 : 0;
                    } else {
                        nextBoard[i][j] = (liveNeighbors == 3) ? 1 : 0;
                    }
                    // Visualize thread activity
                    cellLabels[i][j].setBackground(threadColors[threadId]);
                }
            }
            latch.countDown();
        }

        private int countLiveNeighbors(int x, int y) {
            int liveCount = 0;
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    int nx = (x + dx + rows) % rows;
                    int ny = (y + dy + cols) % cols;
                    liveCount += currentBoard[nx][ny];
                }
            }
            return liveCount;
        }
    }
}
