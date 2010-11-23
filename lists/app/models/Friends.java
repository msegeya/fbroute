package models;

import siena.Id;
import siena.Index;
import siena.Model;
import siena.Query;

public class Friends extends Model {

    @Id
    public Long id;

    public String email;
    public String friends;



    public Friends(String email, String friends) {

        this.email = email;
        this.friends = friends;
    }
    
    static Query<Friends> all() {
        return Model.all(Friends.class);
    }
    
    public static Friends findById(Long id) {
        return all().filter("id", id).get();
    }

    
}

