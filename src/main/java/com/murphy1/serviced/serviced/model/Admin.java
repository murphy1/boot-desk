package com.murphy1.serviced.serviced.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
public class Admin extends User {

    @OneToMany
    private List<Issue> issue;

    @OneToOne(cascade = CascadeType.ALL)
    private Team team;

    public List<Issue> getIssue() {
        return issue;
    }

    public void setIssue(List<Issue> issues) {
        this.issue = issues;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
