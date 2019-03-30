package sk.silvia.projects.iassesment.service;

public class Validation {

    public boolean validateInputInt(String input) {
        if(input.isEmpty()) return false;
        try {
            int num = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public boolean validateString(String input){
        if (input.isEmpty()) return false;
        return true;
    }

}
