public class Node implements Comparable<Node>{

    private int g, h, f;
    private Point p;
    private Node prior;

    public Node(Point p, Node prior, int g, int h)
    {
        this.g = g;
        this.h = h;
        this.f = g + h;
        this.p = p;
        this.prior = prior;
    }

    public int getG() {
        return g;
    }

    public Node getPrior() {
        return prior;
    }

    public Point getPoint(){
        return this.p;
    }

    public int compareTo(Node other)
    {
        return this.f - other.f;
    }

}
