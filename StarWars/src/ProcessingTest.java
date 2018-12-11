import Models.DrawableImage;
import Models.Enemy;
import Models.Rectangle;
import com.leapmotion.leap.Vector;
import processing.core.PApplet;
import com.leapmotion.leap.*;
import processing.core.PImage;
import processing.sound.*;

import java.util.*;
import java.util.stream.Collectors;

import static Constants.Constants.*;

public class ProcessingTest extends PApplet{

    private Controller controller = new Controller();
    private GestureDetector gestureDetector;
    private DrawHandler drawHandler;

    private static long lastMainHandSwipeTimestamp = 0l;
    private static long MainHandSwipeDuration = 200000;
    private static boolean IsMainHandSwiping = false;
    private static boolean IsUsingForceLightning = false;
    private static boolean IsStretchingHand = false;
    private static long lastLightningCastTimestamp = 0l;
    private static long LightningCastDuration = 2000000;
    private static boolean isOnStartScreen = true;
    private static boolean hasGameStarted = false;

    //Sounds
    ArrayList<SoundFile> lightSaberSwings;
    SoundFile lightning;
    SoundFile backGroundMusic;
    SoundFile backGroundMusic2;

    //Images
    PImage lightSaberRed;

    PImage backgroundImage;
    PImage startScreenImage;

    PImage stormTrooperImage;
    PImage stormTrooperElectrocuted;
    ArrayList<PImage> stormTrooperCut;

    PImage closedHandLeft;
    PImage openedHandLeft;
    PImage closedHandRight;
    PImage openedHandRight;

    PImage lightningLeft;

    //Objects
    List<Enemy> enemies = new ArrayList<>();
    List<Enemy> electrocutedEnemies = new ArrayList<>();
    List<Enemy> cutEnemies = new ArrayList<>();
    Rectangle lightSaber;
    DrawableImage jediHand;
    DrawableImage lightningImage;

    public void settings(){
        size(canvasWidth, canvasHeight, P3D);
    }

    public void setup(){
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        backgroundImage = loadImage(System.getProperty("user.dir") + "\\background-1000x700.png");;
        lightSaberRed = loadImage(System.getProperty("user.dir") + "\\lightsaber-red-small-extra-thin.png");
        startScreenImage = loadImage(System.getProperty("user.dir") + "\\longTimeAgo.png");
        startScreenImage.resize(canvasWidth, canvasHeight);

        stormTrooperImage = loadImage(System.getProperty("user.dir") + "\\Stormtrooper.png");
        stormTrooperCut = new ArrayList<>();
        stormTrooperCut.add(loadImage(System.getProperty("user.dir") + "\\StormtrooperCut.png"));
//        stormTrooperCut.add(loadImage(System.getProperty("user.dir") + "\\StormtrooperCut2.png"));;
        stormTrooperCut.add(loadImage(System.getProperty("user.dir") + "\\StormtrooperCut3.png"));
        stormTrooperCut.add(loadImage(System.getProperty("user.dir") + "\\StormtrooperCut4.png"));
        stormTrooperCut.add(loadImage(System.getProperty("user.dir") + "\\StormtrooperCut5.png"));
        stormTrooperCut.add(loadImage(System.getProperty("user.dir") + "\\StormtrooperCut6.png"));
        stormTrooperCut.add(loadImage(System.getProperty("user.dir") + "\\StormtrooperCut7.png"));
        stormTrooperCut.add(loadImage(System.getProperty("user.dir") + "\\StormtrooperCut8.png"));
        stormTrooperCut.add(loadImage(System.getProperty("user.dir") + "\\StormtrooperCut9.png"));
        stormTrooperElectrocuted = loadImage(System.getProperty("user.dir") + "\\StormtrooperLightning.png");

        closedHandLeft = loadImage(System.getProperty("user.dir") + "\\closedHandLeft.png");
        closedHandRight = loadImage(System.getProperty("user.dir") + "\\closedHandRight.png");
        openedHandLeft = loadImage(System.getProperty("user.dir") + "\\openHandLeft.png");
        openedHandRight = loadImage(System.getProperty("user.dir") + "\\openHandRight.png");

        lightningLeft = loadImage(System.getProperty("user.dir") + "\\lightning-small-left-skewed.png");

        gestureDetector = new GestureDetector(controller);
        drawHandler = new DrawHandler(this);

        controller.enableGesture(Gesture.Type.TYPE_SWIPE);

        lightSaberSwings = new ArrayList<>();
        lightSaberSwings.add(new SoundFile(this, System.getProperty("user.dir") + "\\LightsaberSwipe1.wav"));
        lightSaberSwings.add(new SoundFile(this, System.getProperty("user.dir") + "\\LightsaberSwipe2.wav"));
        lightSaberSwings.add(new SoundFile(this, System.getProperty("user.dir") + "\\LightsaberSwipe3.wav"));
        lightSaberSwings.add(new SoundFile(this, System.getProperty("user.dir") + "\\LightsaberSwipe4.wav"));
        lightSaberSwings.add(new SoundFile(this, System.getProperty("user.dir") + "\\LightsaberSwipe5.wav"));

        backGroundMusic = new SoundFile(this, System.getProperty("user.dir") + "\\backGroundMusic.mp3");
        backGroundMusic2 = new SoundFile(this, System.getProperty("user.dir") + "\\ImperialMarch.mp3");

        lightning = new SoundFile(this, System.getProperty("user.dir") + "\\Lightning.wav");

        lightSaber = new Rectangle(0,0,0,lightSaberBladeWidth,lightSaberImageHeight);
        jediHand = new DrawableImage(
                0,
                0,
                jediHandClosedWidth,
                jediHandClosedHeight,
                jediHandClosedWidth,
                jediHandClosedHeight,
                closedHandLeft);
        lightningImage = new DrawableImage(
                0,
                0,
                lightningLeftWidth / 2,
                lightningLeftHeight / 2,
                lightningLeftWidth,
                lightningLeftHeight,
                lightningLeft);
    }

    public void draw() {
        background(backgroundImage);
        Frame frame = controller.frame();

//        System.out.println(this.backGroundMusic.isPlaying());
//        if(this.backGroundMusic.isPlaying() == 0 && hasGameStarted){
//        }

        Hand hand = null;
        Hand hand2 = null;

        for (Hand currentHand : frame.hands()) {
            if (currentHand.isRight()){
                hand = currentHand;
            } else if (currentHand.isLeft()){
                hand2 = currentHand;
            }
        }

        if (isOnStartScreen) {
            background(startScreenImage);
            textSize(35);
//            text("Welcome back, Vader.", (canvasWidth/2) - 200, 50);
//            text("Let's begin your training.", (canvasWidth/2) - 215, canvasHeight - 50);

            if (hand != null){
                this.handleStartScreen(hand, 1);
            }


        } else {
            background(backgroundImage);
            this.spawnEnemies();
            this.drawEnemies();

            if (controller.now() - lastMainHandSwipeTimestamp > MainHandSwipeDuration) {
                IsMainHandSwiping = false;
            }

            if (!IsStretchingHand || controller.now() - lastLightningCastTimestamp > LightningCastDuration) {
                IsUsingForceLightning = false;
                lightning.stop();
            }

            IsStretchingHand = false;

            if (hand != null) {
                this.handleGestures(hand, 0);
            }
            if (hand2 != null) {
                this.handleGestures(hand2, 1);
            }

            this.checkCollision();
        }
    }

    public void handleStartScreen(Hand hand, int handNumber){
        LinkedHashMap<String, Finger> allFingers = this.getFingersArray(hand);
        LinkedHashMap<String, Vector> allFingerTips = this.getFingerTipsArray(allFingers);

        if (handNumber == 1 && this.gestureDetector.IsStretching(
                allFingerTips.get("thumb"),
                allFingerTips.get("index"),
                allFingerTips.get("middle"),
                allFingerTips.get("ring"),
                allFingerTips.get("pinky"),
                hand.palmPosition()
        )) {
            isOnStartScreen = false;
            hasGameStarted = true;
            backGroundMusic.loop();
        }
    }

    public void handleGestures(Hand hand, int handNumber){

        LinkedHashMap<String, Finger> allFingers = this.getFingersArray(hand);
        LinkedHashMap<String, Vector> allFingerTips = this.getFingerTipsArray(allFingers);

        if (this.gestureDetector.IsFlicking(
                allFingerTips.get("thumb"),
                allFingerTips.get("index"),
                allFingerTips.get("middle"),
                allFingerTips.get("ring"),
                allFingerTips.get("pinky"),
                hand.palmPosition()
        )){
            this.handleFlick(handNumber);
        }
        else if (this.gestureDetector.isFlippingTheBird(
                allFingerTips.get("thumb"),
                allFingerTips.get("index"),
                allFingerTips.get("middle"),
                allFingerTips.get("ring"),
                allFingerTips.get("pinky"),
                hand.palmPosition()
        )){
            this.handleTheBird(handNumber);
        }
        else if(this.gestureDetector.IsSwiping(controller.frame())){
            if(handNumber == 0){
                IsMainHandSwiping = true;
                lastMainHandSwipeTimestamp = controller.now();
                this.handleSwipe(handNumber);
            }
        }
        else if(handNumber == 1 && this.gestureDetector.IsStretching(
                allFingerTips.get("thumb"),
                allFingerTips.get("index"),
                allFingerTips.get("middle"),
                allFingerTips.get("ring"),
                allFingerTips.get("pinky"),
                hand.palmPosition())){

            IsUsingForceLightning = true;
            lastLightningCastTimestamp = controller.now();
            this.handleStretching(handNumber);
        }

        if(this.gestureDetector.IsOpenHand(
                allFingerTips.get("thumb"),
                allFingerTips.get("index"),
                allFingerTips.get("middle"),
                allFingerTips.get("ring"),
                allFingerTips.get("pinky"),
                hand.palmPosition())){
            IsStretchingHand = true;
        }

        if(handNumber == 0){
            this.drawWithHand(allFingerTips.get("middle"), hand.palmPosition());
        }
        else if(handNumber == 1){
            float[] coordinates = this.trackVector(hand.palmPosition());
            this.jediHand.setX((int) coordinates[0]);
            this.jediHand.setY((int) coordinates[1]);
            this.correctBoundary(this.jediHand);

            if(IsStretchingHand){
                this.jediHand.setWidth(jediHandOpenedWidth);
                this.jediHand.setHeight(jediHandOpenedHeight);
                this.jediHand.setImageWidth(jediHandOpenedWidth);
                this.jediHand.setImageHeight(jediHandOpenedHeight);
                this.jediHand.setImage(openedHandLeft);
            }
            else{
                this.jediHand.setWidth(jediHandClosedWidth);
                this.jediHand.setHeight(jediHandClosedHeight);
                this.jediHand.setImageWidth(jediHandClosedWidth);
                this.jediHand.setImageHeight(jediHandClosedHeight);
                this.jediHand.setImage(closedHandLeft);
            }

            if(IsUsingForceLightning){
                this.lightningImage.setX((int)coordinates[0]);
                this.lightningImage.setY((int)coordinates[1] - (this.lightningImage.getHeight() / 2));
                this.correctBoundary(this.lightningImage);
                drawImage(lightningImage);
            }

            this.drawImage(this.jediHand);
        }
    }

    public LinkedHashMap<String, Vector> getFingerTipsArray(LinkedHashMap<String, Finger> allFingers){

        LinkedHashMap<String, Vector> fingerTipsMap = new LinkedHashMap<>();
        fingerTipsMap.put("thumb", allFingers.get("thumb").tipPosition());
        fingerTipsMap.put("index", allFingers.get("index").tipPosition());
        fingerTipsMap.put("middle", allFingers.get("middle").tipPosition());
        fingerTipsMap.put("ring", allFingers.get("ring").tipPosition());
        fingerTipsMap.put("pinky", allFingers.get("pinky").tipPosition());

        return  fingerTipsMap;
    }

    private LinkedHashMap<String, Finger> getFingersArray(Hand hand){

        LinkedHashMap<String, Finger> fingersArray = new LinkedHashMap<>();

        for (Finger finger : hand.fingers()) {
            switch (finger.type()){
                case TYPE_THUMB:
                    fingersArray.put("thumb", finger);
                    break;
                case TYPE_INDEX:
                    fingersArray.put("index", finger);
                    break;
                case TYPE_MIDDLE:
                    fingersArray.put("middle", finger);
                    break;
                case TYPE_RING:
                    fingersArray.put("ring", finger);
                    break;
                case TYPE_PINKY:
                    fingersArray.put("pinky", finger);
                    break;
            }
        }

        return fingersArray;

    }

    public void drawWithFinger(Finger finger, int handNumber) {

        float coordinateMultiplier = 3;
        float xOffset = canvasWidth / 2;
        float yOffset = canvasHeight + 200;

        float calculatedX = (finger.tipPosition().getX() * coordinateMultiplier) + xOffset;
        float calculatedY = yOffset - (finger.tipPosition().getY() * coordinateMultiplier);

        ellipse(calculatedX, calculatedY, 4, 4);
    }

    public void drawWithHand(Vector middleTip, Vector palm){
        float tipX = middleTip.getX();
        float tipY = middleTip.getY();
        float palmX = palm.getX();
        float palmY = palm.getY();

        float calculatedTipX = (tipX * leapToCanvasAspectMultiplier) + canvasWidthOffset;
        float calculatedTipY = canvasHeightOffset - (tipY * leapToCanvasAspectMultiplier);

        float calculatedPalmX = (palmX * leapToCanvasAspectMultiplier) + canvasWidthOffset;
        float calculatedPalmY = canvasHeightOffset - (palmY * leapToCanvasAspectMultiplier);

        //move lightSaber
        lightSaber.setX((int)Math.min(calculatedTipX,calculatedPalmX));
        lightSaber.setY((int)Math.min(calculatedTipY, calculatedPalmY));
        lightSaber.setWidth((int)Math.abs(calculatedTipX - calculatedPalmX) + lightSaberBladeWidth);
        lightSaber.setHeight((int) Math.abs(calculatedTipY - calculatedPalmY));

            //for debugging
//        rect(lightSaber.getX(),lightSaber.getY(),lightSaber.getWidth(),lightSaber.getHeight());

        beginShape();

            //for debugging
//        stroke(0,255,0);

        noStroke();
        texture(lightSaberRed);
        vertex(calculatedTipX, calculatedTipY, 0 , 0);
        vertex(calculatedTipX + lightSaberBladeWidth, calculatedTipY, lightSaberImageWidth , 0);
        vertex(calculatedPalmX + lightSaberBladeWidth, calculatedPalmY, lightSaberImageWidth , lightSaberImageHeight);
        vertex(calculatedPalmX, calculatedPalmY, 0 , lightSaberImageHeight);
        endShape();
    }

    public float[] trackVector(Vector vector){
        float vectorX = vector.getX();
        float vectorY = vector.getY();

        //maps X and Y to the screen
        float calculatedX = (vectorX * leapToCanvasAspectMultiplier) + canvasWidthOffset;
        float calculatedY = canvasHeightOffset - (vectorY * leapToCanvasAspectMultiplier);

        return(new float[]{calculatedX,calculatedY});
    }

    public void correctBoundary(DrawableImage image){
        //offsets X and Y with half the image's width and height to the topleft in order to match the boundary correctly
        image.setImageX(image.getX() - image.getImageWidth() / 2);
        image.setImageY(image.getY() - image.getImageHeight() / 2);
        image.setX(image.getX() - image.getWidth() / 2);
        image.setY(image.getY() - image.getHeight() / 2);

    }

    public void drawImage(DrawableImage image){
        //for debugging
//        rect(image.getX(),image.getY(),image.getWidth(),image.getHeight());

        beginShape();

        //for debugging
//        stroke(0,255,0);

        noStroke();
        texture(image.getImage());
        vertex(image.getImageX(), image.getImageY(), 0 , 0);
        vertex(image.getImageX() + image.getImageWidth(), image.getImageY(), image.getImageWidth() , 0);
        vertex(image.getImageX() + image.getImageWidth(), image.getImageY() + image.getImageHeight(), image.getImageWidth() , image.getImageHeight());
        vertex(image.getImageX(), image.getImageY() + image.getImageHeight(), 0 , image.getImageHeight());
        endShape();
    }

    public void handleStretching(int handHumber){
        lightning.play();
    }

    public void handleLeapCircleGesture(CircleGesture gesture) {
        float radius = gesture.radius();
        float x = gesture.center().getX();
        float y = gesture.center().getY();

        ellipse(x, y, radius, radius);
    }

    public void handleTheBird(int handNumber) {
        String specialMessage = "Fuck you";
        System.out.println(specialMessage);
        text(specialMessage, (int) random(1, canvasWidth), (int) random(1, canvasHeight));
    }

    public void handleFlick(int handNumber){
        if(handNumber == 0){
            System.out.println("flick");
        }
    }

    public void handleSwipe(int handNumber){
        if(handNumber == 0){
            int randomIndex = (int)random(0,5);
            lightSaberSwings.get(randomIndex).play();
        }
    }

    public void spawnEnemies(){
        int random = (int)random(1,101);
        if(random <= spawnChance){
            int x = (int)random(spawnZoneTopX, spawnZoneBottomX);
            int y = (int)random(spawnZoneTopY, spawnZoneBottomY);

            int distanceToTopOfSpawnZone = y - spawnZoneTopY;
            if(distanceToTopOfSpawnZone < (spawnZoneBottomY - spawnZoneTopY) / 2){
                x = (int)random(spawnZone2TopX, spawnZone2BottomX);
            }
            float scale = (((float)y / spawnZoneTopY ) - 1)* enemyScaleMultiplier + 1;
            int width = (int)(scale * enemyAverageWidth);
            int height = (int)(scale * enemyAverageHeight);

            int z = (int)Math.max(0, enemyMaxDepth - (scale * depthScalingMultiplier));

            enemies.add(new Enemy(x,y,z,width,height));
        }
    }

    public void checkCollision(){
        List<Enemy> newlyCutEnemies = new ArrayList<>();
        List<Enemy> newlyElectrocutedEnemies = new ArrayList<>();
        if(IsMainHandSwiping){
            newlyCutEnemies = enemies.stream()
                    .filter(x -> x.intersects(lightSaber))
                    .collect(Collectors.toList());

            newlyCutEnemies.forEach(x -> x.setDeathTimestamp(controller.now()));
            newlyCutEnemies.forEach(x -> x.setDeathAnimationIndex((int) random(0, stormTrooperCut.size())));

            enemies = enemies.stream()
                    .filter(x -> !x.intersects(lightSaber))
                    .collect(Collectors.toList());
        }

        if(IsUsingForceLightning){
            newlyElectrocutedEnemies = enemies.stream()
                    .filter(x -> x.intersects(lightningImage))
                    .sorted(Comparator.comparing(Rectangle::getZ))
                    .limit(lightningMaxTargetsPerFrame)
                    .collect(Collectors.toList());

            newlyElectrocutedEnemies.forEach(x -> x.setHealht(x.getHealht() - 1));
            newlyElectrocutedEnemies.stream()
                    .filter(x -> x.getHealht() <= 0)
                    .forEach(x -> x.setDeathTimestamp(controller.now()));

            enemies = enemies.stream()
                    .filter(x -> !x.intersects(lightningImage) || x.getHealht() > 0)
                    .collect(Collectors.toList());
        }

        cutEnemies.addAll(newlyCutEnemies);
        electrocutedEnemies.addAll(newlyElectrocutedEnemies);
    }

    public void drawEnemies(){
        enemies = enemies.stream()
                .sorted(Comparator.comparing(Enemy::getZ).reversed())
                .collect(Collectors.toList());
        for(Enemy enemy : enemies){
//            System.out.println(enemy.getZ());
            image(stormTrooperImage, enemy.getX(),enemy.getY(),enemy.getWidth(),enemy.getHeight());

            //for debugging
//            rect(enemy.getX(),enemy.getY(),enemy.getWidth(),enemy.getHeight());
        }

        for (Enemy enemy: cutEnemies){
            image(stormTrooperCut.get(enemy.getDeathAnimationIndex()), enemy.getX(),enemy.getY(),enemy.getWidth(),enemy.getHeight());
        }

        for (Enemy enemy: electrocutedEnemies){
            image(stormTrooperElectrocuted, enemy.getX(),enemy.getY(),enemy.getWidth(),enemy.getHeight());
        }

        cutEnemies = cutEnemies.stream()
                .filter(x -> controller.now() - x.getDeathTimestamp() < enemyDeathAnimationDuration)
                .sorted(Comparator.comparing(Enemy::getZ).reversed())
                .collect(Collectors.toList());

        electrocutedEnemies = electrocutedEnemies.stream()
                .filter(x -> x.getDeathTimestamp() != -1 && controller.now() - x.getDeathTimestamp() < enemyDeathAnimationDuration)
                .sorted(Comparator.comparing(Enemy::getZ).reversed())
                .collect(Collectors.toList());
    }


    public static void main(String... args){
        PApplet.main("ProcessingTest");
    }
}