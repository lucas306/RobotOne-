package com.mycompany.log;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

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
 