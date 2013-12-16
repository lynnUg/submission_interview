package com.example.gpstracking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.support.v4.app.FragmentActivity;
import android.provider.Settings;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
public class MainActivity extends FragmentActivity {
	
	
	GPSTracker gps;// GPSTracker class
	private boolean isBusy = false;//this flag to indicate whether your async task completed or not
	private Handler handler = new Handler();// handler to handle scheduling
	private GoogleMap map;// google map
	double latitude ;
    double longitude ;
	String androidId ; 
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
       
       gps=new GPSTracker(MainActivity.this);
       androidId = Settings.Secure.getString(getContentResolver(), 
               Settings.Secure.ANDROID_ID);
       map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
  			    .getMap();

       startHandler();
        
       
    }
    //Handler that responsible for starting the async task that sets off the http post and get
    public void startHandler()
    {
       handler.postDelayed(new Runnable()
       {

           @Override
           public void run()
           {
               if(!isBusy)
               	{
               		// check if GPS enabled		
                   	if(gps.canGetLocation())
                   		{
                   	
                   		latitude = gps.getLatitude();
                   		longitude = gps.getLongitude();
                   		
                   		callAysncTask();
                   		// \n is for new line
                   		
                   		}
                   	else
                   		{
                   			// can't get location
                   			// GPS or Network is not enabled
                   			// Ask user to enable GPS/network in settings
                   			gps.showSettingsAlert();
                   			
                   		}
                   
               		
               	}

           	    startHandler();
           }
       }, (1000));
    }
    // method call async task
   private void callAysncTask()
    {
       
   	new HttpAsyncTask().execute("http://dashboardlynn.herokuapp.com/gps/?format=json");
    }

  // http post task to webserver
   public static void POST(String id , double lat , double longt)
   {
  	 	// Creating HTTP client
   	HttpClient httpClient = new DefaultHttpClient();
   	// Creating HTTP Post
   	HttpPost httpPost = new HttpPost("http://dashboardlynn.herokuapp.com/gps/");
   	// Building post parameters
   	// key and value pair
   	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
   	nameValuePair.add(new BasicNameValuePair("device_name", id));
   	nameValuePair.add(new BasicNameValuePair("lat", String.valueOf(lat)));
   	nameValuePair.add(new BasicNameValuePair("longt",String.valueOf(longt)));

   	// Url Encoding the POST parameters
   	try 
   		{
   			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
   		} 
   	catch (UnsupportedEncodingException e) 
   		{
   			// writing error to Log
   			e.printStackTrace();
   		}

      // Making HTTP Request
      try 
      		{
          		HttpResponse response = httpClient.execute(httpPost);
          		// writing response to log
          		Log.d("Http Response:", response.toString());
      		}
      catch (ClientProtocolException e) 
      		{
          		// writing exception to log
          		e.printStackTrace();
      		}
      catch (IOException e) 
      		{
          		// writing exception to log
   	   		e.printStackTrace();

      		}
   }
  
   //http get from web server
   public static String GET(String url) 
   {
       InputStream inputStream = null;
       String result = "";
       System.out.print("here");
       try 
       	{

       		// create HttpClient
           	HttpClient httpclient = new DefaultHttpClient();

           	// make GET request to the given URL
           	HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

           	// receive response as inputStream
           	inputStream = httpResponse.getEntity().getContent();
           	JSONObject json=null;
           	// convert inputstream to string
           	if(inputStream != null)
           	
           		result = convertInputStreamToString(inputStream);
           	else
           		result = "Did not work!";

       	} 
       catch (Exception e) 
       	{
           	Log.d("InputStream", e.getLocalizedMessage());
       	}

       return result;
   }
  //convert json input to string
   private static String convertInputStreamToString(InputStream inputStream) throws IOException
   {
       BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
       String line = "";
       String result = "";
       while((line = bufferedReader.readLine()) != null)
           result += line;

       inputStream.close();
       return result;

   }

  
   private class HttpAsyncTask extends AsyncTask<String, Void, String>
   {
	   //perform async tasks in background
       @Override
       protected String doInBackground(String... urls)
       	{
    ;
       		
       		POST(androidId,latitude ,longitude);
       		isBusy=true;
       		return GET(urls[0]);
       		
       	}
       // onPostExecute displays the results of the AsyncTask.
       @Override
       protected void onPostExecute(String result) 
       {
          // Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
           isBusy=false;
           try 
           	{
					JSONArray json =  new JSONArray(result);
					for ( int i=0;i<json.length();++i)
					{
						//for each object draw marker
						JSONObject js=json.getJSONObject(i);
						double lat=js.getDouble("lat");
						double longt=js.getDouble("longt");
						//Toast.makeText(getBaseContext(), "lat"+lat+"longt"+longt, Toast.LENGTH_LONG).show();
						LatLng loc = new LatLng(lat, longt);
                   		
               			if (map !=null){
               			  map.addMarker(new MarkerOptions()
               	              .position(loc)
               	              .title("other users")
               	              .snippet("Lynn is cool")
               	             .icon(BitmapDescriptorFactory
               	              .fromResource(R.drawable.ic_launcher)));
               			  
               			}
					}
					
           	} 
           catch (JSONException e) 
           	{
					// TODO Auto-generated catch block
					e.printStackTrace();
					//Toast.makeText(getBaseContext(), "FAILED", Toast.LENGTH_LONG).show();
					
						
           	} 
					
				
           	}
       
           
          
       }
    }
