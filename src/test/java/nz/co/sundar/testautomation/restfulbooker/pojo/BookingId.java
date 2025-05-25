package nz.co.sundar.testautomation.restfulbooker.pojo;

public class BookingId {
    private int bookingid;

    public BookingId() {}

    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }

    @Override
    public String toString() {
        return "BookingId{" +
                "bookingid=" + bookingid +
                '}';
    }
}
