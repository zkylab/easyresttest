package DataGenerator;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.devskiller.jfairy.producer.person.PersonProperties;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class DataGenerator{
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

    private String getRandomNationalIdentificationNumber(){
        Random rnd = new Random();
        String tcNo = "";
        String firstNum = rnd.nextInt(9) + 1 + "";
        tcNo += firstNum;
        for(int i=0; i<8; i++){
            tcNo +=  rnd.nextInt(10) + "";
        }
        String tenthNum = ((Integer.parseInt(tcNo.charAt(0)+"") +
                Integer.parseInt(tcNo.charAt(2)+"") +
                Integer.parseInt(tcNo.charAt(4)+"") +
                Integer.parseInt(tcNo.charAt(6)+"") +
                Integer.parseInt(tcNo.charAt(8)+""))*7 -
                (Integer.parseInt(tcNo.charAt(1)+"") +
                        Integer.parseInt(tcNo.charAt(3)+"") +
                        Integer.parseInt(tcNo.charAt(5)+"") +
                        Integer.parseInt(tcNo.charAt(7)+""))) % 10 + "";
        tcNo += tenthNum;
        String eleventhNum = (Integer.parseInt(tcNo.charAt(0)+"") +
                Integer.parseInt(tcNo.charAt(1)+"") +
                Integer.parseInt(tcNo.charAt(2)+"") +
                Integer.parseInt(tcNo.charAt(3)+"") +
                Integer.parseInt(tcNo.charAt(4)+"") +
                Integer.parseInt(tcNo.charAt(5)+"") +
                Integer.parseInt(tcNo.charAt(6)+"") +
                Integer.parseInt(tcNo.charAt(7)+"") +
                Integer.parseInt(tcNo.charAt(8)+"") +
                Integer.parseInt(tcNo.charAt(9)+""))  % 10+ "";

        tcNo += eleventhNum;
        return tcNo;
    }

    public void generateDataWithRegex(String regex, int number,String pathName) {
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-US"), new RandomService());
        File file = new File(pathName);
        try(BufferedWriter br = new BufferedWriter(new FileWriter(file))){
            for(int i=0; i<number; i++){
                String reg = fakeValuesService.regexify(regex);
                br.write(reg);
                br.newLine();
            }
        } catch (IOException e) {
            System.out.println("Unable to read file " +file.toString());
        }
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
            Person person = fairy.person(PersonProperties.maxAge((int)maleMax),PersonProperties.male(),PersonProperties.withNationalIdentificationNumber(getRandomNationalIdentificationNumber()));
            list.add(person);
        }
        for(int i=0; i<secondPartOfMale; i++){
            Person person = fairy.person(PersonProperties.male(),PersonProperties.withNationalIdentificationNumber(getRandomNationalIdentificationNumber()));
            list.add(person);
        }

        for(int i=0; i<firstPartOfFemale; i++){
            Person person = fairy.person(PersonProperties.maxAge((int)femaleMax),PersonProperties.female(),PersonProperties.withNationalIdentificationNumber(getRandomNationalIdentificationNumber()));
            list.add(person);
        }
        for(int i=0; i<secondPartOfFemale; i++){
            Person person = fairy.person(PersonProperties.female(),PersonProperties.withNationalIdentificationNumber(getRandomNationalIdentificationNumber()));
            list.add(person);
        }

        return list;
    }

}

