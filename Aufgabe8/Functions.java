public final class Functions {
    private Functions(){}

    public static double objective(FunctionId id, double[] x) {
        switch (id) {
            case SINE: {
                double deg = x[0];
                return Math.sin(Math.toRadians(deg));
            }
            case COS2_MIN: {
                double deg = x[0];
                double v = Math.cos(Math.toRadians(deg));
                return v * v;
            }
            case HEAVY_MULTI: {
                double s = 0.0;
                for (int i = 0; i < x.length; i++) {
                    double v = x[i];
                    double t = v;
                    for (int k = 0; k < 50; k++) {
                        t = Math.sin(t) + Math.cos(t * 0.7) + 0.0001 * (v * v);
                    }
                    s += t;
                }
                return -Math.abs(Math.sin(s)) + 0.0001 * s * s;
            }
            default:
                throw new IllegalArgumentException("Unknown FunctionId");
        }
    }
}
