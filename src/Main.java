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
    public static Map<String, Vertice> grafo = new HashMap<String, Vertice>();
    public static final URL path = Main.class.getResource("gameofthrones.txt");
    //private static final String path = "C:\\Users\\gusta\\Desktop\\NetworkOfThrones\\src\\gameofthrones.txt";
    
    public static void main(String[] args) throws IOException {
        int key = 0;
        Scanner ler = new Scanner(System.in);
        //Vertice[] grafo = new Vertice[107];
        /*for(int i = 0; i<grafo.length; i++){
            grafo[i] = new Vertice();
        }*/
        FileReader arq = new FileReader(path.getPath());
        BufferedReader buffRead = new BufferedReader(arq);
        String linha = "";
        String array[] = new String[2];
        linha = buffRead.readLine();
        ArrayList<String> verificaNomes = new ArrayList<>();
        
        while (linha != null){
            array = linha.split(",");
            if (!grafo.containsKey(array[0])){
                Vertice v = new Vertice();
                v.nome = array[0]; v.addAdj(array[1]);
                grafo.put(array[0], v);
            }
            else {
                grafo.get(array[0]).addAdj(array[1]);
            }
            if (!grafo.containsKey(array[1])){
                Vertice v = new Vertice();
                v.nome = array[1]; v.addAdj(array[0]);
                grafo.put(array[1], v);
            }
            else {
                grafo.get(array[1]).addAdj(array[0]);
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
            System.out.println("2 - Sair");
            opcao = ler.nextInt();
            if (opcao == 1) {
                System.out.println("DISTÂNCIA ENTRE DOIS PERSONAGENS");
                System.out.println("Digite o nome dos personagens que deseja calcular a distância: ");
                System.out.print("Personagem 1: ");
                String per1 = ler.next();
                System.out.print("Personagem 2: ");
                String per2 = ler.next();
                if (!grafo.containsKey(per1) || !grafo.containsKey(per2)){
                    System.out.println("Nome inválido");
                }else{
                    BFS(grafo, per1);
                    System.out.println("A distância de " + per1 + " a " + per2 + " é " + grafo.get(per2).dist);
                    System.out.println();
                }

            }
        }while (opcao>0 && opcao<2);
    }

    public static Map<String, Vertice> inicializa(Map<String, Vertice> grafo){
        for (Map.Entry<String, Vertice> entry : grafo.entrySet()){
            entry.getValue().cor = Cor.BRANCO;
            entry.getValue().dist = Integer.MAX_VALUE;
            entry.getValue().pred = null;
        }
        return grafo;
    }

    public static void BFS(Map<String, Vertice> grafo, String per1){
        inicializa(grafo);
        Vertice k = grafo.get(per1);

        k.cor = Cor.CINZA;
        k.dist = 0;
        k.pred = null;
        
        Queue<String> Q = new LinkedList<>();
        Q.add(per1);
        while (!Q.isEmpty()) {
            String s = Q.remove();
            for (String v : grafo.get(s).adj) {
                if (grafo.get(v).cor == Cor.BRANCO) {
                    grafo.get(v).cor = Cor.CINZA;
                    grafo.get(v).dist = grafo.get(s).dist + 1;
                    grafo.get(v).pred = grafo.get(s);
                    Q.add(v);
                }
            }
        }
        k.cor = Cor.PRETO;
    }
}
