package com.example.testapi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InsertFragment extends Fragment {

    private static final String TAG = InsertFragment.class.getSimpleName();

    EditText nombre_input;
    Spinner paises_spinner;
    Button botonConfirm;

    public InsertFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_form, container, false);

        nombre_input = (EditText) v.findViewById(R.id.nombre_input);
        paises_spinner = (Spinner) v.findViewById(R.id.input_pais_spinner);
        botonConfirm = (Button) v.findViewById(R.id.buttonConfirm);

        botonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!camposVacios()){
                    guardarMeta();
                } else {
                    Toast.makeText(getActivity(), "Completa los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.removeItem(R.id.action_delete);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                if (!camposVacios()){
                    guardarMeta();
                } else {
                    Toast.makeText(getActivity(), "Completa los campos", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_discard:
                if (!camposVacios()){
                    mostrarDialogo();
                } else {
                    getActivity().finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void guardarMeta(){
        final String nombre = nombre_input.getText().toString();
        final String pais = paises_spinner.getSelectedItem().toString();

        HashMap<String, String> map = new HashMap<>();

        map.put("nombre", nombre);
        map.put("pais", pais);

        JSONObject jsonObject = new JSONObject(map);

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.INSERT,
                        jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuesta(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error: " + error.getMessage());
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
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }

    private void procesarRespuesta(JSONObject response){
        try {
            String estado = response.getString("estado");
            String mensaje = response.getString("mensaje");

            switch (estado){
                case "1":
                    Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                    break;
                case "2":
                    Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
                    getActivity().setResult(Activity.RESULT_CANCELED);
                    getActivity().finish();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean camposVacios() {
        String nombre = nombre_input.getText().toString();
        return (nombre.isEmpty());
    }

    public void mostrarDialogo(){
        DialogFragment dialogo = ConfirmDialogFragment.creareInstance(
                getResources().getString(R.string.dialog_discard_msg)
        );
        dialogo.show(getFragmentManager(), "ConfirmDialog");
    }
}
