package com.example.testapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    RecyclerView recyclerView;
    AdaptadorHome adaptadorHome;
    FloatingActionButton fab;
    private Gson gson = new Gson();

    public MainFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home, container, false);
        fab = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        cargarAdaptador();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivityForResult(
                        new Intent(getActivity(), InsertarActivity.class),3
                );
                Toast.makeText(getActivity(), "INSERTAR", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    public void cargarAdaptador(){
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(
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
                                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
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
                    adaptadorHome = new AdaptadorHome(getActivity(), new ArrayList<>(Arrays.asList(arrayPersonas)));
                    recyclerView.setAdapter(adaptadorHome);
                    break;
                case "2":
                    String msj2 = response.getString("mensaje");
                    Toast.makeText(getActivity(), msj2, Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
