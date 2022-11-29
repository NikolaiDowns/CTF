public class DijkstraNode implements Comparable<DijkstraNode>{

    private int g;
    private Point p;
    private DijkstraNode prior;

    public DijkstraNode(Point p, DijkstraNode prior, int g)
    {
        this.g = g;
        this.p = p;
        this.prior = prior;
    }

    public int getG() {
        return g;
    }

    public DijkstraNode getPrior() {
        return prior;
    }

    public Point getPoint(){
        return this.p;
    }

    public int compareTo(DijkstraNode other)
    {
        return this.getG() - other.getG();
    }

}
