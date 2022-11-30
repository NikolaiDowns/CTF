import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
// make zombieFactory <>
// Easy zombie factory
// Medium Zombie Factory
// Hard Zombie Factory
//


public class Fairy extends Entity implements DynamicEntity, ScheduledEntity{

    public static final String FAIRY_KEY = "fairy";
    public static final int FAIRY_NUM_PROPERTIES = 6;
    public static final int FAIRY_ID = 1;
    public static final int FAIRY_COL = 2;
    public static final int FAIRY_ROW = 3;
    public static final int FAIRY_ANIMATION_PERIOD = 4;

    private PathingStrategy strategy;

    //private PathingStrategy strategy = new AStarPathingStrategy();
     // private PathingStrategy strategy = new DijkstraPathingStrategy();
    public static final int FAIRY_ACTION_PERIOD = 5;

    public Fairy(       String id,
                       Point position,
                       List<PImage> images,
                       int resourceLimit,
                       int resourceCount,
                       int actionPeriod,
                       int animationPeriod,
                       int health,
                       int healthLimit,
                        PathingStrategy strategy)
    {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod,
                animationPeriod, health, healthLimit);
        this.strategy = strategy;
    }
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        System.out.println("herre");
        Optional<Entity> fairyTarget =
                world.findNearest(this.position, new ArrayList<>(Arrays.asList(DudeNotFull.class, DudeFull.class)));

        if (fairyTarget.isPresent()) {
            this.moveToFairy(world, fairyTarget.get(), scheduler);
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
    }

    private Point nextPositionFairy(WorldModel world, Point destPos)
    {
        List<Point> points;

        points = strategy.computePath(this.position, destPos,
                p ->  withinBounds(p, world)
//                        && p.getY() <= 19
//                        && p.getY() >= 4
//                        && p.getX() >= 8
//                        && p.getX() <= 31,
                && world.getBackgroundCell(p).getCurrentImage() == VirtualWorld.imageList.getImageList("forest").get(0),
                (p1, p2) -> neighbors(p1,p2),
                PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS );
        //PathingStrategy.CARDINAL_NEIGHBORS);
        //DIAGONAL_NEIGHBORS);
        //DIAGONAL_CARDINAL_NEIGHBORS);
        if (points.isEmpty())
            return this.position;

        return points.get(0);
    }

    private static boolean withinBounds(Point p, WorldModel world)
    {
        return p.getY() >= 0 && p.getY() < world.getNumCols() &&
                p.getX() >= 0 && p.getX() < world.getNumRows();
    }

    private static boolean neighbors(Point p1, Point p2)
    {
        return p1.getX()+1 == p2.getX() && p1.getY() == p2.getY() ||
                p1.getX()-1 == p2.getX() && p1.getY() == p2.getY() ||
                p1.getX() == p2.getX() && p1.getY()+1 == p2.getY() ||
                p1.getX() == p2.getX() && p1.getY()-1 == p2.getY();
    }


    private void moveToFairy(WorldModel world, Entity target, EventScheduler scheduler)
    {
//        if (this.position.adjacent(target.position)) {
////            world.removeEntity(target);
////            scheduler.unscheduleAllEvents(target);
//            return;
//        }
        Point nextPos = this.nextPositionFairy(world, target.position);
            if (!this.position.equals(nextPos) && !(world.isOccupied(nextPos))) {
//                Optional<Entity> occupant = world.getOccupant(nextPos);
//                if (occupant.isPresent()) {
//                    scheduler.unscheduleAllEvents(occupant.get());
//                }
                if (nextPos.getX() < position.getX())
                {

                    images = VirtualWorld.imageList.getImageList("LeftZombie");
                }
                else
                {
                    images = VirtualWorld.imageList.getImageList("RightZombie");
                }

                world.moveEntity(this, nextPos);
            }
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {                scheduler.scheduleEvent(this,
            this.createActivityAction(world, imageStore),
            this.actionPeriod);
        scheduler.scheduleEvent(this,
                this.createAnimationAction(0),
                this.getAnimationPeriod());}

}