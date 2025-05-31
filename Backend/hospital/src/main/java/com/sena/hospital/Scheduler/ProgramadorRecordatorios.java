package com.sena.hospital.Scheduler;

import com.sena.hospital.DTO.medicamentoDTO;
import com.sena.hospital.DTO.bitacorarecordatorioDTO;
import com.sena.hospital.service.medicamentoService;
import com.sena.hospital.service.emailService;
import com.sena.hospital.service.bitacorarecordatorioService;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProgramadorRecordatorios {

    @Autowired
    private medicamentoService medicamentoService;
    
    @Autowired
    private emailService servicioCorreo;
    
    @Autowired
    private bitacorarecordatorioService servicioBitacora;
    
    // Formateador para el formato de hora "HH:mm"
    private final DateTimeFormatter formateadorHora = DateTimeFormatter.ofPattern("HH:mm");
    
    /**
     * Este método se ejecuta cada 5 minutos, revisando el listado de medicamentos.
     * Para cada medicamento con recordatorios activados, se parsea la cadena CSV de horarios;
     * si la hora actual se encuentra en la ventana [horarioProgramado, horarioProgramado + 5 minutos),
     * se envía el recordatorio (invocando el servicio de correo) y se registra el evento en la bitácora.
     */
    @Scheduled(cron = "0 */5 * * * *")  // Ejecuta cada 5 minutos.
    public void verificarRecordatorios() {
        System.out.println("[" + LocalDateTime.now() + "] Ejecutando verificación de recordatorios...");
        
        // Obtener todos los medicamentos
        List<medicamentoDTO> medicamentos = medicamentoService.getAllMedicamentos();
        // Obtener la hora actual formateada (por ejemplo "14:00")
        String horaActualStr = LocalTime.now().format(formateadorHora);
        LocalTime horaActual = LocalTime.parse(horaActualStr, formateadorHora);
        
        for (medicamentoDTO med : medicamentos) {
            if (med.isNotificaciones()) {
                String horariosCSV = med.getHorarios();
                if (horariosCSV == null || horariosCSV.trim().isEmpty()) {
                    continue;
                }
                // Separar los horarios (se asume que están separados por comas)
                String[] horas = horariosCSV.split(",");
                // Comprobar cada horario
                for (String h : horas) {
                    String horaStr = h.trim();
                    try {
                        LocalTime horaProgramada = LocalTime.parse(horaStr, formateadorHora);
                        // Verificar si la hora actual está dentro de [horaProgramada, horaProgramada + 5 minutos)
                        if (!horaActual.isBefore(horaProgramada) && horaActual.isBefore(horaProgramada.plusMinutes(5))) {
                            // Dispara la acción: enviar correo y registrar en bitácora.
                            // (Simulamos la obtención del correo; en producción, asegúrate de disponerlo en tu DTO o consulta)
                            String correo = "paciente" + med.getPacienteId() + "@example.com";
                            
                            servicioCorreo.sendMedicationReminder(
                                    correo,
                                    med.getPacienteNombre(), 
                                    med.getNombre(),
                                    med.getDosis(),
                                    med.getHorarios() // Se envía la cadena completa
                            );
                            
                            // Registrar en la bitácora
                            bitacorarecordatorioDTO bitacoraDto = new bitacorarecordatorioDTO();
                            bitacoraDto.setPacienteId(med.getPacienteId());
                            bitacoraDto.setMedicamentoId(med.getId());
                            bitacoraDto.setFechaEnvio(LocalDateTime.now());
                            bitacoraDto.setConfirmado(false);
                            servicioBitacora.createRecordatorio(bitacoraDto);
                            
                            // Una vez enviado para este medicamento, no se revisan más horarios.
                            break;
                        }
                    } catch (Exception e) {
                        System.err.println("Error al parsear el horario: " + horaStr + " - " + e.getMessage());
                    }
                } // for cada horario
            } // if notificaciones
        } // for cada medicamento
    }
}
