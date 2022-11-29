import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class House extends Entity implements DynamicEntity, ScheduledEntity
{

    public static final String HOUSE_KEY = "house";
    public static final int HOUSE_ACTION_PERIOD = 4;
    public static final int HOUSE_NUM_PROPERTIES = 5;
    public static final int HOUSE_ID = 1;
    public static final int HOUSE_COL = 2;
    public static final int HOUSE_ROW = 3;



    public House (String id,
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
        }
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        calculateD();
        Optional<Entity> turretTarget =
                world.findNearest(this.position, new ArrayList<>(Arrays.asList(DudeFull.class, DudeNotFull.class)));

        if (turretTarget.isPresent()) {
            Point tgtPos = turretTarget.get().position;
//            {
//                return new Laser(id, position, images, 0, 0,
//                        actionPeriod, animationPeriod, 0, 0);
//            }

            Entity laser = EntityFactory.createLaser("laser_" + this.id, this.position,
                    Laser.LASER_ACTION_PERIOD,
                    40,
                    imageStore.getImageList("laser")); //Laser.LASER_ANIMATION_PERIOD

                world.addEntity(laser);
                ((ScheduledEntity)laser).scheduleActions(scheduler, world, imageStore);

        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
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
                this.getAnimationPeriod());
    }

    private void calculateD()
    {
        double tx;
        double ty;
        double x = position.getX();
        double y = position.getY();
        if(distance(x, y, VirtualWorld.p1X,VirtualWorld.p1Y) > distance(x,y,VirtualWorld.p2X,VirtualWorld.p2Y))
        {
            tx = VirtualWorld.p2X;
            ty = VirtualWorld.p2Y;
        }
        else
        {
            tx = VirtualWorld.p1X;
            ty = VirtualWorld.p1Y;
        }


        double angle = Math.abs((180/Math.PI) *Math.atan(((ty - position.getY()) / (position.getX() - tx))));
        //System.out.println(angle);
        if(tx >= position.getX() && ty <= position.getY())
        {
            if(angle <= 30)
            {
                images = VirtualWorld.imageList.getImageList("cannon0");
                //System.out.println("NE 0");
            }
            else if(angle <= 60)
            {
                images = VirtualWorld.imageList.getImageList("cannon1");
                //System.out.println("NE 1");
            }
            else
            {
                images = VirtualWorld.imageList.getImageList("cannon2");
                //System.out.println("NE 2");
            }
        }
        else if (tx <= position.getX() && ty <= position.getY())
        {
            if(angle <= 30)
            {
                images = VirtualWorld.imageList.getImageList("cannon4");
                //System.out.println("NW 4");
            }
            else if(angle <= 60)
            {
                images = VirtualWorld.imageList.getImageList("cannon3");
                //System.out.println("NW 3");
            }
            else
            {
                images = VirtualWorld.imageList.getImageList("cannon2");
                //System.out.println("NW 2");
            }
        }
        else if (tx <= position.getX() && ty >= position.getY())
        {
            if(angle <= 30)
            {
                images = VirtualWorld.imageList.getImageList("cannon4");
                //System.out.println("SW 4");
            }
            else if(angle <= 60)
            {
                images = VirtualWorld.imageList.getImageList("cannon5");
                //System.out.println("SW 5");
            }
            else
            {
                images = VirtualWorld.imageList.getImageList("cannon6");
                //System.out.println("SW 6");
            }
        }
        else if (tx >= position.getX() && ty >= position.getY())
        {
            if(angle <= 30)
            {
                images = VirtualWorld.imageList.getImageList("cannon0");
                //System.out.println("SE 0");
            }
            else if(angle <= 60)
            {
                images = VirtualWorld.imageList.getImageList("cannon7");
                //System.out.println("SE 7");
            }
            else
            {
                images = VirtualWorld.imageList.getImageList("cannon6");
                //System.out.println("SE 6");
            }
        }

    }

    private double distance(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }


}
