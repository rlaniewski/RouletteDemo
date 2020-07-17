import java.io.*;
import java.util.*;

public class ConsoleRoulette {
    ArrayList<String> players = new ArrayList<>();

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

    public static void main(String[] args) {
        ConsoleRoulette cr = new ConsoleRoulette();

        cr.loadPlayers();
    }
}
