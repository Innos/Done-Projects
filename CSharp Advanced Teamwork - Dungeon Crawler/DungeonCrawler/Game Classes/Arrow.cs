using System;

class Arrow
{
    public int X { get; set; }
    public int Y { get; set; }
    public char shape { get; set; }
    public ConsoleColor color { get; set; }
    public string Direction { get; set; }
    public bool Broken { get; set; }

    public Arrow(int x, int y, string direction)
    {
        this.X = x;
        this.Y = y;
        this.shape = '-';
        this.color = ConsoleColor.Yellow;
        this.Direction = direction;
        this.Broken = false;
    }
}