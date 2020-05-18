import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.devskiller.jfairy.producer.person.PersonProperties;


import java.util.ArrayList;

public class DataGeneratorFairy {
    public static void main(String[] args) {
        try{
            DataGenerator dataGen = new DataGenerator(100);
            dataGen.setGenderRate(40,60);
            dataGen.setMaleAgeRate(20,3);
            dataGen.setFemaleAgeRate(20,3);
            ArrayList<Person> list =  dataGen.getList();
            printList(list);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    public static void printList(ArrayList<Person> list){
        double totalNum = list.size();
        double numOfMale = 0;
        double numOfFemale = 0;
        for(int i=0; i<list.size(); i++){
            System.out.println((i+1) + ". Cinsiyet : " + list.get(i).getSex() + " Yaş : " + list.get(i).getAge() + " " + list.get(i).getFullName());
            if(list.get(i).getSex().toString().equals("MALE"))
                numOfMale++;
            else
                numOfFemale++;

        }

        System.out.println("Male rate : " + (numOfMale/totalNum*100));
        System.out.println("Female rate : " + (numOfFemale/totalNum*100));
    }

}

class DataGenerator{
    private double total = -1;
    private double maleRate = -1;
    private double femaleRate = -1;

    private double maleRateNum = -1, maleMax = -1;
    private double femaleRateNum = -1, femaleMax = -1;

    private ArrayList<Person> list;

    public DataGenerator(int totalNumOfPeople){
        this.total = totalNumOfPeople;
    }
    public void setGenderRate(double maleRate, double femaleRate) throws Exception {
        if(maleRate + femaleRate > 100)
            throw new Exception("Total rate can't be bigger than 100");
        else{
            this.maleRate = maleRate/100;
            this.femaleRate = femaleRate/100;
        }
    }

    public void setMaleAgeRate(double rate, double maxAge){
        this.maleRateNum = rate/100;
        this.maleMax = maxAge;
    }

    public void setFemaleAgeRate(double rate, double maxAge){
        this.femaleRateNum = rate/100;
        this.femaleMax = maxAge;
    }



    public ArrayList<Person> getList() throws Exception {
        Fairy fairy = Fairy.create();
        ArrayList<Person> list = new ArrayList<Person>((int)total);
        if(total == -1 || maleRate == -1 || femaleRate == -1 || maleRateNum == -1
                || maleMax == -1 || femaleRateNum == -1 || femaleMax == -1){
            throw new Exception("All information has not been entered.");
        }

        double numOfMale = total*maleRate;
        double numOfFemale = total*femaleRate;

        double firstPartOfMale = numOfMale*maleRateNum;
        double secondPartOfMale = numOfMale - firstPartOfMale;

        double firstPartOfFemale = numOfFemale*femaleRateNum;
        double secondPartOfFemale = numOfFemale - firstPartOfFemale;

        for(int i=0; i<firstPartOfMale; i++){
            Person person = fairy.person(PersonProperties.maxAge((int)maleMax),PersonProperties.male());
            list.add(person);
        }
        for(int i=0; i<secondPartOfMale; i++){
            Person person = fairy.person(PersonProperties.male());
            list.add(person);
        }

        for(int i=0; i<firstPartOfFemale; i++){
            Person person = fairy.person(PersonProperties.maxAge((int)femaleMax),PersonProperties.female());
            list.add(person);
        }
        for(int i=0; i<secondPartOfFemale; i++){
            Person person = fairy.person(PersonProperties.female());
            list.add(person);
        }

        return list;
    }

}
