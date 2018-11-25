package Model;

import javafx.beans.property.SimpleStringProperty;

public class Subject {
    private SimpleStringProperty subCode;
    private SimpleStringProperty subName;
    private SimpleStringProperty creDit;
    private SimpleStringProperty hardness;
    private SimpleStringProperty year;

    public Subject(){
    }

    public Subject(String subCode, String subName, String creDit, String hardness,String year) {
        this.subCode = new SimpleStringProperty(subCode);
        this.subName = new SimpleStringProperty(subName);
        this.creDit = new SimpleStringProperty(creDit);
        this.hardness = new SimpleStringProperty(hardness);
        this.year = new SimpleStringProperty(year) ;
    }

    public String getSubCode() {
        return subCode.get();
    }

    public SimpleStringProperty subCodeProperty() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode.set(subCode);
    }

    public String getSubName() {
        return subName.get();
    }

    public SimpleStringProperty subNameProperty() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName.set(subName);
    }

    public String getCreDit() {
        return creDit.get();
    }

    public SimpleStringProperty creDitProperty() {
        return creDit;
    }

    public void setCreDit(String creDit) {
        this.creDit.set(creDit);
    }

    public String getHardness() {
        return hardness.get();
    }

    public SimpleStringProperty hardnessProperty() {
        return hardness;
    }

    public void setHardness(String hardness) {
        this.hardness.set(hardness);
    }

    public String getYear() {
        return year.get();
    }

    public SimpleStringProperty yearProperty() {
        return year;
    }

    public void setYear(String year) {
        this.year.set(year);
    }

    @Override
    public String toString() {
        return "Subject{" +
                "subCode='" + subCode + '\'' +
                ", subName='" + subName + '\'' +
                ", creDit=" + creDit +
                ", hardness=" + hardness +
                ", year='" + year + '\'' +
                '}';
    }
}
