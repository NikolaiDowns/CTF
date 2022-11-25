import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Optional;

import processing.core.*;

public final class VirtualWorld extends PApplet
{
    public static double p1X;
    public static double p1Y;

    public static double p2X;
    public static double p2Y;

    public static final int TIMER_ACTION_PERIOD = 100;

//    public static final int VIEW_WIDTH = 640;
//    public static final int VIEW_HEIGHT = 480;

    public static final int VIEW_WIDTH = 1280; //1280
    public static final int VIEW_HEIGHT = 768; //960
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    public static final int WORLD_WIDTH_SCALE = 1;
    public static final int WORLD_HEIGHT_SCALE = 1;

    public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    public static final int WORLD_COLS = 40; //VIEW_COLS * WORLD_WIDTH_SCALE;
    public static final int WORLD_ROWS = 40; //VIEW_ROWS * WORLD_HEIGHT_SCALE;

    public static final String IMAGE_LIST_FILE_NAME = "imagelist";
    public static final String DEFAULT_IMAGE_NAME = "background_default";
    public static final int DEFAULT_IMAGE_COLOR = 0x808080;

    public static String LOAD_FILE_NAME = "world.sav";

    public static final String FAST_FLAG = "-fast";
    public static final String FASTER_FLAG = "-faster";
    public static final String FASTEST_FLAG = "-fastest";
    public static final double FAST_SCALE = 0.5;
    public static final double FASTER_SCALE = 0.25;
    public static final double FASTEST_SCALE = 0.10;

    public static double timeScale = 1.0;


    private ImageStore imageStore;
    public static ImageStore imageList;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    private long nextTime;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT,
                                   DEFAULT_IMAGE_COLOR));
        imageList = this.imageStore;
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                                    createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH,
                                  TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= nextTime) {
            this.scheduler.updateOnTime(time);
            nextTime = time + TIMER_ACTION_PERIOD;
        }
        view.drawViewport();
        DudeNotFull p1 = DudeNotFull.getP1();
        DudeFull p2 = DudeFull.getP2();

        switch (key) {
            case 'w':
                p1Y -= .5;
                break;
            case 's':
                p1Y += .5;
                break;
            case 'a':
                p1X -= .5;
                p1.setImages(imageStore.getImageList(Dude.DUDE_KEY));
                break;
            case 'd':
                p1X += .5;
                p1.setImages(imageStore.getImageList(Sapling.SAPLING_KEY));
                break;

            case 'i':
                p2Y -= .5;
                break;
            case 'k':
                p2Y += .5;
                break;
            case 'j':
                p2X -= .5;
                p2.setImages(imageStore.getImageList(Dude.DUDE_KEY));
                break;
            case 'l':
                p2X += .5;
                p2.setImages(imageStore.getImageList(Sapling.SAPLING_KEY));
                break;
        }

        p1.setPosition(new Point((int)p1X, (int)p1Y));
        p2.setPosition(new Point((int)p2X, (int)p2Y));
        key = ' ';
    }

    // Just for debugging and for P5
    // Be sure to refactor this method as appropriate
    public void mousePressed() {
        Point pressed = mouseToPoint(mouseX, mouseY);
        System.out.println("CLICK! " + pressed.getX() + ", " + pressed.getY());

        Optional<Entity> entityOptional = world.getOccupant(pressed);
        if (entityOptional.isPresent())
        {
            Entity entity = entityOptional.get();
            System.out.println(entity.getId() + ": " + entity.getClass() + " : " + entity.getHealth());
        }

    }

    private Point mouseToPoint(int x, int y)
    {
        return view.getViewport().viewportToWorld(mouseX/TILE_WIDTH, mouseY/TILE_HEIGHT);
    }

    public void keyPressed() {
        if (key == CODED) {
            switch (keyCode) {
                case UP:
                    p2Y -= 1;
                    break;
                case DOWN:
                    p2Y += 1;
                    break;
                case LEFT:
                    p2X -= 1;
                    break;
                case RIGHT:
                    p2X += 1;
                    break;
            }
            DudeFull p2 = DudeFull.getP2();
            p2.setPosition(new Point((int)p1X, (int)p1Y));
        }
    }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
                              imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    static void loadImages(
            String filename, ImageStore imageStore, PApplet screen)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            Functions.loadImages(in, imageStore, screen);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void loadWorld(
            WorldModel world, String filename, ImageStore imageStore)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            world.load(in, imageStore);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof ScheduledEntity)
            ((ScheduledEntity)entity).scheduleActions(scheduler, world, imageStore);
        }
    }

    public static void parseCommandLine(String[] args) {
        if (args.length > 1)
        {
            if (args[0].equals("file"))
            {

            }
        }
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
