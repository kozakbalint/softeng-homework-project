# Homework Project: Rókafogó (2.6.)

## Játékleírás:

A játék egy sakktáblán játszható 4 sötét és 1 világos gyaloggal, melyek a kezdőállásban az [ábrán](#alapfelállás)
látható módon helyezkednek el. A
sötét gyalogok a kutyák, a világos gyalog a róka. Az egyik játékos a kutyákat,
a másik a rókát irányítja. A játékosok felváltva következnek lépni:

- A kutyák átlósan léphetnek egy mezőt, de csak előre.
- A róka szintén átlósan egy mezőt léphet, de mozoghat hátrafelé is.

A rókát irányító játékos akkor nyer, ha a figurát a kutyák mögé vezeti. A
kutyákat vezető játékos akkor nyer, ha a rókát olyan helyzetbe kényszeríti,
amelyben nem tud léepni.

## Alapfelállás:

![Alap felállás](https://lichess1.org/export/fen.gif?fen=2P5/8/8/8/8/8/8/1p1p1p1p_w_-_-_0_1&color=white)

## Egy PLAYER_ONE győztes játszma:

1. PLAYER_1: from: 0 2 to: 1 1
2. PLAYER_2: from: 7 1 to: 6 2
3. PLAYER_1: from: 1 1 to: 2 0
4. PLAYER_2: from: 6 2 to: 5 3
5. PLAYER_1: from: 2 0 to: 3 1
6. PLAYER_2: from: 5 3 to: 4 2
7. PLAYER_1: from: 3 1 to: 4 0
8. PLAYER_2: from: 7 3 to: 6 2
9. PLAYER_1: from: 4 0 to: 5 1
10. PLAYER_2: from: 7 5 to: 6 4
11. PLAYER_1: from: 5 1 to: 6 0
12. PLAYER_2: from: 6 2 to: 5 3
13. PLAYER_1: from: 6 0 to: 7 1

PLAYER_1 wins!

## Egy PLAYER_TWO győztes játszma:

1. PLAYER_1: from: 0 2 to: 1 1
2. PLAYER_2: from: 7 1 to: 6 0
3. PLAYER_1: from: 1 1 to: 0 0
4. PLAYER_2: from: 6 0 to: 5 1
5. PLAYER_1: from: 0 0 to: 1 1
6. PLAYER_2: from: 5 1 to: 4 0
7. PLAYER_1: from: 1 1 to: 0 0
8. PLAYER_2: from: 4 0 to: 3 1
9. PLAYER_1: from: 0 0 to: 1 1
10. PLAYER_2: from: 3 1 to: 2 0
11. PLAYER_1: from: 1 1 to: 0 0
12. PLAYER_2: from: 2 0 to: 1 1

PLAYER_2 wins!
