package github.vege19.popnow.Models.Credits;

import java.io.Serializable;

public class Person implements Serializable {

    private String birthday;
    private String known_for_department;
    private String deathday;
    private int id;
    private String name;
    private String[] also_known_as;
    private int gender;
    private String biography;
    private float popularity;
    private String place_of_birth;
    private String profile_path;
    private boolean adult;

    public String getBirthday() {
        return birthday;
    }

    public String getKnown_for_department() {
        return known_for_department;
    }

    public String getDeathday() {
        return deathday;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String[] getAlso_known_as() {
        return also_known_as;
    }

    public int getGender() {
        return gender;
    }

    public String getBiography() {
        return biography;
    }

    public float getPopularity() {
        return popularity;
    }

    public String getPlace_of_birth() {
        return place_of_birth;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public boolean isAdult() {
        return adult;
    }
}
