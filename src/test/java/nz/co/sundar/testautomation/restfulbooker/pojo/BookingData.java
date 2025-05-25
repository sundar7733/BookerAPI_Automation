package nz.co.sundar.testautomation.restfulbooker.pojo;

public class BookingData {
    public String testcase;
    public String firstname;
    public String lastname;
    public int totalprice;
    public boolean depositpaid;
    public String checkin;
    public String checkout;
    public String additionalneeds;

    public BookingData(String testcase,String firstname, String lastname, int totalprice, boolean depositpaid,
                       String checkin, String checkout, String additionalneeds) {
        this.testcase = testcase;
        this.firstname = firstname;
        this.lastname = lastname;
        this.totalprice = totalprice;
        this.depositpaid = depositpaid;
        this.checkin = checkin;
        this.checkout = checkout;
        this.additionalneeds = additionalneeds;
   }

    public String getTestCase() { return testcase; }

    @Override
    public String toString() {
        return testcase;
    }
}
