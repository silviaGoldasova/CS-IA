package sk.silvia.projects.IAssesment1.model;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    public List<String> categoriesL = new ArrayList<>();
    int numberOfCategories;

    public Manager() {
        categoriesL = new ArrayList<>();
        categoriesL.add("School");
        categoriesL.add("Work");
        categoriesL.add("Household");
        categoriesL.add("Free time");
        categoriesL.add("Other");
        numberOfCategories = 5;
    }

}