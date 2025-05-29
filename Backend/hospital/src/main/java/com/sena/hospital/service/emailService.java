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

    // Método genérico para enviar un correo con contenido HTML
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
            System.out.println(e.getMessage());
        }
        return false;
    }

    // Plantilla para Nueva Cuenta (opcional, si se requiere que el paciente reciba bienvenida)
    public void sendNewAccountEmail(String addressMail) {
        String subject = "Bienvenido a Clínica Salud y Vida";
        String bodyMail = 
            "<html>" +
            "<head>" +
            "  <style>" +
            "    .container { background-color: #ffffff; border: 1px solid #ddd; padding: 20px; border-radius: 10px; }" +
            "    .header { color: #007BFF; font-size: 24px; margin-bottom: 10px; }" +
            "    .text { color: #333333; font-size: 16px; line-height: 1.5; }" +
            "  </style>" +
            "</head>" +
            "<body style='font-family: Arial, sans-serif; background-color: #f2f2f2; padding: 20px;'>" +
            "  <div class='container'>" +
            "    <h1 class='header'>¡Bienvenido a Clínica Salud y Vida!</h1>" +
            "    <p class='text'>Su cuenta ha sido creada exitosamente. Nos complace poder ayudarle a cuidar su salud.</p>" +
            "  </div>" +
            "</body>" +
            "</html>";
        try {
            sendEmail(addressMail, subject, bodyMail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // Plantilla para Recuperación de Contraseña
    public void sendForgotPasswordEmail(String addressMail) {
        String subject = "Recuperación de Contraseña - Clínica Salud y Vida";
        String bodyMail = 
            "<html>" +
            "<head>" +
            "  <style>" +
            "    .container { background-color: #ffffff; border: 1px solid #ddd; padding: 20px; border-radius: 10px; }" +
            "    .header { color: #007BFF; font-size: 24px; margin-bottom: 10px; }" +
            "    .text { color: #333333; font-size: 16px; line-height: 1.5; }" +
            "    .button { background-color: #28A745; color: #ffffff; padding: 10px 20px; border-radius: 5px; text-decoration: none; }" +
            "  </style>" +
            "</head>" +
            "<body style='font-family: Arial, sans-serif; background-color: #f2f2f2; padding: 20px;'>" +
            "  <div class='container'>" +
            "    <h1 class='header'>Restablece tu Contraseña</h1>" +
            "    <p class='text'>Haz clic en el siguiente enlace para restablecer tu contraseña:</p>" +
            "    <p><a href='https://example.com/reset-password' class='button'>Restablecer Contraseña</a></p>" +
            "  </div>" +
            "</body>" +
            "</html>";
        try {
            sendEmail(addressMail, subject, bodyMail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    // Plantilla para Activación de Cuenta
    public void sendActivationEmail(String addressMail) {
        String subject = "Activación de Cuenta - Clínica Salud y Vida";
        // Código de activación fijo (en producción se debe generar dinámicamente)
        String activationCode = "123456";
        String bodyMail =
            "<html>" +
            "<head>" +
            "  <style>" +
            "    .container { background-color: #ffffff; border: 1px solid #ddd; padding: 20px; border-radius: 10px; }" +
            "    .header { color: #007BFF; font-size: 24px; margin-bottom: 10px; }" +
            "    .code { color: #28A745; font-size: 28px; font-weight: bold; margin: 10px 0; }" +
            "    .text { color: #333333; font-size: 16px; line-height: 1.5; }" +
            "  </style>" +
            "</head>" +
            "<body style='font-family: Arial, sans-serif; background-color: #f2f2f2; padding: 20px;'>" +
            "  <div class='container'>" +
            "    <h1 class='header'>Activa tu Cuenta</h1>" +
            "    <p class='text'>Utiliza el siguiente código para activar tu cuenta:</p>" +
            "    <p class='code'>" + activationCode + "</p>" +
            "    <p class='text'>Si no solicitaste la activación, ignora este mensaje.</p>" +
            "  </div>" +
            "</body>" +
            "</html>";
        try {
            sendEmail(addressMail, subject, bodyMail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    // Plantilla para Notificación de Cambio de Contraseña
    public void sendPasswordChangeNotification(String addressMail) {
        String subject = "Cambio de Contraseña Exitoso - Clínica Salud y Vida";
        String bodyMail = 
            "<html>" +
            "<head>" +
            "  <style>" +
            "    .container { background-color: #ffffff; border: 1px solid #ddd; padding: 20px; border-radius: 10px; }" +
            "    .header { color: #007BFF; font-size: 24px; margin-bottom: 10px; }" +
            "    .text { color: #333333; font-size: 16px; line-height: 1.5; }" +
            "  </style>" +
            "</head>" +
            "<body style='font-family: Arial, sans-serif; background-color: #f2f2f2; padding: 20px;'>" +
            "  <div class='container'>" +
            "    <h1 class='header'>Contraseña Actualizada</h1>" +
            "    <p class='text'>Su contraseña ha sido actualizada satisfactoriamente. Si usted no realizó este cambio, por favor contáctenos.</p>" +
            "  </div>" +
            "</body>" +
            "</html>";
        try {
            sendEmail(addressMail, subject, bodyMail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    // PLANTILLA PARA RECORDATORIO DE MEDICAMENTO  
    // Esta plantilla notifica al paciente para que tome su medicamento, para ser usado en la tarea programada cada 5 minutos.
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
            
            // Agregar dinamicamente la imagen embebida.
            // Se asume que la imagen se encuentra en el classpath dentro de "static/images/medImage.png".
            ClassPathResource imageResource = new ClassPathResource("static/images/medImage.png");
            helper.addInline("medImage", imageResource);
            
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
