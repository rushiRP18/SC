import java.util.*;

public class FuzzyRelation {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

    
        System.out.print("Enter number of cars: ");
        int n = sc.nextInt();
        sc.nextLine();
        String[] cars = new String[n];
        for (int i = 0; i < n; i++) {
            System.out.print("Enter car " + (i + 1) + " name: ");
            cars[i] = sc.nextLine();
        }

        System.out.print("\nEnter number of roads: ");
        int m = sc.nextInt();
        sc.nextLine();
        String[] roads = new String[m];
        for (int i = 0; i < m; i++) {
            System.out.print("Enter road " + (i + 1) + " name: ");
            roads[i] = sc.nextLine();
        }

        
        double[][] relationA = new double[n][m];
        double[][] relationB = new double[n][m];

        System.out.println("\nEnter membership values for Fuzzy Relation A (How comfortable a car is on a given road):");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print("uA(" + cars[i] + ", " + roads[j] + "): ");
                relationA[i][j] = sc.nextDouble();
            }
        }

        System.out.println("\nEnter membership values for Fuzzy Relation B (How safe a car is on a given road):");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print("uB(" + cars[i] + ", " + roads[j] + "): ");
                relationB[i][j] = sc.nextDouble();
            }
        }

        double[][] union = new double[n][m];
        double[][] intersection = new double[n][m];
        double[][] complementA = new double[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                union[i][j] = Math.max(relationA[i][j], relationB[i][j]);
                intersection[i][j] = Math.min(relationA[i][j], relationB[i][j]);
                complementA[i][j] = 1 - relationA[i][j];
            }
        }

        System.out.println("\nFUZZY RELATION A (Comfort Level)");
        printMatrix(cars, roads, relationA);

        System.out.println("\nFUZZY RELATION B (Safety Level)");
        printMatrix(cars, roads, relationB);

        System.out.println("\nUNION (A U B) -> overall comfort or safety");
        printMatrix(cars, roads, union);

        System.out.println("\nINTERSECTION (A ∩ B) -> roads that are both comfortable and safe");
        printMatrix(cars, roads, intersection);

        System.out.println("\nCOMPLEMENT of A (A') -> roads that are not comfortable");
        printMatrix(cars, roads, complementA);

        sc.close();
    }

   
    public static void printMatrix(String[] cars, String[] roads, double[][] matrix) {
        System.out.print("\t");
        for (String r : roads) System.out.print(r + "\t");
        System.out.println();
        for (int i = 0; i < cars.length; i++) {
            System.out.print(cars[i] + "\t");
            for (int j = 0; j < roads.length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }
}





