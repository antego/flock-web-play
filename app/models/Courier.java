package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "COURIER_TBL")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Courier {
    @Id
    private String id;

    private String name;
    @OneToOne
    private Order order;

    private int successDeliveries;

    private boolean readyForOrder;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Courier)) return false;

        Courier courier = (Courier) o;

        if (successDeliveries != courier.successDeliveries) return false;
        if (readyForOrder != courier.readyForOrder) return false;
        if (id != null ? !id.equals(courier.id) : courier.id != null) return false;
        if (name != null ? !name.equals(courier.name) : courier.name != null) return false;
        return order != null ? order.equals(courier.order) : courier.order == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (order != null ? order.hashCode() : 0);
        result = 31 * result + successDeliveries;
        result = 31 * result + (readyForOrder ? 1 : 0);
        return result;
    }

    public int getSuccessDeliveries() {
        return successDeliveries;
    }

    public void setSuccessDeliveries(int successDeliveries) {
        this.successDeliveries = successDeliveries;
    }

    public boolean isReadyForOrder() {
        return readyForOrder;
    }

    public void setReadyForOrder(boolean readyForOrder) {
        this.readyForOrder = readyForOrder;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
