public class IslandLakeSurvey {
    Partition<String> islands = new Partition<>();
    Partition<String> lakes = new Partition<>();
    Node<String>[][] IslandClusters;
    Node<String>[][] LakeClusters;

    // constructor initializes island and lake partitions
    public IslandLakeSurvey(Partition<String> islands, Partition<String> lakes) {
        this.islands = islands;
        this.lakes = lakes;
    }

    // builds initial island clusters and merges connected land
    public int[][] initialReadingIslands(int S, int T, int[][] matrix) {
        IslandClusters = (Node<String>[][]) new Node[S][T];
        for (int i = 0; i < S; i++) {
            for (int j = 0; j < T; j++) {
                if (matrix[i][j] == 1) {
                    IslandClusters[i][j] = islands.makeCluster(i + "'" + j);
                }
            }
        }
        islandCombining(matrix, islands);
        return matrix;
    }

    // builds initial lake clusters (unvisited inner water)
    public void initialReadingLake(int S, int T, int[][] matrix) {
        LakeClusters = (Node<String>[][]) new Node[S][T];
        boolean[][] visited = new boolean[S][T];
        // mark all boundary water as visited
        for (int i = 0; i < S; i++) {
            for (int j = 0; j < T; j++) {
                if (i == 0 || j == 0 || i == S - 1 || j == T - 1) {
                    if (matrix[i][j] == 0) {
                        if (!visited[i][j]) {
                            markConnectedWater(matrix, visited, i, j, S, T);
                        }
                    }
                }
            }
        }
        // create clusters for enclosed (lake) water
        for (int i = 0; i < S; i++) {
            for (int j = 0; j < T; j++) {
                if (matrix[i][j] == 0) {
                    if (!visited[i][j]) {
                        LakeClusters[i][j] = lakes.makeCluster(i + "'" + j);
                    }
                }
            }
        }
        lakeCombining(matrix, lakes);
    }

    // marks all boundary-connected water cells using BFS
    private void markConnectedWater(int[][] matrix, boolean[][] visited, int i, int j, int S, int T) {
        int[] rowQ = new int[S * T];
        int[] colQ = new int[S * T];
        int head = 0;
        int tail = 0;
        visited[i][j] = true;
        rowQ[tail] = i;
        colQ[tail] = j;
        tail++;
        while (head < tail) {
            int x = rowQ[head];
            int y = colQ[head];
            head++;
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    int nx = x + dx;
                    int ny = y + dy;
                    if (nx < 0 || ny < 0 || nx >= S || ny >= T) continue;
                    if (matrix[nx][ny] == 1) continue;
                    if (visited[nx][ny]) continue;
                    visited[nx][ny] = true;
                    rowQ[tail] = nx;
                    colQ[tail] = ny;
                    tail++;
                }
            }
        }
    }

    // merges all connected land cells into island clusters
    public void islandCombining(int[][] matrix, Partition<String> islands) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 1) {
                    if (i > 0 && matrix[i - 1][j] == 1) {
                        if (islands.find(IslandClusters[i][j]) != islands.find(IslandClusters[i - 1][j])) {
                            islands.union(IslandClusters[i][j], IslandClusters[i - 1][j]);
                        }
                    }
                    if (i < matrix.length - 1 && matrix[i + 1][j] == 1) {
                        if (islands.find(IslandClusters[i][j]) != islands.find(IslandClusters[i + 1][j])) {
                            islands.union(IslandClusters[i][j], IslandClusters[i + 1][j]);
                        }
                    }
                    if (j > 0 && matrix[i][j - 1] == 1) {
                        if (islands.find(IslandClusters[i][j]) != islands.find(IslandClusters[i][j - 1])) {
                            islands.union(IslandClusters[i][j], IslandClusters[i][j - 1]);
                        }
                    }
                    if (j < matrix[i].length - 1 && matrix[i][j + 1] == 1) {
                        if (islands.find(IslandClusters[i][j]) != islands.find(IslandClusters[i][j + 1])) {
                            islands.union(IslandClusters[i][j], IslandClusters[i][j + 1]);
                        }
                    }
                }
            }
        }
    }

    // merges all connected water cells into lake clusters
    public void lakeCombining(int[][] matrix, Partition<String> lakes) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) {
                    if (LakeClusters[i][j] != null) {
                        if (i > 0 && matrix[i - 1][j] == 0) {
                            if (LakeClusters[i - 1][j] != null) {
                                if (lakes.find(LakeClusters[i][j]) != lakes.find(LakeClusters[i - 1][j])) {
                                    lakes.union(LakeClusters[i][j], LakeClusters[i - 1][j]);
                                }
                            }
                        }
                        if (i < matrix.length - 1 && matrix[i + 1][j] == 0) {
                            if (LakeClusters[i + 1][j] != null) {
                                if (lakes.find(LakeClusters[i][j]) != lakes.find(LakeClusters[i + 1][j])) {
                                    lakes.union(LakeClusters[i][j], LakeClusters[i + 1][j]);
                                }
                            }
                        }
                        if (j > 0 && matrix[i][j - 1] == 0) {
                            if (LakeClusters[i][j - 1] != null) {
                                if (lakes.find(LakeClusters[i][j]) != lakes.find(LakeClusters[i][j - 1])) {
                                    lakes.union(LakeClusters[i][j], LakeClusters[i][j - 1]);
                                }
                            }
                        }
                        if (j < matrix[i].length - 1 && matrix[i][j + 1] == 0) {
                            if (LakeClusters[i][j + 1] != null) {
                                if (lakes.find(LakeClusters[i][j]) != lakes.find(LakeClusters[i][j + 1])) {
                                    lakes.union(LakeClusters[i][j], LakeClusters[i][j + 1]);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // finds which island (if any) fully encloses a given lake
    private Node<String> findSurroundingIsland(int[][] matrix, int i, int j, int S, int T, boolean[][] visited) {
        int[] rowQ = new int[S * T];
        int[] colQ = new int[S * T];
        int head = 0;
        int tail = 0;
        Node<String> foundIsland = null;
        boolean touchesEdge = false;
        visited[i][j] = true;
        rowQ[tail] = i;
        colQ[tail] = j;
        tail++;
        while (head < tail) {
            int x = rowQ[head];
            int y = colQ[head];
            head++;
            for (int a = -1; a <= 1; a++) {
                for (int b = -1; b <= 1; b++) {
                    if (a == 0 && b == 0) continue;
                    int nx = x + a;
                    int ny = y + b;
                    if (nx < 0 || ny < 0 || nx >= S || ny >= T) {
                        touchesEdge = true;
                        continue;
                    }
                    if (matrix[nx][ny] == 0) {
                        if (LakeClusters[nx][ny] != null) {
                            if (!visited[nx][ny]) {
                                visited[nx][ny] = true;
                                rowQ[tail] = nx;
                                colQ[tail] = ny;
                                tail++;
                            }
                        }
                    } else {
                        if (IslandClusters[nx][ny] != null) {
                            Node<String> islandRoot = islands.find(IslandClusters[nx][ny]);
                            if (foundIsland == null) {
                                foundIsland = islandRoot;
                            } else {
                                if (foundIsland != islandRoot) {
                                    return null;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (touchesEdge) return null;
        return foundIsland;
    }

    // sorts an array in descending order (selection sort)
    private void sortDesc(int[] a) {
        for (int i = 0; i < a.length; i++) {
            int maxI = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[j] > a[maxI]) {
                    maxI = j;
                }
            }
            int tmp = a[i];
            a[i] = a[maxI];
            a[maxI] = tmp;
        }
    }

    // computes final island sizes with lake areas added
    private void buildAugmentedIslandSizes(int[][] matrix, int S, int T, int numberOfIslands, int[] outSizes, int[] outLakeArea) {
        Node<String>[] leaders = (Node<String>[]) new Node[numberOfIslands];
        int k = 0;
        // gather all unique island roots
        for (int i = 0; i < S; i++) {
            for (int j = 0; j < T; j++) {
                if (matrix[i][j] == 1) {
                    if (IslandClusters[i][j] != null) {
                        Node<String> root = islands.find(IslandClusters[i][j]);
                        int pos = -1;
                        for (int u = 0; u < k; u++) {
                            if (leaders[u] == root) {
                                pos = u;
                                break;
                            }
                        }
                        if (pos == -1) {
                            leaders[k] = root;
                            outSizes[k] = root.getClusterSize();
                            k++;
                        }
                    }
                }
            }
        }
        boolean[][] visitedLake = new boolean[S][T];
        int totalLake = 0;
        // add enclosed lakes to island areas
        for (int i = 0; i < S; i++) {
            for (int j = 0; j < T; j++) {
                if (matrix[i][j] == 0) {
                    if (LakeClusters[i][j] != null) {
                        if (!visitedLake[i][j]) {
                            Node<String> lakeRoot = lakes.find(LakeClusters[i][j]);
                            int lakeSize = lakes.clusterSize(lakeRoot);
                            Node<String> enclosing = findSurroundingIsland(matrix, i, j, S, T, visitedLake);
                            if (enclosing != null) {
                                Node<String> encRoot = islands.find(enclosing);
                                for (int u = 0; u < k; u++) {
                                    if (leaders[u] == encRoot) {
                                        outSizes[u] = outSizes[u] + lakeSize;
                                        break;
                                    }
                                }
                            }
                            totalLake += lakeSize;
                        }
                    }
                }
            }
        }
        outLakeArea[0] = totalLake;
    }

    // processes new land added during floods
    public int[][] floods(String[] newLandStr, int changed, int[][] matrix, int S, int T) {
        int[] newLand = new int[newLandStr.length];
        for (int i = 0; i < newLand.length; i++) {
            newLand[i] = Integer.parseInt(newLandStr[i]);
        }
        // create new clusters for new land cells
        for (int i = 0; i < changed * 2; i += 2) {
            int x = newLand[i];
            int y = newLand[i + 1];
            matrix[x][y] = 1;
            IslandClusters[x][y] = islands.makeCluster(x + "'" + y);
        }
        islandCombining(matrix, islands);
        initialReadingLake(S, T, matrix);
        return matrix;
    }

    // prints number of islands, lakes, and their total areas
    public String printSurvey(int[][] matrix, int S, int T) {
        int numIslands = islands.numberOfClusters();
        int numLakes = lakes.numberOfClusters();
        String numberOfIslands = Integer.toString(numIslands);
        String numberOfLakes = Integer.toString(numLakes);
        String listOfIslandSizes = "";
        int totalIslandArea = 0;
        int lakeArea = 0;
        if (numIslands == 0) {
            listOfIslandSizes = "-1";
        } else {
            int[] augmented = new int[numIslands];
            int[] lakeAreaOut = new int[1];
            buildAugmentedIslandSizes(matrix, S, T, numIslands, augmented, lakeAreaOut);
            sortDesc(augmented);
            for (int i = 0; i < augmented.length; i++) {
                totalIslandArea += augmented[i];
                listOfIslandSizes += augmented[i];
                if (i != augmented.length - 1) {
                    listOfIslandSizes += " ";
                }
            }
            lakeArea = lakeAreaOut[0];
        }
        if (numLakes > 0 && numIslands == 0) {
            int[] lakeSizes = lakes.clusterSizes();
            for (int i = 0; i < lakeSizes.length; i++) {
                lakeArea += lakeSizes[i];
            }
        }
        return numberOfIslands + "\n" + listOfIslandSizes + "\n" + totalIslandArea + "\n" + numberOfLakes + "\n" + lakeArea;
    }

    // main method for reading input and running the survey
    public static void main(String[] args) {
        Partition<String> islands = new Partition<>();
        Partition<String> lakes = new Partition<>();
        IslandLakeSurvey survey = new IslandLakeSurvey(islands, lakes);
        java.util.Scanner sc = new java.util.Scanner(System.in);
        int S = sc.nextInt();
        int T = sc.nextInt();
        int[][] matrix = new int[S][T];
        // read initial map
        for (int i = 0; i < S; i++) {
            String line = sc.next();
            for (int j = 0; j < T; j++) {
                matrix[i][j] = line.charAt(j) - '0';
            }
        }
        int phases = sc.nextInt();
        matrix = survey.initialReadingIslands(S, T, matrix);
        survey.initialReadingLake(S, T, matrix);
        System.out.println(survey.printSurvey(matrix, S, T));
        System.out.println();
        // process each flood phase
        for (int p = 0; p < phases; p++) {
            int changes = sc.nextInt();
            String[] newLand = new String[changes * 2];
            for (int j = 0; j < changes * 2; j++) {
                newLand[j] = sc.next();
            }
            matrix = survey.floods(newLand, changes, matrix, S, T);
            System.out.println(survey.printSurvey(matrix, S, T));
            if (p != phases - 1) {
                System.out.println();
            }
        }
    }
}
