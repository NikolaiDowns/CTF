import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

import processing.core.*;

public final class VirtualWorld extends PApplet
{
    //forest (8,4) to (31,19)
    public static double p1X;

    public int winner;
    public static double p1Y;

    public static double p2X;
    public static double p2Y;

    public static FairyFactory ff;

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
        for (int i=0;i<50000;i++) {
            this.view.growForest();
        }
        scheduleActions(world, scheduler, imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }
    int gameScreen = 0;

    void initScreen() {
        background(35,45,50);
        textAlign(CENTER);
        textSize(38);
        text("Click on Screen then Press one of the following keys:", 600, 200);
        text(" 1 = Easy, 2 = Hard", 600, 400);

        switch (key)
        {
            case '1':
                gameScreen = 1;
                // Make zombieFactory easy
                ff = new EasyFairyFactory();
                Random random = new Random();
                int fairyX = random.nextInt(12,28);
                int fairyY = random.nextInt(8,15);
                for(int i=0;i<3;i++)
                {
                    while(world.isOccupied(new Point (fairyX,fairyY)))
                    {
                        fairyX = random.nextInt(12,28);
                        fairyY = random.nextInt(8,15);
                    }
                    Fairy entityEasy = ff.createFairy("fairy", new Point( fairyX, fairyY),
                            1000,
                            51,
                            imageStore.getImageList("RightZombie")); //Fairy.FAIRY_KEY
                    world.tryAddEntity(entityEasy);
                    ((ScheduledEntity)entityEasy).scheduleActions(scheduler, world, imageStore);
                }


                break;
            case '2':
                gameScreen = 1;
                // Make zombieFactory hard
                ff = new HardFairyFactory();
                Random random1 = new Random();
                int fairyX1 = random1.nextInt(12,28);
                int fairyY1 = random1.nextInt(8, 15);
                for(int i=0;i<5;i++)
                {
                    while(world.isOccupied(new Point (fairyX1,fairyY1)))
                    {
                        fairyX1 = random1.nextInt(12,28);
                        fairyY1 = random1.nextInt(8,15);
                    }
                    Fairy entityHard = ff.createFairy("fairy", new Point( fairyX1, fairyY1),
                            800,
                            51,
                            imageStore.getImageList("RightZombie")); //Fairy.FAIRY_KEY
                    world.tryAddEntity(entityHard);
                    ((ScheduledEntity)entityHard).scheduleActions(scheduler, world, imageStore);
                }

                break;
        }
    }

    void endScreen() {
        background(35, 45, 50);
        textAlign(CENTER);
        textSize(38);
        text("Game Over!", 500, 200);
        text(" player " + winner + " wins!", 500, 400);
    }





    public void draw() {
        if (gameScreen == 0)
        {
            initScreen();
        }
        else if (gameScreen == 1) {
            long time = System.currentTimeMillis();
            if (time >= nextTime) {
                this.scheduler.updateOnTime(time);
                nextTime = time + TIMER_ACTION_PERIOD;
            }
            view.drawViewport();
            DudeNotFull p1 = DudeNotFull.getP1();
            DudeFull p2 = DudeFull.getP2();

            switch (key) {
//            case 'w':
//                Point potential = new Point(p1.getPosition().getX(),p1.getPosition().getY() - 1);
//                if (this.world.isOccupied(potential)
//                && !(this.world.getOccupancyCell(potential) instanceof LeftScoreBoard) ||
//                        !(this.world.getOccupancyCell(potential) instanceof LeftSafeZone)||
//                        !(this.world.getOccupancyCell(potential) instanceof RightSafeZone) ||
//                        !(this.world.getOccupancyCell(potential) instanceof RightScoreBoard));
//                p1Y -= .5;
//                break;
//            case 's':
//                potential = new Point(p1.getPosition().getX(),p1.getPosition().getY() + 1);
//                if (this.world.isOccupied(potential)
//                        && !(this.world.getOccupancyCell(potential) instanceof LeftScoreBoard) ||
//                        !(this.world.getOccupancyCell(potential) instanceof LeftSafeZone)||
//                        !(this.world.getOccupancyCell(potential) instanceof RightSafeZone) ||
//                        !(this.world.getOccupancyCell(potential) instanceof RightScoreBoard));
//                p1Y += .5;
//                break;
//            case 'a':
//                potential = new Point(p1.getPosition().getX() - 1,p1.getPosition().getY());
//                if (this.world.isOccupied(potential)
//                        && !(this.world.getOccupancyCell(potential) instanceof LeftScoreBoard) ||
//                        !(this.world.getOccupancyCell(potential) instanceof LeftSafeZone)||
//                        !(this.world.getOccupancyCell(potential) instanceof RightSafeZone) ||
//                        !(this.world.getOccupancyCell(potential) instanceof RightScoreBoard));
//                p1X -= .5;
//                break;
//            case 'd':
//                potential = new Point(p1.getPosition().getX() + 1,p1.getPosition().getY());
//                if (this.world.isOccupied(potential)
//                        && !(this.world.getOccupancyCell(potential) instanceof LeftScoreBoard) ||
//                        !(this.world.getOccupancyCell(potential) instanceof LeftSafeZone)||
//                        !(this.world.getOccupancyCell(potential) instanceof RightSafeZone) ||
//                        !(this.world.getOccupancyCell(potential) instanceof RightScoreBoard));
//                p1X += .5;
//                break;

                case 'w':
                    try {
                        Point potential = new Point(p1.getPosition().getX(), p1.getPosition().getY() - 1);
                        if ((potential.getX() == 1 && potential.getY() == 11) ||
                                (potential.getX() == 2 && potential.getY() == 12) ||
                                (potential.getX() == 37 && potential.getY() == 12) ||
                                (potential.getX() == 36 && potential.getY() == 11) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof DudeNotFull) ||
                                !(this.world.isOccupied(potential))
                        )
                            p1Y -= .5;
                        p1.setImages(imageStore.getImageList(Dude.DUDE_KEY));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        p1Y += .5;
                        System.out.println("out of bounds");
                    }
                    break;
                case 's':
                    try {
                        int currY = p1.getPosition().getY() + 1;
                        if (currY > 23) {
                            currY = 100;
                        }
                        Point potential = new Point(p1.getPosition().getX(), currY);
                        if ((potential.getX() == 1 && potential.getY() == 11) ||
                                (potential.getX() == 2 && potential.getY() == 12) ||
                                (potential.getX() == 37 && potential.getY() == 12) ||
                                (potential.getX() == 36 && potential.getY() == 11) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof DudeNotFull) ||
                                !(this.world.isOccupied(potential))
                        )
                            p1Y += .5;
                        p1.setImages(imageStore.getImageList(Dude.DUDE_KEY));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        p1Y -= .5;
                        System.out.println("out of bounds");
                    }
                    break;
                case 'a':
                    try {
                        Point potential = new Point(p1.getPosition().getX() - 1, p1.getPosition().getY());
                        if ((potential.getX() == 1 && potential.getY() == 11) ||
                                (potential.getX() == 2 && potential.getY() == 12) ||
                                (potential.getX() == 37 && potential.getY() == 12) ||
                                (potential.getX() == 36 && potential.getY() == 11) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof DudeNotFull) ||
                                !(this.world.isOccupied(potential))
                        )
                            p1X -= .5;
                        p1.setImages(imageStore.getImageList(Dude.DUDE_KEY));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("out of bounds");
                        p1X += .5;
                    }
                    break;
                case 'd':
                    try {
                        Point potential = new Point(p1.getPosition().getX() + 1, p1.getPosition().getY());
                        if ((potential.getX() == 1 && potential.getY() == 11) ||
                                (potential.getX() == 2 && potential.getY() == 12) ||
                                (potential.getX() == 37 && potential.getY() == 12) ||
                                (potential.getX() == 36 && potential.getY() == 11) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof DudeNotFull) ||
                                !(this.world.isOccupied(potential))
                        )
                            p1X += .5;
                        p1.setImages(imageStore.getImageList(Sapling.SAPLING_KEY));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("out of bounds");
                        p1X -= .5;
                    }
                    break;
                case 'i':
                    try {
                        Point potential = new Point(p2.getPosition().getX(), p2.getPosition().getY() - 1);
                        if ((potential.getX() == 1 && potential.getY() == 11) ||
                                (potential.getX() == 2 && potential.getY() == 12) ||
                                (potential.getX() == 37 && potential.getY() == 12) ||
                                (potential.getX() == 36 && potential.getY() == 11) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof DudeNotFull) ||
                                !(this.world.isOccupied(potential))
                        )
                            p2Y -= .5;
                        p2.setImages(imageStore.getImageList(Dude.DUDE_KEY));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        p2Y += .5;
                        System.out.println("out of bounds");
                    }
                    break;
                case 'k':
                    try {
                        int currY = p2.getPosition().getY() + 1;
                        if (currY > 23) {
                            currY = 100;
                        }
                        Point potential = new Point(p2.getPosition().getX(), currY);
                        if ((potential.getX() == 1 && potential.getY() == 11) ||
                                (potential.getX() == 2 && potential.getY() == 12) ||
                                (potential.getX() == 37 && potential.getY() == 12) ||
                                (potential.getX() == 36 && potential.getY() == 11) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof DudeNotFull) ||
                                !(this.world.isOccupied(potential))
                        )
                            p2Y += .5;
                        p2.setImages(imageStore.getImageList(Dude.DUDE_KEY));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        p2Y -= .5;
                        System.out.println("out of bounds");
                    }
                    break;
                case 'j':
                    try {
                        Point potential = new Point(p2.getPosition().getX() - 1, p2.getPosition().getY());
                        if ((potential.getX() == 1 && potential.getY() == 11) ||
                                (potential.getX() == 2 && potential.getY() == 12) ||
                                (potential.getX() == 37 && potential.getY() == 12) ||
                                (potential.getX() == 36 && potential.getY() == 11) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof DudeNotFull) ||
                                !(this.world.isOccupied(potential))
                        )
                            p2X -= .5;
                        p2.setImages(imageStore.getImageList(Dude.DUDE_KEY));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("out of bounds");
                        p2X += .5;
                    }
                    break;
                case 'l':
                    try {
                        Point potential = new Point(p2.getPosition().getX() + 1, p2.getPosition().getY());
                        if ((potential.getX() == 1 && potential.getY() == 11) ||
                                (potential.getX() == 2 && potential.getY() == 12) ||
                                (potential.getX() == 37 && potential.getY() == 12) ||
                                (potential.getX() == 36 && potential.getY() == 11) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof DudeNotFull) ||
                                !(this.world.isOccupied(potential))
                        )
                            p2X += .5;
                        p2.setImages(imageStore.getImageList(Sapling.SAPLING_KEY));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("out of bounds");
                        p2X -= .5;
                    }
                    break;
            }
            p1.setPosition(new Point((int) p1X, (int) p1Y));
            p2.setPosition(new Point((int) p2X, (int) p2Y));
            if(p1.getScore() == 7)
            {
                winner = 1;
                gameScreen = 2;
            }
            if(p2.getScore() == 7)
            {
                winner = 2;
                gameScreen = 2;
            }
            key = ' ';
        }
        else if(gameScreen == 2)
        {
            endScreen();
        }
    }

    // Just for debugging and for P5
    // Be sure to refactor this method as appropriate
    public void mousePressed() {
        Point pressed = mouseToPoint(mouseX, mouseY);
        System.out.println("CLICK! " + pressed.getX() + ", " + pressed.getY());
        List<Point> neighbors = VirtualWorld.getNeighbors(pressed);
        neighbors.add(pressed);
        for (Point p : neighbors)
        {
            if (this.withinBounds(p))
            {
                // set background to be forest
                this.world.setBackground(p, new Background("forest", imageStore.getImageList("forest")));
            }
        }
        Optional<Entity> entityOptional = world.getOccupant(pressed);
        if (entityOptional.isPresent())
        {
            Entity entity = entityOptional.get();
            System.out.println(entity.getId() + ": " + entity.getClass() + " : " + entity.getHealth());
        }
        else {
            Slime slime = EntityFactory.createSlime("fairy", pressed,
                    800,
                    51,
                    imageStore.getImageList("leftSlime"));
            world.tryAddEntity(slime);
            ((ScheduledEntity)slime).scheduleActions(scheduler, world, imageStore);
        }
        Optional<Entity> nearestFairy =
                world.findNearest(pressed, new ArrayList<>(Arrays.asList(Fairy.class)));

        if(nearestFairy.isPresent())
        {
            nearestFairy.get().setActionPeriod(1600);
            nearestFairy.get().setAnimationPeriod(400);
            ((Fairy)nearestFairy.get()).setStrategy(new SingleStepPathingStrategy());
            ((Fairy)nearestFairy.get()).setDumb(true);
        }

    }

    private Point mouseToPoint(int x, int y)
    {
        return view.getViewport().viewportToWorld(mouseX/TILE_WIDTH, mouseY/TILE_HEIGHT);
    }

    public void keyPressed() {
        if (key == CODED) {
            DudeFull p2 = DudeFull.getP2();
            switch (keyCode) {
                case UP:
                    try {
                        Point potential = new Point(p2.getPosition().getX(),p2.getPosition().getY() - 1);
                        if ((potential.getX() == 1 && potential.getY() == 11) ||
                                (potential.getX() == 2 && potential.getY() == 12) ||
                                (potential.getX() == 37 && potential.getY() == 12) ||
                                (potential.getX() == 36 && potential.getY() == 11) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftSafeZone)||
                                (this.world.getOccupancyCell(potential) instanceof RightSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof DudeNotFull) ||
                                !(this.world.isOccupied(potential))
                        )
                            p2Y -= 1;
                        p2.setImages(imageStore.getImageList(Dude.DUDE_KEY));
                    }
                    catch(ArrayIndexOutOfBoundsException e){
                        p2Y += 1;
                        System.out.println("out of bounds");
                    }
                    break;
                case DOWN:
                    try {
                        int currY = p2.getPosition().getY() + 1;
                        if (currY > 23) {
                            currY = 100;
                        }
                        Point potential = new Point(p2.getPosition().getX(), currY);
                        if ((potential.getX() == 1 && potential.getY() == 11) ||
                                (potential.getX() == 2 && potential.getY() == 12) ||
                                (potential.getX() == 37 && potential.getY() == 12) ||
                                (potential.getX() == 36 && potential.getY() == 11) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftSafeZone)||
                                (this.world.getOccupancyCell(potential) instanceof RightSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof DudeNotFull) ||
                                !(this.world.isOccupied(potential))
                        )
                            p2Y += 1;
                        p2.setImages(imageStore.getImageList(Dude.DUDE_KEY));
                    }
                    catch(ArrayIndexOutOfBoundsException e){
                        p2Y -= 1;
                        System.out.println("out of bounds");
                    }
                    break;
                case LEFT:
                    try{
                        Point potential = new Point(p2.getPosition().getX() - 1,p2.getPosition().getY());
                        if ((potential.getX() == 1 && potential.getY() == 11) ||
                                (potential.getX() == 2 && potential.getY() == 12) ||
                                (potential.getX() == 37 && potential.getY() == 12) ||
                                (potential.getX() == 36 && potential.getY() == 11) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftSafeZone)||
                                (this.world.getOccupancyCell(potential) instanceof RightSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof DudeNotFull) ||
                                !(this.world.isOccupied(potential))
                        )
                            p2X -= 1;
                        p2.setImages(imageStore.getImageList(Dude.DUDE_KEY));
                    }
                    catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("out of bounds");
                        p2X += 1;
                    }
                    break;
                case RIGHT:
                    try{
                        Point potential = new Point(p2.getPosition().getX() + 1, p2.getPosition().getY());
                        if ((potential.getX() == 1 && potential.getY() == 11) ||
                                (potential.getX() == 2 && potential.getY() == 12) ||
                                (potential.getX() == 37 && potential.getY() == 12) ||
                                (potential.getX() == 36 && potential.getY() == 11) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof LeftSafeZone)||
                                (this.world.getOccupancyCell(potential) instanceof RightSafeZone) ||
                                (this.world.getOccupancyCell(potential) instanceof RightScoreBoard) ||
                                (this.world.getOccupancyCell(potential) instanceof DudeNotFull) ||
                                !(this.world.isOccupied(potential))
                        )
                            p2X += 1;
                        p2.setImages(imageStore.getImageList(Sapling.SAPLING_KEY));
                    }
                    catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("out of bounds");
                        p2X -= 1;
                    }
                    break;
            }

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
    private static List<Point> getNeighbors(Point p1)
    {
        List<Point> neighbors = new ArrayList<>();
        neighbors.add(new Point(p1.getX() + 1, p1.getY()));
        neighbors.add(new Point(p1.getX() - 1, p1.getY()));
        neighbors.add(new Point(p1.getX() + 1, p1.getY() + 1));
        neighbors.add(new Point(p1.getX() + 1, p1.getY() - 1));
        neighbors.add(new Point(p1.getX() - 1, p1.getY() + 1));
        neighbors.add(new Point(p1.getX() - 1, p1.getY() - 1));
        neighbors.add(new Point(p1.getX(), p1.getY() + 1));
        neighbors.add(new Point(p1.getX(), p1.getY() - 1));

        return neighbors;
    }

    private boolean withinBounds(Point pos) {
        return pos.getY() >= 0 && pos.getY() < this.world.getNumRows() && pos.getX() >= 0
                && pos.getX() < this.world.getNumCols();
    }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
