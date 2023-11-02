package plugway.mc.music.disc.dj.utils;

public class Math {
    /**
     * Interpolates a value from the input range to the output range
     * @param value Input value
     * @param inMin Input range start
     * @param inMax Input range end
     * @param outMin Output range start
     * @param outMax Output range end
     * @return Interpolated value
     */
    public static double map(double value, double inMin, double inMax, double outMin, double outMax) {
        return (value - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }
}
