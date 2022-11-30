import java.util.HashMap;

public class DijkstraNode implements Comparable<DijkstraNode>{

    private Integer g;
    private Point p;
    private DijkstraNode prior;

    private HashMap<DijkstraNode, Integer> neighbors;

    public DijkstraNode(Point p, DijkstraNode prior, int g)
    {
        this.g = g;
        this.p = p;
        this.prior = prior;
        this.neighbors = new HashMap<DijkstraNode, Integer>();
    }

    public int getG() {
        return g;
    }

    public void setG(int newG)
    {
        this.g = newG;
    }
    public DijkstraNode getPrior() {
        return prior;
    }

    public void setPrior(DijkstraNode prior) {
        this.prior = prior;
    }

    public Point getPoint(){
        return this.p;
    }

    public void setNeighbor(DijkstraNode neighbor, int distance)
    {
        this.neighbors.put(neighbor, distance);
    }

    public int compareTo(DijkstraNode other)
    {
        return this.getG() - other.getG();
    }

}
