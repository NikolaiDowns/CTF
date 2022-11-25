import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Sapling extends Plant implements DynamicEntity{

    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_HEALTH_LIMIT = 5;
    public static final int SAPLING_ACTION_ANIMATION_PERIOD = 1000; // have to be in sync since grows and gains health at same time
    public static final int SAPLING_NUM_PROPERTIES = 4;
    public static final int SAPLING_ID = 1;
    public static final int SAPLING_COL = 2;
    public static final int SAPLING_ROW = 3;
    public static final int SAPLING_HEALTH = 4;

    public Sapling(
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
        super(id, position, images, resourceLimit, resourceCount, actionPeriod,
                animationPeriod, health, healthLimit);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore,
                                EventScheduler scheduler)
    {
        this.health++;
        if (!this.transformPlant(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), this.actionPeriod);
        }
    }
    private int getNumFromRange(int max, int min)
    {
        Random rand = new Random();
        return min + rand.nextInt(
                max
                        - min);
    }

    protected boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.health <= 0) {
            Stump stump = EntityFactory.createStump(this.id,
                    this.position,
                    imageStore.getImageList(Stump.STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);

            return true;
        }
        else if (this.health >= this.healthLimit)
        {
            Tree tree = EntityFactory.createTree("tree_" + this.id, this.position,
                    this.getNumFromRange(Tree.TREE_ACTION_MAX, Tree.TREE_ACTION_MIN),
                    this.getNumFromRange(Tree.TREE_ANIMATION_MAX, Tree.TREE_ANIMATION_MIN),
                    this.getNumFromRange(Tree.TREE_HEALTH_MAX, Tree.TREE_HEALTH_MIN),
                    imageStore.getImageList(Tree.TREE_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(tree);
            ((ScheduledEntity)tree).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

}