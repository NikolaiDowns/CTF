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

        // Begin Dijkstras Algorithm
        openList.add(startNode);

        while(openList.size() > 0) {
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
                            1000000))
                    .collect(Collectors.toList());


            for (DijkstraNode n : neighborDijkstraNodes) {
                if (!(closedList.contains(n.getPoint()))) // Checking if DijkstraNode has been a neighbor before
                {
                    curr.setNeighbor(n, n.getG());
                    if (pointData.containsKey(n.getPoint()))
                    {
                        DijkstraNode prevNode = pointData.get(n.getPoint());
                        if (curr.getG() + 1 < prevNode.getG())
                        {
                            openList.remove(prevNode);
                            n.setG(curr.getG() + 1);
                            curr.setNeighbor(n, n.getG());
                            openList.add(n);
                            pointData.remove(prevNode.getPoint());
                            pointData.put(n.getPoint(), n);
                        }
                    }
                    else
                    {
                        n.setG(curr.getG() + 1);
                        curr.setNeighbor(n, n.getG());
                        openList.add(n);
                        pointData.put(n.getPoint(), n);
                    }
                }
            }
            closedList.add(curr.getPoint()); // Adding current point to the closed list.
        }
        return new LinkedList<>();
    }
}
