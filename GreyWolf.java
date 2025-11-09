import java.util.Random;
import java.util.Scanner;

public class GreyWolf {

    static Random random = new Random();

    // Rosenbrock function as fitness
    public static double rosenbrock(double[] position) {
        double value = 0.0;
        for (int i = 0; i < position.length - 1; i++) {
            value += 100 * Math.pow(position[i + 1] - Math.pow(position[i], 2), 2)
                   + Math.pow(position[i] - 1, 2);
        }
        return value;
    }

    public static double[] optimize(int dimensions, int numWolves, int maxIterations, double lowerBound, double upperBound) {
        // Initialize wolf positions
        double[][] wolves = new double[numWolves][dimensions];
        for (int i = 0; i < numWolves; i++) {
            for (int j = 0; j < dimensions; j++) {
                wolves[i][j] = lowerBound + (upperBound - lowerBound) * random.nextDouble();
            }
        }

        // Initialize Alpha, Beta, Delta
        double[] alphaWolf = new double[dimensions];
        double alphaFitness = Double.MAX_VALUE;

        double[] betaWolf = new double[dimensions];
        double betaFitness = Double.MAX_VALUE;

        double[] deltaWolf = new double[dimensions];
        double deltaFitness = Double.MAX_VALUE;

        // Main GWO loop
        for (int iter = 0; iter < maxIterations; iter++) {
            double a = 2.0 - iter * (2.0 / maxIterations); // linearly decreasing

            for (int w = 0; w < numWolves; w++) {
                // Ensure wolves stay within bounds
                for (int d = 0; d < dimensions; d++) {
                    wolves[w][d] = Math.max(lowerBound, Math.min(upperBound, wolves[w][d]));
                }

                double fit = rosenbrock(wolves[w]);

                // Update Alpha, Beta, Delta
                if (fit < alphaFitness) {
                    alphaFitness = fit;
                    alphaWolf = wolves[w].clone();
                } else if (fit < betaFitness) {
                    betaFitness = fit;
                    betaWolf = wolves[w].clone();
                } else if (fit < deltaFitness) {
                    deltaFitness = fit;
                    deltaWolf = wolves[w].clone();
                }
            }

            // Update positions of wolves
            for (int w = 0; w < numWolves; w++) {
                for (int d = 0; d < dimensions; d++) {
                    double r1 = random.nextDouble();
                    double r2 = random.nextDouble();
                    double A1 = 2 * a * r1 - a;
                    double C1 = 2 * r2;
                    double D_alpha = Math.abs(C1 * alphaWolf[d] - wolves[w][d]);
                    double X1 = alphaWolf[d] - A1 * D_alpha;

                    r1 = random.nextDouble();
                    r2 = random.nextDouble();
                    double A2 = 2 * a * r1 - a;
                    double C2 = 2 * r2;
                    double D_beta = Math.abs(C2 * betaWolf[d] - wolves[w][d]);
                    double X2 = betaWolf[d] - A2 * D_beta;

                    r1 = random.nextDouble();
                    r2 = random.nextDouble();
                    double A3 = 2 * a * r1 - a;
                    double C3 = 2 * r2;
                    double D_delta = Math.abs(C3 * deltaWolf[d] - wolves[w][d]);
                    double X3 = deltaWolf[d] - A3 * D_delta;

                    wolves[w][d] = (X1 + X2 + X3) / 3.0;
                }
            }
        }

        return alphaWolf;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter problem dimension: ");
        int dim = sc.nextInt();

        System.out.print("Enter number of wolves: ");
        int numWolves = sc.nextInt();

        System.out.print("Enter maximum iterations: ");
        int maxIter = sc.nextInt();

        System.out.print("Enter lower bound: ");
        double lowerBound = sc.nextDouble();

        System.out.print("Enter upper bound: ");
        double upperBound = sc.nextDouble();

        double[] bestSolution = optimize(dim, numWolves, maxIter, lowerBound, upperBound);

        System.out.println("\nBest solution found:");
        for (double val : bestSolution) {
            System.out.printf("%.6f ", val);
        }
        System.out.println("\nBest fitness value (Rosenbrock): " + rosenbrock(bestSolution));

        sc.close();
    }
}



//====> Prey changing its position <=====


// import java.util.Random;
// import java.util.Scanner;

// public class GreyWolfOptimizerDynamic {

//     static Random random = new Random();

//     // Dynamic Rosenbrock function: global minimum moves over iterations
//     public static double dynamicRosenbrock(double[] position, int iter, int maxIter) {
//         // Prey moves linearly from (-1, -1, ..., -1) to (1, 1, ..., 1)
//         double[] preyPos = new double[position.length];
//         for (int i = 0; i < position.length; i++) {
//             preyPos[i] = -1 + 2.0 * iter / maxIter; // moves from -1 to 1
//         }

//         // Rosenbrock function relative to moving prey
//         double value = 0.0;
//         for (int i = 0; i < position.length - 1; i++) {
//             double xi = position[i] - preyPos[i];
//             double xnext = position[i + 1] - preyPos[i + 1];
//             value += 100 * Math.pow(xnext - xi * xi, 2) + Math.pow(xi - 1, 2);
//         }
//         return value;
//     }

//     public static double[] optimizeDynamic(int dimensions, int numWolves, int maxIterations, double lowerBound, double upperBound) {
//         // Initialize wolf positions
//         double[][] wolves = new double[numWolves][dimensions];
//         for (int i = 0; i < numWolves; i++) {
//             for (int j = 0; j < dimensions; j++) {
//                 wolves[i][j] = lowerBound + (upperBound - lowerBound) * random.nextDouble();
//             }
//         }

//         // Initialize Alpha, Beta, Delta
//         double[] alphaWolf = new double[dimensions];
//         double alphaFitness = Double.MAX_VALUE;

//         double[] betaWolf = new double[dimensions];
//         double betaFitness = Double.MAX_VALUE;

//         double[] deltaWolf = new double[dimensions];
//         double deltaFitness = Double.MAX_VALUE;

//         // Main GWO loop
//         for (int iter = 0; iter < maxIterations; iter++) {
//             double a = 2.0 - iter * (2.0 / maxIterations);

//             for (int w = 0; w < numWolves; w++) {
//                 // Ensure wolves stay within bounds
//                 for (int d = 0; d < dimensions; d++) {
//                     wolves[w][d] = Math.max(lowerBound, Math.min(upperBound, wolves[w][d]));
//                 }

//                 double fit = dynamicRosenbrock(wolves[w], iter, maxIterations);

//                 // Update Alpha, Beta, Delta
//                 if (fit < alphaFitness) {
//                     alphaFitness = fit;
//                     alphaWolf = wolves[w].clone();
//                 } else if (fit < betaFitness) {
//                     betaFitness = fit;
//                     betaWolf = wolves[w].clone();
//                 } else if (fit < deltaFitness) {
//                     deltaFitness = fit;
//                     deltaWolf = wolves[w].clone();
//                 }
//             }

//             // Update positions of wolves
//             for (int w = 0; w < numWolves; w++) {
//                 for (int d = 0; d < dimensions; d++) {
//                     double r1 = random.nextDouble();
//                     double r2 = random.nextDouble();
//                     double A1 = 2 * a * r1 - a;
//                     double C1 = 2 * r2;
//                     double D_alpha = Math.abs(C1 * alphaWolf[d] - wolves[w][d]);
//                     double X1 = alphaWolf[d] - A1 * D_alpha;

//                     r1 = random.nextDouble();
//                     r2 = random.nextDouble();
//                     double A2 = 2 * a * r1 - a;
//                     double C2 = 2 * r2;
//                     double D_beta = Math.abs(C2 * betaWolf[d] - wolves[w][d]);
//                     double X2 = betaWolf[d] - A2 * D_beta;

//                     r1 = random.nextDouble();
//                     r2 = random.nextDouble();
//                     double A3 = 2 * a * r1 - a;
//                     double C3 = 2 * r2;
//                     double D_delta = Math.abs(C3 * deltaWolf[d] - wolves[w][d]);
//                     double X3 = deltaWolf[d] - A3 * D_delta;

//                     wolves[w][d] = (X1 + X2 + X3) / 3.0;
//                 }
//             }
//         }

//         return alphaWolf;
//     }

//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);

//         System.out.print("Enter problem dimension: ");
//         int dim = sc.nextInt();

//         System.out.print("Enter number of wolves: ");
//         int numWolves = sc.nextInt();

//         System.out.print("Enter maximum iterations: ");
//         int maxIter = sc.nextInt();

//         System.out.print("Enter lower bound: ");
//         double lowerBound = sc.nextDouble();

//         System.out.print("Enter upper bound: ");
//         double upperBound = sc.nextDouble();

//         double[] bestSolution = optimizeDynamic(dim, numWolves, maxIter, lowerBound, upperBound);

//         System.out.println("\nBest solution found (dynamic prey):");
//         for (double val : bestSolution) {
//             System.out.printf("%.6f ", val);
//         }
//         System.out.println("\nBest fitness value (dynamic Rosenbrock): " + dynamicRosenbrock(bestSolution, maxIter, maxIter));

//         sc.close();
//     }
// }



//=====> GWO - Disha <====


// import numpy as np
// import matplotlib.pyplot as plt
// from mpl_toolkits.mplot3d import Axes3D

// def fitness(position):
//     x1 = position[..., 0]
//     x2 = position[..., 1]
//     return 100 * (x2 - x1**2)**2 + (1 - x1)**2

// def gwo(wolves, dim, max_iter, lb, ub, initial_points=None):
//     positions = lb + (ub - lb) * np.random.rand(wolves, dim)

//     if initial_points is not None:
//         initial_points = np.array(initial_points)
//         num_initial_points = len(initial_points)
//         if num_initial_points > wolves:
//             raise ValueError("Number of initial_points cannot exceed the number of wolves.")

//         print(f"Injecting {num_initial_points} specific points into the initial population.")
//         positions[:num_initial_points] = initial_points

//     initial_wolf_positions = positions.copy()

//     alpha_pos = np.zeros(dim)
//     alpha_score = float('inf')
//     beta_pos = np.zeros(dim)
//     beta_score = float('inf')
//     delta_pos = np.zeros(dim)
//     delta_score = float('inf')

//     for t in range(max_iter):
//         for i in range(wolves):
//             current_fitness = fitness(positions[i])
//             if current_fitness < alpha_score:
//                 delta_score, delta_pos = beta_score, beta_pos.copy()
//                 beta_score, beta_pos = alpha_score, alpha_pos.copy()
//                 alpha_score, alpha_pos = current_fitness, positions[i].copy()
//             elif current_fitness < beta_score:
//                 delta_score, delta_pos = beta_score, beta_pos.copy()
//                 beta_score, beta_pos = current_fitness, positions[i].copy()
//             elif current_fitness < delta_score:
//                 delta_score, delta_pos = current_fitness, positions[i].copy()

//         a = 2.0 - t * (2.0 / max_iter)

//         for i in range(wolves):
//             r1, r2 = np.random.rand(dim), np.random.rand(dim)
//             A1 = 2 * a * r1 - a
//             C1 = 2 * r2
//             D_alpha = np.abs(C1 * alpha_pos - positions[i])
//             X1 = alpha_pos - A1 * D_alpha

//             r1, r2 = np.random.rand(dim), np.random.rand(dim)
//             A2 = 2 * a * r1 - a
//             C2 = 2 * r2
//             D_beta = np.abs(C2 * beta_pos - positions[i])
//             X2 = beta_pos - A2 * D_beta

//             r1, r2 = np.random.rand(dim), np.random.rand(dim)
//             A3 = 2 * a * r1 - a
//             C3 = 2 * r2
//             D_delta = np.abs(C3 * delta_pos - positions[i])
//             X3 = delta_pos - A3 * D_delta

//             positions[i] = (X1 + X2 + X3) / 3.0

//         positions = np.clip(positions, lb, ub)
//         print(f"Iteration {t + 1}: Best Fitness = {alpha_score:.4f}")

//     return alpha_pos, alpha_score, initial_wolf_positions

// if __name__ == "__main__":
//     WOLVES_COUNT = 30
//     DIMENSIONS = 2
//     MAX_ITERATIONS = 100
//     LOWER_BOUND = -5.0
//     UPPER_BOUND = 5.0

//     extreme_points = [
//         (5.0, 0.0),
//         (0.0, 5.0),
//         (-5.0, -5.0),
//         (5.0, 5.0),
//         (-2.0, -2.0)
//     ]

//     best_position, best_fitness, initial_positions = gwo(
//         WOLVES_COUNT, DIMENSIONS, MAX_ITERATIONS, LOWER_BOUND, UPPER_BOUND,
//         initial_points=extreme_points
//     )

//     print("\nOptimization Finished")
//     print(f"Best solution found (Alpha Wolf Position): {best_position}")
//     print(f"Fitness of the best solution: {best_fitness:.4f}")

//     PLOT_RESOLUTION = 100
//     x_surf = np.linspace(LOWER_BOUND, UPPER_BOUND, PLOT_RESOLUTION)
//     y_surf = np.linspace(LOWER_BOUND, UPPER_BOUND, PLOT_RESOLUTION)
//     X_surf, Y_surf = np.meshgrid(x_surf, y_surf)
//     positions_for_surface = np.stack([X_surf, Y_surf], axis=-1)
//     Z_surf = fitness(positions_for_surface)
//     initial_wolf_fitness_values = fitness(initial_positions)

//     fig = plt.figure(figsize=(12, 9))
//     ax = fig.add_subplot(111, projection='3d')

//     ax.plot_wireframe(X_surf, Y_surf, Z_surf, color='green', alpha=0.3, linewidth=0.7)

//     ax.scatter(
//         initial_positions[:, 0],
//         initial_positions[:, 1],
//         initial_wolf_fitness_values,
//         c='yellow', s=50, alpha=1.0, edgecolors='k',
//         label='Initial Wolves'
//     )

//     ax.scatter(
//         best_position[0], best_position[1], best_fitness,
//         c='blue', s=50, marker='o', edgecolor='k', linewidth=1,
//         label='Best Solution (Alpha)'
//     )

//     ax.set_xlabel('X1', fontweight='bold')
//     ax.set_ylabel('X2', fontweight='bold')
//     ax.set_zlabel('Fitness (Z)', fontweight='bold')
//     ax.set_title('GWO on Rosenbrock Function', fontsize=16)
//     ax.legend(fontsize=12, loc='upper right')

//     ax.view_init(elev=30, azim=125)

//     ax.set_xlim([LOWER_BOUND, UPPER_BOUND])
//     ax.set_ylim([LOWER_BOUND, UPPER_BOUND])

//     max_surface_z = np.max(Z_surf)
//     max_point_z = np.max(initial_wolf_fitness_values) if initial_wolf_fitness_values.size > 0 else 0

//     final_z_lim = max(max_surface_z, max_point_z) * 1.05
//     ax.set_zlim([0, final_z_lim])

//     plt.show()