import java.util.*;
import java.util.stream.Collectors;

public class GA {

    // CONFIGURATION
    static final int POP_SIZE = 8;
    static final int CHROM_LENGTH = 6;
    static final int GENERATIONS = 10;
    static final int MATING_POOL_SIZE = 4;

    static final String selection_type = "canonical"; // options: canonical, roulette, rank, tournament, steady
    static final String crossover_type = "two_point"; // options: single_point, two_point, uniform
    static final String mutation_type = "bit_flip"; // options: bit_flip, swap
    static final double MUTATION_RATE = 0.1;

    static Random random = new Random();

    // HELPER FUNCTIONS
    static List<String> create_population(int size, int length) {
        List<String> population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            StringBuilder chrom = new StringBuilder();
            for (int j = 0; j < length; j++) {
                chrom.append(random.nextBoolean() ? '1' : '0');
            }
            population.add(chrom.toString());
        }
        return population;
    }

    static int fitness_function(String chromosome) {
        // Example: maximize number of 1's
        int count = 0;
        for (char c : chromosome.toCharArray()) {
            if (c == '1')
                count++;
        }
        return count;
    }

    // SELECTION METHODS
    static List<String> canonical_selection(List<String> pop, List<Integer> fit, int k) {
        double avg_fit = fit.stream().mapToDouble(f -> f).average().orElse(1.0);
        List<Double> probs = new ArrayList<>();
        for (int f : fit)
            probs.add(f / avg_fit);
        double sum = probs.stream().mapToDouble(Double::doubleValue).sum();
        probs = probs.stream().map(p -> p / sum).collect(Collectors.toList());
        return weighted_choice(pop, probs, k);
    }

    static List<String> roulette_wheel_selection(List<String> pop, List<Integer> fit, int k) {
        double sum = fit.stream().mapToDouble(f -> f).sum();
        List<Double> probs = fit.stream().map(f -> f / sum).collect(Collectors.toList());
        return weighted_choice(pop, probs, k);
    }

    static List<String> rank_based_selection(List<String> pop, List<Integer> fit, int k) {
        List<Integer> sortedIdx = new ArrayList<>();
        for (int i = 0; i < fit.size(); i++)
            sortedIdx.add(i);
        sortedIdx.sort(Comparator.comparingInt(fit::get));

        int n = fit.size();
        List<Integer> ranks = new ArrayList<>();
        for (int i = 1; i <= n; i++)
            ranks.add(i);

        double sum = ranks.stream().mapToDouble(r -> r).sum();
        List<Double> rank_probs = ranks.stream().map(r -> r / sum).collect(Collectors.toList());

        List<String> ranked_pop = sortedIdx.stream().map(pop::get).collect(Collectors.toList());
        return weighted_choice(ranked_pop, rank_probs, k);
    }

    static List<String> tournament_selection(List<String> pop, List<Integer> fit, int k, int t_size) {
        List<String> selected = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            List<Integer> indices = random.ints(0, pop.size()).distinct().limit(t_size).boxed()
                    .collect(Collectors.toList());
            int bestIndex = indices.get(0);
            for (int idx : indices) {
                if (fit.get(idx) > fit.get(bestIndex))
                    bestIndex = idx;
            }
            selected.add(pop.get(bestIndex));
        }
        return selected;
    }

    static List<String> steady_state_selection(List<String> pop, List<Integer> fit, int k) {
        List<Map.Entry<String, Integer>> pairs = new ArrayList<>();
        for (int i = 0; i < pop.size(); i++) {
            pairs.add(new AbstractMap.SimpleEntry<>(pop.get(i), fit.get(i)));
        }

        pairs.sort((a, b) -> b.getValue() - a.getValue());
        return pairs.stream().limit(k).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    // CROSSOVER METHODS
    static String[] single_point_crossover(String p1, String p2) {
        int pt = random.nextInt(p1.length() - 1) + 1;
        return new String[] {
                p1.substring(0, pt) + p2.substring(pt),
                p2.substring(0, pt) + p1.substring(pt)
        };
    }

    static String[] two_point_crossover(String p1, String p2) {
        int pt1 = random.nextInt(p1.length() - 2) + 1;
        int pt2 = random.nextInt(p1.length() - 1 - pt1) + pt1 + 1;
        return new String[] {
                p1.substring(0, pt1) + p2.substring(pt1, pt2) + p1.substring(pt2),
                p2.substring(0, pt1) + p1.substring(pt1, pt2) + p2.substring(pt2)
        };
    }

    static String[] uniform_crossover(String p1, String p2) {
        StringBuilder c1 = new StringBuilder();
        StringBuilder c2 = new StringBuilder();
        for (int i = 0; i < p1.length(); i++) {
            if (random.nextDouble() < 0.5) {
                c1.append(p1.charAt(i));
                c2.append(p2.charAt(i));
            } else {
                c1.append(p2.charAt(i));
                c2.append(p1.charAt(i));
            }
        }
        return new String[] { c1.toString(), c2.toString() };
    }

    // MUTATION METHODS
    static String bit_flip_mutation(String chrom, double rate) {
        StringBuilder mutated = new StringBuilder();
        for (char g : chrom.toCharArray()) {
            if (random.nextDouble() < rate)
                mutated.append(g == '1' ? '0' : '1');
            else
                mutated.append(g);
        }
        return mutated.toString();
    }

    static String swap_mutation(String chrom) {
        char[] ch = chrom.toCharArray();
        int i = random.nextInt(ch.length);
        int j = random.nextInt(ch.length);
        char temp = ch[i];
        ch[i] = ch[j];
        ch[j] = temp;
        return new String(ch);
    }

    // SELECTION, CROSSOVER, MUTATION WRAPPERS
    static List<String> select_method(List<String> pop, List<Integer> fit, String method) {
        switch (method) {
            case "canonical":
                return canonical_selection(pop, fit, MATING_POOL_SIZE);
            case "roulette":
                return roulette_wheel_selection(pop, fit, MATING_POOL_SIZE);
            case "rank":
                return rank_based_selection(pop, fit, MATING_POOL_SIZE);
            case "tournament":
                return tournament_selection(pop, fit, MATING_POOL_SIZE, 3);
            case "steady":
                return steady_state_selection(pop, fit, MATING_POOL_SIZE);
            default:
                return new ArrayList<>();
        }
    }

    static String[] crossover_method(String p1, String p2, String method) {
        switch (method) {
            case "single_point":
                return single_point_crossover(p1, p2);
            case "two_point":
                return two_point_crossover(p1, p2);
            case "uniform":
                return uniform_crossover(p1, p2);
            default:
                return new String[] { p1, p2 };
        }
    }

    static String mutation_method(String chrom, String method) {
        switch (method) {
            case "bit_flip":
                return bit_flip_mutation(chrom, MUTATION_RATE);
            case "swap":
                return swap_mutation(chrom);
            default:
                return chrom;
        }
    }

    // Weighted Random Choice Utility
    static List<String> weighted_choice(List<String> items, List<Double> weights, int k) {
        List<String> chosen = new ArrayList<>();
        double total = weights.stream().mapToDouble(Double::doubleValue).sum();
        for (int i = 0; i < k; i++) {
            double r = random.nextDouble() * total;
            double cum = 0;
            for (int j = 0; j < items.size(); j++) {
                cum += weights.get(j);
                if (r <= cum) {
                    chosen.add(items.get(j));
                    break;
                }
            }
        }
        return chosen;
    }

    // MAIN
    public static void main(String[] args) {
        List<String> population = create_population(POP_SIZE, CHROM_LENGTH);

        for (int gen = 0; gen < GENERATIONS; gen++) {
            List<Integer> fitness = population.stream().map(GA::fitness_function).collect(Collectors.toList());
            int best = Collections.max(fitness);
            double avg = fitness.stream().mapToInt(f -> f).average().orElse(0.0);

            System.out.printf("Gen %d: Best = %d, Avg = %.2f%n", gen, best, avg);

            // Selection
            List<String> parents = select_method(population, fitness, selection_type);

            // Crossover & Mutation
            List<String> next_gen = new ArrayList<>();
            for (int i = 0; i < parents.size() - 1; i += 2) {
                String[] children = crossover_method(parents.get(i), parents.get(i + 1), crossover_type);
                next_gen.add(mutation_method(children[0], mutation_type));
                next_gen.add(mutation_method(children[1], mutation_type));
            }

            // Keep population size constant
            population = next_gen.subList(0, Math.min(POP_SIZE, next_gen.size()));
        }
    }
}