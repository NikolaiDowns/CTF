import processing.core.PImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends Dude implements DynamicEntity{

    public static final int NOT_FULL_PROPERTIES = 7;
    public static final int NOT_FULL_LIMIT = 4;
    public static final int NOT_FULL_ACTION_PERIOD = 5;
    public static final int NOT_FULL_ANIMATION_PERIOD = 6;

    private static final Point myZone = new Point(2,12);
    private static final Point goalZone = new Point(37,12);

    private static DudeNotFull p1;

    private boolean hasFlag;
    public boolean isHasFlag() {
        return hasFlag;
    }
    private int score;

    public int getScore() {
        return score;
    }

    public DudeNotFull(
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
        if(distance(VirtualWorld.p1X,VirtualWorld.p1Y, myZone.getX(), myZone.getY())<1.5)
        {
            if(hasFlag)
            {
                hasFlag = false;
                score += 1;
//                scheduler.scheduleEvent(LeftScoreBoard.leftScore,
//                        this.createActivityAction(world, imageStore),
//                        this.actionPeriod);
                System.out.println(score);
            }
        }
        else if (distance(VirtualWorld.p1X,VirtualWorld.p1Y, goalZone.getX(), goalZone.getY())<1.5)
        {
            hasFlag = true;
        }
        else if(hittingEnemy(world))
        {
            hasFlag = false;
            VirtualWorld.p1X = 2;
            VirtualWorld.p1Y = 12;
            this.setPosition(new Point((int)VirtualWorld.p1X, (int)VirtualWorld.p1Y));
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
                world.findNearest(this.position, new ArrayList<>(Arrays.asList(Laser.class)));

        if(nearestFairy.isPresent() && distance(VirtualWorld.p1X,VirtualWorld.p1Y,
                (double)nearestFairy.get().getPosition().getX(),
                (double)nearestFairy.get().getPosition().getY())<1.5)
        {
            return true;
        }
        else if(nearestObstacle.isPresent() && distance(VirtualWorld.p1X,VirtualWorld.p1Y,
                (double)nearestObstacle.get().getPosition().getX(),
                (double)nearestObstacle.get().getPosition().getY())<1)
        {
            return true;
        }

        return false;
    }

    private double distance(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }

    public static DudeNotFull getP1() {
        return p1;
    }
    public static void setP1(DudeNotFull p1) {
        DudeNotFull.p1 = p1;
    }

}