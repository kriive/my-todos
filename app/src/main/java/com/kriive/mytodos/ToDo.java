package com.kriive.mytodos;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class ToDo implements Serializable {
    private String name;
    private String details;
    private String category;
    private Date dueDate;
    private Date createdOn;
    private Integer id = new Random().nextInt();

    public Date getCreatedOn() {
        return createdOn;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    private Boolean completed = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public ToDo(final String name, final String details, final String category, final Date dueDate) {
        this.name = name;
        this.details = details;
        this.category = category;
        this.dueDate = dueDate;
        this.createdOn = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ToDo)) return false;
        ToDo toDo = (ToDo) o;
        return getName().equals(toDo.getName()) &&
                Objects.equals(getDetails(), toDo.getDetails()) &&
                Objects.equals(getCategory(), toDo.getCategory()) &&
                Objects.equals(getDueDate(), toDo.getDueDate()) &&
                getCreatedOn().equals(toDo.getCreatedOn()) &&
                id.equals(toDo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDetails(), getCategory(), getDueDate(), getCreatedOn(), id);
    }
}
