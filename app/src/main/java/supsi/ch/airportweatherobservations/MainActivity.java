package supsi.ch.airportweatherobservations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import supsi.ch.airportweatherobservations.adapters.AirportAdapter;
import supsi.ch.airportweatherobservations.models.Airport;
import supsi.ch.airportweatherobservations.weatherObservationData.WeatherObservation;

public class MainActivity extends AppCompatActivity implements AirportAdapter.ItemClickListener {

    private AirportAdapter airportAdapter;
    private final List<Airport> airports = new ArrayList<>();
    private final Gson gson = new Gson();
    private Airport airportSelected;

    //Views
    private LinearLayout mainView;
    private ImageView weatherIcon;
    private TextView tvName;
    private TextView tvTemperature;
    private TextView tvCloudsValue;
    private TextView tvHumidityValue;
    private TextView tvWindValue;
    private TextView tvDateValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerNetworkCallback(this);

        mainView = findViewById(R.id.main_view);
        weatherIcon = findViewById(R.id.weather_icon);
        tvName = findViewById(R.id.tv_name);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvCloudsValue = findViewById(R.id.tv_clouds_value);
        tvHumidityValue = findViewById(R.id.tv_humidity_value);
        tvWindValue = findViewById(R.id.tv_wind_value);
        tvDateValue = findViewById(R.id.tv_date_value);

        airports.add(new Airport("Basel","LFSB"));
        airports.add(new Airport("Bern","LSZB"));
        airports.add(new Airport("Geneva","LSGG"));
        airports.add(new Airport("Locarno","LSZL"));
        airports.add(new Airport("Lugano","LSZA"));
        airports.add(new Airport("Samedan","LSZS"));
        airports.add(new Airport("St. Gallen","LSZR"));
        airports.add(new Airport("Sion","LSGS"));
        airports.add(new Airport("Zurich","LSZH"));

        RecyclerView recyclerView = findViewById(R.id.airports_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        airportAdapter = new AirportAdapter(this, airports);
        airportAdapter.setClickListener(this);
        recyclerView.setAdapter(airportAdapter);

        // set default airport
        airportSelected = airports.get(0);
        new ConnectToURLTask().execute("http://api.geonames.org/weatherIcaoJSON?ICAO="+airportSelected.getIcao()+"&username=supsi");
    }

    @Override
    public void onItemClick(View view, int position) {
        airportSelected = airportAdapter.getItem(position);
        if(airportSelected != null){
            Toast.makeText(this,airportSelected.getName() + " airport selected", Toast.LENGTH_SHORT).show();

            tvName.setText("Loading data...");
            tvTemperature.setText("...");
            tvCloudsValue.setText("...");
            tvHumidityValue.setText("...");
            tvWindValue.setText("...");
            tvDateValue.setText("...");

            if(airportSelected.needsRefresh()){
                new ConnectToURLTask().execute("http://api.geonames.org/weatherIcaoJSON?ICAO="+airportSelected.getIcao()+"&username=supsi");
            }else{
                refreshView();
            }
        }
    }

    public String convertStreamToString(InputStream is) throws IOException {
        InputStreamReader inp = new InputStreamReader(is,"UTF-8");
        BufferedReader br = new BufferedReader(inp);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    private class ConnectToURLTask extends AsyncTask<String, Void, String> {

        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    return convertStreamToString(connection.getInputStream());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if(result != null){
                JsonObject responseData = gson.fromJson(result,JsonObject.class);
                WeatherObservation weatherObservation = gson.fromJson(responseData.get("weatherObservation"), WeatherObservation.class);

                airportSelected.setTemperature(weatherObservation.getTemperature());
                airportSelected.setCloudsCode(weatherObservation.getCloudCode());
                airportSelected.setClouds(weatherObservation.getClouds());
                airportSelected.setHumidity(weatherObservation.getHumidity());
                airportSelected.setWindSpeed(weatherObservation.getWindSpeed());
                airportSelected.setDatetime(weatherObservation.getDatetime());
                airportSelected.setRequestTimestamp(new Date().getTime());

                refreshView();
            }
        }
    }

    private void refreshView(){
        mainView.setBackgroundResource(airportSelected.getBackground());
        weatherIcon.setImageResource(airportSelected.getIcon());

        tvName.setText(airportSelected.getName());
        tvTemperature.setText(airportSelected.getTemperature() + "°C");
        tvCloudsValue.setText(airportSelected.getClouds());
        tvHumidityValue.setText(airportSelected.getHumidity() + "%");
        tvWindValue.setText(airportSelected.getWindSpeed() + " km/h");
        tvDateValue.setText(airportSelected.getDatetime());
    }

    private void registerNetworkCallback(Context context) {
        final ConnectivityManager manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        manager.registerNetworkCallback(new NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build(), networkCallback);
    }

    private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            Log.d("WIFI", "Wifi is available");
        }

        @Override
        public void onLost(@NonNull Network network) {
            Log.d("WIFI", "Wifi is lost");
        }
    };
}