package com.example.testapi;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetalleFragment extends Fragment {

    private static final String TAG = DetalleFragment.class.getSimpleName();

    private ImageView cabecera;
    private TextView nombre, pais;
    private FloatingActionButton fab;
    private String extra;
    private Gson gson = new Gson();

    public DetalleFragment(){}

    public static DetalleFragment createInstance(String idPersona) {
        DetalleFragment detalleFragment = new DetalleFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constantes.EXTRA_ID, idPersona);
        detalleFragment.setArguments(bundle);
        return detalleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);

        cabecera = (ImageView) v.findViewById(R.id.cabecera);
        nombre = (TextView) v.findViewById(R.id.nombre);
        pais = (TextView) v.findViewById(R.id.pais);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);

        extra = getArguments().getString(Constantes.EXTRA_ID);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                Toast.makeText(getContext(), "EDITAR", Toast.LENGTH_SHORT).show();
            }
        });
        cargarDatos();
        return v;
    }

    public void cargarDatos(){
        String newUrl = Constantes.GET_BY_ID + "?id=" + extra;
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        newUrl,
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
                                Log.e(TAG, "ERROR: " + error.getMessage());
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
                    JSONObject jsonObject = response.getJSONObject("persona");
                    Persona persona = gson.fromJson(jsonObject.toString(), Persona.class);
                    nombre.setText(persona.getNombre());
                    pais.setText(persona.getPais());
                    cabecera.setBackground(getResources().getDrawable(R.drawable.ic_launcher_background));
                    break;
                case "2":
                    String msj2 = response.getString("mensaje");
                    Toast.makeText(getActivity(), msj2, Toast.LENGTH_SHORT).show();
                    break;
                case "3":
                    String msj3 = response.getString("mensaje");
                    Toast.makeText(getActivity(), msj3, Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}