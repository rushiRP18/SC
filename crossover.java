import java.util.*;
import java.util.stream.Collectors;

public class GA_Selection_Crossover {

    static final int POP_SIZE = 8;
    static final int CHROM_LENGTH = 6;
    static final int MATING_POOL_SIZE = 4;

    static Random random = new Random();

    // -----------------------------------------------------------
    // CREATE POPULATION
    // -----------------------------------------------------------
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

    // FITNESS = number of 1s
    static int fitness_function(String chromosome) {
        int count = 0;
        for (char c : chromosome.toCharArray())
            if (c == '1') count++;
        return count;
    }

    // -----------------------------------------------------------
    // CANONICAL SELECTION (Pick top k)
    // -----------------------------------------------------------
    static List<String> canonical_selection(List<String> pop, List<Integer> fit, int k) {
        List<Integer> idx = new ArrayList<>();
        for (int i = 0; i < fit.size(); i++) idx.add(i);

        idx.sort((a, b) -> fit.get(b) - fit.get(a)); // high fitness first

        List<String> selected = new ArrayList<>();
        for (int i = 0; i < k; i++)
            selected.add(pop.get(idx.get(i)));

        return selected;
    }

    // -----------------------------------------------------------
    // CROSSOVER METHODS
    // -----------------------------------------------------------

    // ✅ Single Point Crossover
    static String[] single_point_crossover(String p1, String p2) {
        int pt = random.nextInt(p1.length() - 1) + 1;

        return new String[]{
                p1.substring(0, pt) + p2.substring(pt),
                p2.substring(0, pt) + p1.substring(pt)
        };
    }

    // ✅ Two-Point Crossover
    static String[] two_point_crossover(String p1, String p2) {
        int pt1 = random.nextInt(p1.length() - 2) + 1;
        int pt2 = random.nextInt(p1.length() - pt1 - 1) + pt1 + 1;

        return new String[]{
                p1.substring(0, pt1) + p2.substring(pt1, pt2) + p1.substring(pt2),
                p2.substring(0, pt1) + p1.substring(pt1, pt2) + p2.substring(pt2)
        };
    }

    // ✅ Multi-Point Crossover (3-Point)
    static String[] multi_point_crossover(String p1, String p2) {
        int len = p1.length();

        int pt1 = random.nextInt(len - 3) + 1;
        int pt2 = pt1 + 1 + random.nextInt(len - pt1 - 2);
        int pt3 = pt2 + 1 + random.nextInt(len - pt2 - 1);

        String child1 = p1.substring(0, pt1)
                + p2.substring(pt1, pt2)
                + p1.substring(pt2, pt3)
                + p2.substring(pt3);

        String child2 = p2.substring(0, pt1)
                + p1.substring(pt1, pt2)
                + p2.substring(pt2, pt3)
                + p1.substring(pt3);

        return new String[]{child1, child2};
    }

    // -----------------------------------------------------------
    // MAIN EXECUTION
    // -----------------------------------------------------------
    public static void main(String[] args) {

        // ✅ 1. Create population
        List<String> population = create_population(POP_SIZE, CHROM_LENGTH);

        System.out.println("=== Initial Population ===");
        for (String chrom : population)
            System.out.println(chrom);

        // ✅ 2. Evaluate Fitness
        List<Integer> fitness = population.stream()
                .map(GA_Selection_Crossover::fitness_function)
                .collect(Collectors.toList());

        // ✅ 3. Apply Canonical Selection
        List<String> parents = canonical_selection(population, fitness, MATING_POOL_SIZE);

        System.out.println("\n=== Selected Parents (Canonical Selection) ===");
        for (String p : parents)
            System.out.println(p);

        // ✅ Take first 2 parents for crossover demo
        String p1 = parents.get(0);
        String p2 = parents.get(1);

        System.out.println("\nParents chosen for crossover:");
        System.out.println("P1 = " + p1);
        System.out.println("P2 = " + p2);


        // -----------------------------------------------------------
        // ✅ 4. Perform ALL THREE CROSSOVER TECHNIQUES
        // -----------------------------------------------------------

        // ✅ Single Point
        String[] sp = single_point_crossover(p1, p2);
        System.out.println("\n--- Single-Point Crossover ---");
        System.out.println("Child 1: " + sp[0]);
        System.out.println("Child 2: " + sp[1]);

        // ✅ Two Point
        String[] tp = two_point_crossover(p1, p2);
        System.out.println("\n--- Two-Point Crossover ---");
        System.out.println("Child 1: " + tp[0]);
        System.out.println("Child 2: " + tp[1]);

        // ✅ Multi Point
        String[] mp = multi_point_crossover(p1, p2);
        System.out.println("\n--- Multi-Point (3-Point) Crossover ---");
        System.out.println("Child 1: " + mp[0]);
        System.out.println("Child 2: " + mp[1]);
    }
}
