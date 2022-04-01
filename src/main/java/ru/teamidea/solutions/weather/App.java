package ru.teamidea.solutions.weather;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class.getName());
    private final Properties cfg = new Properties();
    private JSONObject document;

    public App() {
        init();
    }

    private void cfg() {
        try (InputStream in = App.class.getClassLoader()
                .getResourceAsStream("app.properties")) {
            cfg.load(in);
        } catch (IOException e) {
            LOG.error("Impossible to read properties file.", e);
        }
    }

    private void init() {
        cfg();
        String url = String.format("https://api.openweathermap.org/data/2.5/onecall?lat=%s&lon=%s&exclude=minutely,hourly,alerts,current&units=metric&appid=%s",
                cfg.get("lat"), cfg.get("lon"), cfg.get("key"));
        try {
            this.document = (JSONObject) new JSONParser().parse(Jsoup.connect(url)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .followRedirects(true)
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .execute()
                    .body());
        } catch (IOException | ParseException e) {
            LOG.error("Impossible to connect url or parse json.", e);
        }
    }

    private void print(double minTempDiff, long maxDaylight, Date daylightDate, Date tempDate) {
        DateFormat timeForm = new SimpleDateFormat("HH:mm:ss");
        DateFormat dateForm = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.ENGLISH);
        timeForm.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateForm.setTimeZone(TimeZone.getTimeZone("UTC"));
        StringBuilder builder = new StringBuilder()
                .append(String.format("Information for the next 5 days (including the current one) for the city of %s:", cfg.get("city")))
                .append(System.lineSeparator())
                .append(String.format("1. Minimum difference between \"feels like\" and actual temperature at night: %.2f C° on %s%n", minTempDiff, dateForm.format(tempDate)))
                .append(String.format("2. Maximum daylight hours: %s on %s", timeForm.format(maxDaylight), dateForm.format(daylightDate)));
        System.out.println(builder);
    }

    public void run() {
        JSONArray days = (JSONArray) document.get("daily");
        double minTempDiff = 1000;
        long maxDaylight = 0;
        Date daylightDate = null;
        Date tempDate = null;
        for (int i = 0; i < 5; i++) {
            JSONObject day = (JSONObject) days.get(i);
            Date currentDate = new Date((Long) day.get("dt") * 1000);
            JSONObject tempJS = (JSONObject) day.get("temp");
            JSONObject feelsLikeJS = (JSONObject) day.get("feels_like");
            double temp = (Double) tempJS.get("night");
            double feelsLike = (Double) feelsLikeJS.get("night");
            double diff = Math.abs(feelsLike - temp);
            double oldDbl = minTempDiff;
            minTempDiff = Math.min(minTempDiff, diff);
            tempDate = minTempDiff == oldDbl ? tempDate : currentDate;
            long diffInMs = Math.abs((Long) day.get("sunrise") * 1000 - (Long) day.get("sunset") * 1000);
            long daylight = TimeUnit.MILLISECONDS.convert(diffInMs, TimeUnit.MILLISECONDS);
            long oldLng = maxDaylight;
            maxDaylight = Math.max(daylight, maxDaylight);
            daylightDate = maxDaylight == oldLng ? daylightDate : currentDate;
        }
        print(minTempDiff, maxDaylight, daylightDate, tempDate);
    }

    public static void main(String[] args) {
        new App().run();
    }

    public int someLogic() {
        return 1;
    }
}
