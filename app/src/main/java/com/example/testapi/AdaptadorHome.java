package com.example.testapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AdaptadorHome extends RecyclerView.Adapter<AdaptadorHome.ViewHolder> {

    public Context context;
    public ArrayList<Persona> personas;
    private Gson gson = new Gson();

    public AdaptadorHome(Context context, ArrayList<Persona> personas) {
        this.context = context;
        this.personas = personas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_home, parent, false);
        return new AdaptadorHome.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Persona persona = personas.get(position);
        holder.id.setText(Integer.toString(persona.getId()));
        holder.nombre.setText(persona.getNombre());
        holder.pais.setText(persona.getPais());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetallePersona.launch((Activity) context, Integer.toString(persona.getId()));
                /*
                String newUrl = Constantes.GET_BY_ID + "?id=" + persona.getId();
                VolleySingleton.getInstance(context).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                newUrl,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String estado = response.getString("estado");
                                            switch (estado){
                                                case "1":
                                                    JSONObject jsonObject = response.getJSONObject("persona");
                                                    Persona p = gson.fromJson(jsonObject.toString(), Persona.class);
                                                    Toast.makeText(context, "País: " + p.getPais(), Toast.LENGTH_SHORT).show();
                                                    break;
                                                case "2":
                                                    Toast.makeText(context, response.getString("mensaje"), Toast.LENGTH_SHORT).show();
                                                    break;
                                                case "3":
                                                    Toast.makeText(context, response.getString("mensaje"), Toast.LENGTH_SHORT).show();
                                                    break;
                                            }
                                        } catch (JSONException e){
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                        )
                );
                */
            }
        });
    }

    @Override
    public int getItemCount() {
        return personas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView id, nombre, pais;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.id = itemView.findViewById(R.id.item_id);
            this.nombre = itemView.findViewById(R.id.item_nombre);
            this.pais = itemView.findViewById(R.id.item_pais);
            this.cardView = itemView.findViewById(R.id.cardViewHome);
        }
    }

}
