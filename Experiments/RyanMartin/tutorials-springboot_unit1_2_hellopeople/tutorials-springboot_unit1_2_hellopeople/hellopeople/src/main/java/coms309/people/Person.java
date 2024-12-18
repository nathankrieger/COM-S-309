package coms309.people;


/**
 * Provides the Definition/Structure for the people row
 *
 * @author Vivek Bengre
 */

public class Person {

    private String firstName;

    private String lastName;

    private String address;

    private String telephone;

    private String SSN;

    public Person(){
        
    }

    public Person(String firstName, String lastName, String address, String telephone, String SSN){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.telephone = telephone;
        this.SSN = SSN;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSSN() {return this.SSN = SSN;}

    public void setSSN(String SSN) {this.SSN = SSN;}

    @Override
    public String toString() {
        return firstName + " " 
               + lastName + " "
               + address + " "
               + telephone + " "
                + SSN;
    }
}
