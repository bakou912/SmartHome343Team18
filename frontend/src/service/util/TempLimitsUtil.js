export default class TempLimitsUtil {
    static adjustTempWithLimits(temp, limits) {
        if (temp > limits.max) {
            temp = limits.max;
        } else if (temp < limits.min) {
            temp = limits.min
        }
        return temp;
    }
}
