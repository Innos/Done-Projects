using System;

class Terrain
{
    public char shape { get; set; }
    public ConsoleColor color { get; set; }
    public int X { get; set; }
    public int Y { get; set; }

    public Terrain(int x, int y, int shapeRnd)
    {
        char[] shapes = { '\u2588', '\u2580', '\u2593', '\u2588', '\u2588' }; // █ , ▀ , ▓ , █ , █
        this.color = ConsoleColor.White;
        this.X = x;
        this.Y = y;
        this.shape = shapes[shapeRnd % shapes.Length];
    }
}