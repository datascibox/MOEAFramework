/* Copyright 2009-2016 David Hadka
 *
 * This file is part of the MOEA Framework.
 *
 * The MOEA Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The MOEA Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the MOEA Framework.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.moeaframework.algorithm.single;

import java.io.Serializable;

import org.moeaframework.core.Solution;
import org.moeaframework.util.Vector;

public class VectorAngleDistanceScalingComparator implements AggregateObjectiveComparator, Serializable {

	private static final long serialVersionUID = -2535092560377062714L;

	private final double q;
	
	private final double[] weights;
	
	public VectorAngleDistanceScalingComparator(double[] weights) {
		this(weights, 100.0);
	}
	
	public VectorAngleDistanceScalingComparator(double[] weights, double q) {
		super();
		this.weights = weights;
		this.q = q;
	}

	@Override
	public int compare(Solution solution1, Solution solution2) {
		double fitness1 = calculateFitness(solution1, weights, q);
		double fitness2 = calculateFitness(solution2, weights, q);
		
		return Double.compare(fitness1, fitness2);
	}

	public static double calculateFitness(Solution solution, double[] weights, double q) {
		double[] objectives = solution.getObjectives();
		double magnitude = Vector.magnitude(objectives);
		double cosine = Vector.dot(weights, Vector.divide(objectives, magnitude));
		
		// prevent numerical error
		if (cosine > 1.0) {
			cosine = 1.0;
		}
		
		return magnitude / (Math.pow(cosine, q)+0.01);
	}

	
}