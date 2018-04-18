package adapter;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class User {
    SimpleIntegerProperty no, id;
    SimpleStringProperty username, status;

    public User(Integer no, Integer id, String status, String username) {
        this.no       = new SimpleIntegerProperty(no);
        this.id       = new SimpleIntegerProperty(id);
        this.status   = new SimpleStringProperty(status);
        this.username = new SimpleStringProperty(username);
    }


    public Integer getNo() {
        return no.get();
    }

    public Integer getId() {
        return id.get();
    }

    public String getStatus() {
        return status.get();
    }

    public String getUsername() {
        return username.get();
    }
}