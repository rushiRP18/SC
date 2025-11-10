import java.util.*;
import java.util.stream.Collectors;

public class GA_SelectionOnly {

    static final int POP_SIZE = 8;
    static final int CHROM_LENGTH = 6;
    static final int SELECT_COUNT = 4;

    static Random random = new Random();

    // ---------- CREATE RANDOM POPULATION ----------
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

    // ---------- FITNESS FUNCTION ----------
    static int fitness_function(String chromosome) {
        int count = 0;
        for (char c : chromosome.toCharArray()) {
            if (c == '1') count++;
        }
        return count;  // maximize number of 1's
    }

    // ---------- CANONICAL SELECTION ----------
    static List<String> canonical_selection(List<String> pop, List<Integer> fit, int k) {
        List<Integer> idx = new ArrayList<>();
        for (int i = 0; i < fit.size(); i++) idx.add(i);

        // Sort population by descending fitness
        idx.sort((a, b) -> fit.get(b) - fit.get(a));

        List<String> selected = new ArrayList<>();
        for (int i = 0; i < k; i++)
            selected.add(pop.get(idx.get(i)));

        return selected;
    }

    // ---------- ROULETTE WHEEL ----------
    static List<String> roulette_selection(List<String> pop, List<Integer> fit, int k) {
        double totalFit = fit.stream().mapToDouble(f -> f).sum();
        List<String> selected = new ArrayList<>();

        for (int s = 0; s < k; s++) {
            double r = random.nextDouble() * totalFit;
            double cum = 0;
            for (int i = 0; i < pop.size(); i++) {
                cum += fit.get(i);
                if (cum >= r) {
                    selected.add(pop.get(i));
                    break;
                }
            }
        }
        return selected;
    }

    // ---------- TOURNAMENT SELECTION ----------
    static List<String> tournament_selection(List<String> pop, List<Integer> fit, int k, int tSize) {
        List<String> selected = new ArrayList<>();

        for (int s = 0; s < k; s++) {
            int bestIdx = random.nextInt(pop.size());

            for (int i = 1; i < tSize; i++) {
                int competitor = random.nextInt(pop.size());
                if (fit.get(competitor) > fit.get(bestIdx)) {
                    bestIdx = competitor;
                }
            }
            selected.add(pop.get(bestIdx));
        }
        return selected;
    }

    // ---------- MAIN ----------
    public static void main(String[] args) {

        List<String> population = create_population(POP_SIZE, CHROM_LENGTH);
        List<Integer> fitness = population.stream().map(GA_SelectionOnly::fitness_function).collect(Collectors.toList());

        System.out.println("Initial Population:");
        for (int i = 0; i < population.size(); i++)
            System.out.println(population.get(i) + " -> Fit = " + fitness.get(i));

        System.out.println("\n--- Canonical Selection ---");
        System.out.println(canonical_selection(population, fitness, SELECT_COUNT));

        System.out.println("\n--- Roulette Wheel Selection ---");
        System.out.println(roulette_selection(population, fitness, SELECT_COUNT));

        System.out.println("\n--- Tournament Selection (size = 3) ---");
        System.out.println(tournament_selection(population, fitness, SELECT_COUNT, 3));
    }
}
