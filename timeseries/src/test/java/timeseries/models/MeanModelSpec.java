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

package timeseries.models;

import data.DoubleFunctions;
import org.hamcrest.MatcherAssert;
import timeseries.TestData;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import timeseries.TimeSeries;
import timeseries.TimeUnit;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertArrayEquals;

public class MeanModelSpec {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private final TimeSeries series = TestData.ausbeer;
    private final MeanModel meanModel = new MeanModel(series);

    @Test
    public void whenStepsLessThanOneThenIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        new MeanForecast(meanModel, 0, 0.05);
    }

    @Test
    public void whenAlphaLessThanZeroThenIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        new MeanForecast(meanModel, 5, -0.1);
    }

    @Test
    public void whenAlphaGreaterThanOneThenIllegalArgumentException() {
        exception.expect(IllegalArgumentException.class);
        new MeanForecast(meanModel, 5, 1.01);
    }

    @Test
    public void whenMeanForecastComputedForecastValuesCorrect() {
        int h = 6;
        TimeSeries pointForecast = meanModel.pointForecast(h);
        double[] expected = DoubleFunctions.fill(h, series.mean());
        assertArrayEquals(expected, pointForecast.asArray(), 1E-2);
    }

    @Test
    public void whenMeanForecastComputedFirstObservationTimeCorrect() {
        TimeSeries pointForecast = meanModel.pointForecast(6);
        OffsetDateTime expectedTime = OffsetDateTime.of(2008, 10, 1, 0, 0, 0, 0, ZoneOffset.ofHours(0));
        assertThat(pointForecast.observationTimes().get(0), is(equalTo(expectedTime)));
    }

    @Test
    public void whenMeanForecastComputedTimePeriodUnchanged() {
        TimeSeries pointForecast = meanModel.pointForecast(6);
        TimeUnit timeUnit = TimeUnit.QUARTER;
        assertThat(pointForecast.timePeriod().timeUnit(), is(equalTo(timeUnit)));
    }

    @Test
    public void whenMeanForecastThenPredictionIntervalsCorrect() {
        MeanForecast forecast = new MeanForecast(meanModel, 6, 0.05);
        double[] lowerValues = {243.129289, 243.129289, 243.129289, 243.129289, 243.129289, 243.129289};
        double[] upperValues = {586.775924, 586.775924, 586.775924, 586.775924, 586.775924, 586.775924};
        assertArrayEquals(lowerValues, forecast.lowerPredictionValues().asArray(), 1E-2);
        assertArrayEquals(upperValues, forecast.upperPredictionValues().asArray(), 1E-2);
        assertArrayEquals(lowerValues, forecast.computeLowerPredictionBounds(6, 0.05).asArray(), 1E-2);
        assertArrayEquals(upperValues, forecast.computeUpperPredictionBounds(6, 0.05).asArray(), 1E-2);
    }

    @Test
    public void whenLowerBoundsStepsLessThanOneThenIllegalArgument() {
        MeanForecast forecast = new MeanForecast(meanModel, 6, 0.05);
        exception.expect(IllegalArgumentException.class);
        forecast.computeLowerPredictionBounds(0, 0.05);
    }

    @Test
    public void whenUpperBoundsStepsLessThanOneThenIllegalArgument() {
        MeanForecast forecast = new MeanForecast(meanModel, 6, 0.05);
        exception.expect(IllegalArgumentException.class);
        forecast.computeUpperPredictionBounds(0, 0.05);
    }

    @Test
    public void whenLowerBoundsAlphaLessThanZeroThenIllegalArgument() {
        MeanForecast forecast = new MeanForecast(meanModel);
        exception.expect(IllegalArgumentException.class);
        forecast.computeLowerPredictionBounds(6, -0.05);
    }

    @Test
    public void whenUpperBoundsAlphaLessThanZeroThenIllegalArgument() {
        MeanForecast forecast = new MeanForecast(meanModel);
        exception.expect(IllegalArgumentException.class);
        forecast.computeUpperPredictionBounds(6, -0.05);
    }

    @Test
    public void whenLowerBoundsAlphaGreaterThanOneThenIllegalArgument() {
        MeanForecast forecast = new MeanForecast(meanModel);
        exception.expect(IllegalArgumentException.class);
        forecast.computeLowerPredictionBounds(6, 1.05);
    }

    @Test
    public void whenUpperBoundsAlphaGreaterThanOneThenIllegalArgument() {
        MeanForecast forecast = new MeanForecast(meanModel);
        exception.expect(IllegalArgumentException.class);
        forecast.computeUpperPredictionBounds(6, 1.05);
    }

    @Test
    public void testEqualsAndHashCode() {
        MeanModel model1 = new MeanModel(series);
        MeanModel model2 = new MeanModel(TestData.debitcards);
        MeanModel nullModel = null;
        String aNonModel = "";
        assertThat(meanModel, is(meanModel));
        assertThat(meanModel, is(model1));
        assertThat(meanModel.hashCode(), is(model1.hashCode()));
        MatcherAssert.assertThat(model1, is(not(model2)));
        MatcherAssert.assertThat(model2, is(not(nullModel)));
        MatcherAssert.assertThat(model2, is(not(aNonModel)));
    }

}
