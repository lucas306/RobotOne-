package com.mycompany.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;  
import org.jsoup.nodes.Document;  
import org.jsoup.nodes.Element;  
import org.jsoup.select.Evaluator;

public class log {
    
    public static class TransfJson {

        private String namePolicy;
        private String textPolicy;
        
        public TransfJson(String namePolicy, String textPolicy){
           
            this.namePolicy = namePolicy;
            this.textPolicy = textPolicy;    
        }
        
        public TransfJson(){}
        public String getNamePolicy(){return namePolicy;}
        public void setNamePolicy(String namePolicy){this.namePolicy = namePolicy;}
        public String getTextPolicy(){return textPolicy;}
        public void setTextPolicy(String textPolicy){this.textPolicy = textPolicy;}           
    }
    
    public static void main(String[] args) throws IOException {
      
        String URL = "https://developers.google.com/android/management/reference/rest/v1/enterprises.policies";
        Document doc = Jsoup.connect(URL).get();
        Element table = doc.getElementById("Policy.FIELDS-table");
        Element tbody = table.getElementsByTag("tbody").first();
        List<Element> tr = tbody.getElementsByTag("tr");
        List<TransfJson> tagList = new ArrayList<>();
        
        for(Element trs: tr){
            List<Element> attributes = trs.getElementsByTag("td");
            TransfJson transfJson = new TransfJson(
                
                attributes.get(0).text(),
                attributes.get(1).text()
            );
            tagList.add(transfJson);
        }
        
        for(TransfJson transfJson: tagList){

            converterToJson(transfJson);            
        }
    }
    private static void converterToJson(TransfJson transfJson){
        ObjectMapper mapper = new ObjectMapper();
        try{
            String json = mapper.writeValueAsString(transfJson);
            if(json.contains("deprecated")){
                System.out.println("Objeto em JSON: "+json);
            }
        envioEmail novoEmail = new envioEmail();
        }catch(JsonProcessingException e){
            e.printStackTrace();
        } 
    }
}