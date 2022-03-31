package nhy.example.mad_project_2022;

import java.util.ArrayList;
import java.util.HashMap;

public class SchoolInfo {
    public static String NAME = "name";
    public static String ADDRESS = "address";
    public static String MOBILE = "mobile";
    public static String WEBSITE = "website";

    public static ArrayList<HashMap<String, String>> schoolList = new ArrayList<>();

    // Creates and add contact to contact list
    public static void addSchool(String name, String address, String mobile,String website) {
        // Create contact
        HashMap<String, String> school = new HashMap<>();
        school.put(NAME, name);
        school.put(ADDRESS, address);
        school.put(MOBILE, mobile);
        school.put(WEBSITE,website);
        // Add contact to contact list
        schoolList.add(school);
    }
}


