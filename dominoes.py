import random

dominoes = [[0, 0], [0, 1], [0, 2], [0, 3], [0, 4], [0, 5], [0, 6],
            [1, 1], [1, 2], [1, 3], [1, 4], [1, 5], [1, 6], [2, 2],
            [2, 3], [2, 4], [2, 5], [2, 6], [3, 3], [3, 4], [3, 5],
            [3, 6], [4, 4], [4, 5], [4, 6], [5, 5], [5, 6], [6, 6]]
stock = []
player = []
computer = []
snake = []
status = None

for i in range(len(dominoes)):
    if i < 14:
        stock.append(dominoes.pop(random.choice(range(len(dominoes)))))
    elif i % 2 == 0:
        player.append(dominoes.pop(random.choice(range(len(dominoes)))))
    else:
        computer.append(dominoes.pop(random.choice(range(len(dominoes)))))

for i in range(7):
    if [6-i, 6-i] not in stock:
        if [6-i, 6-i] in player:
            player.remove([6-i, 6-i])
            status = 'computer'
        elif [6-i, 6-i] in computer:
            computer.remove([6-i, 6-i])
            status = 'player'
        snake.append([6-i, 6-i])
        break
    else:
        if i == 6:
            print('Error, no doubles!')

while True:
    print('=' * 70)
    print(f'Stock size: {len(stock)}')
    print(f'Computer pieces: {len(computer)}')
    print()
    if len(snake) > 6:
        temp_snake = snake[:3] + snake[-3:]
    else:
        temp_snake = snake
    for i, k in enumerate(temp_snake):
        print(k, end='')
        if len(snake) > 6 and i == 2:
            print('...', end='')
    print()
    print()
    print('Your pieces:')
    for i in range(len(player)):
        print(f'{i+1}:{player[i]}')
    print()
    if not computer:
        print('Status: The game is over. The computer won!')
        break
    if not player:
        print('Status: The game is over. You won!')
        break
    if snake[0][0] == snake[-1][-1]:
        flat_snake = []
        for i, j in snake:
            flat_snake.append(i)
            flat_snake.append(j)
        if flat_snake.count(flat_snake[0]) == 8:
            print("Status: The game is over. It's a draw!")
            break
    if status == 'player':
        print("Status: It's your turn to make a move. Enter your command.")
        while True:
            move = input()
            try:
                if abs(int(move)) <= len(player):
                    if abs(int(move)) == 0:
                        if stock:
                            player.append(stock.pop(random.choice(range(len(stock)))))
                    elif move.startswith('-'):
                        snake.insert(0, player.pop(abs(int(move))-1))
                    else:
                        snake.append(player.pop(abs(int(move))-1))
                    status = 'computer'
                    break
                else:
                    print('Invalid input. Please try again.')
            except ValueError:
                print('Invalid input. Please try again.')
    elif status == 'computer':
        input('Status: Computer is about to make a move. Press Enter to continue...')
        move = random.randint(-len(computer), len(computer))
        if move == 0:
            if stock:
                computer.append(stock.pop(random.choice(range(len(stock)))))
        elif move < 0:
            snake.insert(0, computer.pop(abs(move)-1))
        else:
            snake.append(computer.pop(abs(move)-1))
        status = 'player'
