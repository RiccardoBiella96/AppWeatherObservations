package supsi.ch.airportweatherobservations.models;

import java.util.Date;

import supsi.ch.airportweatherobservations.R;

public class Airport {

    // 30 minuts
    private final int EXPIRATION_TIME = 1800000;
    private Long requestTimestamp;

    private final String name;
    private final String icao;

    private String temperature;
    private String clouds;
    private String cloudsCode;
    private int humidity;
    private String windSpeed;
    private String datetime;


    public Airport(String name, String icao) {
        this.name = name;
        this.icao = icao;
    }

    public boolean needsRefresh(){
        if((requestTimestamp == null)||((new Date().getTime()-requestTimestamp) > EXPIRATION_TIME)){
            return true;
        }
        return false;
    }

    public int getIcon(){
        if(cloudsCode != null){
            switch (cloudsCode) {
                case "FEW":
                    return R.drawable.few_clouds_d;
                case "SCT":
                case "BKN":
                    return R.drawable.broken_cloud;
                case "OVC":
                    return R.drawable.cloud_d;
            }
        }
        return R.drawable.clear_d;
    }

    public int getBackground(){
        if(cloudsCode != null){
            switch (cloudsCode){
                case "FEW":
                    return R.drawable.back_few_clouds_d;
                case "SCT":
                case "BKN":
                case "OVC":
                    return R.drawable.back_cloud_d;
            }
        }
        return R.drawable.back_sun_d;
    }

    public String getName() {
        return name;
    }

    public String getIcao() {
        return icao;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getClouds() {
        return clouds;
    }

    public void setClouds(String clouds) {
        this.clouds = clouds;
    }

    public String getCloudsCode() {
        return cloudsCode;
    }

    public void setCloudsCode(String cloudsCode) {
        this.cloudsCode = cloudsCode;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Long getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(Long requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }
}
