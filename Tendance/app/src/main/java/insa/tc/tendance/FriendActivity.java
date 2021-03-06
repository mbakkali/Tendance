package insa.tc.tendance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import insa.tc.tendance.database.User;

import static android.widget.Toast.makeText;

public class FriendActivity extends Activity {

    ImageButton home;
    ImageButton calendar;
    ImageButton tshirt;
    ImageButton friend;
    ImageButton me;

    private User mUser;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend);

        mUser = User.getUserFromIntent(getIntent());

        home = (ImageButton) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(FriendActivity.this, ActualiteActivity.class);
                mUser.putUserIntoIntent(home);
                startActivity(home);
            }
        });

        calendar = (ImageButton) findViewById(R.id.calendar);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calendrier = new Intent(FriendActivity.this, CalendarActivity.class);
                mUser.putUserIntoIntent(calendrier);
                startActivity(calendrier);
            }
        });

        tshirt = (ImageButton) findViewById(R.id.tshirt);
        tshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tshirt = new Intent(FriendActivity.this, DressingActivity.class);
                mUser.putUserIntoIntent(tshirt);
                startActivity(tshirt);
            }
        });

        friend = (ImageButton) findViewById(R.id.friend);
        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent friend = new Intent(FriendActivity.this, FriendActivity.class);
                mUser.putUserIntoIntent(friend);
                startActivity(friend);
            }
        });

        me = (ImageButton) findViewById(R.id.me);
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent personnel = new Intent(FriendActivity.this, PersonnelActivity.class);
                mUser.putUserIntoIntent(personnel);
                startActivity(personnel);
            }
        });
        //TODO Barre de recherge, onChangeListener on Submit listener + appel fonction recherge amis avec le contenu du SearchView.
        //TODO Quand on clic sur un user, on ouvre une nouvelle activité avec le profil d'un ami (ses info,
        //TODO Recupérer les amis: ProfilPicture and Username, id_user

        EditText searchText = (EditText) findViewById(R.id.searchText);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                String input;
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    input= v.getText().toString();
                    //TODO: recherche amis avec le input dans la BDD
                    new HttpRequestGetSearch().execute(input);

                    Toast toast = makeText(getApplicationContext(), "chercher "+input,
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return true;
                }
                return false;
            }
        });

        //Peupler les amis avec le réseau
        new HttpRequestGetFriend().execute();


    }
    protected void onStart(){
        super.onStart();
    }
    public void afficherFriend(User... friends){
        for (final User friend: friends) {
            final LinearLayout mylayout = new LinearLayout(this);
            final Button myButton = new Button(this);
            final ImageView myPicture = new ImageView (this);

            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
            params1.gravity = Gravity.CENTER_HORIZONTAL;
            params1.setMargins(0,8,150,0);
            mylayout.setLayoutParams(params1);

            myPicture.setImageResource(R.drawable.fakepic);
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(200, 200);
            params2.setMargins(20,0,0,0);
            myPicture.setLayoutParams(params2);

            myButton.setText(friend.getUsername());
            myButton.setId((int) friend.getId_user());
            myButton.setAllCaps(false);
            myButton.setBackgroundColor(Color.TRANSPARENT);
            myButton.setTextSize(20);
            LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
            params3.gravity = Gravity.CENTER_HORIZONTAL;
            params3.setMargins(8,8,0,0);
            myButton.setLayoutParams(params3);
            final int id_ = myButton.getId();

            mylayout.addView(myPicture);
            mylayout.addView(myButton);

            LinearLayout layout = (LinearLayout) findViewById(R.id.layoutButton);
            layout.addView(mylayout);

            myButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent friendProfile = new Intent(FriendActivity.this, FriendProfile.class);
                    friendProfile.putExtra("user", new Gson().toJson(mUser));
                    friendProfile.putExtra("friend", new Gson().toJson(friend));
                    startActivity(friendProfile);
                }
            });
        }

    }

    public void afficherSearch(User... friends){
        for (final User friend: friends) {
            final LinearLayout mylayout = new LinearLayout(this);
            final Button myButton = new Button(this);
            final ImageView myPicture = new ImageView (this);

            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
            params1.gravity = Gravity.CENTER_HORIZONTAL;
            params1.setMargins(0,8,150,0);
            mylayout.setLayoutParams(params1);

            myPicture.setImageResource(R.drawable.fakepic);
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(200, 200);
            params2.setMargins(20,0,0,0);
            myPicture.setLayoutParams(params2);

            myButton.setText(friend.getUsername());
            myButton.setId((int) friend.getId_user());
            myButton.setAllCaps(false);
            myButton.setBackgroundColor(Color.TRANSPARENT);
            myButton.setTextSize(20);
            LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
            params3.gravity = Gravity.CENTER_HORIZONTAL;
            params3.setMargins(8,8,0,0);
            myButton.setLayoutParams(params3);
            final int id_ = myButton.getId();

            mylayout.addView(myPicture);
            mylayout.addView(myButton);

            LinearLayout layout = (LinearLayout) findViewById(R.id.layoutSearch);
            layout.removeView(mylayout);
            layout.addView(mylayout);

            myButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent friendProfile = new Intent(FriendActivity.this, FriendProfile.class);
                    friendProfile.putExtra("user", new Gson().toJson(mUser));
                    friendProfile.putExtra("friend", new Gson().toJson(friend));
                    startActivity(friendProfile);
                }
            });
        }

    }

    private class HttpRequestGetFriend extends AsyncTask<Void, Void, User[]> {
        @Override
        protected User[] doInBackground(Void... params) {
            try {
                final String url = MainActivity.SERVEUR_URL +"/user/" + mUser.getId_user()  +"/friends";
                final String url_local = "http://192.168.1.13:5000/user/friends?iduser=1"; //Pour quand patrik fais des test chez lui...
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
            if(users!=null)
                afficherFriend(users);
        }

    }

    private class HttpRequestGetSearch extends AsyncTask<String, Void, User[]> {
        @Override
        protected User[] doInBackground(String... params) {
            String [] input = params;
            System.out.println("on est dans getsearch input= "+input[0]);
            try {
                final String url = MainActivity.SERVEUR_URL + "/user?username=" + input[0]; //avoir la bonne adresse IP
                //final String url_local = "http://192.168.1.13:5000/user/friends?iduser=1"; //Pour quand patrik fais des test chez lui...
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
            if(users!=null) {
                afficherSearch(users);
            }
        }

    }
}
