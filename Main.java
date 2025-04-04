import java.util.LinkedList;
import java.util.Random;

// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
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
        static String direction = "RIGHT";
        static LinkedList<int[]> snake = new LinkedList<>();

        public static void main(String[] args) throws InterruptedException {
            snake.add(new int[]{snakeX, snakeY});
            snake.add(new int[]{snakeX+1, snakeY});

            while(true){
            placeFood();
            place();
            move();
                Thread.sleep(750);
            }
        }


        static void place() {
            System.out.print("\033[H\033[2J"); // Clear terminal
            System.out.flush();
            int[] firstSegment = snake.getFirst();
            int snake_xhead = firstSegment[0]; // X-coordinate
            int snake_yhead = firstSegment[1]; // Y-coordinate

            for (int y = 0; y <= HEIGHT; y++) {
                for (int x = 0; x <= WIDTH; x++) {
                    if (y == 0 || y == HEIGHT) {
                        System.out.print(WALL);
                    } else if (x == 0 || x == WIDTH) {
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
            while (true) {
                foodX = rand.nextInt(WIDTH - 2) + 1;
                foodY = rand.nextInt(HEIGHT - 2) + 1;
                if (!bodycheck(foodX, foodY)) break;
            }
        }
        static void move(){
            int[] firstSegment = snake.getFirst();
            int snake_xhead = firstSegment[0]; // X-coordinate
            int snake_yhead = firstSegment[1]; // Y-coordinate

            snake.addFirst(new int[]{snake_xhead+1, snake_yhead});

            if (snakeX == foodX && snakeY == foodY) {
                placeFood(); // Grow
            } else {
                snake.removeLast(); // Move forward
            }
        }
    }
}
