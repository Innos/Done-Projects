import processing.core.PApplet;

public class DrawHandler {

    private PApplet applet;

    public DrawHandler(PApplet applet){
        this.applet = applet;
    }

    public void drawLightsaberSwipe(){
        applet.stroke(255, 0, 0);
        applet.ellipse((int)applet.random(0, 321), (int)applet.random(0, 321), 80, 80);
    }
}
