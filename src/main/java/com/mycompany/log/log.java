package com.mycompany.log;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
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
        envioEmail novoEmail = new envioEmail();
    }
    private static void converterToJson(TransfJson transfJson){
        ObjectMapper mapper = new ObjectMapper();
        try{
            String json = mapper.writeValueAsString(transfJson);
            if(json.contains("deprecated")){
                System.out.println("Objeto em JSON: "+json);
            }
        }catch(JsonProcessingException e){
            e.printStackTrace();
        } 
    }
    public class envioEmail {

        String emailPadrao = "le34661@gmail.com";
        String senhaPadrao = "lucas@60";
        public envioEmail(){

            SimpleEmail email = new SimpleEmail();

            email.setDebug(true);
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator(emailPadrao, senhaPadrao ));
            email.setSSLOnConnect(true);
            try{

                email.setFrom(emailPadrao);
                email.setSubject("Teste de funcionamento");
                email.setMsg("Testando o codigo do email");
                email.addTo("lucasjarandia.1428@aluno.saojudas.br");
                email.send();
            }catch(EmailException e){

                System.out.println("ERRO "+ e.getMessage());
            }
        }

    }
}