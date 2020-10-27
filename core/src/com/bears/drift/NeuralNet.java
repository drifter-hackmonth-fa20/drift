package com.bears.drift;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

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
        /*
        if output == 'softmax':
            return lambda X : np.exp(X) / np.sum(np.exp(X), axis=1).reshape(-1, 1)
        if output == 'sigmoid':
            return lambda X : (1 / (1 + np.exp(-X)))
        if output == 'linear':
            return lambda X : X
         */
        return null; //TODO
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
