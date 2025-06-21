package co.median.android.a2025_theangels_new.data.map;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.RawRes;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MapStyleOptions;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility methods for loading and applying Google Map style JSON safely.
 */
public class MapStyleHelper {
    private static final String TAG = "MapStyleHelper";

    /**
     * Loads a map style resource after validating that the JSON is a valid array.
     *
     * @param context    Context used to access resources
     * @param styleResId Raw resource ID of the style JSON
     * @return {@link MapStyleOptions} if the JSON is valid, otherwise null
     */
    public static MapStyleOptions loadValidatedStyle(Context context, @RawRes int styleResId) {
        Resources res = context.getResources();
        try (InputStream input = res.openRawResource(styleResId);
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String json = builder.toString().trim();
            if (!json.startsWith("[") || !json.endsWith("]")) {
                Log.e(TAG, "Map style JSON must be an array. See res/raw/map_style.json");
                return null;
            }
            try {
                new JSONArray(json); // validate JSON structure
            } catch (Exception e) {
                Log.e(TAG, "Invalid JSON in map style resource", e);
                return null;
            }
            return MapStyleOptions.loadRawResourceStyle(context, styleResId);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Map style resource not found", e);
        } catch (IOException e) {
            Log.e(TAG, "Error reading map style", e);
        }
        return null;
    }

    /**
     * Applies the given map style to the provided {@link GoogleMap} instance with logging.
     */
    public static void applyStyle(GoogleMap map, Context context, @RawRes int styleResId) {
        MapStyleOptions options = loadValidatedStyle(context, styleResId);
        if (options != null) {
            boolean success = map.setMapStyle(options);
            if (!success) {
                Log.e(TAG, "Map style parsing failed");
            }
        }
    }
}
