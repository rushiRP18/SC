import numpy as np
import matplotlib.pyplot as plt

# ---------------- Rosenbrock function ----------------
def rosenbrock(x):
    """
    Vectorized Rosenbrock. Accepts (..., D) arrays.
    For D=2, f(x1, x2) = 100(x2 - x1^2)^2 + (1 - x1)^2
    For D>2, sums over adjacent pairs.
    """
    x = np.asarray(x)
    if x.shape[-1] == 2:
        x1, x2 = x[..., 0], x[..., 1]
        return 100.0 * (x2 - x1**2)**2 + (1.0 - x1)**2
    return np.sum(100.0 * (x[..., 1:] - x[..., :-1]**2)**2 + (1.0 - x[..., :-1])**2, axis=-1)

# ---------------- Grey Wolf Optimizer ----------------
def gwo_optimize(dim=2, wolves=30, max_iter=200, lower=-5.0, upper=5.0, seed=9):
    rng = np.random.default_rng(seed)

    # Initialize wolf positions
    pos = rng.uniform(lower, upper, size=(wolves, dim))

    # Alpha, Beta, Delta wolves and their fitness
    alpha = np.zeros(dim); alpha_fit = np.inf
    beta  = np.zeros(dim); beta_fit  = np.inf
    delta = np.zeros(dim); delta_fit = np.inf

    best_fit_curve = []

    for t in range(max_iter):
        a = 2.0 - t * (2.0 / max_iter)   # linearly decreases [2 -> 0]

        # Evaluate fitness of current pack
        fitness = rosenbrock(pos)

        # Update Alpha, Beta, Delta
        for i in range(wolves):
            f = fitness[i]
            if f < alpha_fit:
                delta, delta_fit = beta.copy(), beta_fit
                beta,  beta_fit  = alpha.copy(), alpha_fit
                alpha, alpha_fit = pos[i].copy(), f
            elif f < beta_fit:
                delta, delta_fit = beta.copy(), beta_fit
                beta,  beta_fit  = pos[i].copy(), f
            elif f < delta_fit:
                delta, delta_fit = pos[i].copy(), f

        # Update positions guided by alpha, beta, delta
        for i in range(wolves):
            for j in range(dim):
                r1, r2 = rng.random(), rng.random()
                A1 = 2 * a * r1 - a
                C1 = 2 * r2
                D_alpha = abs(C1 * alpha[j] - pos[i, j])
                X1 = alpha[j] - A1 * D_alpha

                r1, r2 = rng.random(), rng.random()
                A2 = 2 * a * r1 - a
                C2 = 2 * r2
                D_beta = abs(C2 * beta[j] - pos[i, j])
                X2 = beta[j] - A2 * D_beta

                r1, r2 = rng.random(), rng.random()
                A3 = 2 * a * r1 - a
                C3 = 2 * r2
                D_delta = abs(C3 * delta[j] - pos[i, j])
                X3 = delta[j] - A3 * D_delta

                pos[i, j] = (X1 + X2 + X3) / 3.0
                pos[i, j] = np.clip(pos[i, j], lower, upper)

        best_fit_curve.append(alpha_fit)

    return alpha, alpha_fit, best_fit_curve

# ---------------- Run & plot convergence ----------------
if __name__ == "__main__":
    alpha, best_f, curve = gwo_optimize(dim=2, wolves=30, max_iter=200, lower=-5, upper=5, seed=9)

    print("Best Solution Found:", alpha)
    print("Best Fitness Value:", best_f)

    plt.figure(figsize=(8, 5))
    plt.plot(curve, linewidth=2)
    plt.yscale("log")
    plt.xlabel("Iteration")
    plt.ylabel("Best f(x) (log scale)")
    plt.title("GWO Convergence on Rosenbrock Function")
    plt.grid(True)
    plt.show()
