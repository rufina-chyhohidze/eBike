package be.kdg.integration4.domain;

abstract class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private UserRoles userRoles;

    public User() {}

    public User(int id, String name, String email, String password, UserRoles userRoles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRoles = userRoles;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public UserRoles getUserRoles() {return userRoles;}
    public void setUserRoles(UserRoles userRoles) {this.userRoles = userRoles;}

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userRoles=" + userRoles +
                '}';
    }
}
