

//at the start of each simulation-run the following parameters are initiated:
/*
    These values are never changed: (but double check the specs just in case I missed something)
    -the amount of years to simulate
    -stating population of bees
        *per plantspecies:
            -y  (y >= 0)
            -c_lower and c_upper
            -f_lower and f_upper    (0 < f_lower < f_upper < 1)
            -h_lower and h_upper    (0 < h_lower < h_upper)
            -q  (0 < q < 1/15)
            -p  (0 < p < 1/(h_upper - h_lower)

            these values are initiated with 0 but will changed through out simulation:
            these values are already handled internaly.
            -b = 0
            -s = 0
 */

// TODO: make a method to read and create data and generate objects with said data.
// constraints of said data (individual values) are written above.
// ignore bee population (x) and also weathering (d, h) since these values can be generated with one line when we need it (test.java)
