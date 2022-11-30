import processing.core.PImage;

import java.util.List;

public class HardFairyFactory  extends FairyFactory{

    @Override
    public Fairy createFairy(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Fairy(id, position, images, 0, 0,
                actionPeriod, animationPeriod, 0, 0, new DijkstraPathingStrategy());
    }
}
