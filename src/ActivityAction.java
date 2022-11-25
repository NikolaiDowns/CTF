public class ActivityAction extends Action{

    private WorldModel world;
    private ImageStore imageStore;
    public ActivityAction(DynamicEntity entity, WorldModel world, ImageStore imageStore)
    {
        super((Entity)entity);
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler)
    {
        ((DynamicEntity)this.entity).executeActivity(this.world, this.imageStore, scheduler);
    }
}
