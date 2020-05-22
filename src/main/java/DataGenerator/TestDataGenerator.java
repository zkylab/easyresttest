package DataGenerator;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.devskiller.jfairy.producer.person.PersonProperties;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class TestDataGenerator {
    public static void main(String[] args) {
        DataGenerator datagen = new DataGenerator(10);
        datagen.readJSON();
        /*try{
            DataGenerator dataGen = new DataGenerator(100);
            dataGen.setGenderRate(40,60);
            dataGen.setMaleAgeRate(20,3);
            dataGen.setFemaleAgeRate(20,3);
            ArrayList<Person> list =  dataGen.getList();
            printList(list);
            // dataGen.generateDataWithRegex("[a-z1-9]{10}",10,"text.txt");
           // ArrayList<Person> persons = dataGen.generateWithClass(Person.class,10);
        }
        catch (Exception e){
            e.printStackTrace();
        }*/

    }
    public static void printList(ArrayList<Person> list){
        double totalNum = list.size();
        double numOfMale = 0;
        double numOfFemale = 0;
        for(int i=0; i<list.size(); i++){
            System.out.println((i+1) + ". Cinsiyet : " + list.get(i).getSex() + " Yaş : " + list.get(i).getAge() + " " + list.get(i).getFullName()+ " TcNo : " + list.get(i).getNationalIdentificationNumber());
            if(list.get(i).getSex().toString().equals("MALE"))
                numOfMale++;
            else
                numOfFemale++;

        }

        System.out.println("Male rate : " + (numOfMale/totalNum*100));
        System.out.println("Female rate : " + (numOfFemale/totalNum*100));
    }

}

