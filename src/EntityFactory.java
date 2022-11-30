import processing.core.PImage;

import java.util.List;

public final class EntityFactory {

//    public static Entity createEntity(String entityType)
//    {
//        if(entityType != null)
//        {
//            switch(entityType)
//            {
//                case "DudeFull":
//
//            }
//        }
//    }

    // Enemy Factory creates zombie or cannon

    public static House createHouse(
            String id, Point position, int actionPeriod, List<PImage> images)
    {
        return new House(id, position, images, 0, 0, actionPeriod,
                0, 0, 0);
    }

    public static Obstacle createObstacle(
            String id, Point position, int animationPeriod, List<PImage> images)
    {
        return new Obstacle(id, position, images, 0, 0, 0,
                animationPeriod, 0, 0);
    }

    public static Tree createTree(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int health,
            List<PImage> images)
    {
        return new Tree(id, position, images, 0, 0,
                actionPeriod, animationPeriod, health, 0);
    }

    public static Stump createStump(
            String id,
            Point position,
            List<PImage> images)
    {
        return new Stump(id, position, images, 0, 0,
                0, 0, 0, 0);
    }

    public static LeftScoreBoard createLeftScore(
            String id,
            Point position,
            List<PImage> images)
    {
        LeftScoreBoard.createLeftScoreBoard(id, position, images, 0, 0,
                500, 0, 0, 0);
        return LeftScoreBoard.leftScore;
    }

    public static RightScoreBoard createRightScore(
            String id,
            Point position,
            List<PImage> images)
    {
        RightScoreBoard.createRightScoreBoard(id, position, images, 0, 0,
                500, 0, 0, 0);
        return RightScoreBoard.rightScore;
    }

    public static LeftSafeZone createLeftZone(
            String id,
            Point position,
            List<PImage> images)
    {
        LeftSafeZone.createLeftSafeZone(id, position, images, 0, 0,
                200, 0, 0, 0);
        return LeftSafeZone.leftZone;
    }

    public static RightSafeZone createRightZone(
            String id,
            Point position,
            List<PImage> images)
    {
        RightSafeZone.createRightSafeZone(id, position, images, 0, 0,
                200, 0, 0, 0);
        return RightSafeZone.rightZone;
    }

//     health starts at 0 and builds up until ready to convert to Tree
    public static Sapling createSapling(
            String id,
            Point position,
            List<PImage> images)
    {
        return new Sapling(id, position, images, 0, 0,
                Sapling.SAPLING_ACTION_ANIMATION_PERIOD, Sapling.SAPLING_ACTION_ANIMATION_PERIOD, 0, Sapling.SAPLING_HEALTH_LIMIT);
    }


//    public static Fairy createFairy(
//            String id,
//            Point position,
//            int actionPeriod,
//            int animationPeriod,
//            List<PImage> images)
//    {
//        return new Fairy(id, position, images, 0, 0,
//                actionPeriod, animationPeriod, 0, 0);
//    }

    public static Slime createSlime(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Slime(id, position, images, 0, 0,
                actionPeriod, animationPeriod, 0, 0,new SingleStepPathingStrategy());
    }

    public static Laser createLaser(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Laser(id, position, images, 0, 0,
                actionPeriod, animationPeriod, 0, 0);
    }

//     need resource count, though it always starts at 0
    public static DudeNotFull createDudeNotFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images)
    {
        DudeNotFull player = new DudeNotFull(id, position, images, resourceLimit, 0,
                actionPeriod, animationPeriod, 0, 0);
        DudeNotFull.setP1(player);
        VirtualWorld.p1X = ((double)position.getX());
        VirtualWorld.p1Y = ((double)position.getY());
        return player;
    }

 //    don't technically need resource count ... full
    public static DudeFull createDudeFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images) {
        DudeFull player = new DudeFull(id, position, images, resourceLimit, 0,
                actionPeriod, animationPeriod, 0, 0);
        DudeFull.setP2(player);
        VirtualWorld.p2X = ((double)position.getX());
        VirtualWorld.p2Y = ((double)position.getY());
        return player;
    }
}
