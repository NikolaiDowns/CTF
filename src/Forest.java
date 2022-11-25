import processing.core.PImage;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class Forest extends Entity implements DynamicEntity, ScheduledEntity{

    public static final String FOREST_KEY = "forest";
    public static final int FOREST_NUM_PROPERTIES = 7;
    public static final int FOREST_ID = 1;
    public static final int FOREST_COL = 2;
    public static final int FOREST_ROW = 3;
    public static final int FOREST_ACTION_PERIOD = 5;

    public static final int FOREST_ANIMATION_PERIOD = 4;

    public Forest(       String id,
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
        if (notContactingEntity(world))
        {
            int randomNum = ThreadLocalRandom.current().nextInt(0, 10);

            if(randomNum % 7 == 0) {
                Entity obstacle = EntityFactory.createObstacle("obstacle_" + this.id, this.position,
                        Obstacle.OBSTACLE_ANIMATION_PERIOD,
                        imageStore.getImageList(Tree.TREE_KEY));

                world.addEntity(obstacle);
                ((ScheduledEntity)obstacle).scheduleActions(scheduler, world, imageStore);
                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        this.actionPeriod);
            }
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
    }

    private boolean notContactingEntity(WorldModel world)
    {
        Optional<Entity> nearestEntity =
                world.findNearest(this.position, new ArrayList<>(Arrays.asList(Fairy.class,
                        Laser.class, Obstacle.class, DudeNotFull.class, DudeFull.class)));

        if(nearestEntity.isPresent() && distance(this.position.getX(), this.position.getY(),
                nearestEntity.get().getPosition().getX(),
                nearestEntity.get().getPosition().getY())<1.5)
        {
            return false;
        }
        return true;
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

    private double distance(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }

}