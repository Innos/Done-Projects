using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Threading;




class Game
{
    //Declarations

    //random
    static Random random = new Random();

    //player
    static Player player = new Player();
    static bool hasAttacked = false;
    static Position attackedPosition = new Position(0, 0);
    static Position previousPosition = new Position(player.X, player.Y);
    static string currentDirection = "right";
    static bool hasBow = false;
    static bool hasArmor = false;

    //Items
    static List<Items> items = new List<Items>();
    static bool[,] itemPositions = new bool[BoardWidth, BoardHeight];

    //Arrows
    static List<Arrow> arrows = new List<Arrow>();
    static List<Position> previousArrowPosition = new List<Position>();


    //terrain
    static List<Terrain> terrain = new List<Terrain>();
    static bool[,] terrainPositions = new bool[BoardWidth, BoardHeight];

    //enemies
    static List<Enemies> enemies = new List<Enemies>();
    static bool[,] enemyPositions = new bool[BoardWidth, BoardHeight];
    static bool[,] previousEnemyField = new bool[BoardWidth, BoardHeight];
    static List<bool> willMove = new List<bool>();
    static List<Position> previousEnemyPosition = new List<Position>();

    //GUI
    const int BoardWidth = 65;
    const int BoardHeight = 25;
    const int GuiWidth = 25;
    const int GameWidth = BoardWidth + GuiWidth;
    const int GameHeight = BoardHeight;
    static int score = 0;


    static void Main()
    {
        // Console settings and GUI Initialization
        Console.CursorVisible = false;
        Console.Title = "Dungeon Crawler"; // Add title
        Console.WindowWidth = GameWidth;
        Console.BufferWidth = GameWidth;
        Console.WindowHeight = GameHeight;
        Console.BufferHeight = GameHeight;
        PrintGUI();

        //Initialization
        InitializeObjects();

        //Initial Draw

        //Draw Terrain 
        foreach (var terrainObject in terrain)
        {
            Console.SetCursorPosition(terrainObject.X, terrainObject.Y);
            Console.ForegroundColor = terrainObject.color;
            Console.Write(terrainObject.shape);
        }

        //while(true) running part of the game

        while (true)
        {
            ReadUserKey();
            EnemyAi();
            CheckForCollision();
            DrawTheScreen();
            FlyArrows();
            DrawHealth();
            ChangeScore();
            ClearStates();

            //Game Over
            if (player.HP <= 0)
            {
                Console.SetCursorPosition(BoardWidth / 2 - 6, BoardHeight / 2);
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("GAME OVER!!!");
                Console.SetCursorPosition(BoardWidth / 2 - 6, BoardHeight / 2 + 1);
                Console.WriteLine("Score: {0}", score);
                Console.SetCursorPosition(BoardWidth / 2 - 12, BoardHeight / 2 + 2);
                Lose();
                return;
            }

            //Victory
            if(enemies.Count == 0)
            {
                Console.SetCursorPosition(BoardWidth / 2 - 6, BoardHeight / 2);
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("VICTORY!!!");
                Console.SetCursorPosition(BoardWidth / 2 - 6, BoardHeight / 2 + 1);
                Console.WriteLine("Score: {0}", score);
                Console.SetCursorPosition(BoardWidth / 2 - 12, BoardHeight / 2 + 2);
                Win();
                return;
            }

            //Thread Sleep (Game Speed)
            score = score + 1;
            Thread.Sleep(175);
        }

    }

    //Initialization
    static void InitializeObjects()
    {
     
        //Initialize Terrain
        int numberOfTerrainObjects = random.Next(300, 350);
        for (int i = 0; i < numberOfTerrainObjects; i++)
        {
            Terrain newTerrain = new Terrain(random.Next(0, BoardWidth), random.Next(0, BoardHeight), random.Next(0, 100));
            if ((newTerrain.X > 2 || newTerrain.Y > 2) && terrainPositions[newTerrain.X, newTerrain.Y] == false)
            {
                terrainPositions[newTerrain.X, newTerrain.Y] = true;
                terrain.Add(newTerrain);
            }
            else i = i - 1;

        }
        //Initialize Enemies
        int numberOfEnemies = random.Next(14, 26);
        for (int i = 0; i < numberOfEnemies; i++)
        {
            Enemies newEnemy = new Enemies(random.Next(0, BoardWidth), random.Next(0, BoardHeight), random.Next(40,71),random.Next(3,11));
            if (terrainPositions[newEnemy.X, newEnemy.Y] == false && enemyPositions[newEnemy.X, newEnemy.Y] == false && (newEnemy.X > 2 || newEnemy.Y > 2))
            {
                enemyPositions[newEnemy.X, newEnemy.Y] = true;
                previousEnemyPosition.Add(new Position(newEnemy.X, newEnemy.Y));
                willMove.Add(false);
                enemies.Add(newEnemy);
            }
            else i = i - 1;
        }

        //Initialize Items
        int numberOfItemObjects = random.Next(2, 6);
        for (int i = 0; i < numberOfItemObjects; i++)
        {
            Items newItem = new Items(random.Next(0, BoardWidth), random.Next(0, BoardHeight), random.Next(0, 14));
            if ((newItem.X > 1 || newItem.Y > 1) && itemPositions[newItem.X, newItem.Y] == false && terrainPositions[newItem.X, newItem.Y] == false && enemyPositions[newItem.X, newItem.Y] == false)
            {
                itemPositions[newItem.X, newItem.Y] = true;
                items.Add(newItem);
            }
            else i = i - 1;
        }
    }

    //GUI

    static void PrintGUI()
    {
        for (int row = 0; row < GameHeight; row++)
        {
            Console.ForegroundColor = ConsoleColor.Green;
            Console.SetCursorPosition(BoardWidth, row);
            Console.Write("║");
        }
        Console.SetCursorPosition(BoardWidth + (GuiWidth - 7) / 2, 1);
        Console.WriteLine("Controls:");
        Console.SetCursorPosition(BoardWidth + GuiWidth / 2, 3);
        Console.WriteLine(" ↑");
        Console.SetCursorPosition(BoardWidth + 3, 4);
        Console.WriteLine(" Move:   ← →");
        Console.SetCursorPosition(BoardWidth + GuiWidth / 2, 5);
        Console.WriteLine(" ↓");
        Console.SetCursorPosition(BoardWidth + 2, 7);
        Console.WriteLine(" Attack:   Z");
        Console.SetCursorPosition(BoardWidth + 1, 9);
        Console.WriteLine(new string('═', GuiWidth - 1));
        Console.SetCursorPosition(BoardWidth + (GuiWidth - 12) / 2, 11);
        Console.WriteLine("Player Stats:");
        Console.SetCursorPosition(BoardWidth + 4, 13);
        Console.WriteLine("Health:");
        Console.SetCursorPosition(BoardWidth + 15, 13);
        Console.WriteLine("{0}",player.HP);
        Console.SetCursorPosition(BoardWidth + 19, 13);
        Console.WriteLine("♥");
        Console.SetCursorPosition(BoardWidth + (GuiWidth - 6) / 2,15);
        Console.WriteLine("Items:");
        Console.SetCursorPosition(BoardWidth + 1, 17);
        Console.WriteLine("╔   ╗");
        Console.SetCursorPosition(BoardWidth + 1, 19);
        Console.WriteLine("╚   ╝");
        Console.SetCursorPosition(BoardWidth + 12, 17);
        Console.WriteLine("╔   ╗");
        Console.SetCursorPosition(BoardWidth + 12, 19);
        Console.WriteLine("╚   ╝");
        Console.SetCursorPosition(BoardWidth + 1, 20);
        Console.WriteLine(new string('═', GuiWidth - 1));
        Console.SetCursorPosition(BoardWidth + 4, 22);
        Console.WriteLine("Score:");
        Console.SetCursorPosition(BoardWidth + 13, 22);
        Console.WriteLine(score);
    }
    static void DrawHealth()
    {
        Console.SetCursorPosition(BoardWidth + 15, 13);
        Console.WriteLine("   ");
        Console.SetCursorPosition(BoardWidth + 15, 13);
        Console.WriteLine("{0}",player.HP);
        Console.SetCursorPosition(BoardWidth + 19, 13);
        Console.WriteLine("♥");
    }
    static void ChangeScore()
    {
        Console.SetCursorPosition(BoardWidth + 13, 22);
        Console.WriteLine("            ");
        Console.SetCursorPosition(BoardWidth + 13, 22);
        Console.WriteLine(score);
    }

    //Read User Key
    static void ReadUserKey()
    {
        if(Console.KeyAvailable)
        {
            ConsoleKeyInfo keyInfo = Console.ReadKey(true);

            //Declare movement
            if (keyInfo.Key == ConsoleKey.UpArrow)
            {
                previousPosition = new Position(player.X, player.Y);
                player.X = player.X;
                player.Y = player.Y - 1;
                currentDirection = "up";
            }
            else if (keyInfo.Key == ConsoleKey.DownArrow)
            {
                previousPosition = new Position(player.X, player.Y);
                player.X = player.X;
                player.Y = player.Y + 1;
                currentDirection = "down";
            }
            else if (keyInfo.Key == ConsoleKey.RightArrow)
            {
                previousPosition = new Position(player.X, player.Y);
                player.X = player.X + 1;
                player.Y = player.Y;
                currentDirection = "right";
            }
            else if (keyInfo.Key == ConsoleKey.LeftArrow)
            {
                previousPosition = new Position(player.X, player.Y);
                player.X = player.X - 1;
                player.Y = player.Y;
                currentDirection = "left";
            }
            //Declare Attack
            else if (keyInfo.Key == ConsoleKey.Z)
            {
                switch (currentDirection)
                {
                    case "up":
                        attackedPosition.X = player.X;
                        attackedPosition.Y = player.Y - 1;
                        break;
                    case "down":
                        attackedPosition.X = player.X;
                        attackedPosition.Y = player.Y + 1;
                        break;
                    case "right":
                        attackedPosition.X = player.X + 1;
                        attackedPosition.Y = player.Y;
                        break;
                    case "left":
                        attackedPosition.X = player.X - 1;
                        attackedPosition.Y = player.Y;
                        break;
                }
                if (attackedPosition.X >= 0 && attackedPosition.X < BoardWidth &&
                    attackedPosition.Y >= 0 && attackedPosition.Y < BoardHeight &&
                    terrainPositions[attackedPosition.X, attackedPosition.Y] == false)
                {
                    hasAttacked = true;
                    if(hasBow)
                    {
                        Arrow newArrow = new Arrow(attackedPosition.X, attackedPosition.Y,currentDirection);
                        arrows.Add(newArrow);
                        previousArrowPosition.Add(new Position(attackedPosition.X, attackedPosition.Y));
                    }
                }

            }
        }
        
    }

    //Fly Arrows
    static void FlyArrows()
    {
        for (int i = 0; i < arrows.Count; i++)
        {
            previousArrowPosition[i].X = arrows[i].X;
            previousArrowPosition[i].Y = arrows[i].Y;
            switch(arrows[i].Direction)
            {
                case "up": arrows[i].Y -= 1; break;
                case "down": arrows[i].Y += 1; break;
                case "left": arrows[i].X -= 1; break;
                case "right": arrows[i].X += 1; break;
            }
        }
    }

    //Colision
    static void CheckForCollision()
    {
        //Check for enemy collision
        for (int i = 0; i < enemies.Count; i++)
        {
            if (willMove[i] == true)
            {
                if (enemies[i].X < 0 || enemies[i].X >= BoardWidth ||
                    enemies[i].Y < 0 || enemies[i].Y >= BoardHeight ||
                    terrainPositions[enemies[i].X, enemies[i].Y] ||
                    previousEnemyField[enemies[i].X, enemies[i].Y])
                {
                    enemies[i].X = previousEnemyPosition[i].X;
                    enemies[i].Y = previousEnemyPosition[i].Y;
                }
                else if (enemies[i].X == player.X && enemies[i].Y == player.Y)
                {
                    player.HP = player.HP - (enemies[i].dmg - player.Armor);
                    enemies[i].X = previousEnemyPosition[i].X;
                    enemies[i].Y = previousEnemyPosition[i].Y;
                    Hurt();
                }
                else
                {
                    enemyPositions[previousEnemyPosition[i].X, previousEnemyPosition[i].Y] = false;
                    enemyPositions[enemies[i].X, enemies[i].Y] = true;
                }
            }
        }

        //Check for player collision with terrain or outside of bounds
        if (player.X < 0 || player.X >= BoardWidth ||
            player.Y < 0 || player.Y >= BoardHeight ||
            terrainPositions[player.X, player.Y])
        {
            player.X = previousPosition.X;
            player.Y = previousPosition.Y;
        }

        //Check for player collision with enemies
        if (enemyPositions[player.X, player.Y])
        {
            for (int i = 0; i < enemies.Count; i++)
            {
                if (enemies[i].X == player.X && enemies[i].Y == player.Y)
                {
                    player.HP = player.HP - (enemies[i].dmg - player.Armor);
                    Hurt();
                    break;
                }
            }
            Console.SetCursorPosition(player.X, player.Y);
            Console.Write(" ");
            player.X = previousPosition.X;
            player.Y = previousPosition.Y;
        }
        //Check for player collision with items
        if(itemPositions[player.X,player.Y])
        {
            for (int i = 0; i < items.Count; i++)
            {
                if(items[i].X == player.X && items[i].Y == player.Y)
                {
                    ItemEffect(items[i].Name);
                    ItemAquired();
                    items.RemoveAt(i);
                    score += 50;
                }
            }
        }

        //Check for attacks
        if(hasBow)
        {
            for (int i = 0; i < arrows.Count; i++)
            {
                if(arrows[i].X < 0 || arrows[i].X >= BoardWidth ||
                    arrows[i].Y < 0 || arrows[i].Y >= BoardHeight ||
                    terrainPositions[arrows[i].X,arrows[i].Y] == true)
                {
                    arrows[i].Broken = true;
                }
                else if(enemyPositions[arrows[i].X,arrows[i].Y] == true)
                {
                    for (int l = 0; l < enemies.Count; l++)
                    {
                        if(enemies[l].X == arrows[i].X && enemies[l].Y == arrows[i].Y)
                        {
                            arrows[i].Broken = true;
                            enemies[l].HP -= player.dmg;
                            CheckIfEnemyDied(l);
                            break;
                        }
                    }
                }
            }
        }
        else
        {
            for (int i = 0; i < enemies.Count; i++)
            {
                if (hasAttacked == true && attackedPosition.X == enemies[i].X && attackedPosition.Y == enemies[i].Y)
                {
                    enemies[i].HP = enemies[i].HP - player.dmg;
                    Hit();
                    CheckIfEnemyDied(i);
                    break;
                }
            }
        }
        
    }

    //Enemy AI
    static void EnemyAi()
    {
        for (int i = 0; i < willMove.Count; i++)
        {
            int num = random.Next(0, 100);
            if (num < 20)
            {
                willMove[i] = true;

                int direction = random.Next(0, 4);
                previousEnemyField[previousEnemyPosition[i].X, previousEnemyPosition[i].Y] = false;
                previousEnemyPosition[i].X = enemies[i].X;
                previousEnemyPosition[i].Y = enemies[i].Y;
                previousEnemyField[previousEnemyPosition[i].X, previousEnemyPosition[i].Y] = true;
                switch (direction)
                {
                    case 0:
                        // left
                        enemies[i].X -= 1;
                        break;
                    case 1:
                        // up
                        enemies[i].Y -= 1;
                        break;
                    case 2:
                        // right
                        enemies[i].X += 1;
                        break;
                    case 3:
                        // down
                        enemies[i].Y += 1;
                        break;
                }
                //subcheck for other enemies
                if (enemies[i].X > 0 && enemies[i].X < BoardWidth &&
                    enemies[i].Y > 0 && enemies[i].Y < BoardHeight &&
                    enemyPositions[enemies[i].X, enemies[i].Y] == true)
                {
                    enemies[i].X = previousEnemyPosition[i].X;
                    enemies[i].Y = previousEnemyPosition[i].Y;
                    willMove[i] = false;
                }


            }
        }
    }

    static void CheckIfEnemyDied(int position)
    {
        // Remove dead enemies
        if (enemies[position].HP <= 0)
        {
            previousEnemyField[previousEnemyPosition[position].X, previousEnemyPosition[position].Y] = false;
            enemyPositions[enemies[position].X, enemies[position].Y] = false;
            Console.SetCursorPosition(enemies[position].X, enemies[position].Y);
            Console.Write(" ");
            enemies.RemoveAt(position);
            willMove.RemoveAt(position);
            previousEnemyPosition.RemoveAt(position);
            score = score + 25;
        }
    }

    //Draw the screen
    static void DrawTheScreen()
    {
        //Clear attack
        if(hasBow)
        {
            for (int i = 0; i < arrows.Count; i++)
            {
                Console.SetCursorPosition(previousArrowPosition[i].X, previousArrowPosition[i].Y);
                Console.Write(" ");
                if (arrows[i].Broken == true)
                {
                    arrows.RemoveAt(i);
                    previousArrowPosition.RemoveAt(i);
                    i--;
                }
            }
        }
        else
        {
            if (hasAttacked == false &&
          attackedPosition.X >= 0 && attackedPosition.X < BoardWidth &&
          attackedPosition.Y >= 0 && attackedPosition.Y < BoardHeight &&
           terrainPositions[attackedPosition.X, attackedPosition.Y] == false)
            {
                Console.SetCursorPosition(attackedPosition.X, attackedPosition.Y);
                Console.Write(" ");
            }
        }   

        //Clear Player
        Console.SetCursorPosition(previousPosition.X, previousPosition.Y);
        Console.Write(" ");

        //Clear Enemies
        for (int i = 0; i < enemies.Count; i++)
        {
            Console.SetCursorPosition(previousEnemyPosition[i].X, previousEnemyPosition[i].Y);
            Console.Write(" ");
        }
        //Draw Items
        for (int i = 0; i < items.Count; i++)
        {
            Console.SetCursorPosition(items[i].X, items[i].Y);
            Console.ForegroundColor = items[i].color;
            Console.Write(items[i].shape);
        }

        //Draw Player
        Console.SetCursorPosition(player.X, player.Y);
        Console.ForegroundColor = player.color;
        Console.Write(player.shape);

        //Draw Enemies
        for (int i = 0; i < enemies.Count; i++)
        {
            Console.SetCursorPosition(enemies[i].X, enemies[i].Y);
            Console.ForegroundColor = enemies[i].color;
            Console.Write(enemies[i].shape);
        }

        //Draw attack
        if(hasBow)
        {
            for (int i = 0; i < arrows.Count; i++)
            {
                Console.SetCursorPosition(arrows[i].X, arrows[i].Y);
                Console.ForegroundColor = arrows[i].color;
                Console.Write(arrows[i].shape);
            }
        }
        else
        {
            if (hasAttacked)
            {
                Console.SetCursorPosition(attackedPosition.X, attackedPosition.Y);
                Console.ForegroundColor = ConsoleColor.Red;
                Console.Write('\u2588'); // █
            }
        } 
    }

    //Clear States
    static void ClearStates()
    {
        hasAttacked = false;
        for (int i = 0; i < willMove.Count; i++)
        {
            willMove[i] = false;
        }
    }
    //Items
    static void ItemEffect(string Name)
    {
        switch(Name)
        {
            case "Cloak":
                if (hasArmor == false)
                {
                    player.Armor = 2;
                    hasArmor = true;
                    Console.SetCursorPosition((BoardWidth + 14), 18);
                    Console.WriteLine("♀");
                    Console.SetCursorPosition((BoardWidth + 18), 18);
                    Console.WriteLine("Cloak");
                }
                break;
            case "Bow": 
                if(hasBow == false)
                {
                    hasBow = true;
                    Console.SetCursorPosition((BoardWidth + 3), 18);
                    Console.WriteLine("♂");
                    Console.SetCursorPosition((BoardWidth + 7), 18);
                    Console.WriteLine("Bow");
                }
                break;
            case "Health":
                if (player.HP + 20 > 50) player.HP = 50;
                else player.HP += 20;
                break;
        }
    }

    //Sounds
    static void Hurt()
    {
        Console.Beep(175, 150);
    }
    static void Hit()
    {
        Console.Beep(1000, 150);

    }
    static void ItemAquired()
    {
        Console.Beep(1500, 100);
        Console.Beep(2250, 100);
        Console.Beep(3000, 300);
    }
    static void Lose()
    {
        Console.Beep(880, 500);
        Console.Beep(587, 1000);
        Console.Beep(698, 500);
        Console.Beep(880, 500);
        Console.Beep(587, 1000);
        Console.Beep(698, 500);
        Console.Beep(880, 250);
        Console.Beep(1046, 250);
        Console.Beep(987, 500);
        Console.Beep(784, 500);
        Console.Beep(698, 250);
        Console.Beep(784, 250);
        Console.Beep(880, 500);
        Console.Beep(587, 500);
        Console.Beep(523, 250);
        Console.Beep(659, 250);
        Console.Beep(587, 975);
    }
    static void Win()
    {
        Console.Beep(587, 160);
        Console.Beep(698, 160);
        Console.Beep(1175, 280);
        Thread.Sleep(150);

        Console.Beep(587, 160);
        Console.Beep(698, 160);
        Console.Beep(1175, 280);
        Thread.Sleep(150);

        Console.Beep(1319, 250);
        Thread.Sleep(100);
        Console.Beep(1397, 150);
        Thread.Sleep(20);
        Console.Beep(1319, 150);
        Thread.Sleep(20);
        Console.Beep(1397, 150);
        Thread.Sleep(20);
        Console.Beep(1319, 150);
        Thread.Sleep(20);
        Console.Beep(1047, 150);
        Thread.Sleep(20);
        Console.Beep(880, 180);
        Thread.Sleep(300);

        Console.Beep(880, 250);
        Console.Beep(587, 250);
        Console.Beep(698, 200);
        Console.Beep(784, 200);
        Console.Beep(880, 280);
        Thread.Sleep(300);

        Console.Beep(880, 250);
        Console.Beep(587, 250);
        Console.Beep(698, 200);
        Console.Beep(784, 200);
        Console.Beep(659, 360);
    }
}
