import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class GlassArranger {

    /**
     * Solves the ordered setup where first n glasses are filled and last n are empty.
     * @param glasses Array representing glass states ('F' for filled, 'E' for empty)
     * @return Minimum number of moves required
     */
    public static int arrangeOrderedGlasses(char[] glasses) {
        int n = glasses.length / 2;
        int moves = 0;

        for (int i = 1; i < n; i += 2) {
            if (glasses[i] == 'F') {
                int swapPos = findEmptyAtEvenPosition(glasses, n);
                if (swapPos != -1) {
                    System.out.println("Swapping positions " + i + " and " + swapPos);
                    swapGlasses(glasses, i, swapPos);
                    moves++;
                }
            }
        }
        return moves;
    }

    private static int findEmptyAtEvenPosition(char[] glasses, int start) {
        for (int i = start; i < glasses.length; i++) {
            if (i % 2 == 0 && glasses[i] == 'E') {
                return i;
            }
        }
        return -1;
    }

    /**
     * Solves the random setup where glasses are in random order.
     * @param glasses Array representing glass states
     * @return Minimum number of moves required
     */
    public static int arrangeRandomGlasses(char[] glasses) {
        GlassCount counts = countGlassPositions(glasses);

        if (counts.movesForFEFE() <= counts.movesForEFEF()) {
            return arrangeAsFEFE(glasses, counts);
        } else {
            return arrangeAsEFEF(glasses, counts);
        }
    }

    private static GlassCount countGlassPositions(char[] glasses) {
        GlassCount counts = new GlassCount();

        for (int i = 0; i < glasses.length; i++) {
            if (glasses[i] == 'F') {
                if (i % 2 == 0) counts.fEven++;
                else counts.fOdd++;
            } else {
                if (i % 2 == 0) counts.eEven++;
                else counts.eOdd++;
            }
        }
        return counts;
    }

    private static int arrangeAsFEFE(char[] glasses, GlassCount counts) {
        int moves = 0;

        for (int i = 0; i < glasses.length; i++) {
            if (i % 2 == 0 && glasses[i] == 'E') {
                int swapPos = findFilledAtOddPosition(glasses, i + 1);
                if (swapPos != -1) {
                    System.out.println("Swapping positions " + i + " and " + swapPos);
                    swapGlasses(glasses, i, swapPos);
                    moves++;
                }
            } else if (i % 2 == 1 && glasses[i] == 'F') {
                int swapPos = findEmptyAtEvenPosition(glasses, i + 1);
                if (swapPos != -1) {
                    System.out.println("Swapping positions " + i + " and " + swapPos);
                    swapGlasses(glasses, i, swapPos);
                    moves++;
                }
            }
        }
        return moves;
    }

    private static int arrangeAsEFEF(char[] glasses, GlassCount counts) {
        int moves = 0;

        for (int i = 0; i < glasses.length; i++) {
            if (i % 2 == 0 && glasses[i] == 'F') {
                int swapPos = findEmptyAtOddPosition(glasses, i + 1);
                if (swapPos != -1) {
                    System.out.println("Swapping positions " + i + " and " + swapPos);
                    swapGlasses(glasses, i, swapPos);
                    moves++;
                }
            } else if (i % 2 == 1 && glasses[i] == 'E') {
                int swapPos = findFilledAtEvenPosition(glasses, i + 1);
                if (swapPos != -1) {
                    System.out.println("Swapping positions " + i + " and " + swapPos);
                    swapGlasses(glasses, i, swapPos);
                    moves++;
                }
            }
        }
        return moves;
    }

    private static int findFilledAtOddPosition(char[] glasses, int start) {
        for (int i = start; i < glasses.length; i += 2) {
            if (glasses[i] == 'F') return i;
        }
        return -1;
    }

    private static int findEmptyAtOddPosition(char[] glasses, int start) {
        for (int i = start; i < glasses.length; i += 2) {
            if (glasses[i] == 'E') return i;
        }
        return -1;
    }

    private static int findFilledAtEvenPosition(char[] glasses, int start) {
        for (int i = start % 2 == 0 ? start : start + 1; i < glasses.length; i += 2) {
            if (glasses[i] == 'F') return i;
        }
        return -1;
    }

    private static void swapGlasses(char[] glasses, int i, int j) {
        char temp = glasses[i];
        glasses[i] = glasses[j];
        glasses[j] = temp;
    }

    public static char[] generateRandomArrangement(int n) {
        char[] glasses = new char[2 * n];
        Arrays.fill(glasses, 0, n, 'F');
        Arrays.fill(glasses, n, 2 * n, 'E');

        // Fisher-Yates shuffle
        Random rand = new Random();
        for (int i = glasses.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            swapGlasses(glasses, i, j);
        }
        return glasses;
    }

    public static void printGlassArrangement(char[] glasses) {
        for (char glass : glasses) {
            System.out.print(glass + " ");
        }
        System.out.println();
    }

    private static class GlassCount {
        int fEven = 0;
        int fOdd = 0;
        int eEven = 0;
        int eOdd = 0;

        int movesForFEFE() { return Math.max(fOdd, eEven); }
        int movesForEFEF() { return Math.max(fEven, eOdd); }
    }

    public static void main(String[] args) {
        Scanner inputReader = new Scanner(System.in);

        try {
            System.out.print("Enter number of filled glasses (n): ");
            int n = inputReader.nextInt();

            if (n <= 0) {
                System.out.println("Please enter a positive integer.");
                return;
            }

            // Ordered arrangement demonstration
            char[] orderedGlasses = new char[2 * n];
            Arrays.fill(orderedGlasses, 0, n, 'F');
            Arrays.fill(orderedGlasses, n, 2 * n, 'E');

            System.out.println("\nOrdered Initial Arrangement:");
            printGlassArrangement(orderedGlasses);

            int orderedMoves = arrangeOrderedGlasses(orderedGlasses);
            System.out.println("Arranged Pattern:");
            printGlassArrangement(orderedGlasses);
            System.out.println("Moves required: " + orderedMoves);

            // Random arrangement demonstration
            char[] randomGlasses = generateRandomArrangement(n);
            System.out.println("\nRandom Initial Arrangement:");
            printGlassArrangement(randomGlasses);

            int randomMoves = arrangeRandomGlasses(randomGlasses);
            System.out.println("Arranged Pattern:");
            printGlassArrangement(randomGlasses);
            System.out.println("Moves required: " + randomMoves);

        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a positive integer.");
        } finally {
            inputReader.close();
        }
    }
}
