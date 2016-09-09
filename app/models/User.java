package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USER_TBL")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class User {
    @Id
    private String name;
    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<Group> groups = new HashSet<>();

    private String lat;

    private String lng;

    private String lastActionTime;

    public String getLastActionTime() {
        return lastActionTime;
    }

    public void setLastActionTime(String lastActionTime) {
        this.lastActionTime = lastActionTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
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
}