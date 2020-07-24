package co.nxlog.qa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;


public class TestConfig {
    public String baseUrl;
    public String auth;
    public String userId;


    public TestConfig(String profile){
        Gson gson = new Gson();

        URL url = Thread.currentThread().getContextClassLoader().getResource("configuration/"+profile+".json");

        try (Reader reader = new FileReader(url.getPath())) {

            // Convert JSON File to Java Object
            TestConfig t = gson.fromJson(reader, TestConfig.class);
            baseUrl =t.baseUrl;
            auth =t.auth;
            userId=t.userId;

            System.out.println("Config loaded!"+baseUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public TestConfig(String baseUrl,String auth,String userId){
       this.baseUrl =baseUrl;
       this.auth =auth;
       this.userId=userId;
    }
}
