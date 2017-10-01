package guide.projects.vsquad.com.guide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.view.MaterialListView;
import com.squareup.picasso.RequestCreator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public String[][] special_offers = new String[25][9];
    public MaterialListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mListView = (MaterialListView) findViewById(R.id.material_listview);

        SpeciallOffersParser parser = new SpeciallOffersParser();
        parser.execute();

    }

    public void fillArray() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            cards.add(setSpecialOffers(i));
        }
        mListView.getAdapter().addAll(cards);
    }

    public Card setSpecialOffers(final int position) {
        /*
        special_offers[n][0] = 'id'
        special_offers[n][1] = 'Path to img'
        special_offers[n][2] = 'Place name'
        special_offers[n][3] = 'Hostel's stars'
        special_offers[n][4] = 'Date'
        special_offers[n][5] = 'Count of days/nights'
        special_offers[n][6] = 'Departure city'
        special_offers[n][7] = 'Price'
        special_offers[n][8] = 'Country, city of place'
        */

        StringBuilder description = new StringBuilder();
        description.append("Отель «" + special_offers[position][2]+"», "+special_offers[position][3]+" звезды.\n");
        description.append("Дата заезда: " + special_offers[position][4]+".\n");
        description.append("Вылет из города " + special_offers[position][6]+".\n");

        return new Card.Builder(this)
                .withProvider(new CardProvider())
                .setLayout(R.layout.material_image_with_buttons_card)

                .setTitle(special_offers[position][8])
                .setTitleGravity(Gravity.START)
                .setTitleColor(Color.WHITE)
                .setDescription(description.toString())
                .setDescriptionGravity(Gravity.START)
                .setDrawable("http://www.vipgeo.ru/"+special_offers[position][1])
                .setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
                    @Override
                    public void onImageConfigure(@NonNull RequestCreator requestCreator) {
                        requestCreator.fit().centerCrop();
                    }
                })
                .addAction(R.id.left_text_button, new TextViewAction(this)
                        .setText(special_offers[position][7] + " RUB")
                        .setTextResourceColor(R.color.black_button)
                )
                .addAction(R.id.right_text_button, new TextViewAction(this)
                        .setText("Подробнее")
                        .setTextResourceColor(R.color.orange_button)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse("http://www.vipgeo.ru/tour/"+special_offers[position][0]));
                                startActivity(i);
                            }
                        }))
                .endConfig()
                .build();
    }

    /*public String getPrice(int position){
        String price = "";
        if (positions_prices[position] == true) {
            price = special_offers[position][7] + " RUB";
            positions_prices[position] = false;
        } else {
            price = special_offers[position][7] + " GCO";
            positions_prices[position] = true;
        }
        return price;
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class SpeciallOffersParser extends AsyncTask {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(StudentActivity.this);
            dialog.setMessage("Загрузка...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                Document doc = Jsoup.connect("http://www.vipgeo.ru/hottours/?utm_source=main&utm_medium=hottours").get();

                Elements images = doc.select("div.tours-list__base-info__img");
                Elements names = doc.select("a.tours-list__base-info__content__name");
                Elements stars = doc.select("div.tours-list__base-info__content__footer__stars");
                Elements place = doc.select("div.tours-list__base-info__content__place");
                Elements prices = doc.select("div.tours-list__price__title");


        /*
        special_offers[n][0] = 'id'
        special_offers[n][1] = 'Path to img'
        special_offers[n][2] = 'Place name'
        special_offers[n][3] = 'Hostel's stars'
        special_offers[n][4] = 'Date'
        special_offers[n][5] = 'Count of days/nights'
        special_offers[n][6] = 'Departure city'
        special_offers[n][7] = 'Price'
        special_offers[n][8] = 'Country, city of place'
        */

                for (int i = 0; i < images.size(); i++) {

                    special_offers[i][0] = images.get(i).select("a").attr("href").replaceAll("/tour/", "").replaceAll("/", ""); // 591421835
                    special_offers[i][1] = images.get(i).select("img").attr("src"); // /uploads/ImageCache/8a8a7c0437d186e2619979f43258c644__cache_172_96.jpg
                    special_offers[i][2] = names.get(i).select("a").text(); // АНАПА-ПАТИО
                    special_offers[i][3] = stars.get(i).select("span").text(); // 2

                    String s = place.get(i).html().replaceAll("<span>на</span> \n", "").replaceAll("<span>из</span> \n", "").replaceAll("<span>,</span>", "");
                    String[] letters = s.split("");
                    StringBuilder sb = new StringBuilder();
                    byte find_num = 0;
                    for (int j = 0; j < letters.length; j++) {
                        sb.append(letters[j]);
                        if (sb.toString().endsWith("<span>")) {
                            sb.delete(0, sb.length());
                            j++;
                            while (!letters[j].startsWith("<")) {
                                sb.append(letters[j]);
                                j++;
                            }
                            switch (find_num) {
                                case 0:
                                    special_offers[i][8] = sb.toString(); // Россия
                                    break;
                                case 1:
                                    special_offers[i][8] = special_offers[i][8] + ", " + sb.toString(); // Россия, Анапа
                                    break;
                                case 2:
                                    special_offers[i][4] = sb.toString(); // 06.10
                                    break;
                                case 3:
                                    special_offers[i][5] = sb.toString(); // 5, позже добавляем описание, '5 чего?'
                                    break;
                                case 4:
                                    special_offers[i][5] = special_offers[i][5] + " " + sb.toString(); // 5 ночей
                                    break;
                                case 5:
                                    special_offers[i][6] = sb.toString(); // Москва
                                    break;
                            }
                            find_num++;
                            sb.delete(0, sb.length());
                        }
                    }

                    special_offers[i][7] = prices.get(i).text(); // 6 895

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fillArray();
                }
            });
            dialog.dismiss();
            return null;
        }

    }
}
