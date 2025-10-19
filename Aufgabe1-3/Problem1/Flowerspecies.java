package Problem1;
import java.util.Random;



public class Flowerspecies {
        //class to create and maintain different flower-species
        //handles every flower related calculation itself. how cool!

        public double foodvalue(){ //Nahrungsangebot (food supply) n SUM_i(y_i * b_i), in here its the sum of the individual plant,
                return y * b;           //at the end it should be summed up (n for bee population calculation!)
        }

        //simulates the propagation of the plants during the restingphase, so during winter, this is done in one go.
        public void resting_phase(int num){
                //parse a number that stays the same, so that everyone gets the same outcome and the data
                //can be recreated.
                Random rand = new Random(num);

                double min = c_lower;
                double max = c_upper;

                double randomdouble = rand.nextDouble() * (max - min) + min;

                this.y = this.y * this.s * randomdouble;
        }

        public void moisturethreshold(float f){ //this calculates new y value based on if a certain f_lower and f_upper threshold is reached
                if(((this.f_lower/2) < f && f < this.f_lower) || (this.f_upper < f && f < (2*this.f_upper))){
                        this.y = this.y * 0.99;
                }
                if(f <= (f_lower/2) || f >= (f_upper*2)){
                        this.y = this.y * 0.97;
                }
        }

        //sunhours h = sum of sunshine-time d over the time of the vegetationperiod so far.
        // TODO: this also requires asking since the definition can be misinterpreted
        public void bloomtime(float h, float d){ //changes the bloom-state of the given flower based on sunlight and suntime
                if(this.h_lower <= h && h < this.h_upper){
                        this.b += (q * (d + 3));
                        if(this.b > 1){
                                this.b = 1;
                        }
                }

                if(this.h_upper <= h){
                        this.b -= (q * (d - 3));
                        if(this.b < 0){
                                this.b = 0;
                        }
                }
        }

        // TODO: ask if this is actually the right interpretation.
        // this method should be called after collecting the total_foodvalue = the sum of all individual food values
        // increases the quality of the seed production of the flowers
        // d = sunshine-time, its a random number between 0-12
        public void pollutionprobability(float bee_population, float total_foodvalue, float d){
                if(bee_population >= total_foodvalue){
                        this.s += this.p * this.b * (d + 1);
                }else this.s += this.p * this.b * (d + 1) * (bee_population / total_foodvalue);
        }


        //constructor. just check the values for the bounds before constructing the objects. please
        public Flowerspecies(double y, double c_lower, double c_upper, double f_lower, double f_upper,
                                  double h_lower, double h_upper, double q, double p){
                this.y = y; this.c_lower = c_lower; this.c_upper = c_upper; this.f_lower = f_lower; this.f_upper = f_upper;
                this.h_lower = h_lower; this.h_upper = h_upper; this.q = q; this.p = p;
        }

        @Override
        public String toString(){
                return " y:" + y + " b:" + b + " s:" + s + " c_lower:" + c_lower + " c_upper:" + c_upper + " f_lower:" + f_lower + " f_upper:" + f_upper +
                        " h_lower:" + h_lower + " h_upper:" + h_upper + " q:" + q + " p:" + p;
        }



        public double y; //Wuchskraft, y_i >= 0, means the number of bees that find food through this plantspecies
        public double b = 0; //Blüte-zahl, 0 <= b_i <= 1 amount of plants in bloom
        public double s = 0; //Qualität samenentwicklung, 0 <= s_i <= 1, the quality of the seed production (?)
        //b and s are set to 0 at the start of the simulation
        public double c_lower; //vermehrungsgrenzen c_i- und c_i+, lower and upper bound for reproduction
        public double c_upper; // I dont see which c_i- and c_i+. c_i+ and c_i- are defined per flower species
        // TODO: find out lower and upper bounds of c_upper & c_lower
        public double f_lower; //feuchtegrenzen, moisture_bounds: used in conjuction with f (ground moisture) from main simulation
        public double f_upper; // 0 < f_lower < f_upper < 1

        public double h_lower; //Blühgrenzen, specifies the sunhours where the blooming begins/ends
        public double h_upper; //0 < h_lower < h_upper
        public double q; //Blüteintesität, bloomintensivity 0 < q_i < 1/15

        public double p; //Bestaubwahrscheinlichkeit, Pollination probability, 0 < p_i < 1/h_lower - h_upper


        }



