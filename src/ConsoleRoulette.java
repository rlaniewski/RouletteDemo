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

    HashMap<String, Float> totalBets = new HashMap<>();
    HashMap<String, Float> totalWinnings = new HashMap<>();

    private float getTotalBets(String playerName) {
        if (totalBets.containsKey(playerName)) {
            return totalBets.get(playerName);
        } else {
            return 0;
        }
    }

    private float getTotalWinnings(String playerName) {
        if (totalWinnings.containsKey(playerName)) {
            return totalWinnings.get(playerName);
        } else {
            return 0;
        }
    }

    private void updateTotalBets(String playerName, float bets) {
        float currentTotalBets = getTotalBets(playerName);
        totalBets.put(playerName, currentTotalBets + bets);
    }

    private void updateTotalWinnings(String playerName, float winnings) {
        float currentTotalWinnings = getTotalWinnings(playerName);
        totalWinnings.put(playerName, currentTotalWinnings + winnings);
    }

    private int wheelNumber = -1;

    private void addBet(Bet bet) {
        bets.add(bet);
        updateTotalBets(bet.playerName, bet.betAmount);

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
        System.out.println(StringPadder.padStringRight("Player", 25) + "Bet     Outcome     Winnings");
        System.out.println("---");

        if (bets.size() == 0) {
            System.out.println("No bets placed.");
            return;
        }

        for (Bet bet : bets) {
            float winnings = 0;

            System.out.print(StringPadder.padStringRight(bet.playerName, 25));

            if (wheelNumber == 0) {
                outcomeWin = false;
            } else {
                if (bet.betTypeOddEven) {
                    System.out.print(StringPadder.padStringRight(bet.betOnOddEven, 8));

                    if ("EVEN".equals(bet.betOnOddEven) && numberIsEven) {
                        outcomeWin = true;
                        winnings = bet.betAmount * 2;
                    } else if ("ODD".equals(bet.betOnOddEven) && !numberIsEven) {
                        outcomeWin = true;
                        winnings = bet.betAmount * 2;
                    }
                } else {
                    System.out.print(bet.betAmount + "   ");
                    System.out.print(StringPadder.padStringRight("" + bet.betAmount, 8));

                    if (bet.betOnNumber == wheelNumber) {
                        outcomeWin = true;
                        winnings = bet.betAmount * 36;
                    }
                }
            }

            if (outcomeWin) {
                System.out.print(StringPadder.padStringLeft("WIN", 7));
            } else {
                System.out.print(StringPadder.padStringLeft("LOSE", 7));
            }

            System.out.println(StringPadder.padStringLeft(""+winnings, 13));

            updateTotalWinnings(bet.playerName, winnings);
        }

        System.out.println();
    }

    private void clearBets() {
        bets.clear();
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
        clearBets();
        prompt();
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
