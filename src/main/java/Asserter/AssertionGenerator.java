package Asserter;

import AssertionObjects.AssertionSet;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class AssertionGenerator {
    private static File jsonFile = new File(Paths.get("resources").toAbsolutePath().toString() + "/Assertions.json");
    private static AssertionGenerator assertion_instance = null;

    public static AssertionGenerator getInstance() {
        if(assertion_instance == null)
            assertion_instance = new AssertionGenerator();
        return assertion_instance;
    }

    public void runAssertionGenerator() throws Exception {
        if(jsonFile == null)
            throw new FileNotFoundException("JSON file is null");
        generateAsserts();
    }

    private void generateAsserts() throws FileNotFoundException {
        ArrayList<AssertionSet> parsedRaw = parseAssertionsRaw();
        System.out.println(parsedRaw);
    }

    /**
     * Set the json file required for generation assertions codes. Defaulted to Assertions json file under Generated folder.
     *
     * @param path
     */
    public void setCustomAssertionConfFile(String path) {
        if(path != null)
            jsonFile = new File(path);
        else
            System.out.println("Path is null. Default path is chosen instead.");
    }


    private ArrayList<AssertionSet> parseAssertionsRaw() throws FileNotFoundException {
        if(jsonFile != null) {
            try {
                return new ArrayList<>(Arrays.asList(new ObjectMapper().readValue(jsonFile, AssertionSet[].class)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new FileNotFoundException("JSON file not found");
    }
}
