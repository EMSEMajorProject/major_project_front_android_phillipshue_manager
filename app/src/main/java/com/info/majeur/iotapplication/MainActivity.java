package com.info.majeur.iotapplication;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    public PhillipsHueDto phillipsHueDto;
    public ImageButton imageButtonLight;
    public SeekBar sat;
    public SeekBar bri;
    public SeekBar hue;
    public Button buttonRefresh;
    public ScrollView scrollView;
    public ProgressBar progressBar;
    public String url = "https://phillipshue.herokuapp.com/api/lights/";
    private RequestQueue requestQueue;
    private View test;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButtonLight = findViewById(R.id.imageButtonLight);
        requestQueue = Volley.newRequestQueue(this);
        phillipsHueDto = new PhillipsHueDto(false, 0l, 0l, 0l);
        buttonRefresh = findViewById(R.id.buttonRef);
        scrollView = findViewById(R.id.scrollView);
        progressBar = findViewById(R.id.progressBar);
        sat = findViewById(R.id.seekBarSat);
        sat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                MainActivity.this.setColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(MainActivity.this.phillipsHueDto.getOn()) {
                    MainActivity.this.setSatDatabase();
                } else {
                    int mode = ((AudioManager) getApplicationContext().getSystemService(
                            Context.AUDIO_SERVICE)).getRingerMode();
                    ((AudioManager) getApplicationContext().getSystemService(
                            Context.AUDIO_SERVICE))
                            .setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    ((AudioManager) getApplicationContext().getSystemService(
                            Context.AUDIO_SERVICE))
                            .setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    ((AudioManager) getApplicationContext().getSystemService(
                            Context.AUDIO_SERVICE))
                            .setRingerMode(mode);
                    MainActivity.this.setLight();
                }
            }
        });
        bri = findViewById(R.id.seekBarBri);

        bri.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                MainActivity.this.setColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(MainActivity.this.phillipsHueDto.getOn()) {
                    MainActivity.this.setBriDatabase();
                } else {
                    int mode = ((AudioManager) getApplicationContext().getSystemService(
                            Context.AUDIO_SERVICE)).getRingerMode();
                    ((AudioManager) getApplicationContext().getSystemService(
                            Context.AUDIO_SERVICE))
                            .setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    ((AudioManager) getApplicationContext().getSystemService(
                            Context.AUDIO_SERVICE))
                            .setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    ((AudioManager) getApplicationContext().getSystemService(
                            Context.AUDIO_SERVICE))
                            .setRingerMode(mode);
                    MainActivity.this.setLight();
                }
            }
        });

        hue = findViewById(R.id.seekBarHue);
        hue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                MainActivity.this.setColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(MainActivity.this.phillipsHueDto.getOn()) {
                    MainActivity.this.setHueDatabase();
                } else {
                    int mode = ((AudioManager) getApplicationContext().getSystemService(
                            Context.AUDIO_SERVICE)).getRingerMode();
                    ((AudioManager) getApplicationContext().getSystemService(
                            Context.AUDIO_SERVICE))
                            .setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    ((AudioManager) getApplicationContext().getSystemService(
                            Context.AUDIO_SERVICE))
                            .setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    ((AudioManager) getApplicationContext().getSystemService(
                            Context.AUDIO_SERVICE))
                            .setRingerMode(mode);
                    MainActivity.this.setLight();
                }
            }
        });
        getDatabase();
    }

    public void getDatabase() {
        scrollView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        String url = this.url + "10";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String on = response.getString("on");
                    String sat = response.getString("sat");
                    String bri = response.getString("bri");
                    String hue = response.getString("hue");
                    MainActivity.this.phillipsHueDto.setBri(Long.parseLong(bri));
                    MainActivity.this.phillipsHueDto.setSat(Long.parseLong(sat));
                    MainActivity.this.phillipsHueDto.setHue(Long.parseLong(hue));
                    MainActivity.this.phillipsHueDto.setOn(on.equalsIgnoreCase("true"));
                    MainActivity.this.setLight();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void setSatDatabase() {
        scrollView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        String url = this.url + "10/sat/" + sat.getProgress() ;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String on = response.getString("on");
                    String sat = response.getString("sat");
                    String bri = response.getString("bri");
                    String hue = response.getString("hue");
                    MainActivity.this.phillipsHueDto.setBri(Long.parseLong(bri));
                    MainActivity.this.phillipsHueDto.setSat(Long.parseLong(sat));
                    MainActivity.this.phillipsHueDto.setHue(Long.parseLong(hue));
                    MainActivity.this.phillipsHueDto.setOn(on.equalsIgnoreCase("true"));
                    MainActivity.this.setLight();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void setHueDatabase() {
        scrollView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        String url = this.url + "10/hue/" + hue.getProgress() ;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String on = response.getString("on");
                    String sat = response.getString("sat");
                    String bri = response.getString("bri");
                    String hue = response.getString("hue");
                    MainActivity.this.phillipsHueDto.setBri(Long.parseLong(bri));
                    MainActivity.this.phillipsHueDto.setSat(Long.parseLong(sat));
                    MainActivity.this.phillipsHueDto.setHue(Long.parseLong(hue));
                    MainActivity.this.phillipsHueDto.setOn(on.equalsIgnoreCase("true"));
                    MainActivity.this.setLight();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void setBriDatabase() {
        scrollView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        String url = this.url + "10/bri/" + bri.getProgress() ;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String on = response.getString("on");
                    String sat = response.getString("sat");
                    String bri = response.getString("bri");
                    MainActivity.this.phillipsHueDto.setBri(Long.parseLong(bri));
                    MainActivity.this.phillipsHueDto.setSat(Long.parseLong(sat));
                    MainActivity.this.phillipsHueDto.setOn(on.equalsIgnoreCase("true"));
                    MainActivity.this.setLight();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @SuppressLint("ResourceType")
    public void setColor() {
        if(phillipsHueDto.getOn()) {
            imageButtonLight.setColorFilter(Color.HSVToColor(new float[] {360f*hue.getProgress()/65536f, sat.getProgress()/254f, bri.getProgress()/254f}));
            buttonRefresh.setBackgroundColor(Color.HSVToColor(new float[] {360f*hue.getProgress()/65536f, sat.getProgress()/254f, bri.getProgress()/254f}));
        } else {
            imageButtonLight.setColorFilter(Color.HSVToColor(new float[] {360f, 1f, 0f}));
            buttonRefresh.setBackgroundColor(Color.HSVToColor(new float[] {360f, 1f, 0f}));
        }
    }

    public void setLight() {

        if (phillipsHueDto.getOn()) {
            imageButtonLight.setImageResource(R.mipmap.light);
            sat.setProgress(phillipsHueDto.getSat().intValue());
            bri.setProgress(phillipsHueDto.getBri().intValue());
            hue.setProgress(phillipsHueDto.getHue().intValue());
        } else {
            imageButtonLight.setImageResource(R.mipmap.lightoff);
            sat.setProgress(0);
            bri.setProgress(0);
            hue.setProgress(0);
        }
        setColor();
        scrollView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void switchlight(View view) {
        scrollView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        String url = this.url + "10/switch";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String on = response.getString("on");
                    String sat = response.getString("sat");
                    String bri = response.getString("bri");
                    MainActivity.this.phillipsHueDto.setBri(Long.parseLong(bri));
                    MainActivity.this.phillipsHueDto.setSat(Long.parseLong(sat));
                    MainActivity.this.phillipsHueDto.setOn(on.equalsIgnoreCase("true"));
                    MainActivity.this.setLight();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void refresh(View view) {
        getDatabase();
    }


    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public PhillipsHueDto getPhillipsHueDto() {
        return phillipsHueDto;
    }

    public void setPhillipsHueDto(PhillipsHueDto phillipsHueDto) {
        this.phillipsHueDto = phillipsHueDto;
    }
}
