import java.util.Random;

public class AntColony{

    // ----- Problem setup -----
    static final int N = 4;
    static final int SRC = 0, DEST = 3;

    static final double[][] COST = {
            {0, 5, 15, 4},
            {5, 0, 4, 8},
            {15, 4, 0, 1},
            {4, 8, 1, 0}
    };

    // pheromone[i][j] == pheromone[j][i]
    static final double[][] PH = new double[N][N];

    // ACO parameters
    static final double EVAPORATION = 1.0; // 1 = no evaporation (same as your code)
    static final double ALPHA = 1.0;       // pheromone influence
    static final double BETA  = 2.0;       // heuristic (1/cost) influence

    static final Random RNG = new Random();

    // ---------- Utilities ----------
    static double pathCost(int[] path, int len) {
        double sum = 0;
        for (int i = 0; i < len - 1; i++) sum += COST[path[i]][path[i + 1]];
        return sum;
    }

    static void addPheromoneOnPath(int[] path, int len, double amount) {
        for (int i = 0; i < len - 1; i++) {
            int u = path[i], v = path[i + 1];
            PH[u][v] += amount;
            PH[v][u] += amount; // undirected
        }
    }

    static void evaporateAll(double factor) {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                PH[i][j] *= factor;
    }

    static void printMatrix(double[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++)
                System.out.printf("%.3f\t", m[i][j]);
            System.out.println();
        }
    }

    // ---------- Step 1: three fixed ants ----------
    static void seedPheromoneWithThreeAnts() {
        System.out.println("=== Step 1: Fixed Paths for First 3 Ants ===");

        int[][] ants = {
                {0, 1, 2, 3, 0}, // A→B→C→D→A
                {0, 2, 1, 3, 0}, // A→C→B→D→A
                {0, 1, 3, 2, 0}  // A→B→D→C→A
        };
        int L = 5; // all three have length 5

        for (int k = 0; k < ants.length; k++) {
            double c = pathCost(ants[k], L);
            System.out.printf("Ant %d path: ", k + 1);
            for (int i = 0; i < L; i++) System.out.print(ants[k][i] + (i + 1 < L ? " -> " : ""));
            System.out.printf(" | Cost = %.3f%n", c);

            double delta = 1.0 / c;
            addPheromoneOnPath(ants[k], L, delta);
        }

        evaporateAll(EVAPORATION);
        System.out.println("\nPheromone matrix after first 3 ants:");
        printMatrix(PH);
    }

    // ---------- Step 2: build probabilities & pick next (roulette) ----------
    static int pickNextNode(int current, boolean[] visited) {
        double[] prob = new double[N];
        double sum = 0;

        // Compute raw scores
        for (int j = 0; j < N; j++) {
            if (j == current || visited[j]) { prob[j] = 0; continue; }
            // τ^α · (1/cost)^β
            double tau  = Math.max(PH[current][j], 1e-12); // avoid 0^alpha
            double eta  = 1.0 / COST[current][j];
            prob[j] = Math.pow(tau, ALPHA) * Math.pow(eta, BETA);
            sum += prob[j];
        }

        // Normalize + show table
        System.out.println("\nProbabilities from node " + current + ":");
        if (sum > 0) {
            for (int j = 0; j < N; j++) {
                prob[j] /= sum;
                if (!visited[j] && j != current) System.out.printf("To node %d → %.4f%n", j, prob[j]);
            }
        } else {
            // All zero: uniform over feasible (rare with seeded PH, but safe)
            int options = 0;
            for (int j = 0; j < N; j++) if (!visited[j] && j != current) options++;
            double p = options > 0 ? 1.0 / options : 0;
            for (int j = 0; j < N; j++) if (!visited[j] && j != current) {
                prob[j] = p;
                System.out.printf("To node %d → %.4f%n", j, prob[j]);
            }
        }

        // Roulette wheel
        double r = RNG.nextDouble(), cum = 0;
        for (int j = 0; j < N; j++) {
            cum += prob[j];
            if (r <= cum) return j;
        }

        // Fallback: first feasible
        for (int j = 0; j < N; j++) if (!visited[j] && j != current) return j;
        return current;
    }

    static void runFourthAnt() {
        System.out.println("\n=== Step 2: 4th Ant Using Pheromone Matrix ===");

        boolean[] visited = new boolean[N];
        int[] path = new int[N + 1]; // up to N nodes to DEST + one extra to return to SRC
        int len = 0;

        int cur = SRC;
        visited[cur] = true;
        path[len++] = cur;

        // Move until reaching DEST (never revisit due to 'visited')
        while (cur != DEST) {
            int nxt = pickNextNode(cur, visited);
            path[len++] = nxt;
            visited[nxt] = true;
            cur = nxt;
        }

        // Return to source
        path[len++] = SRC;

        double total = pathCost(path, len);
        System.out.print("\n4th Ant path: ");
        for (int i = 0; i < len; i++) System.out.print(path[i] + (i + 1 < len ? " -> " : ""));
        System.out.printf("%nTotal cost for 4th Ant: %.3f%n", total);

        // Update pheromone with 4th ant
        addPheromoneOnPath(path, len, 1.0 / total);

        System.out.println("\nUpdated pheromone matrix after 4th ant:");
        printMatrix(PH);
    }

    // ---------- Main ----------
    public static void main(String[] args) {
        seedPheromoneWithThreeAnts();
        runFourthAnt();
    }
}



// ========> ACO detailed graph <===========


// import numpy as np
// import matplotlib.pyplot as plt
// import networkx as nx
// import time

// # ==============================
// # ACO Setup
// # ==============================
// N = 4
// cost = np.array([
//     [0, 5, 15, 4],
//     [5, 0, 4, 8],
//     [15, 4, 0, 1],
//     [4, 8, 1, 0]
// ])

// pheromone = np.zeros((N, N))
// alpha = 1.0
// beta = 2.0
// source, destination = 0, 3
// np.random.seed(123)

// # Node positions for better visibility
// positions = {
//     0: (0, 0),   # A
//     1: (2, 1.5), # B
//     2: (2, -1.5),# C
//     3: (4, 0)    # D
// }

// labels = {0: "A", 1: "B", 2: "C", 3: "D"}

// # Visualization speed
// STEP_DELAY = 2.0
// MOVE_DELAY = 1.5


// # ==============================
// # Helper Functions
// # ==============================
// def calculate_path_cost(path):
//     return sum(cost[path[i], path[i+1]] for i in range(len(path) - 1))


// def update_pheromone(path):
//     L = calculate_path_cost(path)
//     delta = 1.0 / L
//     for i in range(len(path) - 1):
//         u, v = path[i], path[i+1]
//         pheromone[u, v] += delta
//         pheromone[v, u] += delta


// def draw_graph(title, highlight_edges=None, highlight_nodes=None, total_cost=None):
//     plt.clf()
//     G = nx.Graph()

//     for i in range(N):
//         for j in range(i + 1, N):
//             if cost[i, j] > 0:
//                 G.add_edge(i, j, weight=cost[i, j])

//     edge_colors = []
//     edge_widths = []
//     for (u, v) in G.edges():
//         strength = pheromone[u, v]
//         edge_colors.append((1 - min(strength, 1), 0.1, min(strength, 1)))
//         edge_widths.append(1 + 6 * pheromone[u, v])

//     node_colors = ['lightblue'] * N
//     if highlight_nodes:
//         for n in highlight_nodes:
//             node_colors[n] = 'orange'

//     # Draw nodes and edges
//     nx.draw(
//     G, positions, with_labels=True, labels=labels,
//     node_color=node_colors, node_size=1300,
//     edge_color=edge_colors, width=edge_widths,
//     font_size=14, font_weight='bold'
//    )

//     # Edge labels (move them slightly above edges)
//     edge_labels = {(u, v): f"{cost[u, v]}" for (u, v) in G.edges()}
//     label_pos = {k: (v[0], v[1] + 0.1) for k, v in nx.spring_layout(G, pos=positions, fixed=positions).items()}
//     nx.draw_networkx_edge_labels(G, positions, edge_labels=edge_labels, font_color='black',
//                                  bbox=dict(facecolor='white', edgecolor='none', alpha=0.7), rotate=False)

//     # Highlight specific edges
//     if highlight_edges:
//         nx.draw_networkx_edges(G, positions, edgelist=highlight_edges,
//                        edge_color='orange', width=5, style='solid')


//     # Show total path cost clearly on graph
//     if total_cost is not None:
//         plt.text(0.5, -0.8, f"Total Path Cost: {total_cost:.2f}",
//                  fontsize=14, fontweight='bold', color='darkgreen',
//                  ha='center', transform=plt.gca().transAxes,
//                  bbox=dict(facecolor='white', edgecolor='green', boxstyle='round,pad=0.4'))

//     plt.title(title, fontsize=14, fontweight='bold')
//     plt.pause(STEP_DELAY)


// # ==============================
// # Step 1: Fixed Paths for First 3 Ants
// # ==============================
// print("=== Step 1: Fixed Paths for First 3 Ants ===")
// ants = [
//     [0, 1, 2, 3, 0],  # A → B → C → D → A
//     [0, 2, 1, 3, 0],  # A → C → B → D → A
//     [0, 1, 3, 2, 0]   # A → B → D → C → A
// ]

// plt.figure(figsize=(8, 6))

// for i, path in enumerate(ants, start=1):
//     L = calculate_path_cost(path)
//     print(f"\nAnt {i}: path = {path}, cost = {L:.2f}")
//     highlight = [(path[k], path[k + 1]) for k in range(len(path) - 1)]
//     draw_graph(f"Ant {i} traversing path", highlight_edges=highlight, highlight_nodes=path, total_cost=L)
//     update_pheromone(path)
//     draw_graph(f"Pheromone Update after Ant {i}", highlight_edges=highlight, total_cost=L)
//     input("Press Enter to continue...")

// print("\nPheromone matrix after 3 ants:")
// print(np.round(pheromone, 3))


// # ==============================
// # Step 2: 4th Ant Exploration
// # ==============================
// print("\n=== Step 2: 4th Ant Using Pheromone Matrix ===")
// visited = np.zeros(N, dtype=bool)
// path = [source]
// visited[source] = True
// current = source

// draw_graph("4th Ant Starting at Node A", highlight_nodes=[source])

// while current != destination:
//     probs = np.zeros(N)
//     total = 0
//     for j in range(N):
//         if not visited[j] and j != current:
//             tau = pheromone[current, j] ** alpha
            
//             eta = (1.0 / cost[current, j]) ** beta if cost[current, j] > 0 else 0
//             probs[j] = tau * eta
//             total += probs[j]

//     probs /= total if total > 0 else 1
//     print(f"\nFrom node {current} → Probabilities: {np.round(probs, 3)}")

//     r = np.random.rand()
//     cum = 0
//     next_node = -1
//     for j in range(N):
//         cum += probs[j]
//         if r <= cum:
//             next_node = j
//             break

//     if next_node == -1:
//         next_node = np.where(~visited)[0][0]

//     path.append(next_node)
//     visited[next_node] = True
//     draw_graph(f"4th Ant moves: {labels[current]} → {labels[next_node]}",
//                highlight_edges=[(current, next_node)], highlight_nodes=[current, next_node])
//     time.sleep(MOVE_DELAY)
//     current = next_node

// # Return to source
// path.append(source)
// total_cost = calculate_path_cost(path)
// draw_graph("4th Ant Returning Home", highlight_edges=[(path[-2], path[-1])], total_cost=total_cost)

// print("\n4th Ant final path:", path)
// print(f"Total cost for 4th Ant: {total_cost:.3f}")

// # Update pheromone after 4th ant
// update_pheromone(path)
// draw_graph("Final Pheromone after 4th Ant", total_cost=total_cost)

// print("\nFinal pheromone matrix:")
// print(np.round(pheromone, 3))

// plt.show()



