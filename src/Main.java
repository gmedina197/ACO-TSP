import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    private double c = 1.0; //quantidade de feromonio
    private double alpha = 1; // fator do feromonio pra atratividade
    private double beta = 5; //
    private double evaporation = 0.5; //coeficiente de evaporaçao
    private double Q = 500; //quantidade de feromonio
    private double antFactor = 0.8; //number of ants = antFactor * numCities
    private double randomness = 0.01; //fator randomico

    private int maxIteration = 2000;

    public int n = 0; // # towns
    public int m = 0; // # ants
    private double graph[][] = null;
    private double trails[][] = null;
    private Ant ants[] = null;
    private Random rand = new Random();
    private double probs[] = null;

    private int currentIndex = 0;

    public int[] bestTour;
    public double bestTourLength;

    private class Ant {
        public int tour[] = new int [graph.length];
        public boolean visited[] = new boolean[graph.length];

        public void setVisited(int city) {
            tour[currentIndex + 1] = city;
            visited[city] = true;
        }

        public boolean getVisited(int i) {
            return visited[i];
        }

        public double tourLength() {
            double length = graph[tour[n - 1]][tour[0]];
            for (int i = 0; i < n - 1; i++) {
                length += graph[tour[i]][tour[i + 1]];
            }
            return length;
        }

        public void clear() {
            for (int i = 0; i < n; i++)
                visited[i] = false;
        }
    }

    private void readGraph(String path) throws IOException {
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        String line;
        ArrayList<String[]> values = new ArrayList<>();


        while((line = br.readLine()) != null) {
            String splits[] = line.split(" ");
            values.add(splits);
        }
        br.close();

        graph = new double[values.size()][values.size()];

        for(int i = 0; i < values.size(); i++) {
            for (int j = 0; j < values.size(); j++) {
                if( i != j) {
                    EuclidianDistance ed = new EuclidianDistance(values.get(i), values.get(j));
                    graph[i][j] = graph[j][i] = ed.getDistance();
                }
            }
        }

        n = graph.length;
        m = (int) (n * antFactor);

        trails = new double[n][n];
        probs = new double[n];
        ants = new Ant[m];
        
        for (int j = 0; j < m; j++)
            ants[j] = new Ant();
    }

    private void probTo(Ant ant) {
        int i = ant.tour[currentIndex];
        double denom = 0.0;
        
        for(int l = 0; l < n; l++)
            if(!ant.getVisited(l))
                denom += Math.pow(trails[i][l], alpha) * Math.pow(1.0 / graph[i][l], beta);

        for (int j = 0; j < n; j++) {
            if (ant.getVisited(j)) {
                probs[j] = 0.0;
            } else {
                double numerator = Math.pow(trails[i][j], alpha) * Math.pow(1.0 / graph[i][j], beta);
                probs[j] = numerator / denom;
            }
        }
    }

    private int selectNextTown(Ant ant) {
        if(rand.nextDouble() < randomness) {
            int t = rand.nextInt(n - currentIndex); // cidade aleatoria
            int j = -1;
            for(int i = 0; i < n; i++) {
                if(!ant.getVisited(i)) j++;
                if(j == t) return i;
            }
        }

        probTo(ant);

        double r = rand.nextDouble();
        double tot = 0;
        for(int i = 0; i < n; i++) {
            tot += probs[i];
            if(tot >= r)
                return i;
        }

        throw new RuntimeException("Não deveria ter chegado aqui");
    }

    private void setupAnts() {
        currentIndex = -1;
        for(int i = 0; i < m; i++) {
            ants[i].clear();
            ants[i].setVisited(rand.nextInt(n));
        }
        currentIndex++;
    }

    private void moveAnts() {
        while(currentIndex < n - 1) {
            for(Ant a : ants){
                a.setVisited(selectNextTown(a));
            }
            currentIndex++;
        }
    }

    public int[] solve() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                trails[i][j] = c;
        
        for(int i = 0; i < maxIteration; i++) {
            setupAnts();
            moveAnts();
        }
        
        System.out.println("Best tour length: " + (bestTourLength - n));
        //System.out.println("Best tour:" + tourToString(bestTour));
        return bestTour.clone();
    }

    public static void main(String[] args) throws IOException {

        if(args.length < 1) {
            System.err.println("Por favor, coloque o caminho correto do arquivo nos argumentos!");
            return;
        } 

        Main aco = new Main();
        try {
            aco.readGraph(args[0]);
        } catch (Exception e) {
            System.err.println("Erro lendo o grafo");
            return;
        }

        while(true) aco.solve();
    }
}
