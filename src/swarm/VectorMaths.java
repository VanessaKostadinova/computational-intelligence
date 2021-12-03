package swarm;

import java.util.List;

public class VectorMaths {

    public static Vector multiplyScalar(Vector v, double s) {
        double[] result = new double[v.getNumberOfAxis()];
        double[] vectorPoints = v.getVectorPoints();

        for (int i = 0; i < v.getNumberOfAxis(); i++) {
            result[i] = vectorPoints[i] * s;
        }

        return new Vector(result);
    }

    public static double[] addVectorToCoordinate(Vector v, double[] c) {
        double[] result = new double[c.length];
        double[] vectorPoints = v.getVectorPoints();

        for (int i = 0; i < v.getNumberOfAxis(); i++) {
            result[i] = c[i] + vectorPoints[i];
        }

        result[result.length - 1] = c[c.length - 1];

        return result;
    }

    public static Vector addVectors(List<Vector> vectors) {
        int size = vectors.get(0).vectorPoints.length;
        vectors.forEach(e -> {
            if (e.vectorPoints.length != size) throw new IllegalArgumentException("Number of axis don't match");
        });
        double[] result = new double[size];

        for (int i = 0; i < size; i++) {
            double pointValue = 0;
            for (Vector v : vectors) {
                pointValue += v.vectorPoints[i];
            }
            result[i] = pointValue;
        }

        return new Vector(result);
    }

    public static Vector generateNewVelocityVector(Vector v, double[] personalBest, double[] globalBest, double[] pos, double coefficientFi1, double coefficientFi2, double coefficientEta, double r1, double r2) {
        return addVectors(
                List.of(
                        multiplyScalar(v, coefficientEta),
                        multiplyScalar(new Vector(pos, personalBest), coefficientFi1 * r1),
                        multiplyScalar(new Vector(pos, globalBest), coefficientFi2 * r2)));
    }

    public static void clamp(Vector v, double maxLength) {
        double addedSquares = 0;
        for (double vectorPoint : v.vectorPoints) {
            addedSquares += Math.pow(vectorPoint, 2D);
        }

        double length = Math.sqrt(addedSquares);
        double[] vectorPoints = v.getVectorPoints();

        if (length > maxLength) {
            double multiplier = maxLength / length;
            for (int i = 0; i < v.getNumberOfAxis(); i++) {
                vectorPoints[i] = vectorPoints[i] * multiplier;
            }
        }

        v.updateVector(vectorPoints);
    }
}
