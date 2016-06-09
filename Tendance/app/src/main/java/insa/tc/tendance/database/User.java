package insa.tc.tendance.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by patrik on 18/05/16.
 */
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


    public User(){
    }

    public User(String mail, String username, String password) {
        this.mail = mail;
        this.username = username;
        this.password = password;
    }



    public User(String username, String mail, String profilpicture, String age, String bio, boolean male, boolean priv, String phone) {
        this.username = username;
        this.mail = mail;
        this.profilpicture = profilpicture;
        this.age = age;
        this.bio = bio;
        this.male = male;
        this.priv = priv;
        this.phone = phone;
    }

    public User(String username, String mail, String profilpicture, boolean priv, String bio, boolean male, String phone){
        this.username = username;
        this.mail = mail;
        this.bio = bio;
        this.male = male;
        this.priv = priv;
        this.phone = phone;
        this.profilpicture = profilpicture;
    }

    public static User getMyProfil(SQLiteDatabase db, String email){

        //On récupère les infos de l'utilisateur après authentification
        String[] projection = {
                "user_id",
                "username",
                "mail",
                "profilpicture",
                "bio",
                "male",
                "priv",
                "phone"
        };
        String selection = "mail LIKE ?";
        String[] selectionArgs = { email };
        Cursor c = db.query(
                "USERS",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        c.moveToNext();
        User me = new User(c.getString(1),c.getString(2),"null",c.getInt(6)==1 ,c.getString(4), c.getInt(5)==1, c.getString(7) );
        me.setUser_id(c.getLong(0));
        c.close();
        return me;

    }
    public long getId_user(){
        return user_id;
    }

    public String getBio() {return bio;}
    public boolean isMale() {return male;}
    public boolean isPriv() {return priv;}
    public String getPhone() {return phone;}
    public String getMail() {return mail;}
    public String getUsername() {return username;}
    public String getAge() {return age;}

    private void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getProfilpicture() {        return profilpicture;    }

    public void addUserLocal(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("username", this.username);
        values.put("mail", this.mail);
        values.put("profilpicture", this.profilpicture);
        values.put("bio", this.bio);
        values.put("male", this.male);
        values.put("priv", this.priv);
        values.put("phone", this.phone);
        setUser_id(db.insert("USERS", null, values));
        System.out.println("A user"+ getId_user());
    }

    public void updateUserLocal(SQLiteDatabase db, User modifiedUser){
        ContentValues values = new ContentValues();
        String[] args = {
                String.valueOf(getId_user())
        };
        values.put("username", modifiedUser.getUsername());
        values.put("mail", modifiedUser.getMail());
        //values.put("profil_picture", modifiedUser.profilpicture);
        values.put("bio", modifiedUser.getBio());
        values.put("male", modifiedUser.isMale());
        values.put("priv", modifiedUser.isPriv());
        values.put("phone", modifiedUser.getPhone());
        int result =db.update("USERS", values, "user_id=?", args);
        System.out.println( getId_user() + " Updated..."+ result);
    }

    public ArrayList<Outfit> getFavoriteOutfits(Context context){


        return null;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", mail='" + mail + '\'' +
                ", profilpicture='" + profilpicture + '\'' +
                ", age='" + age + '\'' +
                ", bio='" + bio + '\'' +
                ", male=" + male +
                ", priv=" + priv +
                ", phone='" + phone + '\'' +
                '}';
    }

    public boolean isFriendWith(User target) {
        boolean friend = true;



        return friend;
    }


    private class HttpRequestTask extends AsyncTask<Void, Void, User[]> {
        @Override
        protected User[] doInBackground(Void... params) {
            try {
                final String path = "/user/friends";
                final String url = "http://90.66.114.198?"+path+"?iduser=1";
                final String url_local = "http://192.168.1.13:5000" + path + "?iduser=1"; //Pour quand patrik fais des test chez lui...
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                User[] users = restTemplate.getForObject(url, User[].class);
                return users;

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        protected void onPostExecute(User... users) {
        }

    }

    public static class HttpRequestCreateProfil extends AsyncTask<User, Void, User> {

        @Override
        protected User doInBackground(User... params) {
            User user = null;
            try{
                final  String path = "/user/add";
                final String url = "http://90.66.114.198" + path;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                user = restTemplate.postForObject(url,params[0],User.class);

            }catch (Exception e){
                Log.e("MainActivity", e.getMessage(), e);
            }
            return user;
        }

    }
}
