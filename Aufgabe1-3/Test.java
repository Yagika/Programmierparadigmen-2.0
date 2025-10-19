import Problem1.objectGenerator;
import Problem1.Flowerspecies;
import java.util.ArrayList;

public class Test{

    public static void main(String[] args){

        //create object to create a group
        objectGenerator obj = new objectGenerator();

        //store the created Arraylist where all the generated Flowerspecies objects are
        ArrayList<Flowerspecies> Group1 = new ArrayList<Flowerspecies>();



        Group1 = obj.generatePlantGroups(1);


        for(int i = 0; i < Group1.size(); i++){
            System.out.println(Group1.get(i).toString());
        }
    }

}
