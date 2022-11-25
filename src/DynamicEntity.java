public interface DynamicEntity {

    void executeActivity(WorldModel world,
                         ImageStore imageStore,
                         EventScheduler scheduler);
}
