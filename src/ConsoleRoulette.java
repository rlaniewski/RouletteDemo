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

    private void addBet(Bet bet) {
        bets.add(bet);
        System.out.print(bet.playerName + " betted " + bet.betAmount + " on ");
        if (bet.betTypeOddEven) {
            System.out.println(bet.betOnOddEven);
        } else {
            System.out.println(bet.betOnNumber);
        }
    }

    private void processBets() {
        boolean numberIsEven = (wheelNumber % 2 == 0);
        boolean outcomeWin = false;

        System.out.println();
        System.out.println("Number: " + wheelNumber);
        System.out.println("Player          Bet     Outcome     Winnings");
        System.out.println("---");

        if (bets.size() == 0) {
            System.out.println("No bets placed.");
            return;
        }

        for (Bet bet : bets) {
            float winnings = 0;

            System.out.print(bet.playerName + "     ");

            if (wheelNumber == 0) {
                outcomeWin = false;
            } else {
                if (bet.betTypeOddEven) {
                    System.out.print(bet.betOnOddEven + "    ");

                    if ("EVEN".equals(bet.betOnOddEven) && numberIsEven) {
                        outcomeWin = true;
                        winnings = bet.betAmount * 2;
                    } else if ("ODD".equals(bet.betOnOddEven) && !numberIsEven) {
                        outcomeWin = true;
                        winnings = bet.betAmount * 2;
                    }
                } else {
                    System.out.print(bet.betAmount + "   ");

                    if (bet.betOnNumber == wheelNumber) {
                        outcomeWin = true;
                        winnings = bet.betAmount * 36;
                    }
                }
            }

            if (outcomeWin) {
                System.out.print("    WIN");
            } else {
                System.out.print("   LOSE");
            }

            System.out.println(winnings);
        }

        System.out.println();
    }

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

    private boolean playerFound(String playerName) {
        return players.contains(playerName);
    }

    private void spinWheel() {
        wheelNumber = (int) (Math.random() * 37);

        System.out.println("Ball landed on " + wheelNumber + "!");

        processBets();
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
            boolean malformedInput = false;

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

                if ("EVEN".equalsIgnoreCase(betToken)) {
                    betOnOddEven = "EVEN";
                    betTypeOddEven = true;
                } else if ("ODD".equalsIgnoreCase(betToken)) {
                    betOnOddEven = "ODD";
                    betTypeOddEven = true;
                } else {
                    try {
                        betOnNumber = Integer.parseInt(betToken);
                    } catch (NumberFormatException e) {
                        malformedInput = true;
                    }
                }

                try {
                    betAmount = Float.parseFloat(betAmountToken);
                } catch (NumberFormatException e) {
                    malformedInput = true;
                }

                if (!playerFound(playerName)) {
                    System.out.println(playerName + " not found.");
                } else if (malformedInput) {
                    System.out.println("malformed input.");
                } else {
                    Bet bet = new Bet();
                    bet.playerName = playerName;
                    bet.betTypeOddEven = betTypeOddEven;
                    bet.betOnOddEven = betOnOddEven;
                    bet.betOnNumber = betOnNumber;
                    bet.betAmount = betAmount;
                    addBet(bet);
                }
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
