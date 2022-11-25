/**
 * An action that can be taken by an entity
 */
public abstract class Action
{

    protected final Entity entity;

    protected Action(Entity entity)
    {
        this.entity = entity;
    }

    public abstract void executeAction(EventScheduler scheduler);
}
