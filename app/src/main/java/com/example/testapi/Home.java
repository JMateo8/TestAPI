package com.example.testapi;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class Home extends AppCompatActivity {
    RecyclerView recyclerView;
    AdaptadorHome adaptadorHome;
    ArrayList<Persona> arrayListPersonasGlobal;
    FloatingActionButton fab;
    private Gson gson = new Gson();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        
        arrayListPersonasGlobal = new ArrayList<>();
        cargarDatos();
        
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Home.this, "INSERTAR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cargarDatos(){
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        Constantes.GET,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuesta(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Home.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                )
        );
    }

    private void procesarRespuesta(JSONObject response) {
        try {
            String estado = response.getString("estado");

            switch (estado){
                case "1":
                    JSONArray mensaje = response.getJSONArray("personas");
                    Persona[] arrayPersonas = gson.fromJson(mensaje.toString(), Persona[].class);
                    arrayListPersonasGlobal = new ArrayList<>(Arrays.asList(arrayPersonas));
                    adaptadorHome = new AdaptadorHome(this, arrayListPersonasGlobal);
                    adaptadorHome = new AdaptadorHome(this, arrayListPersonasGlobal);
                    recyclerView.setAdapter(adaptadorHome);
                    break;
                case "2":
                    String msj2 = response.getString("mensaje");
                    Toast.makeText(this, msj2, Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
