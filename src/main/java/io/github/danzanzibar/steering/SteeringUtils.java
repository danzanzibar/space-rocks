//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This utility class provides static methods to add in kinematic calculations.
//******************************************************************************
package io.github.danzanzibar.steering;

public class SteeringUtils {

    //--------------------------------------------------------------------------
    //  Takes a double valued angle and returns the corresponding angle in
    //  the range of (-pi, pi].
    //--------------------------------------------------------------------------
    public static double mapToPlusMinusPi(double x) {
        while (x >= Math.PI)
            x -= 2 * Math.PI;

        while (x < -Math.PI)
            x += 2 * Math.PI;

        return x;
    }

    //--------------------------------------------------------------------------
    //  Provides a random number in the range of (-1, 1) with a tendency towards
    //  values closer to 0.
    //--------------------------------------------------------------------------
    public static double randomBinomial() {
        return Math.random() - Math.random();
    }
}
