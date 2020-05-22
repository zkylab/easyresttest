package DataGenerator;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.devskiller.jfairy.producer.person.PersonProperties;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.google.inject.internal.cglib.core.$MethodWrapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.naming.event.ObjectChangeListener;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class DataGenerator{
    private int total = -1;
    private double maleRate = -1;
    private double femaleRate = -1;

    private double maleRateNum = -1, maleMax = -1;
    private double femaleRateNum = -1, femaleMax = -1;

    private ArrayList<Person> list;


    public void readJSON(){
        JSONParser parser = new JSONParser();
        String output = "";
        int total = 100;
        try {
            Object obj = parser.parse(new FileReader("C:\\Users\\dogru\\IdeaProjects\\easyresttest\\src\\main\\sample-data\\dataGen.json"));
            String genType = "";
            JSONObject jsonObject = (JSONObject) obj;
            for(Object jsonKey : jsonObject.keySet()){
                if(jsonKey.toString().equals("genType"))
                    genType = jsonObject.get(jsonKey).toString();
                if(jsonKey.toString().equals("fields")){
                    HashMap fields = (HashMap)jsonObject.get(jsonKey);
                    for(Object fieldKey : fields.keySet()){
                        String type = fieldKey.toString();
                        HashMap typeHashMap = (HashMap)fields.get(fieldKey);
                        int ratio = Integer.parseInt(typeHashMap.get("ratio").toString());
                        if(typeHashMap.get("innerRatio") == null){
                            String out = generateDataWithoutInnerRatio(total,type,ratio);
                            output += out;
                        }
                        else{
                            String out =  generateDataWithInnerRatio(total,type,ratio,(HashMap)typeHashMap.get("innerRatio"));
                            output += out;
                        }
                    }
                }
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt"));
            writer.write(output);

            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String generateDataWithoutInnerRatio(int total, String genType, int ratio){
        Fairy fairy = Fairy.create();
        String output = "";
        for(int i=0; i<total*ratio/100; i++){
            if(genType.equals("female")){
                Person person = fairy.person(PersonProperties.female());

                output += person.getFullName() + " " + person.getSex() + " " + person.getAge() + "\n";
            }
            else{
                Person person = fairy.person(PersonProperties.male());
                output += person.getFullName() + " " + person.getSex() + " " + person.getAge() + "\n";
            }

        }
        return output;
    }
    private String generateDataWithInnerRatio(int total,String genType, int ratio, HashMap innerRatio){
        Random rnd = new Random();
        Fairy fairy = Fairy.create();
        String output  = "";
        String genType2 = innerRatio.get("genType").toString();
        HashMap fields = (HashMap)innerRatio.get("fields");

        for(Object key : fields.keySet()){
            HashMap map = (HashMap)fields.get(key);
            int innerRatioInt = Integer.parseInt(map.get("ratio").toString());
            ArrayList<JSONObject> list = (ArrayList)map.get("props");
            for(JSONObject obj : list){
                String cond = obj.get("condition").toString();
                String value = obj.get("value").toString();
                int val1 = Integer.parseInt(value.substring(0,value.indexOf("-")));
                int val2 = Integer.parseInt(value.substring(value.indexOf("-")+1));
                if(cond.equals("between")){
                    int size = (total*ratio/100)*innerRatioInt/100/list.size();
                    for(int i=0; i<(total*ratio/100)*innerRatioInt/100/list.size(); i++){
                        if(genType.equals("female")){
                            Person person  = fairy.person(PersonProperties.female());
                            int age = rnd.nextInt(val2-val1) + val1;
                            output += person.getFullName() + " " + person.getSex() + " " + age + "\n";
                        }
                        else{
                            Person person  = fairy.person(PersonProperties.male());
                            int age = rnd.nextInt(val2-val1) + val1;
                            output += person.getFullName() + " " + person.getSex() + " " + age + "\n";
                        }


                    }
                }

            }
        }
        return output;
    }

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

    public <E> ArrayList<E> generateWithClass(Class className, int num) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Constructor[] cons = className.getConstructors();
        Method[] methods = className.getMethods();
        ArrayList<E> list = new ArrayList<>(num);
        for(int i=0; i<num; i++){
            E obj = null;
            for(Constructor con : cons){
                Object[] params = generateParams(con.getParameterTypes());
                obj =  (E) con.newInstance(params);

            }
            list.add(obj);
        }



        return list;
    }
    private Object[] generateParams(Class[] paramList){
        int i = 0;
        Random rnd = new Random();
        Object[] params = new Object[paramList.length];
        for(Class param : paramList){
            String paramName = param.getName();
            if(paramName.equals("boolean")){
                params[i] = rnd.nextBoolean();
            }
            else if(paramName.equals("char")){
                int num = rnd.nextInt(93) + 33;
                Character charac = (char)num;
                params[i] = charac;
             }
            else if(paramName.equals("byte")){
                int num = rnd.nextInt(255) -128;
                Byte byt = (byte)num;
                params[i] = byt;
            }
            else if(paramName.equals("short")){
                int num = rnd.nextInt(65553) - 32768;
                Short sh = (short)num;
                params[i] = sh;
            }
            else if(paramName.equals("int")){
                params[i] = rnd.nextInt();
            }
            else if(paramName.equals("long")){
                params[i] = rnd.nextLong();
            }
            else if(paramName.equals("float")){
                params[i] = rnd.nextFloat();
            }
            else if(paramName.equals("double")){
                params[i] = rnd.nextDouble();
            }
            else if(paramName.equals("java.lang.String")){
                FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-GB"), new RandomService());
                String s = fakeValuesService.letterify("?????");
                params[i] = s;
            }
            else{
                params[i] = null;
            }
            i++;
        }
        return params;
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

