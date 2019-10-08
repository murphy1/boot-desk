package com.murphy1.serviced.serviced.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class ServiceRequest extends BaseEntity{

    private String name;

    @Lob
    private String description;

    @Enumerated(EnumType.ORDINAL)
    private Priority priority;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    private LocalDate dueDate;
    private String label;

    @Lob
    private String messages;

    @Transient
    private String newMessages;

    private String creator;
    private String assignedTo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getNewMessages() {
        return newMessages;
    }

    public void setNewMessages(String newMessages) {
        this.newMessages = newMessages;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}
