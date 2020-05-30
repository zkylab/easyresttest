package DataGenerator;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.devskiller.jfairy.producer.person.PersonProperties;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class PersonData{
    private String name = "";
    private String gender = "";
    private int age;

    public PersonData(String name, String gender, int age){
        this.name = name;
        this.gender = gender;
        this.age = age;
    }
    public String getName() {
        return name;
    }
    public String getGender() {
        return gender;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setName(String name) {
        this.name = name;
    }
}
public class DataGenerator{
    private String pathName = "";
    private ArrayList<Person> list;

    public ArrayList<PersonData> specifiedJSON(){
        JSONParser parser = new JSONParser();
        String output = "";
        int total = 100;
        try {
            Object obj = parser.parse(new FileReader(pathName));
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
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(output);
        /* txt olarak yazma kısmı
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt"));
            writer.write(output);

            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
        ArrayList<PersonData> list = convertDataToPersonClass(output);
        return list;
    }
    public ArrayList<Object> primitiveJSON(){
        ArrayList<Object> list = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(pathName));
            JSONArray jsonArray = (JSONArray) obj;
            for(Object arrObj : jsonArray){
                JSONObject jsonObject =(JSONObject)arrObj;
                int length = -1;
                String type = "";
                JSONArray range = null;
                for (Object jsonKey : jsonObject.keySet()) {
                    switch (jsonKey.toString()) {
                        case "length":
                            length = Integer.parseInt(jsonObject.get(jsonKey).toString());
                            break;
                        case "range":
                            range = (JSONArray)jsonObject.get(jsonKey);
                            break;
                        case "type":
                            type = jsonObject.get(jsonKey).toString();
                            break;
                    }
                }
                switch (type){
                    case "Boolean":
                        list.add(generateBoolean());
                        break;
                    case "Byte":
                        list.add(generateByte(range,length));
                        break;
                    case "Character":
                        list.add(generateChar(range, length));
                        break;
                    case "Short":
                        list.add(generateShort(range, length));
                        break;
                    case "Integer":
                        list.add(generateInteger(range, length));
                        break;
                    case "Long":
                        list.add(generateLong(range, length));
                        break;
                    case "Float":
                        list.add(generateFloat(range, length));
                        break;
                    case "Double":
                        list.add(generateDouble(range,length));
                        break;
                    case "String":
                        list.add(generateString(range,length));
                        break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    private Boolean generateBoolean(){
        return new Random().nextBoolean();
    }
    private Integer generateInteger(JSONArray range, int length){
        Integer num = null;
        ArrayList<Integer> nums = new ArrayList<>();
        Random rnd = new Random();
        for(Object arrObj : range){
            String condition = "";
            String value = "";
            for(Object jsonKey : ((JSONObject)arrObj).keySet()){
                switch (jsonKey.toString()){
                    case "condition":
                        condition = ((JSONObject)arrObj).get(jsonKey).toString();
                        break;
                    case "value":
                        value = ((JSONObject)arrObj).get(jsonKey).toString();
                        break;
                }
            }
            int actualRange1 = (int)((Math.pow(10,length)-1)*-1);
            int actualRange2 = (int)Math.pow(10,length)-1;
            switch (condition){
                case "between":
                    int val1 = Integer.parseInt(value.substring(0,value.indexOf(" ")));
                    int val2 = Integer.parseInt(value.substring(value.lastIndexOf(" ")+1));
                    actualRange1 = val1;
                    actualRange2 = val2;
                    break;
                case "lessthan":
                    actualRange2 = Integer.parseInt(value);
                    break;
                case "morethan":
                    actualRange1 = Integer.parseInt(value);
                    break;
            }
            int temp1 = actualRange1;
            int temp2 = actualRange2;
            actualRange1 = (int)Math.min(temp1,temp2);
            actualRange2 = (int)Math.max(temp1,temp2);
            Integer number = rnd.nextInt(actualRange2-actualRange1) + actualRange1;
            nums.add(number);
        }
        return nums.get(rnd.nextInt(nums.size()));
    }
    private String generateString(JSONArray range, int length){
        ArrayList<String> strings = new ArrayList<>();
        Random rnd = new Random();
        FakeValuesService fk = new FakeValuesService(new Locale("en-US"), new RandomService());
        for(Object arrObj : range) {
            String condition = "";
            String value = "";
            for (Object jsonKey : ((JSONObject) arrObj).keySet()) {
                switch (jsonKey.toString()){
                    case "condition":
                        condition = ((JSONObject)arrObj).get(jsonKey).toString();
                        break;
                    case "value":
                        value = ((JSONObject)arrObj).get(jsonKey).toString();
                        break;
                }
            }
            StringBuilder lengthString = new StringBuilder();
            for(int i=0; i<length; i++){
                lengthString.append("?");
            }
            final String specialAlphabet = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
            switch (condition){
                case "UpperCase":
                    switch (value){
                        case "plain":
                            String plain = fk.letterify(lengthString.toString(),true);
                            strings.add(plain);
                            break;
                        case "special":
                            StringBuilder special = new StringBuilder();
                            for(int i=0; i<length; i++)
                                special.append(specialAlphabet.charAt(rnd.nextInt(specialAlphabet.length())));
                            strings.add(special.toString());
                            break;
                        case "all":
                            StringBuilder all = new StringBuilder();
                            for(int i=0; i<length; i++)
                                all.append((char) (rnd.nextInt(126 - 33) + 33));
                            strings.add(all.toString().toUpperCase());
                            break;
                    }
                    break;
                case "LowerCase" :
                    switch (value){
                        case "plain":
                            String plain = fk.letterify(lengthString.toString(),false);
                            strings.add(plain);
                            break;
                        case "special":
                            StringBuilder special = new StringBuilder();
                            for(int i=0; i<length; i++)
                                special.append(specialAlphabet.charAt(rnd.nextInt(specialAlphabet.length())));
                            strings.add(special.toString());
                            break;
                        case "all":
                            StringBuilder all = new StringBuilder();
                            for(int i=0; i<length; i++)
                                all.append((char) (rnd.nextInt(126 - 33) + 33));
                            strings.add(all.toString().toLowerCase());
                            break;
                    }
                    break;
                case "null":
                    switch (value){
                        case "plain":
                            String plain = fk.letterify(lengthString.toString());
                            strings.add(plain);
                            break;
                        case "special":
                            StringBuilder special = new StringBuilder();
                            for(int i=0; i<length; i++)
                                special.append(specialAlphabet.charAt(rnd.nextInt(specialAlphabet.length())));
                            strings.add(special.toString());
                            break;
                        case "all":
                            StringBuilder all = new StringBuilder();
                            for(int i=0; i<length; i++)
                                all.append((char) (rnd.nextInt(126 - 33) + 33));
                            strings.add(all.toString());
                            break;
                    }
                    break;
            }
        }
        return strings.get(rnd.nextInt(strings.size()));
    }
    private Double generateDouble(JSONArray range, int length) {
        ArrayList<Double> doubles = new ArrayList<>();
        Random rnd = new Random();
        for (Object arrObj : range) {
            String condition = "";
            String value = "";
            for (Object jsonKey : ((JSONObject) arrObj).keySet()) {
                switch (jsonKey.toString()) {
                    case "condition":
                        condition = ((JSONObject) arrObj).get(jsonKey).toString();
                        break;
                    case "value":
                        value = ((JSONObject) arrObj).get(jsonKey).toString();
                        break;
                }
            }
            double actualRange1 = (double)((Math.pow(10,length)-1)*-1);
            double actualRange2 = (double)Math.pow(10,length)-1;
            switch (condition){
                case "between":
                    double val1 = Double.parseDouble(value.substring(0,value.indexOf(" ")));
                    double val2 = Double.parseDouble(value.substring(value.lastIndexOf(" ")+1));
                    actualRange1 = val1;
                    actualRange2 = val2;
                    break;
                case "lessthan":
                    actualRange2 = Double.parseDouble(value);
                    break;
                case "morethan":
                    actualRange1 = Double.parseDouble(value);
                    break;
            }
            double temp1 = actualRange1;
            double temp2 = actualRange2;
            actualRange1 = (double)Math.min(temp1,temp2);
            actualRange2 = (double)Math.max(temp1,temp2);
            Double number = ThreadLocalRandom.current().nextDouble(actualRange1,actualRange2);
            String s = number + "";
            s = s.substring(0,length);
            number = Double.parseDouble(s);
            doubles.add(number);
        }
        return doubles.get(rnd.nextInt(doubles.size()));
    }
    private Float generateFloat(JSONArray range, int length){
        ArrayList<Float> floats = new ArrayList<>();
        Random rnd = new Random();
        for (Object arrObj : range) {
            String condition = "";
            String value = "";
            for (Object jsonKey : ((JSONObject) arrObj).keySet()) {
                switch (jsonKey.toString()) {
                    case "condition":
                        condition = ((JSONObject) arrObj).get(jsonKey).toString();
                        break;
                    case "value":
                        value = ((JSONObject) arrObj).get(jsonKey).toString();
                        break;
                }
            }
            float actualRange1 = (float)((Math.pow(10,length)-1)*-1);
            float actualRange2 = (float)Math.pow(10,length)-1;
            switch (condition){
                case "between":
                    float val1 = Float.parseFloat(value.substring(0,value.indexOf(" ")));
                    float val2 = Float.parseFloat(value.substring(value.lastIndexOf(" ")+1));
                    actualRange1 = val1;
                    actualRange2 = val2;
                    break;
                case "lessthan":
                    actualRange2 = Float.parseFloat(value);
                    break;
                case "morethan":
                    actualRange1 = Float.parseFloat(value);
                    break;
            }
            float temp1 = actualRange1;
            float temp2 = actualRange2;
            actualRange1 = (float)Math.min(temp1,temp2);
            actualRange2 = (float)Math.max(temp1,temp2);
            Float number = (float)ThreadLocalRandom.current().nextDouble(actualRange1,actualRange2);
            String s = number + "";
            s = s.substring(0,length);
            number = Float.parseFloat(s);
            floats.add(number);
        }
        return floats.get(rnd.nextInt(floats.size()));
    }
    private Character generateChar(JSONArray range, int length){
        ArrayList<Character> chars = new ArrayList<>();
        Random rnd = new Random();
        FakeValuesService fk = new FakeValuesService(new Locale("en-US"), new RandomService());
        for(Object arrObj : range) {
            String condition = "";
            String value = "";
            for (Object jsonKey : ((JSONObject) arrObj).keySet()) {
                switch (jsonKey.toString()) {
                    case "condition":
                        condition = ((JSONObject) arrObj).get(jsonKey).toString();
                        break;
                    case "value":
                        value = ((JSONObject) arrObj).get(jsonKey).toString();
                        break;
                }
            }
            String lengthString = "?";
            final String specialAlphabet = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
            switch (condition){
                case "UpperCase":
                    switch (value){
                        case "plain":
                            String plain = fk.letterify(lengthString.toString(),true);
                            chars.add(plain.charAt(0));
                            break;
                        case "special":
                            Character special = specialAlphabet.charAt(rnd.nextInt(specialAlphabet.length()));
                            chars.add(special);
                            break;
                        case "all":
                            Character all = (char) (rnd.nextInt(96 - 33) + 33);
                            chars.add(all);
                            break;
                    }
                    break;
                case "LowerCase" :
                    switch (value){
                        case "plain":
                            String plain = fk.letterify(lengthString.toString(),false);
                            chars.add(plain.charAt(0));
                            break;
                        case "special":
                            Character special = specialAlphabet.charAt(rnd.nextInt(specialAlphabet.length()));
                            chars.add(special);
                            break;
                        case "all":
                            Character all = (char) (rnd.nextInt(64 - 33) + 33);
                            chars.add(all);
                            all = (char) (rnd.nextInt(126 - 91) + 91);
                            chars.add(all);
                            break;
                    }
                    break;
                case "null":
                    switch (value){
                        case "plain":
                            String plain = fk.letterify(lengthString.toString(),true);
                            chars.add(plain.charAt(0));
                            break;
                        case "special":
                            Character special = specialAlphabet.charAt(rnd.nextInt(specialAlphabet.length()));
                            chars.add(special);
                            break;
                        case "all":
                            Character all = (char) (rnd.nextInt(126 - 33) + 33);
                            chars.add(all);
                            break;
                    }
            }
        }
        return chars.get(rnd.nextInt(chars.size()));
    }
    private Long generateLong(JSONArray range, int length){
        Long num = null;
        ArrayList<Long> nums = new ArrayList<>();
        Random rnd = new Random();
        for(Object arrObj : range){
            String condition = "";
            String value = "";
            for(Object jsonKey : ((JSONObject)arrObj).keySet()){
                switch (jsonKey.toString()){
                    case "condition":
                        condition = ((JSONObject)arrObj).get(jsonKey).toString();
                        break;
                    case "value":
                        value = ((JSONObject)arrObj).get(jsonKey).toString();
                        break;
                }
            }
            long actualRange1 = (long)((Math.pow(10,length)-1)*-1);
            long actualRange2 = (long)Math.pow(10,length)-1;
            switch (condition){
                case "between":
                    long val1 = Long.parseLong(value.substring(0,value.indexOf(" ")));
                    long val2 = Long.parseLong(value.substring(value.lastIndexOf(" ")+1));
                    actualRange1 = val1;
                    actualRange2 = val2;
                    break;
                case "lessthan":
                    actualRange2 = Long.parseLong(value);
                    break;
                case "morethan":
                    actualRange1 = Long.parseLong(value);
                    break;
            }
            long temp1 = actualRange1;
            long temp2 = actualRange2;
            actualRange1 = (long)Math.min(temp1,temp2);
            actualRange2 = (long)Math.max(temp1,temp2);
            Long number = ThreadLocalRandom.current().nextLong(actualRange1,actualRange2);
            nums.add(number);
        }
        return nums.get(rnd.nextInt(nums.size()));
    }
    private Byte generateByte(JSONArray range, int length){
        ArrayList<Byte> bytes = new ArrayList<>();
        Random rnd = new Random();
        for(Object arrObj : range) {
            String condition = "";
            String value = "";
            for (Object jsonKey : ((JSONObject) arrObj).keySet()) {
                switch (jsonKey.toString()) {
                    case "condition":
                        condition = ((JSONObject) arrObj).get(jsonKey).toString();
                        break;
                    case "value":
                        value = ((JSONObject) arrObj).get(jsonKey).toString();
                        break;
                }
            }
            byte actualRange1 = (byte)((Math.pow(10,length)-1)*-1);
            byte actualRange2 = (byte)(Math.pow(10,length)-1);
            switch (condition){
                case "between":
                    byte val1 = Byte.parseByte(value.substring(0,value.indexOf(" ")));
                    byte val2 = Byte.parseByte(value.substring(value.lastIndexOf(" ")+1));
                    actualRange1 = val1;
                    actualRange2 = val2;
                    break;
                case "lessthan":
                    actualRange2 = Byte.parseByte(value);
                    break;
                case "morethan":
                    actualRange1 = Byte.parseByte(value);
                    break;
            }
            byte temp1 = actualRange1;
            byte temp2 = actualRange2;
            actualRange1 = (byte)Math.min(temp1,temp2);
            actualRange2 = (byte)Math.max(temp1,temp2);
            Byte sayi = (byte)(rnd.nextInt(actualRange2-actualRange1) + actualRange1);
            bytes.add(sayi);
        }
        return bytes.get(rnd.nextInt(bytes.size()));
    }
    private Short generateShort(JSONArray range, int length){
        ArrayList<Short> shorts = new ArrayList<>();
        Random rnd = new Random();
        for(Object arrObj : range) {
            String condition = "";
            String value = "";
            for (Object jsonKey : ((JSONObject) arrObj).keySet()) {
                switch (jsonKey.toString()) {
                    case "condition":
                        condition = ((JSONObject) arrObj).get(jsonKey).toString();
                        break;
                    case "value":
                        value = ((JSONObject) arrObj).get(jsonKey).toString();
                        break;
                }
            }
            short actualRange1 = (short)((Math.pow(10,length)-1)*-1);
            short actualRange2 = (short)(Math.pow(10,length)-1);
            switch (condition){
                case "between":
                    short val1 = Short.parseShort(value.substring(0,value.indexOf(" ")));
                    short val2 = Short.parseShort(value.substring(value.lastIndexOf(" ")+1));
                    actualRange1 = val1;
                    actualRange2 = val2;
                    break;
                case "lessthan":
                    actualRange2 = Short.parseShort(value);
                    break;
                case "morethan":
                    actualRange1 = Short.parseShort(value);
                    break;
            }
            short temp1 = actualRange1;
            short temp2 = actualRange2;
            actualRange1 = (short)Math.min(temp1,temp2);
            actualRange2 = (short)Math.max(temp1,temp2);
            Short sayi = (short)(rnd.nextInt(actualRange2-actualRange1) + actualRange1);
            shorts.add(sayi);
        }
        return shorts.get(rnd.nextInt(shorts.size()));
    }
    private ArrayList<PersonData> convertDataToPersonClass(String output){
        ArrayList<PersonData> list = new ArrayList<>();
        while(output.contains("\n")){
            String firstName = "";
            String lastName = "";
            String gender = "";
            int age;

            firstName = output.substring(0,output.indexOf(" "));
            output = output.substring(output.indexOf(" ")+1);

            lastName = output.substring(0,output.indexOf(" "));
            output = output.substring(output.indexOf(" ")+1);

            gender = output.substring(0,output.indexOf(" "));
            output = output.substring(output.indexOf(" ")+1);

            age = Integer.parseInt(output.substring(0,output.indexOf("\n")));
            output = output.substring(output.indexOf("\n")+1);

            PersonData person = new PersonData(firstName + " " + lastName,gender,age);
            list.add(person);
        }
        return list;
    }
    private String generateDataWithoutInnerRatio(int total, String genType, int ratio){
        Fairy fairy = Fairy.create();
        String output = "";
        for(int i=0; i<total*ratio/100; i++){
            switch (genType){
                case "female":
                    Person personFemale = fairy.person(PersonProperties.female());
                    output += personFemale.getFullName() + " " + personFemale.getSex() + " " + personFemale.getAge() + "\n";
                    break;
                case "male":
                    Person personMale = fairy.person(PersonProperties.male());
                    output += personMale.getFullName() + " " + personMale.getSex() + " " + personMale.getAge() + "\n";
                    break;
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
                        switch (genType){
                            case "female":
                                Person personFemale  = fairy.person(PersonProperties.female());
                                int ageFemale = rnd.nextInt(val2-val1) + val1;
                                output += personFemale.getFullName() + " " + personFemale.getSex() + " " + ageFemale + "\n";
                                break;
                            case "male":
                                Person personMale  = fairy.person(PersonProperties.male());
                                int ageMale = rnd.nextInt(val2-val1) + val1;
                                output += personMale.getFullName() + " " + personMale.getSex() + " " + ageMale + "\n";
                                break;
                        }
                    }
                }

            }
        }
        return output;
    }

    public DataGenerator(String pathName){
        this.pathName = pathName;
    }

}

