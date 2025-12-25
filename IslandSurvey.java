public class IslandSurvey {
    Partition<String> islands = new Partition<>();
    Node<String>[][] clusters;

    public IslandSurvey(Partition<String> islands) {
        this.islands = islands;
    }

    public int[][] initialReading(int S, int T, int[][] matrix) {
        clusters = (Node<String>[][]) new Node[matrix.length][matrix[0].length];
        // make cluster for every land cell
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 1) {
                    clusters[i][j] = islands.makeCluster(i + "'" + j);
                }
            }
        }
        // merge connected land into islands
        islandCombining(matrix, islands);
        return matrix;
    }

    public int[][] floodOnIslands(String[] newLandStr, int changed, int[][] matrix) {
        int[] newLand = new int[newLandStr.length];
        for (int i = 0; i < newLand.length; i++) {
            newLand[i] = Integer.parseInt(newLandStr[i]);
        }
        // add new land clusters
        for (int i = 0; i < changed * 2; i += 2) {
            clusters[newLand[i]][newLand[i + 1]] =
                islands.makeCluster(newLand[i] + "'" + newLand[i + 1]);
            matrix[newLand[i]][newLand[i + 1]] = 1;
        }
        // recheck connected land
        islandCombining(matrix, islands);
        return matrix;
    }

    public void islandCombining(int[][] matrix, Partition<String> islands) {
        // merge adjacent land cells in 4 directions
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 1) {

                    if (i > 0 && matrix[i - 1][j] == 1) {
                        if (islands.find(clusters[i][j]) != islands.find(clusters[i - 1][j])) {
                            islands.union(clusters[i][j], clusters[i - 1][j]);
                        }
                    }

                    if (i < matrix.length - 1 && matrix[i + 1][j] == 1) {
                        if (islands.find(clusters[i][j]) != islands.find(clusters[i + 1][j])) {
                            islands.union(clusters[i][j], clusters[i + 1][j]);
                        }
                    }

                    if (j > 0 && matrix[i][j - 1] == 1) {
                        if (islands.find(clusters[i][j]) != islands.find(clusters[i][j - 1])) {
                            islands.union(clusters[i][j], clusters[i][j - 1]);
                        }
                    }

                    if (j < matrix[i].length - 1 && matrix[i][j + 1] == 1) {
                        if (islands.find(clusters[i][j]) != islands.find(clusters[i][j + 1])) {
                            islands.union(clusters[i][j], clusters[i][j + 1]);
                        }
                    }
                }
            }
        }
    }

    public String printSurvey(Partition<String> islands) {
        String numberOfIslands = Integer.toString(islands.numberOfClusters());
        String listOfIslandSizes = "";
        String totalIslands;
        int count = 0;

        // handle case with no islands
        if (numberOfIslands.equals("0")) {
            listOfIslandSizes = "-1";
        } else {
            // list and count all island sizes
            for (int j = 0; j < islands.clusterSizes().length; j++) {
                if (j != islands.clusterSizes().length - 1) {
                    listOfIslandSizes += islands.clusterSizes()[j] + " ";
                    count += islands.clusterSizes()[j];
                } else {
                    listOfIslandSizes += islands.clusterSizes()[j];
                    count += islands.clusterSizes()[j];
                }
            }
        }

        totalIslands = Integer.toString(count);
        return numberOfIslands + "\n" + listOfIslandSizes + "\n" + totalIslands;
    }

    public static void main(String[] args) {
        Partition<String> islands = new Partition<>();
        IslandSurvey islandSurvey = new IslandSurvey(islands);
        java.util.Scanner sc = new java.util.Scanner(System.in);

        int S = sc.nextInt();
        int T = sc.nextInt();
        int[][] matrix = new int[S][T];

        // read initial grid
        for (int i = 0; i < S; i++) {
            String line = sc.next();
            for (int j = 0; j < T; j++) {
                matrix[i][j] = line.charAt(j) - '0';
            }
        }

        int phases = sc.nextInt();
        matrix = islandSurvey.initialReading(S, T, matrix);
        System.out.println(islandSurvey.printSurvey(islands));
        System.out.println();

        // process flood updates
        for (int i = 0; i < phases; i++) {
            int changes = sc.nextInt();
            String[] newLandArr = new String[changes * 2];

            for (int j = 0; j < changes * 2; j++) {
                newLandArr[j] = sc.next();
            }

            matrix = islandSurvey.floodOnIslands(newLandArr, changes, matrix);
            System.out.println(islandSurvey.printSurvey(islands));

            if (i != phases - 1) {
                System.out.println("");
            }
        }
    }
}
