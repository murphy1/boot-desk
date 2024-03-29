package com.murphy1.serviced.serviced.model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class EndUser extends User {

    @OneToMany
    private List<Issue> issue;

    public List<Issue> getIssue() {
        return issue;
    }

    public void setIssue(List<Issue> issue) {
        this.issue = issue;
    }
}
