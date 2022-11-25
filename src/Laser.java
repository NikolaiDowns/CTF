import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class Laser extends Entity implements DynamicEntity, ScheduledEntity{


    public static final String LASER_KEY = "laser";
    public static final int LASER_NUM_PROPERTIES = 10;
    public static final int LASER_ID = 1;
    public static final int LASER_COL = 2;
    public static final int LASER_ROW = 3;
    public static final int LASER_ANIMATION_PERIOD = 4;
    public static final int LASER_ACTION_PERIOD = 5;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    private double x;
    private double y;
    private double dx;
    private double dy;

    public Laser( String id,
                  Point position,
                  List<PImage> images,
                  int resourceLimit,
                  int resourceCount,
                  int actionPeriod,
                  int animationPeriod,
                  int health,
                  int healthLimit)
    {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod,
                animationPeriod, health, healthLimit);
        this.x = position.getX();
        this.y = position.getY();
    // need to get nearest player and make calculation
                //direction;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        if (dx == 0 && dy == 0)
        {
            calculateD();
        }
        this.x += this.dx;
        this.y += this.dy;
        Point nextPos = new Point ((int) x, (int) y);
        if (withinBounds(nextPos, world) && (world.getOccupancyCell(nextPos) instanceof Forest || world.getOccupancyCell(nextPos) == null))
        {
            //world.moveEntity(this, nextPos);
            this.setPosition(nextPos);
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.actionPeriod);
        }
        else
        {
            world.moveEntity(this, new Point(21,0));
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
        }

    }

    private void calculateD()
    {
        double tx;
        double ty;
        if(distance(x,y,VirtualWorld.p1X,VirtualWorld.p1Y) > distance(x,y,VirtualWorld.p2X,VirtualWorld.p2Y))
        {
            tx = VirtualWorld.p2X;
            ty = VirtualWorld.p2Y;
        }
        else
        {
            tx = VirtualWorld.p1X;
            ty = VirtualWorld.p1Y;
        }


        double angle = Math.abs(Math.atan(((ty - position.getY()) / (position.getX() - tx))));
        if(tx >= position.getX() && ty <= position.getY())
        {
            this.dx = Math.cos(angle);
            this.dy = -1*Math.sin(angle);
        }
        else if (tx <= position.getX() && ty <= position.getY())
        {
            this.dx = -1 * Math.cos(angle);
            this.dy = -1 * Math.sin(angle);
        }
        else if (tx <= position.getX() && ty >= position.getY())
        {
            this.dx = -1*Math.cos(angle);
            this.dy = Math.sin(angle);
        }
        else if (tx >= position.getX() && ty >= position.getY())
        {
            this.dx = 1.4*Math.cos(angle);
            this.dy = 1.4*Math.sin(angle);
        }

    }


    private static boolean withinBounds(Point p, WorldModel world)
    {
        return p.getY() >= 0 && p.getY() < world.getNumCols() &&
                p.getX() >= 0 && p.getX() < world.getNumRows();
    }


    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore){
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                this.createAnimationAction(0),
                this.getAnimationPeriod());
    }

    private double distance(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }


}
