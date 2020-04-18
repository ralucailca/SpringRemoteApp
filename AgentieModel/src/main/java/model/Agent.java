package model;

import java.io.Serializable;

public class Agent extends Entity<Integer> implements Serializable {
    private String user;
    private String password;

    public Agent(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    public void setId(Integer integer) {
        super.setId(integer);
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id="+this.getId()+
                " user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
