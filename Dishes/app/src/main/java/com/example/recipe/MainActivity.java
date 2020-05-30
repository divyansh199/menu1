package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.Key;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private TextInputLayout search;
    private String url;
    private TextView textView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference reference_recipe;
    private Object ArrayList;
    private Object String;
    private ArrayList<String> po=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=(Button)findViewById(R.id.btton_search);
        search = (TextInputLayout)findViewById(R.id.edit_search);
        textView = (TextView)findViewById(R.id.text);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference_recipe = firebaseDatabase.getReference().child("recipe");
        databaseReference = firebaseDatabase.getReference().child("restaurants").child("delhi");
        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Log.i("123123123123", singleSnapshot.toString());
                    String  p=null;
                      for(DataSnapshot si :  singleSnapshot.getChildren())
                      {
                            p =si.getValue().toString();

                          break;
                      }
                    po.add(p);
                    Log.i("12321",p );

                }
                int i;
                for(i=0;i<po.size();i++)
                {
                    StringBuilder stringBuilder = new StringBuilder("https://api.spoonacular.com/recipes/complexSearch?query=");
                    stringBuilder.append(po.get(0)+"&maxFat=200&number=2&apiKey=eca56254c0684f64a1a8d776bb67f285");
                    url = stringBuilder.toString();
                    GroceryAsyncTask groceryAsyncTask = new GroceryAsyncTask();
                    groceryAsyncTask.execute();
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search2 = search.getEditText().getText().toString().trim();
                StringBuilder stringBuilder = new StringBuilder("https://api.spoonacular.com/recipes/complexSearch?query=");
                stringBuilder.append(search2+"&maxFat=200&number=2&apiKey=eca56254c0684f64a1a8d776bb67f285");
                url = stringBuilder.toString();
                GroceryAsyncTask groceryAsyncTask = new GroceryAsyncTask();
                groceryAsyncTask.execute();
            }
        });
    }
    class GroceryAsyncTask extends AsyncTask<Void,Void,Integer>
    {
        @Override
        protected Integer doInBackground(Void... voids) {
            Log.i("check", url.toString());
            URL url3=null;
            try {
                url3 = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            String response=null;
            try {
                response = getResponse(url3);
            }
            catch (Exception e)
            {

            }
           Integer gr =null;
            gr= extractJson(response);
            return gr;
        }
        public String getResponse(URL url3) throws IOException
        {
            HttpURLConnection httpURLConnection=null;
            InputStream inputStream=null;
            String json=null;
            try {
                httpURLConnection = (HttpURLConnection)url3.openConnection();
                httpURLConnection.connect();
                if(httpURLConnection.getResponseCode()==200)
                {
                    inputStream=httpURLConnection.getInputStream();
                    Log.i("12313213213", inputStream.toString()+" 123");
                    json=getJson(inputStream);
                }
            }
            catch (Exception e)
            {
                Log.i("check", "getResponse: ");
            }
            finally
            {
                if(httpURLConnection!=null)
                {
                    httpURLConnection.disconnect();
                }
                if(inputStream!=null)
                {
                    inputStream.close();
                }
            }
            return json;
        }
        public String getJson(InputStream inputStream)
        {
            StringBuilder stringBuilder = new StringBuilder();
            try
            {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line=bufferedReader.readLine();
                while (line!=null)
                {
                    Log.i("c14313545254545", line);
                    stringBuilder.append(line);
                    line=bufferedReader.readLine();
                }
            }
            catch (IOException e)
            {
                Log.i("check", "getJson: ");
            }
            return stringBuilder.toString();
        }
        public Integer extractJson(String json)
        {
            Integer gr =null;
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray js = jsonObject.getJSONArray("results");
                int i;
                JSONObject js2 = js.getJSONObject(0);
                gr = js2.getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return gr;
        }

        @Override
        protected void onPostExecute(Integer groceries) {
            super.onPostExecute(groceries);
            updateUI(groceries);
        }
    }
    public void updateUI(Integer a)
    {
        StringBuilder stringBuilder = new StringBuilder("https://api.spoonacular.com/recipes/");
        stringBuilder.append(a+"/information?includeNutrition=false&apiKey=eca56254c0684f64a1a8d776bb67f285");
        url = stringBuilder.toString();
        Log.i("!@3213346536", "asdasd"+ "  " +a);
        RecipeAsyncTask groceryAsyncTask = new RecipeAsyncTask();
        groceryAsyncTask.execute();

    }
    class RecipeAsyncTask extends AsyncTask<Void,Void,Recipe>
    {
        @Override
        protected Recipe doInBackground(Void... voids) {
            Log.i("check", url.toString());
            URL url3=null;
            try {
                url3 = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            String response=null;
            try {
                response = getResponse(url3);
            }
            catch (Exception e)
            {

            }
            Recipe gr =null;
            gr= extractJson(response);
            return gr;
        }
        public String getResponse(URL url3) throws IOException
        {
            HttpURLConnection httpURLConnection=null;
            InputStream inputStream=null;
            String json=null;
            try {
                httpURLConnection = (HttpURLConnection)url3.openConnection();
                httpURLConnection.connect();
                if(httpURLConnection.getResponseCode()==200)
                {
                    inputStream=httpURLConnection.getInputStream();
                    Log.i("12313213213", inputStream.toString()+" 123");
                    json=getJson(inputStream);
                }
            }
            catch (Exception e)
            {
                Log.i("check", "getResponse: ");
            }
            finally
            {
                if(httpURLConnection!=null)
                {
                    httpURLConnection.disconnect();
                }
                if(inputStream!=null)
                {
                    inputStream.close();
                }
            }
            return json;
        }
        public String getJson(InputStream inputStream)
        {
            StringBuilder stringBuilder = new StringBuilder();
            try
            {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line=bufferedReader.readLine();
                while (line!=null)
                {
                    Log.i("c14313545254545", line);
                    stringBuilder.append(line);
                    line=bufferedReader.readLine();
                }
            }
            catch (IOException e)
            {
                Log.i("check", "getJson: ");
            }
            return stringBuilder.toString();
        }
        public Recipe extractJson(String json)
        {
            Recipe gr=null;
            ArrayList<Ingredients> ad = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(json);
               int ckng = jsonObject.getInt("readyInMinutes");
                int serv = jsonObject.getInt("servings");
                JSONArray js = jsonObject.getJSONArray("extendedIngredients");
                int i;
                for(i=0;i<js.length();i++)
                {
                    JSONObject js2 = js.getJSONObject(i);
                    String name = js2.getString("name");
                    double amt = js2.getDouble("amount");
                    ad.add(new Ingredients(name,amt));
                }
                 gr=new Recipe(ckng,serv,ad);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return gr;
        }

        @Override
        protected void onPostExecute(Recipe groceries) {
            super.onPostExecute(groceries);
            updateUI2(groceries);
        }
    }
    public void updateUI2(Recipe r)
    {
        Log.i("rererere","123123");

        reference_recipe.push().setValue(r);
        String p = textView.getText().toString();
        StringBuilder stringBuilder = new StringBuilder(p+"\n"+"cooking time  :" + r.getCkng_time()+"\n");
        stringBuilder.append("servings for   :"+r.getServings() + "\n");
        ArrayList<Ingredients> b = r.getArrayList();
        int i;
        for(i=0;i<b.size();i++)
        {
            Ingredients c = b.get(i);
            stringBuilder.append(i+1 +":  "+ c.getName()+"    "+c.getTime()+"\n");
        }
        textView.setText(stringBuilder.toString());
    }
}
