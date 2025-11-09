import numpy as np
import matplotlib.pyplot as plt

# Rosenbrock Function
def rosenbrock(x):
    x1, x2 = x[..., 0], x[..., 1]
    return 100 * (x2 - x1**2)**2 + (1 - x1)**2

# PSO Parameters
N = 20                 # number of particles
dimensions = 2
T = 100                # iterations
w = 0.75               # inertia factor
c1 = 1.5               # cognitive term
c2 = 2.0               # social term
min_bound, max_bound = -5, 5

rng = np.random.default_rng(42)

# Initialization
x = rng.uniform(min_bound, max_bound, size=(N, dimensions))
v = rng.uniform(-1, 1, size=(N, dimensions))
pbest = x.copy()
pbest_score = rosenbrock(x)
gbest = pbest[np.argmin(pbest_score)].copy()
gbest_score = np.min(pbest_score)

# For plotting convergence
gbest_scores = [gbest_score]

# PSO Loop
for t in range(T):
    fitness = rosenbrock(x)

    better = fitness < pbest_score
    pbest[better] = x[better]
    pbest_score[better] = fitness[better]

    if np.min(fitness) < gbest_score:
        gbest_score = np.min(fitness)
        gbest = x[np.argmin(fitness)].copy()

    r1, r2 = rng.random((N, dimensions)), rng.random((N, dimensions))
    v = w*v + c1*r1*(pbest - x) + c2*r2*(gbest - x)
    x = x + v
    x = np.clip(x, min_bound, max_bound)

    gbest_scores.append(gbest_score)

# Print Results
print("\nOptimal Solution Found:")
print(f"x1 = {gbest[0]:.6f}, x2 = {gbest[1]:.6f}")
print(f"f(x) = {gbest_score:.6f}")

# Plot: Convergence Curve
plt.figure(figsize=(8, 5))
plt.plot(gbest_scores, linewidth=2)
plt.yscale("log")
plt.title("PSO Convergence on Rosenbrock Function")
plt.xlabel("Iteration")
plt.ylabel("Best f(x) Found (log scale)")
plt.grid(True)
plt.show()
