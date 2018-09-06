package com.tareas.diego.proyectosemestralv1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main2Activity extends AppCompatActivity {
    Button btn, btnComprar;
    ListView list1;
    TextView tv1;
    final List<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btn = findViewById(R.id.btnScan);
        btnComprar = findViewById(R.id.btnCarrito);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                intent.putExtra("user", "hola");
                startActivityForResult(intent, 1);
            }
        });

        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv1 = findViewById(R.id.textView1);

                //Toast.makeText(Main2Activity.this, String.valueOf(tv1.getText().toString().length()), Toast.LENGTH_SHORT).show();
              if (!tv1.getText().toString().equals("0"))
                {
                    new AlertDialog.Builder(Main2Activity.this)
                            .setTitle("Confirmar compra")
                            .setMessage("¿Realmente desea comprar los productos listados?\n Total: " + tv1.getText())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    /* Comienza llamada a Volley */

                                    //String PLACES_URL = "192.168.1.33:8080/item?id=606f8120891e42af86db0ae19e1f138c";
                                    //String PLACES_URL = "http://uinames.com/api/?amount=1";
                                    String PLACES_URL = "http://192.168.43.240:8080/mail";
                                    String LOG_TAG = "VolleyPlacesRemoteDS";
                                    // Instantiate the RequestQueue
                                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                                    //Prepare the Request
                                    JsonObjectRequest request = new JsonObjectRequest(
                                            Request.Method.GET, //GET or POST
                                            PLACES_URL, //URL
                                            null, //Parameters
                                            new Response.Listener<JSONObject>() { //Listener OK

                                                @Override
                                                public void onResponse(JSONObject responsePlaces) {
                                                    names.clear();
                                                    list1 = findViewById(R.id.list1);
                                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, names);
                                                    list1.setAdapter(adapter);

                                                    tv1 = findViewById(R.id.textView1);
                                                    tv1.setText("0");
                                                    Toast.makeText(getApplicationContext(), "Compra realizada con éxito!", Toast.LENGTH_SHORT).show();

                                                }
                                            }, new Response.ErrorListener() { //Listener ERROR

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getApplicationContext(), "Volley Malo", Toast.LENGTH_SHORT).show();
                                        }
                                    });

//Send the request to the requestQueue
                                    requestQueue.add(request);
                                    //List<CraftARItem> lista = new ArrayList<CraftARItem>();
                                    //myStringList.add(item.getItemName());
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
                else
                {
                    new AlertDialog.Builder(Main2Activity.this)
                            .setTitle("No ha seleccionado productos")
                            .setMessage("Precione Escanear para agregar productos.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Escanear", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                                    intent.putExtra("user", "hola");
                                    startActivityForResult(intent, 1);
                                }
                            })
                            .setNegativeButton("Cancelar", null).show();
                }
            }
        });






    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra("myList");
                if(result.size() > 0)
                {
                    tv1 = findViewById(R.id.textView1);
                    String nombre = "";
                    int precio = 0;
                    int totalParcial = 0;
                    for(int i = 0; i < result.size()/2; i++)
                    {
                        precio = Integer.parseInt(result.get((i*2)+1));
                        totalParcial = totalParcial + precio;
                        nombre = result.get(i*2)+".                  Precio: $"+String.valueOf(precio);
                        names.add(nombre);
                    }
                    list1 = findViewById(R.id.list1);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
                    list1.setAdapter(adapter);

                    if(tv1.getText().toString().length() > 1)
                    {
                        int total = Integer.parseInt(tv1.getText().toString()) + totalParcial;
                        tv1.setText(String.valueOf(total));
                    }

                    else
                        tv1.setText(String.valueOf(totalParcial));
                }

                //ArrayList<String> result = data.getStringArrayListExtra("array");
                //String[] result = data.getStringArrayExtra("array");

            }
            if (resultCode == Activity.RESULT_CANCELED) {

                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }


}

