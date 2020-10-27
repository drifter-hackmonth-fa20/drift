package com.bears.drift;

import org.apache.commons.math3.analysis.function.Exp;
import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixChangingVisitor;

import java.util.ArrayList;

public class NeuralNet {
    ArrayList<RealMatrix> layers;
    ArrayList<RealMatrix> biases;
    boolean useBias;
    String output;

    public NeuralNet(ArrayList<Integer> dimensions, boolean useBias, String output) {
        this.layers = new ArrayList<>();
        this.biases = new ArrayList<>();
        this.useBias = useBias;
        this.output = output;

        NormalDistribution dist;
        for (int i = 0; i < dimensions.size(); i++) {
            int size1 = dimensions.get(i);
            int size2 = dimensions.get(i+1);
            double std = Math.sqrt(2f/(size1+size2));
            dist = new NormalDistribution(0, std);
            RealMatrix layer = RandomNormalMatrix(dist, size1, size2);
            RealMatrix bias;
            if (useBias)
                bias = RandomNormalMatrix(dist, 1, size2);
            else {
                bias = null;
            }
            layers.add(layer);
            biases.add(bias);
        }
    }

    public RealMatrix output(RealMatrix input) {
        //if (output.equals("softmax")
            //return lambda X : np.exp(X) / np.sum(np.exp(X), axis=1).reshape(-1, 1)
        RealMatrix copy = input.copy();
        if (output.equals("softmax")) {
            exp(copy);
            RealMatrix temp = MatrixUtils.createRealMatrix(new double[copy.getColumnDimension()][1]);
            for (int i = 0; i < copy.getColumnDimension(); i++) temp.setEntry(i, 0, 1);
            RealMatrix rowSums = copy.multiply(temp);
            for (int i = 0; i < copy.getColumnDimension(); i++)
                for (int j = 0; j < rowSums.getRowDimension(); j++)
                    copy.multiplyEntry(i, j, (float)(1/rowSums.getEntry(j, 0)));
        }
        if (output.equals("sigmoid")) {
            copy = copy.scalarMultiply(-1);
            exp(copy);
            copy = copy.scalarAdd(1);
            elementInverse(copy);
            return copy;
        }
        return copy;
    }

    public RealMatrix predict(RealMatrix input) {
        /*
        if not X.ndim == 2:
            raise ValueError(f'Input has {X.ndim} dimensions, expected 2')
        if not X.shape[1] == self.layers[0].shape[0]:
            raise ValueError(f'Input has {X.shape[1]} features, expected {self.layers[0].shape[0]}')
        for index, (layer, bias) in enumerate(zip(self.layers, self.biases)):
            X = X @ layer + np.ones((X.shape[0], 1)) @ bias
            if index == len(self.layers) - 1:
                X = self.output(X) # output activation
            else:
                X = np.clip(X, 0, np.inf)  # ReLU
        return X
         */
        return null; //TODO
    }

    public static void elementInverse(RealMatrix input) {
        input.walkInOptimizedOrder(new RealMatrixChangingVisitor() {
            @Override
            public void start(int rows, int columns, int startRow, int endRow
                    , int startColumn, int endColumn) {}

            @Override
            public double visit(int row, int column, double value) {
                return 1/value;
            }

            @Override
            public double end() {return 0;}
        });
    }

    public static void relu(RealMatrix input) {
        input.walkInOptimizedOrder(new RealMatrixChangingVisitor() {
            @Override
            public void start(int rows, int columns, int startRow, int endRow
                    , int startColumn, int endColumn) {}

            @Override
            public double visit(int row, int column, double value) {
                if (value < 0) return 0;
                return value;
            }

            @Override
            public double end() {return 0;}
        });
    }

    public static void exp(RealMatrix input) {
        input.walkInOptimizedOrder(new RealMatrixChangingVisitor() {
            @Override
            public void start(int rows, int columns, int startRow, int endRow
                    , int startColumn, int endColumn) {}

            @Override
            public double visit(int row, int column, double value) {
                Exp exp = new Exp();
                return exp.value(value);
            }

            @Override
            public double end() {return 0;}
        });
    }

    public static RealMatrix RandomNormalMatrix(AbstractRealDistribution dist, int dimX, int dimY) {
        double[][] data = new double[dimX][dimY];
        for (int i = 0; i < dimX; i++) {
            for (int j = 0; j < dimY; j++) {
                data[i][j] = dist.sample();
            }
        }
        return MatrixUtils.createRealMatrix(data);
    }
}
