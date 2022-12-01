import processing.core.PApplet;
import processing.core.PImage;
import java.util.Random;

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
        growForest();
        for (Entity entity : this.world.getEntities()) {
            Point pos = entity.getPosition();

            if( entity instanceof Laser)
            {
                this.screen.image(entity.getCurrentImage(),
                        (float)((Laser) entity).getX() * this.tileWidth,
                        (float)((Laser) entity).getY() * this.tileHeight);
            }
            else if (this.viewport.contains(pos) &&
                    !(entity instanceof DudeNotFull) &&
                    !(entity instanceof DudeFull) &&
                    !(entity instanceof LeftScoreBoard) &&
                    !(entity instanceof RightScoreBoard))
            {
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
                if (DudeNotFull.getP1().isHasFlag())
                {
                    if (entity.getCurrentImage() == VirtualWorld.imageList.getImageList("sapling").get(0) ||
                            entity.getCurrentImage() == VirtualWorld.imageList.getImageList("sapling").get(1) ||
                            entity.getCurrentImage() == VirtualWorld.imageList.getImageList("sapling").get(2) ||
                            entity.getCurrentImage() == VirtualWorld.imageList.getImageList("sapling").get(3) )
                    {
                        this.screen.image(VirtualWorld.imageList.getImageList("leftFlag").get(0),
                                (float) (VirtualWorld.p1X +.6) * this.tileWidth,
                                (float) (((float) VirtualWorld.p1Y - .3) * this.tileHeight));
                    }
                    else
                    {
                        this.screen.image(VirtualWorld.imageList.getImageList("rightFlag").get(0),
                                (float) VirtualWorld.p1X * this.tileWidth,
                                (float) (((float) VirtualWorld.p1Y - .3) * this.tileHeight));
                    }
                }
            }
            else if( entity instanceof DudeFull)
            {
                this.screen.image(entity.getCurrentImage(),
                        (float)VirtualWorld.p2X * this.tileWidth,
                        (float)VirtualWorld.p2Y * this.tileHeight);
                if (DudeFull.getP2().isHasFlag())
                {
                    if (entity.getCurrentImage() == VirtualWorld.imageList.getImageList("sapling").get(0) ||
                            entity.getCurrentImage() == VirtualWorld.imageList.getImageList("sapling").get(1) ||
                            entity.getCurrentImage() == VirtualWorld.imageList.getImageList("sapling").get(2) ||
                            entity.getCurrentImage() == VirtualWorld.imageList.getImageList("sapling").get(3) )
                    {
                        this.screen.image(VirtualWorld.imageList.getImageList("leftFlag").get(0),
                                (float) (VirtualWorld.p2X +.6) * this.tileWidth,
                                (float) (((float) VirtualWorld.p2Y - .3) * this.tileHeight));
                    }
                    else
                    {
                        this.screen.image(VirtualWorld.imageList.getImageList("rightFlag").get(0),
                                (float) VirtualWorld.p2X * this.tileWidth,
                                (float) (((float) VirtualWorld.p2Y - .3) * this.tileHeight));
                    }
                }
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

    public void growForest()
    {
        growForestSection(8,31,4,19,400000);
        growForestSection(0,39,4,8,100000);
        growForestSection(0,39,15,19,100000);
        growForestSection(8,12,0,24,100000);
        growForestSection(27,31,0,24,100000);
    }

    private void growForestSection(int xLow, int xHigh, int yLow, int yHigh, int odds)
    {
        Random random = new Random();
        for (int i=xLow; i<xHigh; i++)
        {
            for (int j=yLow; j<yHigh; j++)
            {
                int value = random.nextInt(odds);
                if(value % (odds-1) == 0 &&
                        !(world.isOccupied(new Point(i,j))) &&
                        (world.getBackgroundCell(new Point(i,j)).getCurrentImage() == VirtualWorld.imageList.getImageList("forest").get(0)) &&
                        !(world.isOccupied(new Point(i+1,j))) &&
                        !(world.isOccupied(new Point(i+1,j+1))) &&
                        !(world.isOccupied(new Point(i+1,j-1))) &&
                        !(world.isOccupied(new Point(i-1,j))) &&
                        !(world.isOccupied(new Point(i-1,j+1))) &&
                        !(world.isOccupied(new Point(i-1,j-1))) &&
                        !(world.isOccupied(new Point(i,j+1))) &&
                        !(world.isOccupied(new Point(i,j-1))) )
                {
                    String id = "tree_" + i +"_" + j;
                    Obstacle obs = EntityFactory.createObstacle(
                            id,new Point(i,j), 500,
                            VirtualWorld.imageList.getImageList("tree"));
                    world.tryAddEntity(obs);
                }
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
