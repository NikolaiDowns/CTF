import processing.core.PImage;

import java.util.List;

public abstract class FairyFactory {

    public Fairy createFairy(String id,
                                      Point position,
                                      int actionPeriod,
                                      int animationPeriod,
                                      List<PImage> images)
    {
        return null;
    }
}
