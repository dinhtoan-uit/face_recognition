package org.tensorflow.lite.examples.detection.SVM;

public class LinearSVM {
    private double[][] coefficients;
    private double[] intercepts;

    public LinearSVM(double[][] coefficients, double[] intercepts) {
        this.coefficients = coefficients;
        this.intercepts = intercepts;
    }

    public int predict(double[] features) {
        int classIdx = 0;
        double classVal = Double.NEGATIVE_INFINITY;
        for (int i = 0, il = this.intercepts.length; i < il; i++) {
            double prob = 0.;
            for (int j = 0, jl = this.coefficients[0].length; j < jl; j++) {
                prob += this.coefficients[i][j] * features[j];
            }
            if (prob + this.intercepts[i] > classVal) {
                classVal = prob + this.intercepts[i];
                classIdx = i;
            }
        }
        return classIdx;
    }
}
