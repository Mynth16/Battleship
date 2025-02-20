import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class BattleShip {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static int col, row;
    private static int[][] grid;
    private static int shipCounter = 0, turnCounter = 0;
    private static int ammo = 0;

    public static void main (String[] args) {
        System.out.print("Enter column length: ");
        col = getValidatedInt("Invalid input. Please enter an odd number", 1, 3, Integer.MAX_VALUE);
        System.out.print("Enter row length: ");
        row = getValidatedInt("Invalid input. Please enter an odd number", 1, 3, Integer.MAX_VALUE);

        grid = new int[col][row];
        int maxShips = (col * row) - 3;

        System.out.print("Enter number of ships: ");
        int ships = getValidatedInt("Invalid input. Please enter a number between 1 and " + maxShips, 2, 1, maxShips);
        ammo = ships * 2;

        initializeGame(ships);
        displayGrid(col, row);
        System.out.println("\n");

        while (ammo > 0 && !gameOver()) {
            runGame();
            turnCounter++;
        }

        if (ammo == 0) System.out.println("Game Over! Ammo depleted. \nTotal turns: " + turnCounter);
        else System.out.println("Game Over! All ships destroyed. \nTotal turns: " + turnCounter);
    }


    private static int getValidatedInt(String errorMessage, int prompt, int min, int max) {
        while (true) {
            try {
                int input = scanner.nextInt();
                switch (prompt) {
                    case 1: // grid
                        if (input % 2 == 0) {
                            System.out.println(errorMessage);
                            continue;
                        }
                        break;
                    case 2: // ships
                        if (input < min || input > max) {
                            System.out.println(errorMessage);
                            continue;
                        }
                        break;
                    case 3: // coordinates
                        if (input < min || input > max) {
                            System.out.println(errorMessage);
                            continue;
                        }
                        break;
                }
                return input;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number");
                scanner.next();
            }
        }
    }


    private static void initializeGame (int ships) {
        for (int i = 0; i < ships; i++) spawnShip();
    }


    // sets a random point in the grid to a unique negative number, ignoring the center and duplicates
    private static void spawnShip() {
        int shipY, shipX;
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
                if (i == col / 2 && j == row / 2) {
                    System.out.print("[CC] ");  // center
                }
                else if (grid[i][j] == 99) {
                    System.out.print("[XX] ");  // hit
                }
                else if (grid[i][j] == 98) {
                    System.out.print("[!!] ");  // miss
                    grid[i][j] = 0;
                }
                else if (grid[i][j] < 0) {
                    System.out.print("[" + (Math.abs(grid[i][j]) > 9 ? Math.abs(grid[i][j]) : "0" + Math.abs(grid[i][j])) + "] ");
                    // (Math.abs(grid[i][j]) turns the set negative number into a positive number
                    // read this as: IF the num is greater than 9, print the num, ELSE print 0 + the num
                }
                else System.out.print("[  ] ");
            }
        }
    }


    // clones the current value of the grid to an adjacent spot then sets the current spot to 0
    private static void moveShips() {
        int[][] newGrid = new int[col][row]; // copy of grid to prevent double movement

        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                if (grid[i][j] < 0) {
                    int shipId = grid[i][j];
                    boolean moved = false;
                    int[] directions = {0, 1, 2, 3};
                    shuffleArray(directions);   // randomize the order of directions for each ship

                    for (int dir : directions) {
                        int newX = j, newY = i;

                        switch (dir) {
                            case 0: newY = i - 1; break; // Up
                            case 1: newY = i + 1; break; // Down
                            case 2: newX = j - 1; break; // Left
                            case 3: newX = j + 1; break; // Right
                        }

                        // check if the new position is within bounds, empty and not the center
                        if (newY >= 0 && newY < col && newX >= 0 && newX < row && newGrid[newY][newX] == 0 && !(newY == col / 2 && newX == row / 2)) {
                            newGrid[newY][newX] = shipId;
                            moved = true;
                            break;
                        }
                    }

                    if (!moved) newGrid[i][j] = shipId; // If no move possible, stay in place
                }
            }
        }
        grid = newGrid;
    }


    private static void shuffleArray(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);  // random index from 0 to i
            int temp = array[i];                      // swap current element with random index
            array[i] = array[index];
            array[index] = temp;
        }
    }



    private static void runGame() {
       System.out.print("Enter x coordinate: ");
       int x = getValidatedInt("Invalid input. Please enter a number between 0 and " + (row - 1), 3, 0, row - 1);

       System.out.print("Enter y coordinate: ");
       int y = getValidatedInt("Invalid input. Please enter a number between 0 and " + (col - 1), 3, 0, col - 1);

       moveShips();
       fire(x, y);
    }


    private static void fire(int x, int y) {
        if (grid[y][x] < 0) {
            System.out.println("\nHit!");
            grid[y][x] = 99;
        }
        else {
            System.out.println("\nMiss!");
            grid[y][x] = 98;
        }
        displayGrid(col, row);
        System.out.println("\n");
        ammo--;
        System.out.println("Remaining Ammo: " + ammo);
    }
    
    
    private static boolean gameOver() {
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                if (grid[i][j] < 0) return false;
            }
        }
        return true;
    }
}
