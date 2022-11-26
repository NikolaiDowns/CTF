import processing.core.PImage;

import java.util.List;

public class LeftSafeZone extends Entity implements ScheduledEntity, DynamicEntity
{
    public static LeftSafeZone leftZone;

    public static final String LeftSafeZone_KEY = "zone_l";
    public static final int LeftSafeZone_ACTION_PERIOD = 4;
    public static final int LeftSafeZone_NUM_PROPERTIES = 5;
    public static final int LeftSafeZone_ID = 1;
    public static final int LeftSafeZone_COL = 2;
    public static final int LeftSafeZone_ROW = 3;

    private LeftSafeZone(       String id,
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

    public static void createLeftSafeZone(String id,
                                            Point position,
                                            List<PImage> images,
                                            int resourceLimit,
                                            int resourceCount,
                                            int actionPeriod,
                                            int animationPeriod,
                                            int health,
                                            int healthLimit)
    {
        if (leftZone == null)
        {
            leftZone = new
                    LeftSafeZone(id, position, images, resourceLimit,
                    resourceCount, actionPeriod, animationPeriod, health, healthLimit);
        }
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        if (DudeFull.getP2().isHasFlag())
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
