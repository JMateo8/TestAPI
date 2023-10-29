package com.example.testapi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class DetallePersona extends AppCompatActivity {
    private String idPersona;

    public static void launch(Activity activity, String idPersona) {
        Intent intent = getLaunchIntent(activity, idPersona);
        activity.startActivityForResult(intent, Constantes.CODIGO_DETALLE);
    }

    public static Intent getLaunchIntent(Context context, String idPersona) {
        Intent intent = new Intent(context, DetallePersona.class);
        intent.putExtra(Constantes.EXTRA_ID, idPersona);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
        }

        if (getIntent().getStringExtra(Constantes.EXTRA_ID) != null){
            idPersona = getIntent().getStringExtra(Constantes.EXTRA_ID);
        }
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, DetalleFragment.createInstance(idPersona), "DetailFragment")
                    .commit();
        }
    }

    /*
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constantes.CODIGO_ACTUALIZACION) {
            if (resultCode == RESULT_OK) {
                DetalleFragment fragment = (DetalleFragment) getSupportFragmentManager().findFragmentByTag("DetalleFragment");
                fragment.cargarDatos();
                setResult(RESULT_OK);
            } else if (resultCode == 203) {
                setResult(203);
                finish();
            } else {
                setResult(RESULT_CANCELED);
            }
        }
    }
}