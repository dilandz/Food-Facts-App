package com.example.nutritionfactsapp;


import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class OpenFoodFactAPI {

    // Variable declaration
    private RequestQueue mQueue;
    private String tempBarcode;
    private String name, grade, ingredient, nova_group, barcode, nutrient, imageURL;



    public OpenFoodFactAPI(Context context, String barcode){ // constructor pass the context screen and barcode
        mQueue = Volley.newRequestQueue(context); // creating instance
        this.tempBarcode = barcode;

    }

    // get methods for all the variables
    public String getName() {
        return name;
    }

    public String getGrade() {
        return grade;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getNutrient() {
        return nutrient;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getNova_group() {
        return nova_group;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void jsonParse(){


        String url = "https://world.openfoodfacts.org/api/v0/product/" + tempBarcode +".json"; //request link

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() { //sending the request
            @Override
            public void onResponse(String response) { // On the response get all the data in json format

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject product = jsonObject.getJSONObject("product");
                    JSONObject nutriments = product.getJSONObject("nutriments");
                    int status = jsonObject.getInt("status");


                    if ( status != 0) {

                        barcode = product.getString("_id"); //Get from each field and store to local variable
                        name = product.getString("product_name");
                        grade = product.getString("nutriscore_grade");
                        nova_group = product.getString("nova_group");
                        ingredient = product.getString("ingredients_text_en");

                        nutrient = "fat 100g                          - " + nutriments.getString("fat_100g") + "\n" +
                                   "salt 100g                        - " + nutriments.getString("salt_100g") + "\n" +
                                   "sugar 100g                    - " + nutriments.getString("sugars_100g") + "\n" +
                                   "protein 100g                  - " + nutriments.getString("proteins_100g") + "\n" +
                                   "energy-kcal 100g         - " + nutriments.getString("energy-kcal_100g") + "\n" +
                                   "carbohydrates 100g    - " + nutriments.getString("carbohydrates_100g");

                        imageURL = product.getString("image_url");



                        System.out.println(name);
                        Log.d( "onResponse: ", nova_group);

                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{ // authentication of the user with API
                Map<String, String> params = new HashMap<String, String>();
                params.put("User-Agent", "FoodFacts");
                params.put("Android", "Version 1.0");

                return params;
            }
        };

        mQueue.add(request);




    }

}
