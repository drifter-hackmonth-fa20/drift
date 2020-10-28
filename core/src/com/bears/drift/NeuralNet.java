package com.bears.drift;

import org.apache.commons.math3.analysis.function.Exp;
import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixChangingVisitor;

import java.util.ArrayList;

public class NeuralNet {
    ArrayList<RealMatrix> layers;
    ArrayList<RealMatrix> biases;
    boolean useBias;
    String output;
    ArrayList<Integer> dimensions;

    public NeuralNet(ArrayList<Integer> dimensions, boolean useBias, String output) {
        this.layers = new ArrayList<>();
        this.biases = new ArrayList<>();
        this.useBias = useBias;
        this.output = output;
        this.dimensions = dimensions;

        NormalDistribution dist;
        for (int i = 0; i < dimensions.size()-1; i++) {
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
        RealMatrix copy = input.copy();
        if (output.equals("softmax")) {
            exp(copy);
            RealMatrix temp = MatrixUtils.createRealMatrix(new double[copy.getColumnDimension()][1]);
            for (int i = 0; i < copy.getColumnDimension(); i++) temp.setEntry(i, 0, 1);
            RealMatrix rowSums = copy.multiply(temp);
            for (int i = 0; i < copy.getColumnDimension(); i++)
                for (int j = 0; j < rowSums.getRowDimension(); j++)
                    copy.multiplyEntry(j, i, (float)(1/rowSums.getEntry(j, 0)));
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
        RealMatrix copy = input.copy();
        for (int i = 0; i < this.layers.size(); i++) {
            RealMatrix ones = MatrixUtils.createRealMatrix(new double[copy.getRowDimension()][1]);
            for (int j = 0; j < copy.getRowDimension(); j++) ones.setEntry(j, 0, 1);
            copy = copy.multiply(layers.get(i)).add(ones.multiply(this.biases.get(i)));
            if (i == this.layers.size() - 1)
                copy = output(copy);
            else
                relu(copy);
        }
        return copy;
    }

    public NeuralNet mate(NeuralNet other) {
        final NeuralNet child = new NeuralNet(this.dimensions, this.useBias, this.output);
        final NeuralNet finalOther = other;
        for (int i = 0; i < child.layers.size(); i++) {
//            RealMatrix pass_on = MatrixUtils.createRealMatrix(1, child.layers.get(i).getColumnDimension());
//            RealMatrix pass_on_opp = MatrixUtils.createRealMatrix(1, child.layers.get(i).getColumnDimension());
//            for (int j = 0; j < pass_on.getColumnDimension(); j++) {
//                boolean test = Math.random() < 0.5;
//                pass_on.setEntry(0, j, (test) ? 0 : 1);
//                pass_on_opp.setEntry(0, j, (test) ? 1 : 0);
//            }
//            System.out.println(pass_on.getRowDimension() + "," + pass_on.getColumnDimension());
//            System.out.println(this.layers.get(i).getRowDimension() + "," + this.layers.get(i).getColumnDimension());
//            child.layers.set(i, pass_on.multiply(this.layers.get(i)).add(pass_on_opp.multiply(other.layers.get(i))));
//            child.biases.set(i, pass_on.multiply(this.biases.get(i)).add(pass_on_opp.multiply(other.biases.get(i))));

            final int finalI = i;
            child.layers.get(i).walkInOptimizedOrder(new RealMatrixChangingVisitor() {
                @Override
                public void start(int rows, int columns, int startRow,
                                  int endRow, int startColumn, int endColumn) {

                }

                @Override
                public double visit(int row, int column, double value) {
                    boolean test = Math.random() < 0.5;
                    if (test) {
                        child.biases.get(finalI).setEntry(0, column, NeuralNet.this.biases.get(finalI).getEntry(0, column));
                        return NeuralNet.this.layers.get(finalI).getEntry(row, column);
                    } else {
                        child.biases.get(finalI).setEntry(0, column, finalOther.biases.get(finalI).getEntry(0, column));
                        return finalOther.layers.get(finalI).getEntry(row, column);
                    }
                }

                @Override
                public double end() {
                    return 0;
                }
            });
        }
        child.mutate();
        return child;
    }

    private void mutate() {
        float stdev = (float) 0.03;
        NormalDistribution dist = new NormalDistribution(0, stdev);
        for (int i = 0; i < this.layers.size(); i++) {
            RealMatrix layer = this.layers.get(i);
            RealMatrix bias = this.biases.get(i);
            RealMatrix normalL = RandomNormalMatrix(dist, layer.getRowDimension(), layer.getColumnDimension());
            RealMatrix normalB = RandomNormalMatrix(dist, bias.getRowDimension(), bias.getColumnDimension());
            this.layers.set(i, layer.add(normalL));
            if (this.useBias) {
                this.biases.set(i, bias.add(normalB));
            }
        }
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
