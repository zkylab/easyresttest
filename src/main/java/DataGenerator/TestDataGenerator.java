package DataGenerator;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.devskiller.jfairy.producer.person.PersonProperties;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class TestDataGenerator {
    public static void main(String[] args) {
        DataGenerator datagen1 = new DataGenerator("C:\\Users\\dogru\\IdeaProjects\\easyresttest\\src\\main\\sample-data\\dataGen.json");
        datagen1.specifiedJSON();

        DataGenerator datagen2 = new DataGenerator("C:\\Users\\dogru\\IdeaProjects\\easyresttest\\resources\\generator_primitive.json");

        ArrayList<Object> list = datagen2.primitiveJSON();
        System.out.println(list);

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

