package com.sena.hospital.controller;

import com.sena.hospital.service.emailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class emailController {

    @Autowired
    private emailService emailService;

    // Envío de recordatorio de medicamento
    // Ejemplo de uso:
    // GET /api/email/medicationReminder/test@example.com/John%20Doe/Aspirina/500mg/08:00,14:00,20:00
    @GetMapping("/medicationReminder/{email}/{patientName}/{medicamento}/{dosis}/{horarios}")
    public String sendMedicationReminder(
            @PathVariable String email,
            @PathVariable String patientName,
            @PathVariable String medicamento,
            @PathVariable String dosis,
            @PathVariable String horarios) {
        try {
            emailService.sendMedicationReminder(email, patientName, medicamento, dosis, horarios);
            return "Recordatorio enviado a " + email;
        } catch (Exception e) {
            return "Error al enviar recordatorio: " + e.getMessage();
        }
    }    
}
