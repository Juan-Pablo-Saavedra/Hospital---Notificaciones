package com.sena.hospital.Scheduler;

import com.sena.hospital.DTO.bitacorarecordatorioDTO;
import com.sena.hospital.DTO.pacientemedicamentoDTO;
import com.sena.hospital.model.medicamento;
import com.sena.hospital.model.paciente;
import com.sena.hospital.repository.Ipaciente;
import com.sena.hospital.repository.Imedicamento;
import com.sena.hospital.service.bitacorarecordatorioService;
import com.sena.hospital.service.emailService;
import com.sena.hospital.service.pacientemedicamentoService;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ProgramadorRecordatorios {

    @Autowired
    private pacientemedicamentoService servicioPacienteMedicamento;
    
    @Autowired
    private Ipaciente repositorioPaciente;
    
    @Autowired
    private Imedicamento repositorioMedicamento;
    
    @Autowired
    private emailService servicioCorreo;
    
    @Autowired
    private bitacorarecordatorioService servicioBitacora;
    
    // Formateador para el formato de hora "HH:mm"
    private final DateTimeFormatter formateadorHora = DateTimeFormatter.ofPattern("HH:mm");
    
    /**
     * Método programado para ejecutarse cada 5 minutos.
     * Recorre las asignaciones activas de medicamentos y, si la hora actual se encuentra en
     * la ventana de 5 minutos a partir del horario programado, envía el recordatorio por correo
     * y registra el evento en la bitácora.
     */
    @Scheduled(fixedRate = 5 * 60 * 1000) // 5 minutos en milisegundos
    public void verificarRecordatorios() {
        System.out.println("Ejecutando verificación de recordatorios a las: " + LocalDateTime.now());
        
        // Obtener las asignaciones activas
        List<pacientemedicamentoDTO> asignaciones = servicioPacienteMedicamento.getActiveMedicationsForNow();
        LocalTime horaActual = LocalTime.now();
        
        for (pacientemedicamentoDTO asignacion : asignaciones) {
            // Obtener los datos del paciente
            Optional<paciente> optPaciente = repositorioPaciente.findById(asignacion.getPacienteId());
            if (!optPaciente.isPresent()) {
                continue;
            }
            paciente paciente = optPaciente.get();
            
            // Si el paciente tiene suspendidos los recordatorios, se omite
            if (paciente.isRecordatoriosSuspendidos()) {
                continue;
            }
            
            // Obtener los datos del medicamento
            Optional<medicamento> optMedicamento = repositorioMedicamento.findById(asignacion.getMedicamentoId());
            if (!optMedicamento.isPresent()) {
                continue;
            }
            medicamento med = optMedicamento.get();
            
            // Verificar que el campo de horarios no esté vacío y parsearlos
            String horarios = med.getHorarios();
            if (horarios == null || horarios.trim().isEmpty()) {
                continue;
            }
            String[] horas = horarios.split(",");
            for (String h : horas) {
                String horaStr = h.trim();
                try {
                    LocalTime horaProgramada = LocalTime.parse(horaStr, formateadorHora);
                    // Verificar si la hora actual se encuentra en la ventana [horaProgramada, horaProgramada + 5 minutos)
                    if (!horaActual.isBefore(horaProgramada) && horaActual.isBefore(horaProgramada.plusMinutes(5))) {
                        // Enviar correo recordatorio
                        servicioCorreo.sendMedicationReminder(
                            paciente.getEmail(),
                            paciente.getNombre(),
                            med.getNombre(),
                            med.getDosis(),
                            horarios
                        );
                        // Registrar en la bitácora el envío del recordatorio
                        bitacorarecordatorioDTO bitacoraDto = new bitacorarecordatorioDTO();
                        bitacoraDto.setPacienteId(paciente.getId());
                        bitacoraDto.setMedicamentoId(med.getId());
                        bitacoraDto.setFechaEnvio(LocalDateTime.now());
                        bitacoraDto.setConfirmado(false);
                        servicioBitacora.createRecordatorio(bitacoraDto);
                    }
                } catch (Exception e) {
                    System.err.println("Error al parsear el horario: " + horaStr + " - " + e.getMessage());
                }
            }
        }
    }
}
