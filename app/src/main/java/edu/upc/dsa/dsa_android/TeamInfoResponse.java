package edu.upc.dsa.dsa_android;

import java.util.List;
import edu.upc.dsa.dsa_android.User;

public class TeamInfoResponse {
    private String team;
    private List<User> members;

    public TeamInfoResponse() {}

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}