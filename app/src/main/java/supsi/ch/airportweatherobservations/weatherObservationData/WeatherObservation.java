package supsi.ch.airportweatherobservations.weatherObservationData;

public class WeatherObservation {

    private int elevation;
    private double lng;
    private String observation;
    private String ICAO;
    private String clouds;
    private int dewPoint;
    private String cloudsCode;
    private String datetime;
    private String countryCode;
    private String temperature;
    private int humidity;
    private String stationName;
    private String weatherCondition;
    private int windDirection;
    private int hectoPascAltimeter;
    private String windSpeed;
    private double lat;

    public int getElevation() {
        return elevation;
    }

    public double getLng() {
        return lng;
    }

    public String getObservation() {
        return observation;
    }

    public String getICAO() {
        return ICAO;
    }

    public String getClouds() {
        return clouds;
    }

    public int getDewPoint() {
        return dewPoint;
    }

    public String getCloudCode() {
        return cloudsCode;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public String getStationName() {
        return stationName;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public int getHectoPascAltimeter() {
        return hectoPascAltimeter;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public double getLat() {
        return lat;
    }
}
