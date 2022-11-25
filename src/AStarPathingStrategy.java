import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{
    private int heuristic(Point curr, Point end)
    {
        return Math.abs(curr.getX() - end.getX()) + Math.abs(curr.getY() - end.getY());
    }

    private List<Point> walkBackPath(Node finalNode, Point start)
    {
        List<Point> path = new LinkedList<>();
        Node pointer = finalNode;
        while(pointer.getPoint() != start)
        {
            path.add(0, pointer.getPoint());
            pointer = pointer.getPrior();
        }
        return path;
    }

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        Set<Point> closedList = new HashSet<>();
        PriorityQueue<Node> openList = new PriorityQueue<>();
        int startToGoal = heuristic(start, end);
        Node startNode = new Node(start,null, 0, startToGoal);
        HashMap<Point, Node> pointData = new HashMap<>();

        // Begin A* Algorithm
        openList.add(startNode);
        int iter = 0;
        while(openList.size() > 0)
        {
            iter ++;

            if(iter % 1000000 == 0)
            {
                System.out.println("iterations: " + iter);
            }
            Node curr = openList.poll();

            // Check if current node is within reach of target point.

            if (withinReach.test(curr.getPoint(), end))
            {
                return walkBackPath(curr, start);
            }

            // get valid points adjacent to current node.
            Stream<Point> neighborPoints = potentialNeighbors.apply(curr.getPoint())
                    .filter(canPassThrough)
                    .filter(pt -> !pt.equals(start));

            // create neighbor nodes of current: All valid nodes that are not on the closed list.
            List<Node> neighborNodes = neighborPoints
                    .filter(p -> !(closedList.contains(p))) // filter out points in closed list already
                    .map(p -> new Node(p, curr, // create neighbor nodes
                    curr.getG() + 1, Math.abs(p.getX() - end.getX()) + Math.abs(p.getY() - end.getY())))
                    .collect(Collectors.toList());

            for (Node n : neighborNodes)
            {
                if (pointData.containsKey(n.getPoint())) // Checking if Node has been a neighbor before
                {
                    Node prev = pointData.get(n.getPoint());
                    if (prev.getG() > n.getG()) // Checking if G value has improved in this iteration
                    {
                        pointData.put(n.getPoint(), n);
                        openList.remove(prev); // updating dictonary with Node's new G value
                        openList.add(n); // Adding neighbor node to queue
                    }
                }
                else
                {
                    pointData.put(n.getPoint(), n);
                    openList.add(n); // Adding neighbor node to queue
                }
            }
            closedList.add(curr.getPoint()); // Adding current point to the closed list.
        }
        return new LinkedList<>();
    }
}
