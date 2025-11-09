import java.util.Random;
public class PSO_Rosenbrock_Framework {
    static int N = 20;            
    static int dimensions = 2;   
    static int T = 100;         
    static double w = 0.75;     
    static double c1 = 1.5;     
    static double c2 = 2.0;     
    static double minBound = -5, maxBound = 5;

    static Random rand = new Random();

    public static double rosenbrock(double[] x) {
        double x1 = x[0], x2 = x[1];
        return 100 * Math.pow((x2 - x1 * x1), 2) + Math.pow((1 - x1), 2);
    }

    public static void main(String[] args) {
        double[][] x = new double[N][dimensions];
        double[][] v = new double[N][dimensions];
        double[][] pbest = new double[N][dimensions];
        double[] pbestScore = new double[N];
        double[] gbest = new double[dimensions];
        double gbestScore = Double.MAX_VALUE;

    

        // Randomly initialize x, v, pbest, gbest
        for (int i = 0; i < N; i++) {
            for (int d = 0; d < dimensions; d++) {
                x[i][d] = minBound + (maxBound - minBound) * rand.nextDouble();
                v[i][d] = -1 + 2 * rand.nextDouble();
                pbest[i][d] = x[i][d];
            }
            pbestScore[i] = rosenbrock(x[i]);
            if (pbestScore[i] < gbestScore) {
                gbestScore = pbestScore[i];
                gbest = pbest[i].clone();
            }
        }

        int t = 1;
        while (t <= T) {
           //update pbest and gbest
            for (int i = 0; i < N; i++) {
                double fitness = rosenbrock(x[i]);
                if (fitness < pbestScore[i]) {
                    pbestScore[i] = fitness;
                    pbest[i] = x[i].clone();
                }
                if (fitness < gbestScore) {
                    gbestScore = fitness;
                    gbest = x[i].clone();
                }
            }
            //update velocity and position
            for (int i = 0; i < N; i++) {
                for (int d = 0; d < dimensions; d++) {
                    double r1 = rand.nextDouble();
                    double r2 = rand.nextDouble();

                    v[i][d] = w * v[i][d]
                            + c1 * r1 * (pbest[i][d] - x[i][d])
                            + c2 * r2 * (gbest[d] - x[i][d]);
                    
                    x[i][d] += v[i][d];

                    if (x[i][d] < minBound) x[i][d] = minBound;
                    if (x[i][d] > maxBound) x[i][d] = maxBound;
                }
            }
            t++;
        }

        System.out.println("\nOptimal Solution Found:");
        System.out.printf("x1 = %.6f, x2 = %.6f%n", gbest[0], gbest[1]);
        System.out.printf("f(x) = %.6f%n", gbestScore);
    }
}

//best soln x1= 1 x2 =1 f(x)=0





//=====> PSO DISHA <======

// import numpy as np
// import matplotlib.pyplot as plt
// from mpl_toolkits.mplot3d import Axes3D

// def fitness(position):
//     x = position[..., 0]
//     y = position[..., 1]
//     return 100 * (y - x**2)**2 + (1 - x)**2


// def pso_optimizer(swarm_size, dimn, max_iter, lb, ub, weight, c1, c2, initial_points=None):
//     positions = lb + (ub - lb) * np.random.rand(swarm_size, dimn)
//     if initial_points is not None:
//         initial_points = np.array(initial_points)
//         num_initial_points = len(initial_points)
//         if num_initial_points > swarm_size:
//             raise ValueError("Number of initial_points cannot exceed swarm_size.")

//         print(f"Injecting {num_initial_points} specific points into the initial swarm.")
//         positions[:num_initial_points] = initial_points

//     velocities = np.zeros((swarm_size, dimn))
//     initial_positions = positions.copy()

//     pbest_positions = positions.copy()
//     pbest_scores = fitness(positions)

//     gbest_idx = np.argmin(pbest_scores)
//     gbest_pos = pbest_positions[gbest_idx].copy()
//     gbest_score = pbest_scores[gbest_idx]

//     for t in range(max_iter):
//         r1, r2 = np.random.rand(swarm_size, 1), np.random.rand(swarm_size, 1)
//         velocities = (weight * velocities +
//                       c1 * r1 * (pbest_positions - positions) +
//                       c2 * r2 * (gbest_pos - positions))

//         positions = positions + velocities
//         positions = np.clip(positions, lb, ub)
//         current_scores = fitness(positions)
//         update_mask = current_scores < pbest_scores
//         pbest_positions[update_mask] = positions[update_mask]
//         pbest_scores[update_mask] = current_scores[update_mask]

//         gbest_idx = np.argmin(pbest_scores)
//         gbest_score = pbest_scores[gbest_idx]
//         gbest_pos = pbest_positions[gbest_idx].copy()

//         print(f"Iteration {t + 1}: Best Fitness = {gbest_score:.4f}")

//     return gbest_pos, gbest_score, initial_positions

// if __name__ == "__main__":
//     SWARM_SIZE = 20
//     DIMENSIONS = 2
//     MAX_ITERATIONS = 50
//     LOWER_BOUND = -5.0
//     UPPER_BOUND = 5.0
//     WEIGHT = 0.75
//     C1 = 1.5
//     C2 = 2.0

//     extreme_points = [
//         (5.0, 0.0),
//         (0.0, 5.0),
//         (-5.0, -5.0),
//         (5.0, 5.0)
//     ]

//     best_position, best_fitness, initial_positions = pso_optimizer(
//         SWARM_SIZE, DIMENSIONS, MAX_ITERATIONS, LOWER_BOUND, UPPER_BOUND,
//         WEIGHT, C1, C2, initial_points=extreme_points
//     )

//     print("\n--- Optimization Finished ---")
//     print(f"Best solution found (gBest Position): {best_position}")
//     print(f"Fitness of the best solution: {best_fitness:.4f}")

//     PLOT_RESOLUTION = 100
//     x_surf = np.linspace(LOWER_BOUND, UPPER_BOUND, PLOT_RESOLUTION)
//     y_surf = np.linspace(LOWER_BOUND, UPPER_BOUND, PLOT_RESOLUTION)
//     X_surf, Y_surf = np.meshgrid(x_surf, y_surf)
//     positions_for_surface = np.stack([X_surf, Y_surf], axis=-1)
//     Z_surf = fitness(positions_for_surface)
//     initial_particle_fitness = fitness(initial_positions)

//     fig = plt.figure(figsize=(12, 9))
//     ax = fig.add_subplot(111, projection='3d')

//     ax.plot_wireframe(X_surf, Y_surf, Z_surf, color='green', alpha=0.3, linewidth=0.7)

//     ax.scatter(
//         initial_positions[:, 0],
//         initial_positions[:, 1],
//         initial_particle_fitness,
//         c='red', s=50, alpha=1.0, edgecolors='k',
//         label='Initial Swarm'
//     )

//     ax.scatter(
//         best_position[0], best_position[1], best_fitness,
//         c='black', s=200, marker='*', edgecolor='k', linewidth=1,
//         label='Best Solution (gBest)'
//     )

//     ax.set_xlabel('X1', fontweight='bold')
//     ax.set_ylabel('X2', fontweight='bold')
//     ax.set_zlabel('Fitness (Z)', fontweight='bold')
//     ax.set_title('PSO on Rosenbrock Function (Full Coverage)', fontsize=16)
//     ax.legend(fontsize=12, loc='upper right')

//     ax.view_init(elev=30, azim=125)

//     ax.set_xlim([LOWER_BOUND, UPPER_BOUND])
//     ax.set_ylim([LOWER_BOUND, UPPER_BOUND])

//     max_surface_z = np.max(Z_surf)
//     max_point_z = np.max(initial_particle_fitness) if initial_particle_fitness.size > 0 else 0

//     final_z_lim = max(max_surface_z, max_point_z) * 1.05
//     ax.set_zlim([0, final_z_lim])

//     plt.show()