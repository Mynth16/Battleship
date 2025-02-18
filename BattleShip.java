/*
1. check if grid is valid
   a. grid must have a center
2. deploy ships
   a. ship cannot be deployed on center
   b. max is grid - 3
3. locate, player picks a spot
   a. check if it hit or missed, update map
4. ships move one adjacent position each turn
5. continue until all ships destroyed or ammo runs out
6. display end screen and total turns
 */

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class BattleShip {
    private static final Scanner scanner = new Scanner(System.in);
    private static int col, row;
    private static int[][] grid;
    private static int shipX, shipY;
    private static int shipCounter = 0;
    public static void main (String[] args) {
        int ammo = 10;

        System.out.print("Enter column length: ");
        col = getValidatedInt("Invalid input. Please enter an odd number", true, 3, Integer.MAX_VALUE);
        System.out.print("Enter row length: ");
        row = getValidatedInt("Invalid input. Please enter an odd number", true, 3, Integer.MAX_VALUE);

        grid = new int[col][row];
        int maxShips = (col * row) - 3;

        System.out.print("Enter number of ships: ");
        int ships = getValidatedInt("Invalid input. Please enter a number between 1 and " + maxShips, false, 1, maxShips);

        initializeGame(ships);
        displayGrid(col, row);
    }


    private static void initializeGame (int ships) {
        for (int i = 0; i < ships; i++) spawnShip();
    }


    // sets a random point in the grid to a unique negative number, ignoring the center and duplicates
    private static void spawnShip() {
        Random random = new Random();
        do {
            shipY = random.nextInt(col);
            shipX = random.nextInt(row);
        } while (grid[shipY][shipX] != 0 || (shipY == col / 2 && shipX == row / 2));

        grid[shipY][shipX] = -(++shipCounter);
    }


    private static void displayGrid(int col, int row) {
        for (int i = 0; i < col; i++) {
            System.out.println(" ");
            for (int j = 0; j < row; j++) {
                if (i == col / 2 && j == row / 2) System.out.print("[CC] ");
                else if (grid[i][j] < 0) {
                    System.out.print("[" + (Math.abs(grid[i][j]) > 9 ? Math.abs(grid[i][j]) : "0" + Math.abs(grid[i][j])) + "] ");
                    // (Math.abs(grid[i][j]) turns the set negative number into a positive number
                    // read this as: IF the num is greater than 9, print the num, ELSE print 0 + the num
                }
                else System.out.print("[  ] ");
            }
        }
    }


    private static int getValidatedInt(String errorMessage, boolean gridPrompt, int min, int max) {
        while (true) {
            try {
                int input = scanner.nextInt();
                if (gridPrompt) {
                    if (input >= min && input % 2 != 0) return input;
                    else System.out.println(errorMessage);
                }
                else if (input >= min && input <= max) return input;
                else System.out.println(errorMessage);
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number");
                scanner.next();
            }
        }
    }
}
