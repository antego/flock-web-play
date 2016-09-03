package models;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "ORDER_TBL")
public class Order {
    @Id
    private String id;

    private String description;

    private String lat;

    private String lng;

    private String courier;

    private String fromAdress;

    private String toAdress;

    private String subject;

    public String getCourier() {

        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getFromAdress() {
        return fromAdress;
    }

    public void setFromAdress(String fromAdress) {
        this.fromAdress = fromAdress;
    }

    public String getToAdress() {
        return toAdress;
    }

    public void setToAdress(String toAdress) {
        this.toAdress = toAdress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;

        Order order = (Order) o;

        if (id != null ? !id.equals(order.id) : order.id != null) return false;
        if (description != null ? !description.equals(order.description) : order.description != null) return false;
        if (lat != null ? !lat.equals(order.lat) : order.lat != null) return false;
        if (lng != null ? !lng.equals(order.lng) : order.lng != null) return false;
        if (courier != null ? !courier.equals(order.courier) : order.courier != null) return false;
        if (fromAdress != null ? !fromAdress.equals(order.fromAdress) : order.fromAdress != null) return false;
        if (toAdress != null ? !toAdress.equals(order.toAdress) : order.toAdress != null) return false;
        return subject != null ? subject.equals(order.subject) : order.subject == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lat != null ? lat.hashCode() : 0);
        result = 31 * result + (lng != null ? lng.hashCode() : 0);
        result = 31 * result + (courier != null ? courier.hashCode() : 0);
        result = 31 * result + (fromAdress != null ? fromAdress.hashCode() : 0);
        result = 31 * result + (toAdress != null ? toAdress.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        return result;
    }

}
