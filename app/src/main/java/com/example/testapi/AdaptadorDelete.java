package com.example.testapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdaptadorDelete extends RecyclerView.Adapter<AdaptadorDelete.ViewHolder>{

    public Context context;
    public ArrayList<Persona> personas;
    private Gson gson = new Gson();

    public AdaptadorDelete(Context context, ArrayList<Persona> personas) {
        this.context = context;
        this.personas = personas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_home, parent, false);
        return new AdaptadorDelete.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Persona persona = personas.get(position);
        holder.id.setText(Integer.toString(persona.getId()));
        holder.nombre.setText(persona.getNombre());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", Integer.toString(persona.getId()));
                JSONObject jObject = new JSONObject(map);

                VolleySingleton.getInstance(context).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.DELETE,
                                jObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String estado = response.getString("estado");
                                            String mensaje = response.getString("mensaje");
                                            switch (estado){
                                                case "1":
                                                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                                                    notifyDataSetChanged();
                                                    break;
                                                case "2":
                                                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
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
                        ) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" + getParamsEncoding();                            }
                        }
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return personas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView id, nombre;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.id = itemView.findViewById(R.id.item_id);
            this.nombre = itemView.findViewById(R.id.item_nombre);
            this.cardView = itemView.findViewById(R.id.cardViewHome);
        }
    }

}