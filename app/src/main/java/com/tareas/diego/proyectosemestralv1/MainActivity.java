package com.tareas.diego.proyectosemestralv1;

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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.craftar.CraftARActivity;
import com.craftar.CraftARCloudRecognition;
import com.craftar.CraftARError;
import com.craftar.CraftARItem;
import com.craftar.CraftARResult;
import com.craftar.CraftARSDK;
import com.craftar.CraftARSearchResponseHandler;
import com.craftar.SetCloudCollectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends CraftARActivity implements
        CraftARSearchResponseHandler, SetCloudCollectionListener{

    CraftARSDK mCraftARSDK;
    CraftARCloudRecognition mCloudIR;
    Button btnBuscar, btnCancelar;
    //String[] myStringArray = {"a","b","c"};
    //final ArrayList<String> myStringList = new ArrayList<>();
    //List<JSONObject> myJsonList= new ArrayList<JSONObject>();
    ArrayList<String> myList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       btnBuscar = findViewById(R.id.btnBuscar);
       btnCancelar = findViewById(R.id.btnCancelar);

       btnBuscar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               mySearchFunction();
           }
       });

       btnCancelar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

                   Intent returnIntent = getIntent();
                   returnIntent.putStringArrayListExtra("myList", myList);

                   setResult(RESULT_OK, returnIntent);
                   finish();



               //Intent intent = new Intent(MainActivity.this, Main2Activity.class);
               //startActivity(intent);
           }
       });
    }

    @Override
    public void onPostCreate() {



        View mainLayout= (View) getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(mainLayout);

        mCraftARSDK = CraftARSDK.Instance();


        /**
         * Initialise the SDK with your Application Context before doing any other
         * operation with any other module. You can do this from any Activity
         * (for example the Splash Activity); */
        mCraftARSDK.init(getApplicationContext());

        mCraftARSDK.startCapture((CraftARActivity)this); //Pass a reference to the Activity

        //Get the Cloud Image Recognition instance
        mCloudIR = CraftARCloudRecognition.Instance();

        /* Set this class as the one to receive search responses. */
        mCloudIR.setCraftARSearchResponseHandler((CraftARSearchResponseHandler)this);

        /**
         * Set the Search controller from the Cloud IR class.
         * The Cloud IR class knows how to perform visual searches in the CraftAR CRS
         * Service. The searchController is a helper class that receives the camera frames
         * and pictures from the SDK and manages the Single shot and the Finder mode searches.*/
        mCraftARSDK.setSearchController(mCloudIR.getSearchController());

        /**
         * Set the collection we want to search with the COLLECTION_TOKEN.
         * When the collection is ready, the collectionReady callback will be triggered.
         */
        mCloudIR.setCollection("71b7a229d1834f28", (SetCloudCollectionListener)this);
    }
    @Override
    public void onPreviewStarted(int frameWidth, int frameHeight) {
        // startCapture() was completed successfully, and the Camera Preview has been started
    }

    @Override
    public void onCameraOpenFailed(){
        // startCapture() failed, so the Camera Preview could not be started
    }

    @Override
    public void collectionReady() {
        //setCollection() was completed successfully, and the token is valid.

    }

    @Override
    public void setCollectionFailed(CraftARError craftARError){
        //setCollection could not be completed. Check the error for more details
        Toast.makeText(this, "Collection Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void searchResults(ArrayList<CraftARResult> results, long searchTime, int requestCode) {


        if(results.size()==0){
            mCraftARSDK.stopFinder();
            Toast.makeText(this, "Nothing Found", Toast.LENGTH_SHORT).show();
        }else{

            mCraftARSDK.stopFinder();
            CraftARResult result = results.get(0); //We get only the first result
            CraftARItem item = result.getItem();

            /* Comienza llamada a Volley */

            //String PLACES_URL = "192.168.1.33:8080/item?id=606f8120891e42af86db0ae19e1f138c";
            //String PLACES_URL = "http://uinames.com/api/?amount=1";
            String PLACES_URL = "http://192.168.43.240:8080/item?id="+item.getItemId();
            String LOG_TAG = "VolleyPlacesRemoteDS";
            // Instantiate the RequestQueue
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            //Prepare the Request
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET, //GET or POST
                    PLACES_URL, //URL
                    null, //Parameters
                    new Response.Listener<JSONObject>() { //Listener OK

                        @Override
                        public void onResponse(JSONObject responsePlaces) {

                            try {
                                myList.add(responsePlaces.getString("name"));
                                myList.add(responsePlaces.getString("price"));
                                Toast.makeText(MainActivity.this, "Encontrado: "+responsePlaces.getString("name"), Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() { //Listener ERROR

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Volley Malo", Toast.LENGTH_SHORT).show();
                }
            });

//Send the request to the requestQueue
            requestQueue.add(request);
            //List<CraftARItem> lista = new ArrayList<CraftARItem>();
            //myStringList.add(item.getItemName());




        }

    }

    @Override
    public void searchFailed(CraftARError craftARError, int requestCode){
        //An image recognition request failed, check the error for more details
        Toast.makeText(this, "Search Failed", Toast.LENGTH_SHORT).show();


    }

    void mySearchFunction() {
        //Trigger this function when clicking on a button, for example.
        Toast.makeText(this, "Buscando...", Toast.LENGTH_SHORT).show();
        mCraftARSDK.startFinder();

    }


}
