import java.util.*;

public class A1_fuzzySet {

    public static double[][] inputFuzzySet(Scanner sc, String setName, String[] universe) {
        double[][] fuzzySet = new double[universe.length][2]; // [0] crisp value, [1] membership value
        System.out.println("\nEnter element values and membership values for Fuzzy Set: " + setName);
        for (int i = 0; i < universe.length; i++) {
            System.out.print(universe[i] + " -> Element value: ");
            fuzzySet[i][0] = sc.nextDouble();

            System.out.print(universe[i] + " -> Membership value (0-1): ");
            double membership = sc.nextDouble();
            if (membership < 0 || membership > 1) {
                System.out.println("Invalid membership value! Must be between 0 and 1.");
                System.exit(0);
            }
            fuzzySet[i][1] = membership;
        }
        return fuzzySet;
    }

    public static double[] union(double[] A, double[] B) {
        double[] result = new double[A.length];
        for (int i = 0; i < A.length; i++)
            result[i] = Math.max(A[i], B[i]);
        return result;
    }

    public static double[] intersection(double[] A, double[] B) {
        double[] result = new double[A.length];
        for (int i = 0; i < A.length; i++)
            result[i] = Math.min(A[i], B[i]);
        return result;
    }

    public static double[] complement(double[] A) {
        double[] result = new double[A.length];
        for (int i = 0; i < A.length; i++)
            result[i] = 1 - A[i];
        return result;
    }

    public static void displayFuzzySet(String setName, String[] universe, double[][] fuzzySet) {
        System.out.print("\n" + setName + " = { ");
        for (int i = 0; i < universe.length; i++) {
            System.out.print("(" + universe[i] + ", " + String.format("%.2f", fuzzySet[i][1]) + ")");
            if (i < universe.length - 1) System.out.print(", ");
        }
        System.out.println(" }");
    }

    public static void displayOperation(String opName, String[] universe, double[] memberships) {
        System.out.print("\n" + opName + " = { ");
        for (int i = 0; i < universe.length; i++) {
            System.out.print("(" + universe[i] + ", " + String.format("%.2f", memberships[i]) + ")");
            if (i < universe.length - 1) System.out.print(", ");
        }
        System.out.println(" }");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Universe of discourse
        String[] universe = {"Highway", "CityRoad", "RuralRoad", "MountainPass", "Expressway"};

        System.out.println("Universe of Roads: " + Arrays.toString(universe));
        System.out.println("\nWe are analyzing driving safety based on:");
        System.out.println("A ⇒ Traffic Density (0: Empty, 1: Very Crowded)");
        System.out.println("B ⇒ Road Condition (0: Bad, 1: Excellent)");

        double[][] A = inputFuzzySet(sc, "A (Traffic Density)", universe);
        double[][] B = inputFuzzySet(sc, "B (Road Condition)", universe);

        double[] Aμ = Arrays.stream(A).mapToDouble(x -> x[1]).toArray();
        double[] Bμ = Arrays.stream(B).mapToDouble(x -> x[1]).toArray();

        double[] unionSet = union(Aμ, Bμ);
        double[] intersectionSet = intersection(Aμ, Bμ);
        double[] complementA = complement(Aμ);

        displayFuzzySet("A (Traffic Density)", universe, A);
        displayFuzzySet("B (Road Condition)", universe, B);
        displayOperation("A U B (Overall Risk Level)", universe, unionSet);
        displayOperation("A ∩ B (Crowded & Bad Roads)", universe, intersectionSet);
        displayOperation("A' (Low Traffic Roads)", universe, complementA);

        System.out.println("\nInterpretation:");
        System.out.println("A U B ⇒ Overall risk due to heavy traffic or poor roads.");
        System.out.println("A ∩ B ⇒ Roads that are both crowded and in bad condition (most unsafe).");
        System.out.println("A' ⇒ Roads with low traffic, hence safer and more comfortable.");

        sc.close();
    }
}
