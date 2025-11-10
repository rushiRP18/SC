import random

POP_SIZE = 8
CHROM_LENGTH = 6
SELECT_COUNT = 4


# ---------- CREATE RANDOM POPULATION ----------
def create_population(size, length):
    population = []
    for _ in range(size):
        chrom = "".join("1" if random.random() < 0.5 else "0" for _ in range(length))
        population.append(chrom)
    return population


# ---------- FITNESS FUNCTION ----------
def fitness_function(chromosome):
    return sum(1 for c in chromosome if c == '1')


# ---------- CANONICAL SELECTION ----------
def canonical_selection(pop, fit, k):
    idx = list(range(len(fit)))
    idx.sort(key=lambda i: fit[i], reverse=True)  # descending fitness

    selected = []
    for i in range(k):
        selected.append(pop[idx[i]])

    return selected


# ---------- ROULETTE WHEEL SELECTION ----------
def roulette_selection(pop, fit, k):
    total_fit = sum(fit)
    selected = []

    for _ in range(k):
        r = random.random() * total_fit
        cum = 0
        for i in range(len(pop)):
            cum += fit[i]
            if cum >= r:
                selected.append(pop[i])
                break

    return selected


# ---------- TOURNAMENT SELECTION ----------
def tournament_selection(pop, fit, k, t_size):
    selected = []

    for _ in range(k):
        best_idx = random.randint(0, len(pop) - 1)
        for _ in range(1, t_size):
            competitor = random.randint(0, len(pop) - 1)
            if fit[competitor] > fit[best_idx]:
                best_idx = competitor
        selected.append(pop[best_idx])

    return selected


# ---------- MAIN EXECUTION ----------
def main():
    population = create_population(POP_SIZE, CHROM_LENGTH)
    fitness = [fitness_function(ch) for ch in population]

    print("Initial Population:")
    for chrom, fit in zip(population, fitness):
        print(f"{chrom} -> Fit = {fit}")

    print("\n--- Canonical Selection ---")
    print(canonical_selection(population, fitness, SELECT_COUNT))

    print("\n--- Roulette Wheel Selection ---")
    print(roulette_selection(population, fitness, SELECT_COUNT))

    print("\n--- Tournament Selection (size = 3) ---")
    print(tournament_selection(population, fitness, SELECT_COUNT, 3))


if _name_ == "_main_":
    main()

#=============visualisaion=======
import random
import matplotlib.pyplot as plt

# ============================
# Genetic Algorithm Components
# ============================

POP_SIZE = 8
CHROM_LENGTH = 6

def create_population(size, length):
    population = []
    for _ in range(size):
        chrom = "".join("1" if random.random() < 0.5 else "0" for _ in range(length))
        population.append(chrom)
    return population

def fitness_function(chromosome):
    return sum(1 for c in chromosome if c == '1')


# ============================
# Visualization Function
# ============================

def visualize_fitness(population, fitness):
    plt.figure(figsize=(8, 5))
    plt.bar(range(len(fitness)), fitness)
    plt.xlabel("Individual Index")
    plt.ylabel("Fitness Value")
    plt.title("Fitness Visualization of Population")
    plt.tight_layout()
    plt.show()


# ============================
# MAIN EXECUTION
# ============================

if _name_ == "_main_":
    population = create_population(POP_SIZE, CHROM_LENGTH)
    fitness = [fitness_function(ch) for ch in population]

    print("Population:", population)
    print("Fitness:   ", fitness)

    visualize_fitness(population, fitness)
