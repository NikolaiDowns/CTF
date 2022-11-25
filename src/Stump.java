import processing.core.PImage;

import java.util.List;

public class Stump extends Entity{

    public static final String STUMP_KEY = "stump";
    public Stump(
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
}