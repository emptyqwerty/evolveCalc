import POGOProtos.Enums.PokemonFamilyIdOuterClass;
import POGOProtos.Enums.TeamColorOuterClass;
import com.pokegoapi.api.PokemonGo;

import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.auth.GoogleUserCredentialProvider;

import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import okhttp3.OkHttpClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class main {
    private static PokemonGo go;

    private static String username;
    private static int level;
    private static String team;
    private static long exp;
    private static int candy;

    private static List<Pokemon> mons;
    private static List<Evolution> allEvolutions = new ArrayList<Evolution>();

    public static void init() throws Exception {
        OkHttpClient httpClient = new OkHttpClient();

        go = new PokemonGo(new GoogleUserCredentialProvider(httpClient, "{Your Refresh Token}"), httpClient);

        username = go.getPlayerProfile().getPlayerData().getUsername();
        level = go.getPlayerProfile().getStats().getLevel();
        TimeUnit.SECONDS.sleep(1);
        team = teamFix(go.getPlayerProfile().getPlayerData().getTeam());
        exp = go.getPlayerProfile().getStats().getExperience();
        TimeUnit.SECONDS.sleep(1);
        mons = go.getInventories().getPokebank().getPokemons();
        candy = go.getInventories().getCandyjar().getCandies(PokemonFamilyIdOuterClass.PokemonFamilyId.forNumber(2));
    }

    public static void mainframe() throws LoginFailedException, RemoteServerException {
        Frame main = new JFrame();
        main.setLayout(new BorderLayout());

        String htmlLabel =  "<html><center>" +
                            "<br><b>" + username + "</b>" +
                            "<sub><br></sub>" +
                            "Team " + team + "<br>" +
                            "Level " + level + " (" + exp + " XP)" +
                            "<br><br></center></html>";

        JLabel infoLabel = new JLabel(htmlLabel, JLabel.CENTER);
        main.add(infoLabel, BorderLayout.NORTH);

        main.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        boolean found = false;

        for (Pokemon poke : mons) {
            if (poke.getCandiesToEvolve() > 0) {
                if (poke.getPokemonFamily().toString().substring(7).equals(poke.getPokemonId().toString())) {
                    found = false;

                    for (Evolution evol : allEvolutions) {
                        if (evol.getFamily().equals(poke.getPokemonFamily().toString())) {
                            found = true;
                            evol.increment();
                        }
                    }

                    if (!found) {
                        Evolution temp = new Evolution(poke.getPokemonFamily().toString(), poke.getCandy(), poke.getCandiesToEvolve());
                        allEvolutions.add(temp);
                    }
                }
            }
        }

        Object columnNames[] = {"Family Name", "Candies", "Candies to Evolve", "Evolutions", "# of Pokemon"};

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        for (Evolution evol : allEvolutions) {
            tableModel.addRow(evol.toArray());
        }

        JTable table = new JTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(table);

        main.add(scrollPane, BorderLayout.CENTER);

        main.setTitle("Evolution Calculator");
        main.setSize(600, 500);
        main.setVisible(true);
    }

    private static String teamFix(TeamColorOuterClass.TeamColor team) {
        String modifiedTeam ;

        switch (team) {
            case RED:
                modifiedTeam = "Valor";
            break;
            case BLUE:
                modifiedTeam = "Mystic";
            break;
            case YELLOW:
                modifiedTeam = "Instinct";
            break;
            case NEUTRAL:
                modifiedTeam = "Neutral";
            break;
            default:
                modifiedTeam = "unknown";
            break;
        }

        return modifiedTeam;
    }

    public static void main(String[] args) throws Exception {

        init();

        if (go.equals(null)) {
            System.out.print("Failed to initialize!");
        } else {
            mainframe();
        }
    }
}