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
import java.util.Scanner;

public class Battleship {
    private static final Scanner scanner = new Scanner(System.in);
    public static void main (String[] args) {
        int ammo = 10;

        System.out.print("Enter column length: ");
        int col = getValidatedInt();
        System.out.print("Enter row length: ");
        int row = getValidatedInt();

        int totalGrid = row * col;
        int maxShips = totalGrid - 3;

        System.out.print("Enter number of ships: ");

        displayGrid(col, row);
    }

    private static void displayGrid(int col, int row) {
        for (int i = 0; i < col; i++) {
            System.out.println(" ");
            for (int j = 0; j < row; j++) {
                System.out.print("[" +" " +"]");
            }
        }
    }

    private static int getValidatedInt() {
        while (true) {
            try {
                int input = scanner.nextInt();
                if (input >= 1 && input % 2 != 0) return input;
                else System.out.println("Invalid input. Please enter an odd number");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number");
                scanner.next();
            }
        }
    }
}
