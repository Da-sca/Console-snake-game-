import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class Main {
    static final int WIDTH = 50;
    static final int HEIGHT = 15;
    static final char SNAKE_HEAD = 'O';
    static final char SNAKE_BODY = 'o';
    static final char WALL = '#';
    static final char FOOD = '*';
    static final char EMPTY = ' ';

    static int foodX, foodY;
    static char direction = 'd'; //Au debut sa va a la droite
    static LinkedList<int[]> snake = new LinkedList<>();
    static boolean gameOver = false;
    static int score = 0;

    public static void main(String[] args) throws InterruptedException {
        // Snake should obviously start at the middle
        snake.add(new int[]{WIDTH / 2, HEIGHT / 2});
        snake.add(new int[]{WIDTH / 2 - 1, HEIGHT / 2});
        placeFood();
// creates a thread for inputs cuz otherwise it's waaaay too slow
        new Thread(() -> {
            try {
                while (!gameOver) {
                    if (System.in.available() > 0) {
                        char newDir = (char) System.in.read();
                        updateDirection(newDir);
                    }
                }
            } catch (IOException ignored) {}
        }).start();
        //End of thread litterally just handles inputs

        while (!gameOver) {
            move();
            place();
            Thread.sleep(Math.max(120, 400 - score * 10)); // Progression change as you wish pour etre plus a l'aise
        }

        System.out.println("\nGame Over! Final score: " + score);
    }

    static void updateDirection(char newDir) {
        switch (newDir) {
            case 'w':
            case 'W':
                if (direction != 's') direction = 'w';
                break;
            case 's':
            case 'S':
                if (direction != 'w') direction = 's';
                break;
            case 'a':
            case 'A':
                if (direction != 'd') direction = 'a';
                break;
            case 'd':
            case 'D':
                if (direction != 'a') direction = 'd';
                break;
        }
    }


    static void move() {
        //Get the snake head a partir de la linked list, fin gets the snake head coordinates
        int[] head = snake.getFirst();
        int x = head[0], y = head[1];

        switch (direction) {
            case 'w':
                y--;
                break;
            case 's':
                y++;
                break;
            case 'a':
                x--;
                break;
            case 'd':
                x++;
                break;
        }
        // Verfie pour les collisions
        if (x <= 0 || x >= WIDTH || y <= 0 || y >= HEIGHT || bodycheck(x, y)) {
            gameOver = true;
            return;
        }

        snake.addFirst(new int[]{x, y});
        if (x == foodX && y == foodY) {
            //Aggrandis le serpent si il mange pour gerer la progression
            score++;
            placeFood();
        } else {
            snake.removeLast();
        }
    }

    static boolean bodycheck(int x, int y) {
        // As the name says sa verifie si c'est le corps du serpent
        for (int i = 1; i < snake.size(); i++) {
            int[] seg = snake.get(i);
            if (seg[0] == x && seg[1] == y) return true;
        }
        return false;
    }

    static void placeFood() {
        Random rand = new Random();
        int attempts = 0;
        while (true) {
            foodX = rand.nextInt(WIDTH - 2) + 1;
            foodY = rand.nextInt(HEIGHT - 2) + 1;
            if (!bodycheck(foodX, foodY)) break;
            if (++attempts > 100) return;
        }
    }

    static void place() {
        System.out.print("\033[H"); // Move to top-left
        System.out.println("Score: " + score);
        int[] head = snake.getFirst();
        int xh = head[0], yh = head[1];

        for (int y = 0; y <= HEIGHT; y++) {
            for (int x = 0; x <= WIDTH; x++) {
                if (y == 0 || y == HEIGHT || x == 0 || x == WIDTH) {
                    System.out.print("\u001B[34m" + WALL + "\u001B[0m");       // Blue walls
                } else if (x == foodX && y == foodY) {
                    System.out.print("\u001B[31m" + FOOD + "\u001B[0m");       // Red food
                } else if (x == xh && y == yh) {
                    System.out.print("\u001B[32m" + SNAKE_HEAD + "\u001B[0m"); // Green snake body
                } else if (bodycheck(x, y)) {
                    System.out.print("\u001B[32m" + SNAKE_BODY + "\u001B[0m"); // Green snake body
                } else {
                    System.out.print(EMPTY);
                }
            }
            System.out.println();
        }
    }
}
