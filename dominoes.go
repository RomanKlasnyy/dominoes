package main

import (
	"fmt"
	"math/rand"
	"time"
)

func main() {
	rand.Seed(time.Now().UnixNano())

	dominoes := [][]int{
		{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6},
		{1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {1, 6}, {2, 2},
		{2, 3}, {2, 4}, {2, 5}, {2, 6}, {3, 3}, {3, 4}, {3, 5},
		{3, 6}, {4, 4}, {4, 5}, {4, 6}, {5, 5}, {5, 6}, {6, 6},
	}
	stock := make([][]int, 0)
	player := make([][]int, 0)
	computer := make([][]int, 0)
	snake := make([][]int, 0)
	var status string

	for i := 0; i < len(dominoes); i++ {
		if i < 14 {
			index := rand.Intn(len(dominoes))
			stock = append(stock, dominoes[index])
			dominoes = append(dominoes[:index], dominoes[index+1:]...)
		} else if i%2 == 0 {
			index := rand.Intn(len(dominoes))
			player = append(player, dominoes[index])
			dominoes = append(dominoes[:index], dominoes[index+1:]...)
		} else {
			index := rand.Intn(len(dominoes))
			computer = append(computer, dominoes[index])
			dominoes = append(dominoes[:index], dominoes[index+1:]...)
		}
	}

	for i := 0; i < 7; i++ {
		if !containsDomino(stock, []int{6 - i, 6 - i}) {
			if containsDomino(player, []int{6 - i, 6 - i}) {
				player, _ = removeDomino(player, []int{6 - i, 6 - i})
				status = "computer"
			} else if containsDomino(computer, []int{6 - i, 6 - i}) {
				computer, _ = removeDomino(computer, []int{6 - i, 6 - i})
				status = "player"
			}
			snake = append(snake, []int{6 - i, 6 - i})
			break
		} else {
			if i == 6 {
				fmt.Println("Error, no doubles!")
			}
		}
	}

	for {
		fmt.Println("======================================================================")
		fmt.Printf("Stock size: %d\n", len(stock))
		fmt.Printf("Computer pieces: %d\n\n", len(computer))

		var tempSnake [][]int
		if len(snake) > 6 {
			tempSnake = append(tempSnake, snake[:3]...)
			tempSnake = append(tempSnake, snake[len(snake)-3:]...)
		} else {
			tempSnake = snake
		}

		for i, k := range tempSnake {
			fmt.Print(k)
			if len(snake) > 6 && i == 2 {
				fmt.Print("...")
			}
		}
		fmt.Print("\n\nYour pieces:\n")
		for i, p := range player {
			fmt.Printf("%d:%v\n", i+1, p)
		}
		fmt.Println()

		if len(computer) == 0 {
			fmt.Println("Status: The game is over. The computer won!")
			break
		}
		if len(player) == 0 {
			fmt.Println("Status: The game is over. You won!")
			break
		}

		if snake[0][0] == snake[len(snake)-1][1] {
			flatSnake := []int{}
			for _, s := range snake {
				flatSnake = append(flatSnake, s...)
			}
			first := flatSnake[0]
			if countOccurrences(flatSnake, first) == 8 {
				fmt.Println("Status: The game is over. It's a draw!")
				break
			}
		}

		if status == "player" {
			fmt.Println("Status: It's your turn to make a move. Enter your command.")
			var move string
			for {
				fmt.Scanln(&move)
				index := parseInt(move)
				if abs(index) <= len(player) {
					if index == 0 {
						if len(stock) > 0 {
							index := rand.Intn(len(stock))
							player = append(player, stock[index])
							stock = append(stock[:index], stock[index+1:]...)
						}
					} else if index < 0 {
						if containsDomino(player, player[abs(index)-1]) {
							domino := player[abs(index)-1]
							if domino[1] == snake[0][0] {
								snake = append([][]int{domino}, snake...)
							} else if domino[0] == snake[0][0] {
								snake = append([][]int{{domino[1], domino[0]}}, snake...)
							} else {
								fmt.Println("Illegal move. Please try again.")
								continue
							}
							player, _ = removeDomino(player, domino)
						} else {
							fmt.Println("Illegal move. Please try again.")
							continue
						}
					} else {
						if containsDomino(player, player[index-1]) {
							domino := player[index-1]
							if domino[0] == snake[len(snake)-1][1] {
								snake = append(snake, domino)
							} else if domino[1] == snake[len(snake)-1][1] {
								snake = append(snake, []int{domino[1], domino[0]})
							} else {
								fmt.Println("Illegal move. Please try again.")
								continue
							}
							player, _ = removeDomino(player, domino)
						} else {
							fmt.Println("Illegal move. Please try again.")
							continue
						}
					}
					status = "computer"
					break
				} else {
					fmt.Println("Invalid input. Please try again.")
				}
			}
		} else if status == "computer" {
			fmt.Println("Status: Computer is about to make a move. Press Enter to continue...")
			var move string
			fmt.Scanln(&move)
			for {
				index := (rand.Intn(len(computer)+1) * randSign())
				if index == 0 {
					if len(stock) > 0 {
						index := rand.Intn(len(stock))
						computer = append(computer, stock[index])
						stock = append(stock[:index], stock[index+1:]...)
					}
				} else if index < 0 {
					if containsDomino(computer, computer[abs(index)-1]) {
						domino := computer[abs(index)-1]
						if domino[1] == snake[0][0] {
							snake = append([][]int{domino}, snake...)
						} else if domino[0] == snake[0][0] {
							snake = append([][]int{{domino[1], domino[0]}}, snake...)
						} else {
							continue
						}
						computer, _ = removeDomino(computer, domino)
					} else {
						continue
					}
				} else {
					if containsDomino(computer, computer[index-1]) {
						domino := computer[index-1]
						if domino[0] == snake[len(snake)-1][1] {
							snake = append(snake, domino)
						} else if domino[1] == snake[len(snake)-1][1] {
							snake = append(snake, []int{domino[1], domino[0]})
						} else {
							continue
						}
						computer, _ = removeDomino(computer, domino)
					} else {
						continue
					}
				}
				status = "player"
				break
			}
		}
	}
}

func abs(n int) int {
	if n < 0 {
		return -n
	}
	return n
}

func countOccurrences(arr []int, x int) int {
	count := 0
	for _, num := range arr {
		if num == x {
			count++
		}
	}
	return count
}

func parseInt(s string) int {
	n, err := fmt.Sscanf(s, "%d")
	if n == 1 && err == nil {
		return n
	}
	return 0
}

func randSign() int {
	if rand.Intn(2) == 0 {
		return -1
	}
	return 1
}

func containsDomino(arr [][]int, target []int) bool {
	for _, domino := range arr {
		if domino[0] == target[0] && domino[1] == target[1] {
			return true
		}
	}
	return false
}

func removeDomino(arr [][]int, target []int) ([][]int, bool) {
	for i, domino := range arr {
		if domino[0] == target[0] && domino[1] == target[1] {
			return append(arr[:i], arr[i+1:]...), true
		}
	}
	return arr, false
}
