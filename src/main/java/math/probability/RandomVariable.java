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

package math.probability;

import lombok.NonNull;
import math.Real;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public final class RandomVariable<T> implements Function<Outcome<T>, Real> {

    private final Function<Outcome<T>, Real> function;
    private final SampleSpace<T> sampleSpace;
    private final Set<Real> range;

    RandomVariable(Function<Outcome<T>, Real> function, SampleSpace<T> sampleSpace) {
        this.function = function;
        this.sampleSpace = sampleSpace;
        this.range = getRange();
    }

    @Override
    public Real apply(@NonNull Outcome<T> t) {
        return this.function.apply(t);
    }

    private Set<Real> getRange() {
        Set<Real> range = new HashSet<>(sampleSpace.samplePoints().size());
        for (Outcome<T> outcome : sampleSpace.samplePoints()) {
            range.add(this.function.apply(outcome));
        }
        return range;
    }

    Set<Real> range() {
        return this.range;
    }
}
