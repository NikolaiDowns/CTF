import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public abstract class Entity
{
    protected final String id;
    protected Point position;
    protected List<PImage> images;
    private int imageIndex;
    protected int resourceLimit;
    protected int resourceCount;
    protected int actionPeriod;
    protected int animationPeriod;
    protected int health;
    protected int healthLimit;


    public Entity(

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
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
        this.healthLimit = healthLimit;
    }

    // super

    public String getId() {return this.id;}

    // intermediate class
    public int getHealth() {return this.health;}

    // super
    public Point getPosition() {return this.position;}

    // super
    public void setPosition(Point pos) {this.position = pos;}

    public List<PImage> getImages() {
        return images;
    }

    public void setImages(List<PImage> images) {
        this.images = images;
    }

    // Intermediate abstract class or interface DynamicEntity interface
    public int getAnimationPeriod()
    {
            return this.animationPeriod;
    }
    // Intermediate abstract class or interface AnimatableEntity DynamicEntity Interface

    public Action createAnimationAction(int repeatCount) {
        return new AnimationAction( this, repeatCount);
    }

    // super
    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }


    // DynamicEntity interface
    protected Action createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new ActivityAction((DynamicEntity) this, world, imageStore);
    }

    public PImage getCurrentImage() {return this.images.get(this.imageIndex); }


}
