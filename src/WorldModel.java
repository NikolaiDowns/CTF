import processing.core.PImage;

import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel
{
    private int numRows;
    private int numCols;
    private Background background[][];
    private Entity occupancy[][];
    private Set<Entity> entities;

    public static final int PROPERTY_KEY = 0;



    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }

    public int getNumRows() {return this.numRows;}

    public int getNumCols() {return this.numCols;}

    public Set<Entity> getEntities() {return this.entities;}
    public void addEntity(Entity entity) {
        if (withinBounds(entity.getPosition())) {
            setOccupancyCell(entity.getPosition(), entity);
            this.entities.add(entity);
        }
    }

    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (this.withinBounds(pos) && !pos.equals(oldPos)) {
            this.setOccupancyCell(oldPos, null);
            removeEntityAt(pos);
            this.setOccupancyCell(pos, entity);
            entity.setPosition(pos);
        }
    }

    public void removeEntity(Entity entity) {
        this.removeEntityAt(entity.getPosition());
    }

    private void removeEntityAt(Point pos) {
        if (this.withinBounds(pos) && this.getOccupancyCell(pos) != null) {
            Entity entity = this.getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            this.entities.remove(entity);
            this.setOccupancyCell(pos, null);
        }
    }

    public Optional<PImage> getBackgroundImage(Point pos)
    {
        if (this.withinBounds(pos)) {
            return Optional.of(this.getBackgroundCell(pos).getCurrentImage());
        }
        else {
            return Optional.empty();
        }
    }

    private void setBackground(Point pos, Background background)
    {
        if (withinBounds(pos)) {
            this.setBackgroundCell(pos, background);
        }
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (this.isOccupied(pos)) {
            return Optional.of(this.getOccupancyCell(pos));
        }
        else {
            return Optional.empty();
        }
    }

    public Entity getOccupancyCell(Point pos) {
        return this.occupancy[pos.getY()][pos.getX()];
    }

    private void setOccupancyCell(Point pos, Entity entity)
    {
        this.occupancy[pos.getY()][pos.getX()] = entity;
    }

    private Background getBackgroundCell(Point pos) {
        return this.background[pos.getY()][pos.getX()];
    }

    private void setBackgroundCell(Point pos, Background background)
    {
        this.background[pos.getY()][pos.getX()] = background;
    }

    private boolean withinBounds(Point pos) {
        return pos.getY() >= 0 && pos.getY() < this.numRows && pos.getX() >= 0
                && pos.getX() < this.numCols;
    }

    public boolean isOccupied(Point pos) {
        return withinBounds(pos) && getOccupancyCell(pos) != null;
    }
    private boolean parseBackground(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Background.BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Background.BGND_COL]),
                    Integer.parseInt(properties[Background.BGND_ROW]));
            String id = properties[Background.BGND_ID];
            this.setBackground(pt, new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == Background.BGND_NUM_PROPERTIES;
    }



    private boolean parseSapling(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Sapling.SAPLING_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Sapling.SAPLING_COL]),
                    Integer.parseInt(properties[Sapling.SAPLING_ROW]));
            String id = properties[Sapling.SAPLING_ID];
//            int health = Integer.parseInt(properties[Sapling.SAPLING_HEALTH]);
            Sapling entity = EntityFactory.createSapling(id, pt, imageStore.getImageList(Sapling.SAPLING_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == Sapling.SAPLING_NUM_PROPERTIES;
    }

    private boolean parseLeftScoreBoard(
            String[] properties, ImageStore imageStore)
    {
        System.out.println("there");
        if (properties.length == LeftScoreBoard.LeftScoreBoard_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[LeftScoreBoard.LeftScoreBoard_COL]),
                    Integer.parseInt(properties[LeftScoreBoard.LeftScoreBoard_ROW]));
            String id = properties[LeftScoreBoard.LeftScoreBoard_ID];
            //int health = Integer.parseInt(properties[Sapling.SAPLING_HEALTH]);
            LeftScoreBoard entity = EntityFactory.createLeftScore(id, pt, imageStore.getImageList("score0"));
            this.tryAddEntity(entity);
        }

        return properties.length == LeftScoreBoard.LeftScoreBoard_NUM_PROPERTIES;
    }

    private boolean parseRightScoreBoard(
            String[] properties, ImageStore imageStore)
    {
        System.out.println("there");
        if (properties.length == RightScoreBoard.RightScoreBoard_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[RightScoreBoard.RightScoreBoard_COL]),
                    Integer.parseInt(properties[RightScoreBoard.RightScoreBoard_ROW]));
            String id = properties[RightScoreBoard.RightScoreBoard_ID];
            //int health = Integer.parseInt(properties[Sapling.SAPLING_HEALTH]);
            RightScoreBoard entity = EntityFactory.createRightScore(id, pt, imageStore.getImageList("score0"));
            this.tryAddEntity(entity);
        }

        return properties.length == RightScoreBoard.RightScoreBoard_NUM_PROPERTIES;
    }

    private boolean parseDude(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == DudeNotFull.NOT_FULL_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Dude.DUDE_COL]),
                    Integer.parseInt(properties[Dude.DUDE_ROW]));
            DudeNotFull entity = EntityFactory.createDudeNotFull(properties[Dude.DUDE_ID],
                    pt,
                    Integer.parseInt(properties[DudeNotFull.NOT_FULL_ACTION_PERIOD]),
                    Integer.parseInt(properties[DudeNotFull.NOT_FULL_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[DudeNotFull.NOT_FULL_LIMIT]),
                    imageStore.getImageList(Dude.DUDE_KEY));
            this.tryAddEntity(entity);
        }
        else if (properties.length == DudeFull.FULL_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[Dude.DUDE_COL]),
                    Integer.parseInt(properties[Dude.DUDE_ROW]));
            DudeFull entity = EntityFactory.createDudeFull(properties[Dude.DUDE_ID],
                    pt,
                    Integer.parseInt(properties[DudeFull.FULL_ACTION_PERIOD]),
                    Integer.parseInt(properties[DudeFull.FULL_ANIMATION_PERIOD]),
                    0,
                    imageStore.getImageList(Dude.DUDE_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == DudeNotFull.NOT_FULL_PROPERTIES || properties.length == DudeFull.FULL_PROPERTIES;
    }

    private boolean parseFairy(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Fairy.FAIRY_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Fairy.FAIRY_COL]),
                    Integer.parseInt(properties[Fairy.FAIRY_ROW]));
            Fairy entity = EntityFactory.createFairy(properties[Fairy.FAIRY_ID],
                    pt,
                    Integer.parseInt(properties[Fairy.FAIRY_ACTION_PERIOD]),
                    Integer.parseInt(properties[Fairy.FAIRY_ANIMATION_PERIOD]),
                    imageStore.getImageList("RightZombie")); //Fairy.FAIRY_KEY
            this.tryAddEntity(entity);
        }

        return properties.length == Fairy.FAIRY_NUM_PROPERTIES;
    }

    private boolean parseLaser(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Laser.LASER_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Laser.LASER_COL]),
                    Integer.parseInt(properties[Laser.LASER_ROW]));
            Laser entity = EntityFactory.createLaser(properties[Laser.LASER_ID],
                    pt,
                    Integer.parseInt(properties[Laser.LASER_ACTION_PERIOD]),
                    Integer.parseInt(properties[Laser.LASER_ANIMATION_PERIOD]),
                    imageStore.getImageList(Fairy.FAIRY_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == Laser.LASER_NUM_PROPERTIES;
    }

    private boolean parseTree(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Tree.TREE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Tree.TREE_COL]),
                    Integer.parseInt(properties[Tree.TREE_ROW]));
            Tree entity = EntityFactory.createTree(properties[Tree.TREE_ID],
                    pt,
                    Integer.parseInt(properties[Tree.TREE_ACTION_PERIOD]),
                    Integer.parseInt(properties[Tree.TREE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[Tree.TREE_HEALTH]),
                    imageStore.getImageList(Tree.TREE_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == Tree.TREE_NUM_PROPERTIES;
    }

    private boolean parseObstacle(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Obstacle.OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Obstacle.OBSTACLE_COL]),
                    Integer.parseInt(properties[Obstacle.OBSTACLE_ROW]));
            Obstacle entity = EntityFactory.createObstacle(properties[Obstacle.OBSTACLE_ID], pt,
                    Integer.parseInt(properties[Obstacle.OBSTACLE_ANIMATION_PERIOD]),
                    imageStore.getImageList(Obstacle.OBSTACLE_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == Obstacle.OBSTACLE_NUM_PROPERTIES;
    }

    private boolean parseHouse(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == House.HOUSE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[House.HOUSE_COL]),
                    Integer.parseInt(properties[House.HOUSE_ROW]));
            House entity = EntityFactory.createHouse(properties[House.HOUSE_ID], pt,
                    Integer.parseInt(properties[House.HOUSE_ACTION_PERIOD]),
                    imageStore.getImageList("cannon1")); //House.HOUSE_KEY
            this.tryAddEntity(entity);
        }

        return properties.length == House.HOUSE_NUM_PROPERTIES;
    }
    private void tryAddEntity(Entity entity) {
        if (this.isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        this.addEntity(entity);
    }
    private boolean processLine(String line, ImageStore imageStore)
    {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[PROPERTY_KEY]) {
                case Background.BGND_KEY:
                    return this.parseBackground(properties,imageStore);
                case Dude.DUDE_KEY:
                    return parseDude(properties, imageStore);
                case Obstacle.OBSTACLE_KEY:
                    return parseObstacle(properties,imageStore);
                case Fairy.FAIRY_KEY:
                    return parseFairy(properties,imageStore);
                case House.HOUSE_KEY:
                    return parseHouse(properties,imageStore);
                case Tree.TREE_KEY:
                    return parseTree(properties,imageStore);
                case Sapling.SAPLING_KEY:
                    return parseSapling(properties, imageStore);
                case Laser.LASER_KEY:
                    return parseLaser(properties, imageStore);
                case LeftScoreBoard.LeftScoreBoard_KEY:
                    return parseLeftScoreBoard(properties, imageStore);
                case RightScoreBoard.RightScoreBoard_KEY:
                    return parseRightScoreBoard(properties, imageStore);
            }
        }

        return false;
    }

    public Optional<Entity> findNearest(Point pos, List<Class> entityClasses)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Class c: entityClasses)
        {
            for (Entity entity : this.getEntities()) {
                if (entity.getClass() == c) {
                    ofType.add(entity);
                }
            }
        }

        return this.nearestEntity(ofType, pos);
    }

        public  Optional<Entity> nearestEntity(
            List<Entity> entities, Point pos)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = this.distanceSquared(nearest.getPosition(), pos);

            for (Entity other : entities) {
                int otherDistance = this.distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }
    public void load(
            Scanner in, ImageStore imageStore)
    {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!this.processLine(in.nextLine(), imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            }
            catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            }
            catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                e.getMessage()));
            }
            lineNumber++;
        }
    }

    private int distanceSquared(Point p1, Point p2) {
        int deltaX = p1.getX() - p2.getX();
        int deltaY = p1.getY() - p2.getY();

        return deltaX * deltaX + deltaY * deltaY;
    }
}
