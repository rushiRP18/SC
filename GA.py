
#======> GA DISHA <======#
import random
import numpy as np
import matplotlib.pyplot as plt

# CONFIGURATION
POP_SIZE = 8
CHROM_LENGTH = 6
GENERATIONS = 10
MATING_POOL_SIZE = 4

selection_type = "canonical"     # options: canonical, roulette, rank, tournament, steady
crossover_type = "two_point"   # options: single_point, two_point, uniform
mutation_type = "bit_flip"        # options: bit_flip, swap
MUTATION_RATE = 0.1


def create_population(size, length):
    return [''.join(random.choice('01') for _ in range(length)) for _ in range(size)]

def fitness_function(chromosome):
    # Example: maximize number of 1's
    return chromosome.count('1')

# SELECTION METHODS
def canonical_selection(pop, fit, k):
    avg_fit = np.mean(fit)
    probs = [f / avg_fit for f in fit]
    probs = [p / sum(probs) for p in probs]
    return random.choices(pop, weights=probs, k=k)

def roulette_wheel_selection(pop, fit, k):
    probs = [f / sum(fit) for f in fit]
    return random.choices(pop, weights=probs, k=k)

def rank_based_selection(pop, fit, k):
    sorted_idx = np.argsort(fit)
    ranks = np.arange(1, len(fit)+1)
    rank_probs = [r / sum(ranks) for r in ranks]
    ranked_pop = [pop[i] for i in sorted_idx]
    return random.choices(ranked_pop, weights=rank_probs, k=k)

def tournament_selection(pop, fit, k, t_size=3):
    selected = []
    for _ in range(k):
        competitors = random.sample(list(zip(pop, fit)), t_size)
        winner = max(competitors, key=lambda x: x[1])[0]
        selected.append(winner)
    return selected

def steady_state_selection(pop, fit, k):
    pairs = sorted(zip(pop, fit), key=lambda x: x[1], reverse=True)
    return [p for p, f in pairs[:k]]

# CROSSOVER METHODS
def single_point_crossover(p1, p2):
    pt = random.randint(1, len(p1)-1)
    return p1[:pt]+p2[pt:], p2[:pt]+p1[pt:]

def two_point_crossover(p1, p2):
    pt1, pt2 = sorted(random.sample(range(1, len(p1)-1), 2))
    return p1[:pt1]+p2[pt1:pt2]+p1[pt2:], p2[:pt1]+p1[pt1:pt2]+p2[pt2:]

def uniform_crossover(p1, p2):
    c1, c2 = "", ""
    for i in range(len(p1)):
        if random.random() < 0.5:
            c1 += p1[i]; c2 += p2[i]
        else:
            c1 += p2[i]; c2 += p1[i]
    return c1, c2

# MUTATION METHODS
def bit_flip_mutation(chrom, rate=0.1):
    return ''.join('0' if g=='1' and random.random()<rate else
                   '1' if g=='0' and random.random()<rate else g for g in chrom)

def swap_mutation(chrom):
    ch = list(chrom)
    i, j = random.sample(range(len(ch)), 2)
    ch[i], ch[j] = ch[j], ch[i]
    return ''.join(ch)

# SELECTOR WRAPPERS
def select_method(pop, fit, method):
    if method == "canonical": return canonical_selection(pop, fit, MATING_POOL_SIZE)
    elif method == "roulette": return roulette_wheel_selection(pop, fit, MATING_POOL_SIZE)
    elif method == "rank": return rank_based_selection(pop, fit, MATING_POOL_SIZE)
    elif method == "tournament": return tournament_selection(pop, fit, MATING_POOL_SIZE)
    elif method == "steady": return steady_state_selection(pop, fit, MATING_POOL_SIZE)

def crossover_method(p1, p2, method):
    if method == "single_point": return single_point_crossover(p1, p2)
    elif method == "two_point": return two_point_crossover(p1, p2)
    elif method == "uniform": return uniform_crossover(p1, p2)

def mutation_method(chrom, method):
    if method == "bit_flip": return bit_flip_mutation(chrom, MUTATION_RATE)
    elif method == "swap": return swap_mutation(chrom)

# GA EXECUTION
population = create_population(POP_SIZE, CHROM_LENGTH)

# Tracking data
best_fitness_list = []
avg_fitness_list = []
worst_fitness_list = []
diversity_list = []

for gen in range(GENERATIONS):
    fitness = [fitness_function(ch) for ch in population]

    best_fitness_list.append(max(fitness))
    avg_fitness_list.append(np.mean(fitness))
    worst_fitness_list.append(min(fitness))

    diversity = len(set(population)) / POP_SIZE
    diversity_list.append(diversity)

    print(f"\n--- Generation {gen} ---")
    print("Population:", population)
    print("Fitness:", fitness)

    # Selection
    parents = select_method(population, fitness, selection_type)

    # 👁️ SHOW SELECTED CHROMOSOMES
    print("Selected Parents:", parents)

    # Crossover & Mutation
    next_gen = []
    for i in range(0, len(parents)-1, 2):
        c1, c2 = crossover_method(parents[i], parents[i+1], crossover_type)
        c1 = mutation_method(c1, mutation_type)
        c2 = mutation_method(c2, mutation_type)
        next_gen.extend([c1, c2])

    population = next_gen[:POP_SIZE]  # maintain population size

    print("Next Generation:", population)
    print(f"Best = {max(fitness)}, Avg = {np.mean(fitness):.2f}, Diversity = {diversity:.2f}")

# --- Plot 1: Fitness Evolution ---
plt.figure(figsize=(10, 5))
plt.plot(best_fitness_list, label="Best Fitness", color='green')
plt.plot(avg_fitness_list, label="Average Fitness", color='blue')
plt.plot(worst_fitness_list, label="Worst Fitness", color='red')
plt.title("Fitness Evolution over Generations")
plt.xlabel("Generation")
plt.ylabel("Fitness")
plt.legend()
plt.grid(True)
plt.show()

# --- Plot 2: Population Diversity ---
plt.figure(figsize=(10, 5))
plt.plot(diversity_list, color='purple')
plt.title("Population Diversity over Generations")
plt.xlabel("Generation")
plt.ylabel("Diversity (Unique Chromosomes / Population Size)")
plt.grid(True)
plt.show()
