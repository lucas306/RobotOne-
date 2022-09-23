package com.mycompany.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;  
import org.jsoup.nodes.Document;  
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.PasswordAuthentication;

/**
 * 
 * @author lucas.jarandia
 * @version 1.0
 */
public class log {
        
    public static void envioDeEmail(List lista) {
    
        String emailEnvioPrimario = "";
        String senhaEnvioPrimario = "";
        String emailDestinatarioPrimario = "lucasjarandia.1428@aluno.saojudas.br";

        String emailEnvioSecundario = "le34661@gmail.com";
        String senhaEnvioSecundario = "tuygvkhdvfbjzzwd";
        String emailDestinatarioSecundario = "lucasjarandia.1428@aluno.saojudas.br";
        boolean confirmaEnvio = false;
        
        
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        
        
            
        Session sessionPri = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(emailEnvioPrimario,
                senhaEnvioPrimario);
            }
        });
        sessionPri.setDebug(true);
        String msgBody = ""+lista.toString().replace("[","").replace("]", "").replace(",", "").replace("{", "").replace("}", "");

        try {

            Message msg = new MimeMessage(sessionPri);
            msg.setFrom(new InternetAddress(emailEnvioPrimario));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailDestinatarioPrimario));
            msg.setSubject("POLITICAS DO GOOGLE ANDROID MANAGEMENT API");
            msg.setContent(msgBody, "html");
            msg.setText(msgBody);
            Transport.send(msg);
            confirmaEnvio = true;

        }catch(AddressException e) { System.out.println(e.toString());
        }catch(MessagingException e) { System.out.println(e.toString());}
        
        if(confirmaEnvio == false){
            
            Session sessionSec = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(emailEnvioSecundario,
                    senhaEnvioSecundario);
                }
            });
            sessionSec.setDebug(true);
            String msgBodySenc = lista.toString();

            try {

                Message msg = new MimeMessage(sessionSec);
                msg.setFrom(new InternetAddress(emailEnvioSecundario));
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailDestinatarioSecundario));
                msg.setSubject("POLITICAS DO GOOGLE ANDROID MANAGEMENT API");
                msg.setContent(msgBodySenc, "text/html");
                msg.setText(msgBodySenc);
                Transport.send(msg);
                confirmaEnvio = true;

            }catch(AddressException e) { System.out.println(e.toString());
            }catch(MessagingException e) { System.out.println(e.toString());}
        }
        
    }

    public static class TransfJson {

        private String namePolicy;
        public TransfJson(String namePolicy) {
           
            this.namePolicy = namePolicy;   
        }
        public TransfJson(){}
        public String getNamePolicy() {return namePolicy;}
        public void setNamePolicy(String namePolicy) {this.namePolicy = namePolicy;}      
    }
    
    public static void main(String[] args) throws IOException {
        
        String URL = "https://developers.google.com/android/management/reference/rest/v1/enterprises.policies";
        Document doc = Jsoup.connect(URL).get();
        Element table = doc.getElementById("Policy.FIELDS-table");
        Element tbody = table.getElementsByTag("tbody").first();
        List<Element> tr = tbody.getElementsByTag("tr");
        List<TransfJson> tagList = new ArrayList<>();
        
        for(Element trs: tr) {
            
            List<Element> attributes = trs.getElementsByTag("td");
            TransfJson transfJson = new TransfJson(attributes.get(0).text());
            tagList.add(transfJson);
        }
        
        ObjectMapper mapper = new ObjectMapper();
        List<String> lista = new ArrayList<String>();
        
        try{
            
            for(TransfJson transfJson: tagList) {
                
                String Json = mapper.writeValueAsString(transfJson);
                if(Json.contains("deprecated")) {
                    
                    lista.add(Json+"\n");}
            } 
        }catch(JsonProcessingException e) {
            
            e.printStackTrace();
        }
        envioDeEmail(lista); 
    }

}
