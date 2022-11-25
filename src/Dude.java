import processing.core.PImage;

import java.util.List;

public abstract class Dude extends Entity implements ScheduledEntity{

    public static final String DUDE_KEY = "dude";
    public static final int DUDE_ID = 1;
    public static final int DUDE_COL = 2;
    public static final int DUDE_ROW = 3;


    public Dude(String id,
                Point position,
                List<PImage> images,
                int resourceLimit,
                int resourceCount,
                int actionPeriod,
                int animationPeriod,
                int health,
                int healthLimit) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod,
                animationPeriod, health, healthLimit);
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