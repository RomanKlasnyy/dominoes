import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Dominoes {
    public static void main(String[] args) {
        List<List<Integer>> dominoes = new ArrayList<>(Arrays.asList(
                Arrays.asList(0, 0), Arrays.asList(0, 1), Arrays.asList(0, 2), Arrays.asList(0, 3),
                Arrays.asList(0, 4), Arrays.asList(0, 5), Arrays.asList(0, 6), Arrays.asList(1, 1),
                Arrays.asList(1, 2), Arrays.asList(1, 3), Arrays.asList(1, 4), Arrays.asList(1, 5),
                Arrays.asList(1, 6), Arrays.asList(2, 2), Arrays.asList(2, 3), Arrays.asList(2, 4),
                Arrays.asList(2, 5), Arrays.asList(2, 6), Arrays.asList(3, 3), Arrays.asList(3, 4),
                Arrays.asList(3, 5), Arrays.asList(3, 6), Arrays.asList(4, 4), Arrays.asList(4, 5),
                Arrays.asList(4, 6), Arrays.asList(5, 5), Arrays.asList(5, 6), Arrays.asList(6, 6)));

        List<List<Integer>> stock = new ArrayList<>();
        List<List<Integer>> player = new ArrayList<>();
        List<List<Integer>> computer = new ArrayList<>();
        List<List<Integer>> snake = new ArrayList<>();
        String status = null;
        Random random = new Random();

        for (int i = 0; i < dominoes.size(); i++) {
            if (i < 14) {
                stock.add(dominoes.remove(random.nextInt(dominoes.size())));
            } else if (i % 2 == 0) {
                player.add(dominoes.remove(random.nextInt(dominoes.size())));
            } else {
                computer.add(dominoes.remove(random.nextInt(dominoes.size())));
            }
        }

        for (int i = 0; i < 7; i++) {
            if (!containsDomino(stock, Arrays.asList(6 - i, 6 - i))) {
                if (containsDomino(player, Arrays.asList(6 - i, 6 - i))) {
                    player.remove(Arrays.asList(6 - i, 6 - i));
                    status = "computer";
                } else if (containsDomino(computer, Arrays.asList(6 - i, 6 - i))) {
                    computer.remove(Arrays.asList(6 - i, 6 - i));
                    status = "player";
                }
                snake.add(Arrays.asList(6 - i, 6 - i));
                break;
            } else {
                if (i == 6) {
                    System.out.println("Error, no doubles!");
                }
            }
        }

        while (true) {
            System.out.println("=".repeat(70));
            System.out.println("Stock size: " + stock.size());
            System.out.println("Computer pieces: " + computer.size());
            System.out.println();

            List<List<Integer>> tempSnake = snake.size() > 6 ? snake.subList(0, 3) : snake;
            tempSnake.addAll(snake.size() > 6 ? snake.subList(snake.size() - 3, snake.size()) : new ArrayList<>(snake));

            for (List<Integer> k : tempSnake) {
                System.out.print(k);
                if (snake.size() > 6 && tempSnake.indexOf(k) == 2) {
                    System.out.print("...");
                }
            }
            System.out.println("\n");

            System.out.println("Your pieces:");
            for (int i = 0; i < player.size(); i++) {
                System.out.println(i + 1 + ":" + player.get(i));
            }
            System.out.println();

            if (computer.isEmpty()) {
                System.out.println("Status: The game is over. The computer won!");
                break;
            }
            if (player.isEmpty()) {
                System.out.println("Status: The game is over. You won!");
                break;
            }

            if (snake.get(0).get(0).equals(snake.get(snake.size() - 1).get(1))) {
                List<Integer> flatSnake = new ArrayList<>();
                for (List<Integer> s : snake) {
                    flatSnake.addAll(s);
                }
                int first = flatSnake.get(0);
                if (flatSnake.stream().allMatch(x -> x == first)) {
                    System.out.println("Status: The game is over. It's a draw!");
                    break;
                }
            }

            if ("player".equals(status)) {
                System.out.println("Status: It's your turn to make a move. Enter your command.");
                while (true) {
                    String move = System.console().readLine();
                    try {
                        int index = Integer.parseInt(move);
                        if (Math.abs(index) <= player.size()) {
                            if (index == 0) {
                                if (!stock.isEmpty()) {
                                    player.add(stock.remove(random.nextInt(stock.size())));
                                }
                            } else if (index < 0) {
                                if (player.contains(player.get(Math.abs(index) - 1))) {
                                    List<Integer> domino = player.get(Math.abs(index) - 1);
                                    if (domino.get(1).equals(snake.get(0).get(0))) {
                                        snake.add(0, domino);
                                    } else if (domino.get(0).equals(snake.get(0).get(0))) {
                                        snake.add(0, Arrays.asList(domino.get(1), domino.get(0)));
                                    } else {
                                        System.out.println("Illegal move. Please try again.");
                                        continue;
                                    }
                                    player.remove(domino);
                                } else {
                                    System.out.println("Illegal move. Please try again.");
                                    continue;
                                }
                            } else {
                                if (player.contains(player.get(index - 1))) {
                                    List<Integer> domino = player.get(index - 1);
                                    if (domino.get(0).equals(snake.get(snake.size() - 1).get(1))) {
                                        snake.add(domino);
                                    } else if (domino.get(1).equals(snake.get(snake.size() - 1).get(1))) {
                                        snake.add(Arrays.asList(domino.get(1), domino.get(0)));
                                    } else {
                                        System.out.println("Illegal move. Please try again.");
                                        continue;
                                    }
                                    player.remove(domino);
                                } else {
                                    System.out.println("Illegal move. Please try again.");
                                    continue;
                                }
                            }
                            status = "computer";
                            break;
                        } else {
                            System.out.println("Invalid input. Please try again.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please try again.");
                    }
                }
            } else if ("computer".equals(status)) {
                System.out.println("Status: Computer is about to make a move. Press Enter to continue...");
                while (true) {
                    int index = random.nextInt(computer.size() + 1) * (random.nextBoolean() ? 1 : -1);
                    if (index == 0) {
                        if (!stock.isEmpty()) {
                            computer.add(stock.remove(random.nextInt(stock.size())));
                        }
                    } else if (index < 0) {
                        if (computer.contains(computer.get(Math.abs(index) - 1))) {
                            List<Integer> domino = computer.get(Math.abs(index) - 1);
                            if (domino.get(1).equals(snake.get(0).get(0))) {
                                snake.add(0, domino);
                            } else if (domino.get(0).equals(snake.get(0).get(0))) {
                                snake.add(0, Arrays.asList(domino.get(1), domino.get(0)));
                            } else {
                                continue;
                            }
                            computer.remove(domino);
                        } else {
                            continue;
                        }
                    } else {
                        if (computer.contains(computer.get(index - 1))) {
                            List<Integer> domino = computer.get(index - 1);
                            if (domino.get(0).equals(snake.get(snake.size() - 1).get(1))) {
                                snake.add(domino);
                            } else if (domino.get(1).equals(snake.get(snake.size() - 1).get(1))) {
                                snake.add(Arrays.asList(domino.get(1), domino.get(0)));
                            } else {
                                continue;
                            }
                            computer.remove(domino);
                        } else {
                            continue;
                        }
                    }
                    status = "player";
                    break;
                }
            }
        }
    }

    private static boolean containsDomino(List<List<Integer>> list, List<Integer> target) {
        for (List<Integer> domino : list) {
            if (domino.equals(target)) {
                return true;
            }
        }
        return false;
    }
}
