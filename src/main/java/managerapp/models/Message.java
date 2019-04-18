package managerapp.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Message {

    @Id
    @GeneratedValue
    private int id;

    @NotNull(message = "Write your name")
    @Size(min = 3, max = 15, message = "Name must be 3 to 20 characters")
    private String author;

    @NotNull(message = "Title can not be empty")
    @Size(min = 3, max = 15, message = "Title name must be 3 to 20 characters")
    private String title;

    @NotNull
    @Size(min = 1, message = "Write a message")
    private String body;

    @ManyToOne
    private Category category;

    private String shift;

    private LocalDateTime dateUpdated;

    private boolean markDone;

    public Message(String author, String title, String body, String shift) {
        this.author = author;
        this.title = title;
        this.body = body;
        this.shift = shift;
    }
    public Message() { }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }

    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }

    public String getShift() {
        return shift;
    }
    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getDateUpdated() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
        return dateUpdated.format(formatter);
    }
    public void setDateUpdated(LocalDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public boolean isMarkDone() {
        return markDone;
    }
    public void setMarkDone(boolean markDone) {
        this.markDone = markDone;
    }
}
