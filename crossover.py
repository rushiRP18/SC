import random

POP_SIZE = 8
CHROM_LENGTH = 6
MATING_POOL_SIZE = 4

random.seed()   # same randomness as Java


# -----------------------------------------------------------
# CREATE POPULATION
# -----------------------------------------------------------
def create_population(size, length):
    population = []
    for _ in range(size):
        chrom = "".join("1" if random.random() < 0.5 else "0" for _ in range(length))
        population.append(chrom)
    return population


# FITNESS FUNCTION
def fitness_function(chromosome):
    return sum(1 for c in chromosome if c == "1")


# -----------------------------------------------------------
# CANONICAL SELECTION
# -----------------------------------------------------------
def canonical_selection(pop, fit, k):
    idx = list(range(len(pop)))
    idx.sort(key=lambda i: fit[i], reverse=True)  # high fitness first
    return [pop[idx[i]] for i in range(k)]


# -----------------------------------------------------------
# CROSSOVER METHODS
# -----------------------------------------------------------

# ✅ Single-Point Crossover
def single_point_crossover(p1, p2):
    pt = random.randint(1, len(p1) - 1)
    c1 = p1[:pt] + p2[pt:]
    c2 = p2[:pt] + p1[pt:]
    return c1, c2


# ✅ Two-Point Crossover
def two_point_crossover(p1, p2):
    pt1 = random.randint(1, len(p1) - 2)
    pt2 = random.randint(pt1 + 1, len(p1) - 1)

    c1 = p1[:pt1] + p2[pt1:pt2] + p1[pt2:]
    c2 = p2[:pt1] + p1[pt1:pt2] + p2[pt2:]
    return c1, c2


# ✅ Multi-Point (3-Point) Crossover
def multi_point_crossover(p1, p2):
    length = len(p1)

    pt1 = random.randint(1, length - 3)
    pt2 = random.randint(pt1 + 1, length - 2)
    pt3 = random.randint(pt2 + 1, length - 1)

    c1 = (
        p1[:pt1]
        + p2[pt1:pt2]
        + p1[pt2:pt3]
        + p2[pt3:]
    )
    c2 = (
        p2[:pt1]
        + p1[pt1:pt2]
        + p2[pt2:pt3]
        + p1[pt3:]
    )

    return c1, c2


# -----------------------------------------------------------
# MAIN EXECUTION
# -----------------------------------------------------------
def main():
    # ✅ 1. Create population
    population = create_population(POP_SIZE, CHROM_LENGTH)

    print("=== Initial Population ===")
    for chrom in population:
        print(chrom)

    # ✅ 2. Fitness
    fitness = [fitness_function(ch) for ch in population]

    # ✅ 3. Canonical Selection
    parents = canonical_selection(population, fitness, MATING_POOL_SIZE)

    print("\n=== Selected Parents (Canonical Selection) ===")
    for p in parents:
        print(p)

    # ✅ Use first 2 parents for all crossover demos
    p1, p2 = parents[0], parents[1]

    print("\nParents chosen for crossover:")
    print("P1 =", p1)
    print("P2 =", p2)

    # ✅ Single-Point
    sp1, sp2 = single_point_crossover(p1, p2)
    print("\n--- Single-Point Crossover ---")
    print("Child 1:", sp1)
    print("Child 2:", sp2)

    # ✅ Two-Point
    tp1, tp2 = two_point_crossover(p1, p2)
    print("\n--- Two-Point Crossover ---")
    print("Child 1:", tp1)
    print("Child 2:", tp2)

    # ✅ Multi-Point
    mp1, mp2 = multi_point_crossover(p1, p2)
    print("\n--- Multi-Point (3-Point) Crossover ---")
    print("Child 1:", mp1)
    print("Child 2:", mp2)


if _name_ == "_main_":
    main()

#=========visualizaion
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
