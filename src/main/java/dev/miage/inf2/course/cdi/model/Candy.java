package dev.miage.inf2.course.cdi.model;

public class Candy {
    private String flavor;
    private double weight;

    public Candy(String flavor, double weight) {
        this.flavor = flavor;
        this.weight = weight;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    //... autres méthodes éventuelles (equals, hashCode, toString, etc.)
}
