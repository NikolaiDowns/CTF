import processing.core.PImage;

import java.util.List;

public class RightSafeZone extends Entity implements ScheduledEntity, DynamicEntity
{
    public static RightSafeZone rightZone;

    public static final String RightSafeZone_KEY = "zone_r";
    public static final int RightSafeZone_ACTION_PERIOD = 4;
    public static final int RightSafeZone_NUM_PROPERTIES = 5;
    public static final int RightSafeZone_ID = 1;
    public static final int RightSafeZone_COL = 2;
    public static final int RightSafeZone_ROW = 3;

    private RightSafeZone(       String id,
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

    public static void createRightSafeZone(String id,
                                          Point position,
                                          List<PImage> images,
                                          int resourceLimit,
                                          int resourceCount,
                                          int actionPeriod,
                                          int animationPeriod,
                                          int health,
                                          int healthLimit)
    {
        if (rightZone == null)
        {
            rightZone = new
                    RightSafeZone(id, position, images, resourceLimit,
                    resourceCount, actionPeriod, animationPeriod, health, healthLimit);
        }
    }


    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        if (DudeNotFull.getP1().isHasFlag())
        {
            images = VirtualWorld.imageList.getImageList("noFlag");
        }
        else
        {
            images = VirtualWorld.imageList.getImageList("hasFlag");
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
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
}
