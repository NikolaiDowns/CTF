import processing.core.PImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeFull extends Dude implements DynamicEntity{


    public static final int FULL_PROPERTIES = 6;
    public static final int FULL_ACTION_PERIOD = 4;
    public static final int FULL_ANIMATION_PERIOD = 5;

    private static DudeFull p2;

    private boolean hasFlag;

    public boolean isHasFlag() {
        return hasFlag;
    }

    public int getScore() {
        return score;
    }

    private int score;

    private static final Point myZone = new Point(20,0);
    private static final Point goalZone = new Point(15,0);

    public DudeFull(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod,
            int health,
            int healthLimit)
    {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
        this.hasFlag = false;
        this.score = 0;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        if(distance(VirtualWorld.p2X,VirtualWorld.p2Y, myZone.getX(), myZone.getY())<1.5)
        {
            if(hasFlag)
            {
                hasFlag = false;
                score += 1;
                System.out.println(score);
//                scheduler.scheduleEvent(LeftScoreBoard.leftScore,
//                        this.createActivityAction(world, imageStore),
//                        this.actionPeriod);
            }
        }
        else if (distance(VirtualWorld.p2X,VirtualWorld.p2Y, goalZone.getX(), goalZone.getY())<1.5)
        {
            hasFlag = true;
        }
        else if(hittingEnemy(world))
        {
            hasFlag = false;
            VirtualWorld.p2X = 4;
            VirtualWorld.p2Y = 3;
            this.setPosition(new Point((int)VirtualWorld.p2X, (int)VirtualWorld.p2Y));
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
    }

    private boolean hittingEnemy(WorldModel world)
    {
        Optional<Entity> nearestFairy =
                world.findNearest(this.position, new ArrayList<>(Arrays.asList(Fairy.class)));
        Optional<Entity> nearestSapling =
                world.findNearest(this.position, new ArrayList<>(Arrays.asList(Sapling.class)));
        Optional<Entity> nearestObstacle =
                world.findNearest(this.position, new ArrayList<>(Arrays.asList(Obstacle.class, Laser.class)));

        if(nearestFairy.isPresent() && distance(VirtualWorld.p2X,VirtualWorld.p2Y,
                (double)nearestFairy.get().getPosition().getX(),
                (double)nearestFairy.get().getPosition().getY())<1.5)
        {
            return false;
        }
        else if(nearestObstacle.isPresent() && distance(VirtualWorld.p2X,VirtualWorld.p2Y,
                (double)nearestObstacle.get().getPosition().getX(),
                (double)nearestObstacle.get().getPosition().getY())<1)
        {
            return false;
        }

        return false;
    }

    private double distance(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }

    public static DudeFull getP2() {
        return p2;
    }
    public static void setP2(DudeFull p2) {
        DudeFull.p2 = p2;
    }

}