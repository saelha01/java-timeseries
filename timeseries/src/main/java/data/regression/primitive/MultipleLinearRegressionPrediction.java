/*
 * Copyright (c) 2017 Jacob Rachiele
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to
 * do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * Contributors:
 *
 * Jacob Rachiele
 */

package data.regression.primitive;


import lombok.EqualsAndHashCode;
import lombok.ToString;
import math.linear.doubles.Matrix;
import math.linear.doubles.Vector;

@EqualsAndHashCode @ToString
public class MultipleLinearRegressionPrediction implements LinearRegressionPrediction{

    private final LinearRegressionModel model;
    private final double[] predictedValues;

    MultipleLinearRegressionPrediction(LinearRegressionModel model, double[][] newPredictors) {
        this.model = model;
        Matrix predictionMatrix = new Matrix(newPredictors, Matrix.Order.COLUMN_MAJOR);
        Vector beta = Vector.from(model.beta());
        this.predictedValues = predictionMatrix.times(beta).elements();
    }

    private double[][] copy(double[][] values) {
        double[][] copied = new double[values.length][];
        for (int i = 0; i < values.length; i++) {
            copied[i] = values[i].clone();
        }
        return copied;
    }

    @Override
    public double[] predictedValues() {
        return this.predictedValues.clone();
    }
}
