using System;

class Items
{
    public char shape { get; set; }
    public string Name { get; set; }
    public ConsoleColor color { get; set; }
    public int X { get; set; }
    public int Y { get; set; }

    public Items(int x,int y, int shapeRnd)
    {
        switch(shapeRnd)
        {
            case 0:
            case 1: this.shape = '\u2640'; this.Name = "Cloak"; break; //♀
            case 2:
            case 3: this.shape = '\u2642'; this.Name = "Bow"; break; //♂ 
            default: this.shape = '\u2665'; this.Name = "Health"; break; //♥
        }

        this.color = ConsoleColor.Green;
        this.X = x;
        this.Y = y;

    }
}