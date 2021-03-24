package models;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;

public class Camera {
    private View view;
    private int focalX, focalY;

    public Camera(View view, int focal) {
        this.view = view;
        this.focalX = focal;
        this.focalY = focal;
    }

    public ArrayList<Line> project() {
        ArrayList<Line> newLines = new ArrayList<>();

        RealMatrix focalMatrix = getFocalMatrix();
        for (Line line : this.view.getLines()) {
            RealMatrix aMatrix = line.a.toRealMatrix();
            RealMatrix aResult = focalMatrix.multiply(aMatrix);

            RealMatrix bMatrix = line.b.toRealMatrix();
            RealMatrix bResult = focalMatrix.multiply(bMatrix);

            double dz = 1 / aResult.getEntry(2, 0);
            if (dz <= 0) dz = 1;
            Point a = new Point(
                    dz * aResult.getEntry(0, 0),
                    dz * aResult.getEntry(1, 0),
                    0
            );

            dz = 1 / bResult.getEntry(2, 0);
            if (dz <= 0) dz = 1;
            Point b = new Point(
                    dz * bResult.getEntry(0, 0),
                    dz * bResult.getEntry(1, 0),
                    0
            );
            Line newLine = new Line(a, b);
            newLines.add(newLine);
        }

        return newLines;
    }

    public void move(int x, int y, int z) {
        ArrayList<Line> newLines = new ArrayList<>();

        RealMatrix translationMatrix = this.getTranslationMatrix(x, y, z);
        for (Line line : this.view.getLines()) {
            RealMatrix aMatrix = line.a.toRealMatrix();
            Point a = new Point(translationMatrix.multiply(aMatrix));

            RealMatrix bMatrix = line.b.toRealMatrix();
            Point b = new Point(translationMatrix.multiply(bMatrix));
            newLines.add(new Line(a, b));
        }

        this.view.setLines(newLines);
    }

    public void rotate(int rotationX, int rotationY, int rotationZ) {
        ArrayList<Line> newLines = new ArrayList<>();

        RealMatrix rotationMatrix = this.getXRotationMatrix(rotationX).multiply(
                this.getYRotationMatrix(rotationY)).multiply(
                this.getZRotationMatrix(rotationZ));
        for (Line line : this.view.getLines()) {
            RealMatrix aMatrix = line.a.toRealMatrix();
            Point a = new Point(rotationMatrix.multiply(aMatrix));

            RealMatrix bMatrix = line.b.toRealMatrix();
            Point b = new Point(rotationMatrix.multiply(bMatrix));
            newLines.add(new Line(a, b));
        }

        this.view.setLines(newLines);
    }

    public void zoom(int x, int y) {
        this.focalX += x;
        this.focalY += y;
        if (this.focalX <= 0) this.focalX = 1;
        if (this.focalY <= 0) this.focalY = 1;
    }

    public static RealMatrix getTranslationMatrix(int x, int y, int z) {
        Array2DRowRealMatrix translationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1, 0, 0, x},
                {0, 1, 0, y},
                {0, 0, 1, z},
                {0, 0, 0, 1}
        });
        return translationMatrix;
    }

    public static RealMatrix getXRotationMatrix(int angle) {
        double sin = Math.sin(angle * Math.PI / 180), cos = Math.cos(angle * Math.PI / 180);
        Array2DRowRealMatrix xRotationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1, 0, 0, 0},
                {0, cos, -sin, 0},
                {0, sin, cos, 0},
                {0, 0, 0, 1}
        });
        return xRotationMatrix;
    }

    public static RealMatrix getYRotationMatrix(int angle) {
        double sin = Math.sin(angle * Math.PI / 180), cos = Math.cos(angle * Math.PI / 180);
        Array2DRowRealMatrix yRotationMatrix = new Array2DRowRealMatrix(new double[][]{
                {cos, 0, sin, 0},
                {0, 1, 0, 0},
                {-sin, 0, cos, 0},
                {0, 0, 0, 1}
        });
        return yRotationMatrix;
    }

    public static RealMatrix getZRotationMatrix(int angle) {
        double sin = Math.sin(angle * Math.PI / 180), cos = Math.cos(angle * Math.PI / 180);
        Array2DRowRealMatrix zRotationMatrix = new Array2DRowRealMatrix(new double[][]{
                {cos, -sin, 0, 0},
                {sin, cos, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        return zRotationMatrix;
    }

    public RealMatrix getFocalMatrix() {
        RealMatrix focalMatrix = new Array2DRowRealMatrix(new double[][]{
                {this.focalX, 0, 0, 0},
                {0, this.focalY, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        return focalMatrix;
    }
}
