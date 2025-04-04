import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class Main {
    static class KeyPressExample {
        static final int WIDTH = 30;
        static final int HEIGHT = 15;
        static final char SNAKE_HEAD = 'O';
        static final char SNAKE_BODY = 'o';
        static final char WALL = '#';
        static final char FOOD = '*';
        static final char EMPTY = ' ';

        static int snakeX = WIDTH / 2;
        static int snakeY = HEIGHT / 2;
        static int foodX, foodY;
        static int direction = 'w';
        static LinkedList<int[]> snake = new LinkedList<>();

        public static void main(String[] args) throws InterruptedException {
            snake.add(new int[]{snakeX, snakeY});
            snake.add(new int[]{snakeX + 1, snakeY});
            placeFood();

            while (true) {
                try {
                    if (System.in.available() > 0) {
                        direction = System.in.read();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                place();
                move(direction);
                Thread.sleep(750);
            }
        }

        static void place() {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            int[] firstSegment = snake.getFirst();
            int snake_xhead = firstSegment[0];
            int snake_yhead = firstSegment[1];

            for (int y = 0; y <= HEIGHT; y++) {
                for (int x = 0; x <= WIDTH; x++) {
                    if (y == 0 || y == HEIGHT || x == 0 || x == WIDTH) {
                        System.out.print(WALL);
                    } else if (x == foodX && y == foodY) {
                        System.out.print(FOOD);
                    } else if (x == snake_xhead && y == snake_yhead) {
                        System.out.print(SNAKE_HEAD);
                    } else if (bodycheck(x, y)) {
                        System.out.print(SNAKE_BODY);
                    } else {
                        System.out.print(EMPTY);
                    }
                }
                System.out.println();
            }
        }

        static boolean bodycheck(int x, int y) {
            for (int i = 1; i < snake.size(); i++) {
                int[] segment = snake.get(i);
                if (segment[0] == x && segment[1] == y) return true;
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

        static void move(int direction) {
            int[] firstSegment = snake.getFirst();
            int snake_xhead = firstSegment[0];
            int snake_yhead = firstSegment[1];

            switch (direction) {
                case 'w', 'W': snake_yhead--; break;
                case 's', 'S': snake_yhead++; break;
                case 'a', 'A': snake_xhead--; break;
                case 'd', 'D': snake_xhead++; break;
                default:
            }

            snake.addFirst(new int[]{snake_xhead, snake_yhead});
            if (snake_xhead == foodX && snake_yhead == foodY) {
                placeFood();
            } else {
                snake.removeLast();
            }
        }
    }
}
