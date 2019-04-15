package managerapp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue
    private int id;

    @NotNull(message = "Message name can not be empty")
    @Size(min = 3, max = 15, message = "Message name must be 3 to 15 characters")
    private String name;

    @ManyToMany
    private List<Message> messages = new ArrayList<>();

    public Menu() { }

    public Menu(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addItem(Message item) {
        messages.add(item);
    }

    public void removeItem(Message item) {
        messages.remove(item);
    }
}
