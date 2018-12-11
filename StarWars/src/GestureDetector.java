import com.leapmotion.leap.Vector;
import com.leapmotion.leap.*;

//Offers boolean functions that take in all finger tips and palm
// and return if the user is making a specific gesture like "Is Flipping", "Is Stretching"
public class GestureDetector {

    private Controller controller = new Controller();
    private static long MaximumFlickDuration = 700000;
    private static long SwipeInBetweenTime = 500000;
    private static long MaximumHandOpeningDuration = 500000;
    private boolean isGesturingBird;
    private boolean isHandCurrentlyOpen;
    private long closedFingersTimestamp = 0l;
    private long openedFingersTimestamp = 0l;
    private long closedFistTimestamp = 0l;
    private long openedHandTimestamp = 0l;
    private long lastSwipeTimestamp = 0l;

    public GestureDetector(Controller controller) {
        this.controller = controller;
        this.isGesturingBird = false;
        this.isHandCurrentlyOpen = false;
    }

    public boolean IsClosedFist(
            Vector thumbTip,
            Vector indexTip,
            Vector middleTip,
            Vector ringTip,
            Vector pinkyTip,
            Vector palm
    ){

        float distanceThumb = thumbTip.distanceTo(palm);
        float distanceIndex = indexTip.distanceTo(palm);
        float distanceMiddle = middleTip.distanceTo(palm);
        float distanceRing = ringTip.distanceTo(palm);
        float distancePinky = pinkyTip.distanceTo(palm);

        float requiredMaxClosedFistDistance = 50;

        boolean thumbIsFolded = distanceThumb <= requiredMaxClosedFistDistance;
        boolean indexIsFolded = distanceIndex <= requiredMaxClosedFistDistance;
        boolean ringIsFolded = distanceRing <= requiredMaxClosedFistDistance;
        boolean pinkyIsFolded = distancePinky <= requiredMaxClosedFistDistance;
        boolean middleIsFolded = distanceMiddle <= requiredMaxClosedFistDistance;

        if (indexIsFolded && middleIsFolded && ringIsFolded && pinkyIsFolded){
            closedFistTimestamp = controller.now();
//            System.out.println("CLOSED FIST");
            return true;
        }
        return false;
    }

    public boolean IsOpenHand(
            Vector thumbTip,
            Vector indexTip,
            Vector middleTip,
            Vector ringTip,
            Vector pinkyTip,
            Vector palm
    ){


        float distanceThumb = thumbTip.distanceTo(palm);
        float distanceIndex = indexTip.distanceTo(palm);
        float distanceMiddle = middleTip.distanceTo(palm);
        float distanceRing = ringTip.distanceTo(palm);
        float distancePinky = pinkyTip.distanceTo(palm);

        float requiredMinOpenHandDistance = 70;

        boolean thumbIsStretched = distanceThumb >= requiredMinOpenHandDistance;
        boolean indexIsStretched = distanceIndex >= requiredMinOpenHandDistance;
        boolean ringIsStretched = distanceRing >= requiredMinOpenHandDistance;
        boolean pinkyIsStretched = distancePinky >= requiredMinOpenHandDistance;
        boolean middleIsStretched = distanceMiddle >= requiredMinOpenHandDistance;

        if (thumbIsStretched && indexIsStretched && middleIsStretched && ringIsStretched && pinkyIsStretched){
            openedHandTimestamp = controller.now();
            isHandCurrentlyOpen = true;
//            System.out.println("OPEN HAND");
            return true;
        }
        isHandCurrentlyOpen = false;
        return false;
    }

    public boolean IsFlicking(
            Vector thumbTip,
            Vector indexTip,
            Vector middleTip,
            Vector ringTip,
            Vector pinkyTip,
            Vector palm) {

        double distance = thumbTip.distanceTo(indexTip);

        if (distance < 30) {
            closedFingersTimestamp = controller.now();
            //System.out.println("Closed: " + closedFingersTimestamp);
        }

        if (distance > 75) {
            openedFingersTimestamp = controller.now();
            long timeDifference = openedFingersTimestamp - closedFingersTimestamp;
            boolean isFlicking = closedFingersTimestamp != -1 && timeDifference < MaximumFlickDuration;
            if (isFlicking) {
                closedFingersTimestamp = -1l;
                return true;
            }
        }

        return false;
    }

    public boolean isFlippingTheBird(
            Vector thumbTip,
            Vector indexTip,
            Vector middleTip,
            Vector ringTip,
            Vector pinkyTip,
            Vector palm) {


        float distanceThumb = thumbTip.distanceTo(palm);
        float distanceIndex = indexTip.distanceTo(palm);
        float distanceMiddle = middleTip.distanceTo(palm);
        float distanceRing = ringTip.distanceTo(palm);
        float distancePinky = pinkyTip.distanceTo(palm);

        float requiredMaxFingersDistance = 50;
        float requiredMinMiddleDistance = 70;
        String specialMessage = "Fuck you";

        boolean thumbIsFolded = distanceThumb <= requiredMaxFingersDistance;
        boolean indexIsFolded = distanceIndex <= requiredMaxFingersDistance;
        boolean ringIsFolded = distanceRing <= requiredMaxFingersDistance;
        boolean pinkyIsFolded = distancePinky <= requiredMaxFingersDistance;
        boolean middleIsStraight = distanceMiddle >= requiredMinMiddleDistance;

        if (indexIsFolded && ringIsFolded && pinkyIsFolded && middleIsStraight) {
            if (!this.isGesturingBird) {
                return true;
            }

        } else {
            this.isGesturingBird = false;
        }


//        System.out.println("Thumb - " + distanceThumb);
//        System.out.println("Index - " + distanceIndex);
//        System.out.println("Ring - " + distanceRing);
//        System.out.println("Pinky - " + distancePinky);
//        System.out.println("*****Middle - " + distanceMiddle);

        return false;
    }

    public boolean IsStretching(
            Vector thumbTip,
            Vector indexTip,
            Vector middleTip,
            Vector ringTip,
            Vector pinkyTip,
            Vector palm) {

        // execute detections for open and closed fist
        // if one of them is detected > they will be timestamped
        // check current timestamps > if "open hand" is detected after "closed fist" within a certain time frame ...
        // then the user has "opened" their previously closed hand

        this.IsClosedFist(thumbTip, indexTip, middleTip, ringTip, pinkyTip, palm);
        this.IsOpenHand(thumbTip, indexTip, middleTip, ringTip, pinkyTip, palm);


        if (closedFistTimestamp != -1 && openedHandTimestamp != -1){
            //MaximumHandOpeningDuration
            long timeDifferenceBetweenClosedAndOpened = openedHandTimestamp - closedFistTimestamp;
            boolean timeIsWithinLimit = timeDifferenceBetweenClosedAndOpened <= MaximumHandOpeningDuration;
            if(isHandCurrentlyOpen && timeIsWithinLimit){
                // gesture has been made

                //reset timers
                openedHandTimestamp = -1l;
                closedFistTimestamp = -1l;

                System.out.println("UNLIMITED POWAAAAA!!!!");

                return true;
            }

        }

        return false;
    }

    public boolean IsSwiping(Frame frame) {

        for (Gesture gesture : frame.gestures()) {
            if(gesture.type() == Gesture.Type.TYPE_SWIPE){
                long timeDifference = controller.now() - lastSwipeTimestamp;
                if(timeDifference > SwipeInBetweenTime){
                    lastSwipeTimestamp = controller.now();
                    return true;
                }
            }
        }

        return false;
    }
}
