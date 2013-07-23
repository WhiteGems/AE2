package net.aetherteam.aether.util;

import java.util.Arrays;
import java.util.Random;

/**
 * Author: Jack-YYF
 * Created at: 6/14/13 2:21 AM
 * Published under MIT License.
 */
public class WeightedRandom {

	private final double[] pool;
	private final double EPS = 1e-8D;
	private static Random rand;

	static {
		rand = new Random();
	}

	public WeightedRandom (double[] probability) {
		if (probability == null) {
			throw new NullPointerException("Probability can not be null.");
		}
		if(probability.length == 0) {
			throw new IllegalArgumentException("At least one possibility should be given.");
		}
		this.pool = new double[probability.length];
		this.pool[0] = probability[0];
		for(int i = 1; i < probability.length; ++ i) {
			this.pool[i] = this.pool[i - 1] + probability[i];
		}
		if(Math.abs(this.pool[this.pool.length - 1] - 1.0D) > EPS) {
			throw new IllegalArgumentException("Total probability is not 1");
		}
	}

	public int nextRand() {
		double pos = rand.nextDouble();
		int ret = Arrays.binarySearch(pool, pos);
		if(ret < 0) ret = ~ret;
		return ret;
	}

}
