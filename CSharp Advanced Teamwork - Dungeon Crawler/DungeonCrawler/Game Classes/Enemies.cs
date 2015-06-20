using System;

class Enemies
{
    public int HP { get; set; }
    public int dmg { get; set; }
    public char shape { get; set; }
    public ConsoleColor color { get; set; }
    public int X { get; set; }
    public int Y { get; set; }

    public Enemies(int x, int y, int health, int damage)
    {
        this.HP = health;
        this.dmg = damage;
        this.shape = '\u263A'; //☺
        this.color = ConsoleColor.Red;
        this.X = x;
        this.Y = y;
    }
}