package com.sena.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.sena.hospital.service.emailService;

@RestController
public class emailController {

    @Autowired
    private emailService emailService;

    // Envío de correo para nueva cuenta
    // Ej: GET /AccountEmail/{email}
    @GetMapping("/AccountEmail/{email}")
    public String sendNewAccountEmail(@PathVariable String email) {
        emailService.sendNewAccountEmail(email);
        return "Correo de nueva cuenta enviado a " + email;
    }

    // Envío de correo para recuperación de contraseña
    // Ej: GET /forgotPasswordEmail/{email}
    @GetMapping("/forgotPasswordEmail/{email}")
    public String sendForgotPasswordEmail(@PathVariable String email) {
        emailService.sendForgotPasswordEmail(email);
        return "Correo de recuperación de contraseña enviado a " + email;
    }

    // Envío de correo para activación de cuenta
    // Ej: GET /activationEmail/{email}
    @GetMapping("/activationEmail/{email}")
    public String sendActivationEmail(@PathVariable String email) {
        emailService.sendActivationEmail(email);
        return "Correo de activación enviado a " + email;
    }    

    // Envío de notificación de cambio de contraseña
    // Ej: GET /passwordChangeNotification/{email}
    @GetMapping("/passwordChangeNotification/{email}")
    public String sendPasswordChangeNotification(@PathVariable String email) {
        emailService.sendPasswordChangeNotification(email);
        return "Notificación de cambio de contraseña enviada a " + email;
    }
    
    // Envío de recordatorio de medicamento
    // Ej: GET /medicationReminder/{email}/{patientName}/{medicamento}/{dosis}/{horarios}
    @GetMapping("/medicationReminder/{email}/{patientName}/{medicamento}/{dosis}/{horarios}")
    public String sendMedicationReminder(
            @PathVariable String email,
            @PathVariable String patientName,
            @PathVariable String medicamento,
            @PathVariable String dosis,
            @PathVariable String horarios) {
        emailService.sendMedicationReminder(email, patientName, medicamento, dosis, horarios);
        return "Recordatorio de medicamento enviado a " + email;
    }
}
