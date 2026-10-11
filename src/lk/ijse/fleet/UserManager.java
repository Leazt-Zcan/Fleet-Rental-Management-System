package lk.ijse.fleet;

import java.util.LinkedList;

public class UserManager {
    
    private LinkedList<SystemUser> users = new LinkedList<>();

    public UserManager(){}
    
    public UserManager(LinkedList<SystemUser> users) {
        this.users = users;
    }

    public LinkedList<SystemUser> getUsers() {
        return users;
    }

    public void setUsers(SystemUser user) {
        if(user != null){
            this.users.add(user);
        }
    }
    
    public SystemUser registerUser(String username, String password, String fullName){
        String u = findByUsername(username);
        
        if (u != null){
            SystemUser user = new SystemUser(u, password, fullName); 
            return user;
        }
        return null;
    }
    
    public SystemUser login(String username, String password){
        for (SystemUser user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }
    
    public String findByUsername(String username){
        for (SystemUser user : users) {
            if (user.getUsername().equals(username)){
                System.out.println("Username is already taken!");
                return null;
            }
        }
        return username;
    }
}
