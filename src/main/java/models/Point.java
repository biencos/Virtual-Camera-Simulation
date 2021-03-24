package models;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class Point {
    public double x, y, z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(RealMatrix matrix) {
        if (matrix.getRowDimension() >= 3 && matrix.getColumnDimension() == 1) {
            this.x = matrix.getEntry(0, 0);
            this.y = matrix.getEntry(1, 0);
            this.z = matrix.getEntry(2, 0);
        } else {
            this.x = 0;
            this.y = 0;
            this.z = 0;
        }
    }

    public RealMatrix toRealMatrix() {
        return new Array2DRowRealMatrix(new double[][]{
                {this.x}, {this.y},
                {this.z}, {1}
        });
    }
}
