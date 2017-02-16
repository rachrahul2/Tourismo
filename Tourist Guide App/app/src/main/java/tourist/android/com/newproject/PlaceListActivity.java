package tourist.android.com.newproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tourist.android.com.newproject.sql.Place;
import tourist.android.com.newproject.sql.PlaceAdapter;
import tourist.android.com.newproject.sql.PlaceDataSource;


public class PlaceListActivity extends AppCompatActivity {

    ListView lstPlaces;
    PlaceDataSource placeDataSource;
    private ProgressDialog progressDialog;
    PlaceAdapter placeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_places);

        // Init datasource
        placeDataSource = new PlaceDataSource(this);
        placeDataSource.open();

        placeAdapter = new PlaceAdapter(this, new ArrayList<Place>());

        lstPlaces = (ListView) findViewById(R.id.lstPlaces);
        lstPlaces.setAdapter(placeAdapter);

        lstPlaces.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Place place = (Place)placeAdapter.getItem(i);
                if(place != null) {
                    Intent intent = new Intent(PlaceListActivity.this, PlaceDetailActivity.class);
                    intent.putExtra("placeId", place);
                    startActivity(intent);
                }
            }
        });

        new PlaceThread().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        placeDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        placeDataSource.open();
    }

    class PlaceThread extends AsyncTask<String, Void, List<Place>> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(PlaceListActivity.this, null, "Loading...", true);
            super.onPreExecute();
        }

        @Override
        protected List<Place> doInBackground(String... params) {
            if(params != null && params.length == 3) {
                List<Place> places = placeDataSource.filterPlaces(params[0], params[1], params[2]);
                return places;
            } else {
                List<Place> places = placeDataSource.getAllPlaces();
                return places;
            }
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            if(places != null && !places.isEmpty()) {
                if(placeAdapter == null) {
                    placeAdapter = new PlaceAdapter(PlaceListActivity.this, places);
                } else {
                    placeAdapter.setPlaces(places);
                }

                placeAdapter.notifyDataSetChanged();
            }
            progressDialog.dismiss();
        }
    }
}
