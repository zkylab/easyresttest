package Asserter;

import AssertionObjects.AssertionSet;
import DataManager.DataManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class AssertionParser {
    private static File jsonFile = new File(Paths.get("resources").toAbsolutePath().toString() + "/Assertions.json");
    private static AssertionParser assertion_instance = null;

    public static AssertionParser getInstance() {
        if(assertion_instance == null)
            assertion_instance = new AssertionParser();
        return assertion_instance;
    }

    public void runAssertionGenerator() throws Exception {
        if(jsonFile == null)
            throw new FileNotFoundException("JSON file is null");
        DataManager dataManager = DataManager.getInstance();
        dataManager.setAsserts(parseAsserts());
        System.out.println("Assertions parsed.");
    }

    private ArrayList<AssertionSet> parseAsserts() throws FileNotFoundException {
        if(jsonFile != null) {
            try {
                return new ArrayList<>(Arrays.asList(new ObjectMapper().readValue(jsonFile, AssertionSet[].class)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new FileNotFoundException("JSON file not found");
    }

    /**
     * Set the json file required for generation assertions codes. Defaulted to Assertions json file under Generated folder.
     * Must set before parse method to actually matter.
     *
     * @param path
     */
    public void setCustomAssertionConfFile(String path) {
        if(path != null)
            jsonFile = new File(path);
        else
            System.out.println("Path is null. Default path is chosen instead.");
    }
}
