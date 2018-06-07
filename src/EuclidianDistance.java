public class EuclidianDistance {

    private Double xa;
    private Double xb;
    private Double ya;
    private Double yb;
    private Double distance;

    EuclidianDistance(String firstCity[], String secondCity[]) {
        this.xa = Double.parseDouble(firstCity[1]);
        this.xb = Double.parseDouble(firstCity[2]);
        this.ya = Double.parseDouble(secondCity[1]);
        this.yb = Double.parseDouble(secondCity[2]);
        this.distance = this.calcDistance();
    }

    private Double calcDistance() {
        Double a = Math.pow((this.xa - this.xb), 2);
        Double b = Math.pow((this.ya - this.yb), 2);        
        return Math.sqrt( a + b );
    }

    public Double getXa() {
        return xa;
    }

    public Double getXb() {
        return xb;
    }

    public Double getYa() {
        return ya;
    }

    public Double getYb() {
        return yb;
    }

    public Double getDistance() {
        return distance;
    }
}
