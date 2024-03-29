package com.murphy1.serviced.serviced.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    private Admin teamLeader;

    @OneToMany
    private List<Agent> teamMembers;

    @Transient
    private String addMember;

    @Transient
    private String removeMember;

    private Long target;

    private Long progressToTarget;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Admin getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(Admin teamLeader) {
        this.teamLeader = teamLeader;
    }

    public List<Agent> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<Agent> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public String getAddMember() {
        return addMember;
    }

    public void setAddMember(String addMember) {
        this.addMember = addMember;
    }

    public String getRemoveMember() {
        return removeMember;
    }

    public void setRemoveMember(String removeMember) {
        this.removeMember = removeMember;
    }

    public Long getTarget() {
        return target;
    }

    public void setTarget(Long target) {
        this.target = target;
    }

    public Long getProgressToTarget() {
        return progressToTarget;
    }

    public void setProgressToTarget(Long progressToTarget) {
        this.progressToTarget = progressToTarget;
    }
}
