package com.sena.hospital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class emailService {

    @Autowired
    private JavaMailSender javaMailSender;

    // Método para enviar correos con contenido HTML
    public boolean sendEmail(String addressMail, String subject, String bodyMail) throws MessagingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(addressMail);
            helper.setSubject(subject);
            helper.setText(bodyMail, true);
            javaMailSender.send(message);
            return true;
        } catch (MessagingException e) {
            System.out.println("Error en sendEmail: " + e.getMessage());
        }
        return false;
    }

    // Envío de recordatorio para medicamento
    public void sendMedicationReminder(String addressMail, String patientName, String medicamento, String dosis, String horarios) {
        String subject = "Recordatorio de Medicamento - Clínica Salud y Vida";
        String bodyMail = 
            "<html>" +
            "<head>" +
            "  <style>" +
            "    .container { background-color: #ffffff; padding: 20px; border: 1px solid #ddd; border-radius: 10px; text-align: center; }" +
            "    .header { color: #007BFF; font-size: 24px; margin-bottom: 20px; }" +
            "    .text { color: #333333; font-size: 16px; margin-bottom: 10px; }" +
            "    .medInfo { font-size: 18px; font-weight: bold; color: #28A745; }" +
            "    .medImg { margin-top: 20px; max-width: 150px; }" +
            "  </style>" +
            "</head>" +
            "<body style='font-family: Arial, sans-serif; background-color: #f2f2f2; padding: 20px;'>" +
            "  <div class='container'>" +
            "    <h1 class='header'>Recordatorio de Medicamento</h1>" +
            "    <p class='text'>Estimado/a " + patientName + ",</p>" +
            "    <p class='text'>Es momento de tomar su medicamento:</p>" +
            "    <p class='medInfo'>" + medicamento + " - " + dosis + "</p>" +
            "    <p class='text'>Horarios programados: " + horarios + "</p>" +
            "    <p class='text'>Por favor, confirme la toma desde la plataforma.</p>" +
            "    <img src='cid:medImage' alt='Recordatorio de Medicamento' class='medImg' />" +
            "  </div>" +
            "</body>" +
            "</html>";
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(addressMail);
            helper.setSubject(subject);
            helper.setText(bodyMail, true);
            
            // Agregar imagen embebida: asegúrate de colocar la imagen en src/main/resources/static/images/medImage.png
            ClassPathResource imageResource = new ClassPathResource("static/images/medImage.png");
            helper.addInline("medImage", imageResource);
            
            javaMailSender.send(message);
            System.out.println("Recordatorio enviado a: " + addressMail);
        } catch (MessagingException e) {
            System.err.println("Error al enviar recordatorio: " + e.getMessage());
        }
    }

    // Los otros métodos (plantillas para nueva cuenta, recuperación, activación, etc.) se mantienen tal cual
    // ...
}
