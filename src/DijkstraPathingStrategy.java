import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DijkstraPathingStrategy
        implements PathingStrategy
{

    private List<Point> walkBackPath(DijkstraNode finalDijkstraNode, Point start)
    {
        List<Point> path = new LinkedList<>();
        DijkstraNode pointer = finalDijkstraNode;
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
        PriorityQueue<DijkstraNode> openList = new PriorityQueue<>();
        DijkstraNode startNode = new DijkstraNode(start,null, 0);
        HashMap<Point, DijkstraNode> pointData = new HashMap<>();

        // Begin A* Algorithm
        openList.add(startNode);

        while(openList.size() > 0)
        {
            DijkstraNode curr = openList.poll();

            // Check if current DijkstraNode is within reach of target point.

            if (withinReach.test(curr.getPoint(), end))
            {
                return walkBackPath(curr, start);
            }

            // get valid points adjacent to current DijkstraNode.
            Stream<Point> neighborPoints = potentialNeighbors.apply(curr.getPoint())
                    .filter(canPassThrough)
                    .filter(pt -> !pt.equals(start));

            // create neighbor DijkstraNodes of current: All valid DijkstraNodes that are not on the closed list.
            List<DijkstraNode> neighborDijkstraNodes = neighborPoints
                    .filter(p -> !(closedList.contains(p))) // filter out points in closed list already
                    .map(p -> new DijkstraNode(p, curr, // create neighbor DijkstraNodes
                            curr.getG() + 1))
                    .collect(Collectors.toList());

            for (DijkstraNode n : neighborDijkstraNodes)
            {
                if (pointData.containsKey(n.getPoint())) // Checking if DijkstraNode has been a neighbor before
                {
                    DijkstraNode prev = pointData.get(n.getPoint());
                    if (prev.getG() > n.getG()) // Checking if G value has improved in this iteration
                    {
                        pointData.put(n.getPoint(), n);
                        openList.remove(prev); // updating dictonary with DijkstraNode's new G value
                    }
                }
                else
                {
                    pointData.put(n.getPoint(), n);
                }
                openList.add(n); // Adding neighbor DijkstraNode to queue
            }
            closedList.add(curr.getPoint()); // Adding current point to the closed list.
        }
        return new LinkedList<>();
    }
}
