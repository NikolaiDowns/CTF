import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

public final class WorldView
{
    private PApplet screen;
    private WorldModel world;
    private int tileWidth;
    private int tileHeight;
    private Viewport viewport;

    public WorldView(
            int numRows,
            int numCols,
            PApplet screen,
            WorldModel world,
            int tileWidth,
            int tileHeight)
    {
        this.screen = screen;
        this.world = world;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.viewport = new Viewport(numRows, numCols);
    }

    public Viewport getViewport() {return this.viewport;}

    private void drawBackground() {
        for (int row = 0; row < this.viewport.getNumCols(); row++) {
            for (int col = 0; col < this.viewport.getNumCols(); col++) {
                Point worldPoint = this.viewport.viewportToWorld(col, row);
                Optional<PImage> image =
                        this.world.getBackgroundImage(worldPoint);
                if (image.isPresent()) {
                    this.screen.image(image.get(), col * this.tileWidth,
                            row * this.tileHeight);
                }
            }
        }
    }

    private void drawEntities() {
        for (Entity entity : this.world.getEntities()) {
            Point pos = entity.getPosition();

            if( entity instanceof DudeNotFull)
            {
//                this.screen.image(entity.getCurrentImage(),
//                        (float)VirtualWorld.p1X * this.tileWidth,
//                        (float)VirtualWorld.p1Y * this.tileHeight);
            }
            else if( entity instanceof DudeFull)
            {
//                this.screen.image(entity.getCurrentImage(),
//                        (float)VirtualWorld.p2X * this.tileWidth,
//                        (float)VirtualWorld.p2Y * this.tileHeight);
            }
            else if( entity instanceof LeftScoreBoard)
            {
//                this.screen.image(entity.getCurrentImage(),
//                        (float)VirtualWorld.p1X * this.tileWidth,
//                        (float)VirtualWorld.p1Y * this.tileHeight);
            }
            else if( entity instanceof RightScoreBoard)
            {
//                this.screen.image(entity.getCurrentImage(),
//                        (float)VirtualWorld.p2X * this.tileWidth,
//                        (float)VirtualWorld.p2Y * this.tileHeight);
            }
            else if( entity instanceof Laser)
            {
                this.screen.image(entity.getCurrentImage(),
                        (float)((Laser) entity).getX() * this.tileWidth,
                        (float)((Laser) entity).getY() * this.tileHeight);
            }
            else if (this.viewport.contains(pos)) {
                Point viewPoint = this.viewport.worldToViewport(pos.getX(), pos.getY());
                this.screen.image(entity.getCurrentImage(),
                        viewPoint.getX() * this.tileWidth,
                        viewPoint.getY() * this.tileHeight);
            }
        }

        for (Entity entity : this.world.getEntities()) {
            Point pos = entity.getPosition();

            if( entity instanceof DudeNotFull)
            {
                this.screen.image(entity.getCurrentImage(),
                        (float)VirtualWorld.p1X * this.tileWidth,
                        (float)VirtualWorld.p1Y * this.tileHeight);
            }
            else if( entity instanceof DudeFull)
            {
                this.screen.image(entity.getCurrentImage(),
                        (float)VirtualWorld.p2X * this.tileWidth,
                        (float)VirtualWorld.p2Y * this.tileHeight);
            }

        }
        for (Entity entity : this.world.getEntities()) {
            Point pos = entity.getPosition();

            if( entity instanceof LeftScoreBoard)
            {
                Point viewPoint = this.viewport.worldToViewport(pos.getX(), pos.getY());
                this.screen.image(entity.getCurrentImage(),
                        viewPoint.getX() * this.tileWidth,
                        viewPoint.getY() * this.tileHeight);
            }
            else if( entity instanceof RightScoreBoard)
            {
                Point viewPoint = this.viewport.worldToViewport(pos.getX(), pos.getY());
                this.screen.image(entity.getCurrentImage(),
                        viewPoint.getX() * this.tileWidth,
                        viewPoint.getY() * this.tileHeight);
            }

        }
    }

    private int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }

    public void shiftView(int colDelta, int rowDelta) {
        int newCol = this.clamp(this.viewport.getCol() + colDelta, 0,
                this.world.getNumCols() - this.viewport.getNumCols());
        int newRow = this.clamp(this.viewport.getRow() + rowDelta, 0,
                this.world.getNumRows() - this.viewport.getNumRows());

        this.viewport.shift(newCol, newRow);
    }

    public void drawViewport() {
        this.drawBackground();
        this.drawEntities();
    }
}
