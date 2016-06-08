package server;


import java.security.Timestamp;
import java.util.Date;

public class Outfit {

    private long outfit_id;
    private String timestamp;
    private String description;
    private String photo;
    private long style_id;

    public Outfit(long outfit_id, String timestamp, String description,String photo, long style_id) {
        this.outfit_id = outfit_id;
        this.timestamp = timestamp;
        this.photo = photo;
        this.description = description;
        this.style_id = style_id;
    }

    public Outfit(long outfit_id, String description, String photo, long style_id) {
        this.outfit_id = outfit_id;
        this.description = description;
        this.photo = photo;
        this.timestamp = new Timestamp(new Date(), null).toString();
        this.style_id = style_id;

    }

    public long getOutfit_id() {
        return outfit_id;
    }

    public void setOutfit_id(long outfit_id) {
        this.outfit_id = outfit_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getStyle_id() {
        return style_id;
    }

    public void setStyle_id(long style_id) {
        this.style_id = style_id;
    }

    //Cette méthode demande une requête vers l'association Likes (CE n'EST PAS UN CHAMP DE OUTFIT)
    //MAIS UNE RELATION N-N ENTRE USER ET OUTFIT


    public long getLikes() {
        //SELECT COUNT(*) FROM LIKES WHERE LIKES.outfit_id = ?
        //Un truc du genre
        return 50;
    }
}
