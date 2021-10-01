package com.vision.nakala.jms.listener;

import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Slf4j
@Component
public class MyMessageListener {

	@Autowired
	private JavaMailSender emailSender;

	@JmsListener(destination = "${input.queue}")
	public void onMessageReceived(final Message message) throws JMSException {
		if (message instanceof TextMessage) {
			String text = ((TextMessage) message).getText();
			System.out.println("Message reçu : \n" + text);
			
			JSONParser parser = new JSONParser();
			
			try {
				JSONObject object = (JSONObject) parser.parse(text);
				
				String matricule = (String)object.get("matricule");
				
				sendSimpleMessage("ibrahima-mamadou.ndiaye@atos.net", "Création de l''agent n° : "+matricule, text);
			} catch (ParseException e) {
				log.error("Erreur lors du parsing des données reçu du JMS : ", e);
			}
			
			log.debug("Message reçu : \n" + text);
		}
	}

	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("ndiayeibou2@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);
	}
}
