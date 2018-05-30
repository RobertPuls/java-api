package hello;

import java.util.Arrays;

import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.util.JSONPObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String hello() {
        return "Hello";
    }

    @RequestMapping("/{searchTerm}/{responseCount}")
    public String index(@PathVariable String searchTerm, @PathVariable int responseCount) {
        return Arrays.toString(WordBank.getWords(searchTerm, responseCount));
    }

    @RequestMapping("/solr/{searchTerm}/{responseCount}")
    public JsonNode solr(@PathVariable String searchTerm, @PathVariable int responseCount) {
        String urlString = "http://172.18.5.128:8983/solr/list_simple/suggest?suggest.dictionary=Suggester&suggest.count=" + responseCount + "&suggest.q=" + searchTerm;
        
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            connection.connect();

            int status = connection.getResponseCode();

            Scanner scan = new Scanner(url.openStream());
            String inline = "";
            ObjectMapper objectMapper = new ObjectMapper();

            while(scan.hasNext()) {
                inline+=scan.nextLine();
            }
            scan.close();

            JsonNode jsonNode = objectMapper.readTree(inline);

            connection.disconnect();

            return jsonNode.get("suggest").get("Suggester");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}