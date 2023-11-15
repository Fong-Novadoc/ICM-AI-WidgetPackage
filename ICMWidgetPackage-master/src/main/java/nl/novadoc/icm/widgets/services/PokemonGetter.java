package nl.novadoc.icm.widgets.services;

import com.ibm.json.java.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
public class PokemonGetter {
        public static JSONObject getPokemonData(String name) {
            JSONObject jsonResponse = new JSONObject();

            try {
                // Specify the URL
                String apiUrl = "https://pokeapi.co/api/v2/pokemon/"+name;

                // Create a URL object
                URL url = new URL(apiUrl);

                // Open a connection to the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Set the request method to GET
                connection.setRequestMethod("GET");

                // Get the response code
                int responseCode = connection.getResponseCode();
                System.out.println("Response Code: " + responseCode);

                // Read the response from the API
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer response = new StringBuffer();
                
                
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Close the BufferedReader
                reader.close();

                // Disconnect the HttpURLConnection
                connection.disconnect();

                // Parse the JSON response
                jsonResponse.put("response", response) ; 
            } catch (IOException e) {
                e.printStackTrace();
        }

        return jsonResponse;
    }
}
