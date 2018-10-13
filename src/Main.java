
import java.net.URL;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

enum Cor{
    BRANCO,
    CINZA,
    PRETO
}

public class Main {
    public static int tempo;
    public static Map<String, Integer> mapaNomes = new HashMap<String, Integer>();
    public static final URL path = Main.class.getResource("gameofthrones.txt");
    //private static final String path = "C:\\Users\\gusta\\Desktop\\NetworkOfThrones\\src\\gameofthrones.txt";
    
    public static void main(String[] args) throws IOException {
        int key = 0;
        Scanner ler = new Scanner(System.in);
        Vertice[] grafo = new Vertice[107];
        for(int i = 0; i<grafo.length; i++){
            grafo[i] = new Vertice();
        }
        FileReader arq = new FileReader(path.getPath());
        BufferedReader buffRead = new BufferedReader(arq);
        String linha = "";
        String array[] = new String[3];
        linha = buffRead.readLine();
        ArrayList<String> verificaNomes = new ArrayList<>();
        
        while (linha != null){
            array = linha.split(",");
            if (!verificaNomes.contains(array[0])){
                verificaNomes.add(array[0]);
                grafo[key].nome = array[0];
                grafo[key].addAdj(array[1]);
                mapaNomes.put(array[0], key);
                key++;
            }
            else {
                grafo[mapaNomes.get(array[0])].addAdj(array[1]);
            }
            if (!verificaNomes.contains(array[1])){
                verificaNomes.add(array[1]);
                grafo[key].nome = array[1];
                grafo[key].addAdj(array[0]);
                mapaNomes.put(array[1], key);
                key++;
            }
            else {
                grafo[mapaNomes.get(array[1])].addAdj(array[0]);
            }
            linha = buffRead.readLine();
        }
        if (linha == null)
            buffRead.close();

        int opcao;
        do {
            System.out.println("******NETWORK OF THRONES******");
            System.out.println("Selecione a opção desejada: ");
            System.out.println("1 - Distância entre dois personagens");
            System.out.println("2 - Pontos de articulação");
            System.out.println("3 - Pontes");
            System.out.println("4 - Sair");
            opcao = ler.nextInt();
            if (opcao == 1) {
                System.out.println("DISTÂNCIA ENTRE DOIS PERSONAGENS");
                System.out.println("Digite o nome dos personagens que deseja calcular a distância: ");
                System.out.print("Personagem 1: ");
                String per1 = ler.next();
                System.out.print("Personagem 2: ");
                String per2 = ler.next();
                BFS(grafo, per1);
                System.out.println("A distância de " + per1 + " a " + per2 + " é " + grafo[mapaNomes.get(per2)].dist);
                System.out.println();
            }
            if (opcao == 2) {
                tempo = 0;
                inicializa(grafo);
                ArrayList<String> Q = new ArrayList<>();
                System.out.println();
                System.out.println("PONTOS DE ARTICULAÇÃO:");
                articulation_point(grafo, 0, Q);
                System.out.println();
            }
            if (opcao == 3) {
                tempo = 0;
                inicializa(grafo);
                System.out.println();
                System.out.println("PONTES:");
                bridges(grafo, 0);
                System.out.println();
            }
        }while (opcao>0 && opcao<4);
    }

    public static Vertice[] inicializa(Vertice[] grafo){
        for (int i = 0; i<grafo.length; i++){
            grafo[i].cor = Cor.BRANCO;
            grafo[i].dist = Integer.MAX_VALUE;
            grafo[i].pred = null;
            grafo[i].low=0;
            grafo[i].d=0;
            grafo[i].f=0;
        }
        return grafo;
    }

    public static void BFS(Vertice[] grafo, String per1){
        inicializa(grafo);
        int k = mapaNomes.get(per1);
        
        grafo[k].cor = Cor.CINZA;
        grafo[k].dist = 0;
        grafo[k].pred = null;
        
        Queue<String> Q = new LinkedList<>();
        Q.add(per1);
        while (!Q.isEmpty()) {
            String s = Q.remove();
            for (String v : grafo[mapaNomes.get(s)].adj) {
                if (grafo[mapaNomes.get(v)].cor == Cor.BRANCO) {
                    grafo[mapaNomes.get(v)].cor = Cor.CINZA;
                    grafo[mapaNomes.get(v)].dist = grafo[mapaNomes.get(s)].dist + 1;
                    grafo[mapaNomes.get(v)].pred = grafo[mapaNomes.get(s)];
                    Q.add(v);
                }
            }
        }
        grafo[k].cor = Cor.PRETO;
    }

    public static void articulation_point(Vertice[] grafo, int u, ArrayList Q){
        tempo++;
        grafo[u].cor = Cor.CINZA;
        grafo[u].d = tempo;
        grafo[u].low = grafo[u].d;
        
        grafo[u].adj.forEach((v) -> {
            if (grafo[mapaNomes.get(v)].cor == Cor.BRANCO){
                grafo[mapaNomes.get(v)].pred = grafo[u];
                articulation_point(grafo, mapaNomes.get(v), Q);
                if (grafo[u].pred == null){
                    if (grafo[u].adj.indexOf(v) >= 1){
                        if (!Q.contains(grafo[u].nome)) {
                            Q.add(grafo[u].nome);
                            System.out.println(grafo[u].nome + " é ponto de articulação");
                        }
                    }
                }
                else {
                    grafo[u].low = Math.min(grafo[u].low, grafo[mapaNomes.get(v)].low);
                    if (grafo[mapaNomes.get(v)].low >= grafo[u].d){
                        if (!Q.contains(grafo[u].nome)) {
                            Q.add(grafo[u].nome);
                            System.out.println(grafo[u].nome + " é ponto de articulação");
                        }
                    }
                }
            }
            else {
                if ((grafo[mapaNomes.get(v)] != grafo[u].pred) && grafo[mapaNomes.get(v)].d < grafo[u].d)
                    grafo[u].low = Math.min(grafo[u].low,  grafo[mapaNomes.get(v)].d);
            }
        });
        
        grafo[u].cor = Cor.PRETO;
        tempo++;
        grafo[u].f = tempo;
    }

    public static void bridges(Vertice[] grafo, int u){
        tempo++;
        grafo[u].cor = Cor.CINZA;
        grafo[u].d = tempo;
        grafo[u].low = grafo[u].d;
        
        grafo[u].adj.forEach((v) -> {
            if (grafo[mapaNomes.get(v)].cor == Cor.BRANCO){
                grafo[mapaNomes.get(v)].pred = grafo[u];
                bridges(grafo, mapaNomes.get(v));
                grafo[u].low = Math.min(grafo[u].low, grafo[mapaNomes.get(v)].low);
                if (grafo[mapaNomes.get(v)].low > grafo[u].d)
                    System.out.println("Há ponte entre "+grafo[u].nome+" e "+v);
            }
            else {
                if ((grafo[mapaNomes.get(v)] != grafo[u].pred) && (grafo[mapaNomes.get(v)].d < grafo[u].d))
                    grafo[u].low = Math.min(grafo[u].low, grafo[mapaNomes.get(v)].d);
            }
        });
        
        grafo[u].cor = Cor.PRETO;
        tempo++;
        grafo[u].f = tempo;
    }
}
