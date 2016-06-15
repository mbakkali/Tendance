package server;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable{

    private long user_id;
    private String username;
    private String mail;
    private String profilpicture;
    private String age;
    private String bio;
    private boolean male;
    private boolean priv;
    private String phone;
    private String password;

    public static String ROOT = "users"+ File.separator +"profil"+ File.separator;

    public User(){
    }

    public User(String username, String mail, String password) {
        this.username = username;
        this.mail = mail;
        this.password = password;
    }

    public User(long user_id,
                String username,
                String mail,
                String profilpicture,
                String bio,
                boolean male,
                boolean priv,
                String phone,
                String age) {

        this.user_id = user_id;
        this.username = username;
        this.mail = mail; 
        this.profilpicture = profilpicture; 
        this.bio = bio; 
        this.male= male;
        this.priv=priv;
        this.age = age;
        this.phone = phone;
    }

    public User(String username, String mail, String profilpicture, String bio, boolean male, boolean priv, String phone, String age, String password) {
        this.username = username;
        this.mail = mail;
        this.profilpicture = profilpicture;
        this.bio = bio;
        this.male = male;
        this.priv = priv;
        this.phone = phone;
        this.password = password;
        this.age = age;
    }
    public User(long user_id, String username, String mail, String profilpicture, String bio, boolean male, boolean priv, String phone, String age, String password) {
        this.username = username;
        this.user_id = user_id;
        this.mail = mail;
        this.profilpicture = profilpicture;
        this.bio = bio;
        this.male = male;
        this.priv = priv;
        this.phone = phone;
        this.password = password;
        this.age = age;
    }

    public User(long user_id, String username, String profilpicture) {
        this.user_id = user_id;
        this.username = username;
        this.profilpicture = profilpicture;
    }


    public long getUser_id() {return user_id;}
    public String getUsername() {return username;}
    public String getMail() {return mail;}
    public String getProfilpicture(){return profilpicture;}
    public String getBio(){return bio;}
    public boolean isMale() {return male;}
    public boolean isPriv() {return priv;}
    public String getPhone() {return phone;}
    public String getPassword() {return password;}
    public String getAge() {return age;}

    public File retrieveProfilePicture(){
        File file;
        if(profilpicture != null) {
             file = new File(getProfilpicture());
        }else{
             file = new File(ROOT + UUID.randomUUID().toString());
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public void setUserId(long userId) {
        this.user_id = userId;
    }
}












