import processing.core.PImage;

import java.util.List;



public class RightScoreBoard extends Entity implements DynamicEntity, ScheduledEntity
{
    public static RightScoreBoard rightScore;

    public static final String RightScoreBoard_KEY = "score_r";
    public static final int RightScoreBoard_ACTION_PERIOD = 4;
    public static final int RightScoreBoard_NUM_PROPERTIES = 5;
    public static final int RightScoreBoard_ID = 1;
    public static final int RightScoreBoard_COL = 2;
    public static final int RightScoreBoard_ROW = 3;

    private RightScoreBoard(       String id,
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

    public static void createRightScoreBoard(String id,
                                            Point position,
                                            List<PImage> images,
                                            int resourceLimit,
                                            int resourceCount,
                                            int actionPeriod,
                                            int animationPeriod,
                                            int health,
                                            int healthLimit)
    {
        if (rightScore == null)
        {
            rightScore = new
                    RightScoreBoard(id, position, images, resourceLimit,
                    resourceCount, actionPeriod, animationPeriod, health, healthLimit);
        }
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        int score = DudeFull.getP2().getScore();
        if (score == 0)
        {
            images = VirtualWorld.imageList.getImageList("score0");
        }
        else if (score == 1)
        {
            images = VirtualWorld.imageList.getImageList("score1");
        }
        else if (score == 2)
        {
            images = VirtualWorld.imageList.getImageList("score2");
        }
        else if (score == 3)
        {
            images = VirtualWorld.imageList.getImageList("score3");
        }
        else if (score == 4)
        {
            images = VirtualWorld.imageList.getImageList("score4");
        }
        else if (score == 5)
        {
            images = VirtualWorld.imageList.getImageList("score5");
        }
        else if (score == 6)
        {
            images = VirtualWorld.imageList.getImageList("score6");
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
