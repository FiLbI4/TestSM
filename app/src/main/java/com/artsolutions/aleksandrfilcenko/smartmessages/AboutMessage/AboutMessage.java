package com.artsolutions.aleksandrfilcenko.smartmessages.AboutMessage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.artsolutions.aleksandrfilcenko.smartmessages.AboutCompany.AboutCompanyActivity;
import com.artsolutions.aleksandrfilcenko.smartmessages.R;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class AboutMessage extends AppCompatActivity {

    private String messageId;

    public static final String ENDPOINT = "https://smartmessage.ru/api/";


    TextView nameAboutMessage, addressAboutMessage, numberAboutMessage, dateAboutMessage, typeAboutMessage, textAboutMessage;
    ImageView imageAboutMessage;
    ImageView bigImageAboutMessage;
    CircleProgressBar progressBar;
    ScrollView scrollContainer;
    private final static String TAG = "TestImageGetter";

    public interface AboutMessagesAPI {
        @GET("https://smartmessage.ru/api/message/{messageId}")
        Call<AboutMessageClass> aboutMessages(@Path("messageId") String messageId, @Query("session") String session);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_message);

        nameAboutMessage = (TextView) findViewById(R.id.nameAboutMessage);
        addressAboutMessage = (TextView) findViewById(R.id.addressAboutMessage);
        numberAboutMessage = (TextView) findViewById(R.id.numberAboutMessage);
        dateAboutMessage = (TextView) findViewById(R.id.dateAboutMessage);
        typeAboutMessage = (TextView) findViewById(R.id.typeAboutMessage);
        textAboutMessage = (TextView) findViewById(R.id.textAboutMessage);
        imageAboutMessage = (ImageView) findViewById(R.id.imageAboutMessage);
        bigImageAboutMessage = (ImageView) findViewById(R.id.bigImageAboutMessage);
        progressBar = (CircleProgressBar) findViewById(R.id.progressBar);

        scrollContainer = (ScrollView) findViewById(R.id.scrollContainer);

        scrollContainer.setVisibility(View.GONE);


        progressBar.setColorSchemeResources(R.color.mainColorGreen);
        progressBar.setVisibility(View.VISIBLE);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            messageId = extras.getString("messageId");
            System.out.println(messageId);
        }

        Intent intent = getIntent();
        if (intent != null) {
            messageId = intent.getStringExtra("messageId");
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String session = sharedPreferences.getString("session", "");


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AboutMessagesAPI service = retrofit.create(AboutMessagesAPI.class);

        Call<AboutMessageClass> call = service.aboutMessages(messageId, session);

        call.enqueue(new Callback<AboutMessageClass>() {
            @Override
            public void onResponse(Call<AboutMessageClass> call, final Response<AboutMessageClass> response) {

                if (response.isSuccessful()) {

                        DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");
                        fromFormat.setLenient(false);
                        DateFormat toFormat = new SimpleDateFormat("dd.MM.yyyy");
                        toFormat.setLenient(false);
                        String dateStr = response.body().getDate();
                        Date date = null;
                        try {
                            date = fromFormat.parse(dateStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    nameAboutMessage.setText(response.body().getName());
                    addressAboutMessage.setText(response.body().getAddress());
                    try {
                        dateAboutMessage.setText(toFormat.format(date));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    typeAboutMessage.setText(response.body().getType());
                    numberAboutMessage.setText(response.body().getPhone());
                    nameAboutMessage.setText(response.body().getName());
                    textAboutMessage.setText(Html.fromHtml(response.body().getText(), new Html.ImageGetter() {
                        @Override
                        public Drawable getDrawable(String source) {
                            LevelListDrawable d = new LevelListDrawable();
                            Drawable empty = getResources().getDrawable(R.drawable.ic_launcher);
                            d.addLevel(0, 0, empty);
                            d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

                            new LoadImage().execute(source, d);

                            return d;
                        }
                    }, null));
                    textAboutMessage.setMovementMethod(LinkMovementMethod.getInstance());
                    Picasso.with(getApplicationContext()).load(response.body().getLogo()).into(imageAboutMessage);
                    Picasso.with(getApplicationContext()).load(response.body().getImages()).into(bigImageAboutMessage);

                    final String company = response.body().getCompany();
                    final String id = response.body().getCompanyId();

                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    scrollContainer.setVisibility(View.VISIBLE);

                    nameAboutMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), AboutCompanyActivity.class);
                            intent.putExtra("companyId", id);
                            intent.putExtra("companyName", company);
                            startActivity(intent);
                        }
                    });


                    imageAboutMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent intent = new Intent(getApplicationContext(), AboutCompanyActivity.class);
                            intent.putExtra("companyId", id);
                            intent.putExtra("companyName", company);
                            startActivity(intent);
                        }
                    });

                } else {
                    Log.d("Response", "Failed");
                }

            }

            @Override
            public void onFailure(Call<AboutMessageClass> call, Throwable t) {
                Log.d("Error", t.getMessage());
                Toast.makeText(getApplicationContext(), "Произошла ошибка на сервере!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            Log.d(TAG, "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d(TAG, "onPostExecute drawable " + mDrawable);
            Log.d(TAG, "onPostExecute bitmap " + bitmap);
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                CharSequence t = textAboutMessage.getText();
                textAboutMessage.setText(t);
            }
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
