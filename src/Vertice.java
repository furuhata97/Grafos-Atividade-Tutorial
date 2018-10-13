
import java.util.ArrayList;

/**
 * Created by GustavoLuiz on 22/05/2017.
 */
public class Vertice {
    int dist;
    ArrayList<String> adj = new ArrayList<>();
    Vertice pred;
    Cor cor;
    int d;
    int f;
    int low;
    String nome = "";

    public void addAdj(String v) {
        adj.add(v);
    }
}
