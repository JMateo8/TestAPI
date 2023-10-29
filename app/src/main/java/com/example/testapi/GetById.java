package com.example.testapi;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class GetById extends AppCompatActivity {
    EditText et;
    Button btnId, btnPais;
    TextView tv;
    private Gson gson = new Gson();
    Spinner spinner;
    RecyclerView recyclerView;
    AdaptadorHome adaptadorHome;
    TextView tvPais;
    ArrayList<String> arrayListPaises;
    Dialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_by_id);

        et = findViewById(R.id.editTextNumber);
        btnId = findViewById(R.id.buttonId);
        btnPais = findViewById(R.id.buttonPais);

        tvPais = findViewById(R.id.textViewPais);
        arrayListPaises = new ArrayList<>();
        arrayListPaises.add("Alemania");
        arrayListPaises.add("Argentina");
        arrayListPaises.add("España");
        arrayListPaises.add("Francia");
        arrayListPaises.add("México");
        arrayListPaises.add("Portugal");
        arrayListPaises.add("Rusia");
        arrayListPaises.add("Neptuno");

        tvPais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(GetById.this);
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(700, 800);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);
                ArrayAdapter<String> adapter=new ArrayAdapter<>(GetById.this, android.R.layout.simple_list_item_1,arrayListPaises);

                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        tvPais.setText(adapter.getItem(i));
                        cargarDatosPais(adapter.getItem(i), true);
                        dialog.dismiss();
                    }
                });


            }
        });

        tv = findViewById(R.id.textView2);
        spinner = (Spinner) findViewById(R.id.spinner_pais);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.planets_array,
                android.R.layout.simple_spinner_item
        );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        recyclerView = findViewById(R.id.recyclerGetBy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        btnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarDatosId();
            }
        });


        btnPais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String spinnerPais = spinner.getSelectedItem().toString();
                cargarDatosPais(spinnerPais, false);
            }
        });
    }

    private void cargarDatosId(){
        String id = et.getText().toString();
        String newUrl = Constantes.GET_BY_ID + "?id=" + id;
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        newUrl,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaId(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                tv.setText(error.toString());
                            }
                        }

                )
        );

    }

    private void cargarDatosPais(String pais, boolean searchable){
        String newUrl = Constantes.GET_BY_PAIS + "?pais=" + pais;
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        newUrl,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaPais(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                tv.setText(error.toString());
                            }
                        }

                )
        );

    }

    private void procesarRespuestaId(JSONObject response) {
        try {
            String estado = response.getString("estado");

            switch (estado){
                case "1":
                    JSONObject jsonObject = response.getJSONObject("persona");
                    Persona persona = gson.fromJson(jsonObject.toString(), Persona.class);
                    ArrayList<Persona> personas = new ArrayList<>();
                    personas.add(persona);
                    adaptadorHome = new AdaptadorHome(this, personas);
                    recyclerView.setAdapter(adaptadorHome);
                    tv.setText(persona.getNombre());
                    break;
                case "2":
                    String msj2 = response.getString("mensaje");
                    Toast.makeText(this, msj2, Toast.LENGTH_SHORT).show();
                    break;
                case "3":
                    String msj3 = response.getString("mensaje");
                    Toast.makeText(this, msj3, Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void procesarRespuestaPais(JSONObject response) {
        try {
            String estado = response.getString("estado");

            switch (estado){
                case "1":
                    JSONArray mensaje = response.getJSONArray("personas");
                    Persona[] arrayPersonas = gson.fromJson(mensaje.toString(), Persona[].class);
                    ArrayList<Persona> personas = new ArrayList<>(Arrays.asList(arrayPersonas));
                    adaptadorHome = new AdaptadorHome(this, personas);
                    recyclerView.setAdapter(adaptadorHome);
                    String txt="";
                    for (Persona p: personas) {
                        txt += "ID: " + p.getId() + " | Nombre: " + p.getNombre() + "| País: " + p.getPais() + "\n";
                    }
                    tv.setText(txt);
                    break;
                case "2":
                    String msj2 = response.getString("mensaje");
                    Persona persona2 = new Persona(0, msj2, null);
                    ArrayList<Persona> personasError2 = new ArrayList<>();
                    personasError2.add(persona2);
                    adaptadorHome = new AdaptadorHome(this, personasError2);
                    recyclerView.setAdapter(adaptadorHome);
                    Toast.makeText(this, msj2, Toast.LENGTH_SHORT).show();
                    break;
                case "3":
                    String msj3 = response.getString("mensaje");
                    Persona persona3 = new Persona(0, msj3, null);
                    ArrayList<Persona> personasError3 = new ArrayList<>();
                    personasError3.add(persona3);
                    adaptadorHome = new AdaptadorHome(this, personasError3);
                    recyclerView.setAdapter(adaptadorHome);
                    Toast.makeText(this, msj3, Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

    }
}
