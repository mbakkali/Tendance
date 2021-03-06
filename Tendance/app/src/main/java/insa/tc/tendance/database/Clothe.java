package insa.tc.tendance.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by patrik on 18/05/16.
 */
public class Clothe {
    private long id;
    private int type;
    private String s_type; //pour directement afficher le type
    private String pathphoto;
    private long owner;


    public Clothe(int type, long owner){
        this.type = type;
        this.owner = owner;
        String photo = "no_picture";
    }


    public Clothe(long id, String type, String photo, long owner){
        this.id = id;
        this.s_type = type;
        this.pathphoto = photo;
        this.owner = owner;
    }
    public Clothe(long id, int type, String photo, long owner){
        this.id = id;
        this.type = type;
        this.pathphoto = photo;
        this.owner = owner;
    }
    public Clothe(int owner){
        this.owner=owner;
    }

    public Clothe() {

    }


    public Drawable getImageFromWeb(){
        //From the web
        try {
            InputStream is = (InputStream) new URL(pathphoto).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    public void setPhoto(String photo) {
        this.pathphoto = photo;
    }
    public void setType(int type) {
        this.type = type;
    }


    public void addClothLocalDB(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("type", type);
        values.put("photo", pathphoto);
        values.put("owner", owner);
        id = db.insert("CLOTHES", null, values);
    }

    public Map<String, List<Clothe>> getMyClothRemote(Context context, final User me){
        Map<String, List<Clothe>> myclothes = new HashMap<>();

        //TODO REQUESTs
        return myclothes;
    }


    public List<Clothe> getMyCloth(User me, SQLiteDatabase db){
        List<Clothe> myclothes = new ArrayList<>();

        String[] projection = {
                "id_cloth",
                "type",
                "photo"
        };
        String selection = "owner LIKE ?";
        String[] selectionArgs = { String.valueOf(me.getId_user()) };
        try (Cursor c = db.query(
                "CLOTHES",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        )) {
            while (c.moveToNext()) {
                Clothe cloth = new Clothe(c.getLong(0), c.getInt(1), c.getString(2), me.getId_user());
                myclothes.add(cloth);
            }
        }
        return myclothes;
    }

    public void removeClothLocal(SQLiteDatabase db, Clothe target){
        //Cette méthode est immunisé aux injections SQL;
        String selection = "id_cloth LIKE ?";
        String[] selectionArgs = {String.valueOf(target.id)};
        db.delete("CLOTHES", selection, selectionArgs);
    }

    public long getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getS_type() {
        return s_type;
    }

    public String getPathphoto() {
        return pathphoto;
    }

    public long getOwner() {
        return owner;
    }

    public void putClotheIntoIntent(Intent intent){
        intent.putExtra("clothe", new Gson().toJson(this));
    }
    public static Clothe getClotheFromIntent(Intent intent){
        Gson gson = new Gson();
        return gson.fromJson(intent.getStringExtra("clothe"), Clothe.class);
    }

    public void setOwner(User user) {
        this.owner = user.getId_user();
    }
}
