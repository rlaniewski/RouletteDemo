import java.io.*;
import java.util.*;

class Bet {
    public String playerName;
    public boolean betTypeOddEven = false;
    public String betOnOddEven;
    public int betOnNumber = -1;
    public float betAmount;
}

public class ConsoleRoulette {
    ArrayList<String> players = new ArrayList<>();
    ArrayList<Bet> bets = new ArrayList<>();

    private int wheelNumber = -1;

    public void loadPlayers() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("Players.txt"));
            String playerName = reader.readLine();
            while (playerName != null) {
                System.out.println(playerName + " joined.");
                players.add(playerName);
                playerName = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void spinWheel() {
        wheelNumber = (int) (Math.random() * 37);

        System.out.println("Ball landed on " + wheelNumber + "!");
    }

    class SpinTask extends TimerTask {
        private String name;

        public SpinTask(String name) {
            this.name = name;
        }

        public void run() {
            System.out.println();
            System.out.println("[" + new java.util.Date() + "] " + name + ": Executing task!");
            spinWheel();
        }
    }

    private SpinTask spinTask;
    private Timer timer;

    public void loadTasks() {
        spinTask = new SpinTask("Spin");
        timer = new Timer();
        timer.scheduleAtFixedRate(spinTask, 30000, 30000);  // executes every 30 seconds
    }

    public void cancelTasks() {
        System.out.println("Cancelling tasks...");
        if (timer != null) {
            timer.cancel();
        }
    }

    private void prompt() {
        System.out.print("place_your_bets> ");
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);

        String input = null;
        boolean quit = false;

        while (!quit) {
            prompt();
            input = scanner.nextLine();
            StringTokenizer defaultTokenizer = new StringTokenizer(input);
            int tokens = defaultTokenizer.countTokens();

            if (tokens == 3) {
                String playerName;
                String betToken;
                boolean betTypeOddEven = false;
                String betOnOddEven = null;
                int betOnNumber = -1;
                String betAmountToken;
                float betAmount = -1;

                playerName = defaultTokenizer.nextToken();
                betToken = defaultTokenizer.nextToken();
                betAmountToken = defaultTokenizer.nextToken();

            } else {
                quit = "quit".equalsIgnoreCase(input);
            }
        }
    }

    public static void main(String[] args) {
        ConsoleRoulette cr = new ConsoleRoulette();

        cr.loadPlayers();
        cr.loadTasks();
        cr.play();
        cr.cancelTasks();
    }
}
