package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final ImageView image = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);

        /** https://github.com/square/picasso/issues/1140
         * for some reason, shawarma shows an error**/
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        image.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        image.setVisibility(View.GONE);
                    }
                });

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        TextView additionalNamesLabel = (TextView) findViewById(R.id.also_known_as_label);
        TextView additionalNames = (TextView) findViewById(R.id.also_known_tv);
        TextView originLabel = (TextView) findViewById(R.id.place_of_origin_label);
        TextView origin = (TextView) findViewById(R.id.origin_tv);
        TextView descriptionLabel = (TextView) findViewById(R.id.detail_description_label);
        TextView description = (TextView) findViewById(R.id.description_tv);
        TextView ingredientsLabel = (TextView) findViewById(R.id.ingredients_label);
        TextView ingredients = (TextView) findViewById(R.id.ingredients_tv);
        ImageView image = (ImageView) findViewById(R.id.image_iv);

        contentSanityCheck(additionalNamesLabel, additionalNames,
                TextUtils.join(", ", sandwich.getAlsoKnownAs()));

        contentSanityCheck(originLabel, origin, sandwich.getPlaceOfOrigin());

        contentSanityCheck(descriptionLabel, description, sandwich.getDescription());

        contentSanityCheck(ingredientsLabel, ingredients,
                TextUtils.join(", ", sandwich.getIngredients()));

        isThereAnyImage(image, sandwich.getImage());
    }
        /** if there is no data in JSON, it's better to hide those views**/
    private void contentSanityCheck(TextView label, TextView tv, String content) {
        if (TextUtils.isEmpty(content)) {
            label.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
            return;
        }
        tv.setText(content);
    }

    private void isThereAnyImage (ImageView imageView, String content){
        if (TextUtils.isEmpty(content)){
            imageView.setVisibility(View.GONE);
        } else { imageView.setVisibility(View.VISIBLE);
           }
    }
}
