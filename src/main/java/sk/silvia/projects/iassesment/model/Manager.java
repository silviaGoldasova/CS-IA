package sk.silvia.projects.iassesment.model;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    private List<String> categoriesL;
    private int numberOfCategories;

    public Manager() {
        categoriesL = new ArrayList<String>();
        categoriesL.add("School");
        categoriesL.add("Work");
        categoriesL.add("Household");
        categoriesL.add("Free time");
        categoriesL.add("Other");
        numberOfCategories = categoriesL.size();
    }

    public List<String> getCategoriesL() {
        return categoriesL;
    }

    public void setCategoriesL(List<String> categoriesL) {
        this.categoriesL = categoriesL;
    }
}