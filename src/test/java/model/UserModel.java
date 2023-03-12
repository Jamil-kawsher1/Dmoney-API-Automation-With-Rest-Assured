package model;

public class UserModel {
    public String email;
    public String password;
    public String name, phone_number, nid, role;


    public UserModel (String email, String password) {
        this.email = email;
        this.password = password;

    }

    public UserModel (String name,String email, String password, String phone_number, String nid, String role ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone_number = phone_number;
        this.nid = nid;
        this.role = role;
    }


}
