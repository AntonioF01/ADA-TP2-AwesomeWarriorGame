import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader((System.in)));

        String[] aux = in.readLine().split(" ");
        int challenges = Integer.parseInt(aux[0]);
        int decisions = Integer.parseInt(aux[1]);

        AwesomeWarriorGame game = new AwesomeWarriorGame(challenges);

        for (int i = 0; i < decisions; i++) {
            aux = in.readLine().split(" ");
            game.handleConnection(Integer.parseInt(aux[0]), aux[1], Integer.parseInt(aux[2]), Integer.parseInt(aux[3]));
        }


        aux = in.readLine().split(" ");
        game.processFinalLine(Integer.parseInt(aux[0]), Integer.parseInt(aux[1]), Integer.parseInt(aux[2]));
        System.out.println(game.solve());
    }


}
