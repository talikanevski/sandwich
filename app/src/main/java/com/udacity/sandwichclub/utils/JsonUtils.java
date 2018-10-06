package com.udacity.sandwichclub.utils;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        String mainName;
        String placeOfOrigin;
        String description;
        String image;
//
//        /**If the JSON string is empty or null, then return early.**/
//        if (TextUtils.isEmpty(json)) {
//            return null;
//        }

        try {
            JSONObject baseJsonResponse = new JSONObject(json);
            JSONObject name = baseJsonResponse.getJSONObject("name");
            mainName = name.getString("mainName");

            JSONArray alsoKnownAsArray = name.getJSONArray("alsoKnownAs");
            ArrayList<String> listOfNames =  new ArrayList<>();
            for (int i = 0; i < alsoKnownAsArray.length(); i++) {
                listOfNames.add(alsoKnownAsArray.getString(i));
            }

            placeOfOrigin = baseJsonResponse.getString("placeOfOrigin");
            description = baseJsonResponse.getString("description");

            image =  baseJsonResponse.getString("image");

            JSONArray ingredientsArray = baseJsonResponse.getJSONArray("ingredients");
            ArrayList<String> listOfIngredients =  new ArrayList<>();
            for (int i = 0; i < ingredientsArray.length(); i++) {
                listOfIngredients.add(ingredientsArray.getString(i));
            }

            return new Sandwich(mainName,listOfNames,placeOfOrigin,description,image,listOfIngredients);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
