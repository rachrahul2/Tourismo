package tourist.android.com.newproject;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class DasboardActivity extends AppCompatActivity {

    Button btnCreatePlace, btnFindPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasboard);

        Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );

        btnCreatePlace = (Button)findViewById(R.id.btnCreatePlace);
        btnCreatePlace.setTypeface(font);

        btnFindPlace = (Button)findViewById(R.id.btnFindPlace);
        btnFindPlace.setTypeface(font);

        btnCreatePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show("Create");
            }
        });

        btnFindPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show("Find");
            }
        });
    }

    private void show(String screen) {
        // Show create function
        if(screen.equals("Create")) {
            Intent intent = new Intent(this, CreatePlacesActivity.class);
            intent.putExtra("data", "");
            startActivity(intent);
        } else { // Show find function
            Intent intent = new Intent(this, PlaceListActivity.class);
            intent.putExtra("data", "");
            startActivity(intent);
        }
    }
}
