package model;

public class Outsourced extends Part {
    private String companyName;


    /**
     * This method is a constructor that takes in Outsourced data with a company name.
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param companyName
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName){
        super(id, name, price, stock, min, max);
        this.companyName = companyName;

    }

    /**
     * This method sets the company name.
     * @param companyName sets the company name
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * This method gets the company name.
     * @return Returns the company name
     */
    public String getCompanyName() {
        return companyName;
    }

}
