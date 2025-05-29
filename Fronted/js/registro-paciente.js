const API_PACIENTES_URL = "http://localhost:8085/api/v1/pacientes";

// Al cargar la página, se configura el envío del formulario.
document.addEventListener("DOMContentLoaded", function () {
  document.getElementById("registroForm").addEventListener("submit", registerPaciente);
});

/**
 * Muestra un mensaje de alerta personalizado.
 * @param {string} message El mensaje a mostrar.
 * @param {string} type "success" o "error" para determinar el estilo.
 */
function showAlert(message, type) {
  const alertContainer = document.getElementById("alert-container");
  alertContainer.textContent = message;
  alertContainer.className = ""; // Limpia clases previas
  alertContainer.classList.add(type === "success" ? "alert-success" : "alert-error");
  alertContainer.style.display = "block";
  // Forzar reflow para que la transición funcione
  void alertContainer.offsetWidth;
  alertContainer.style.opacity = 1;
  
  // Después de 3 segundos, se desvanece el mensaje.
  setTimeout(() => {
    alertContainer.style.opacity = 0;
    setTimeout(() => {
      alertContainer.style.display = "none";
    }, 500);
  }, 3000);
}

// Validación del email
function isValidEmail(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

// Validación del teléfono
function isValidTelefono(telefono) {
  const telefonoRegex = /^\+?\d+$/;
  return telefonoRegex.test(telefono);
}

// Registrar un nuevo paciente
async function registerPaciente(event) {
  event.preventDefault(); // Evitar el envío por defecto

  // Obtener valores del formulario
  const nombre = document.getElementById("paciente-nombre").value.trim();
  const email = document.getElementById("paciente-email").value.trim();
  const telefono = document.getElementById("paciente-telefono").value.trim();
  const receiveNotifications = document.getElementById("paciente-receive-notifications").checked;

  // Validaciones de campo
  if (!nombre) {
    showAlert("El nombre es obligatorio.", "error");
    return;
  }
  if (!email) {
    showAlert("El correo es obligatorio.", "error");
    return;
  }
  if (!isValidEmail(email)) {
    showAlert("El correo no tiene un formato válido.", "error");
    return;
  }
  if (!telefono) {
    showAlert("El teléfono es obligatorio.", "error");
    return;
  }
  if (!isValidTelefono(telefono)) {
    showAlert("El teléfono debe contener únicamente dígitos y el símbolo '+' opcionalmente.", "error");
    return;
  }

  // Preparar el payload conforme al DTO:
  // Si el paciente desea recibir notificaciones, recordatoriosSuspendidos será false.
  const payload = {
    nombre: nombre,
    email: email,
    telefono: telefono,
    recordatoriosSuspendidos: !receiveNotifications
  };

  try {
    const response = await fetch(API_PACIENTES_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });
    if (!response.ok) {
      throw new Error("Error al registrar el paciente.");
    }
    const data = await response.json();
    showAlert("Paciente registrado exitosamente.", "success");
    // Limpiar el formulario
    document.getElementById("registroForm").reset();
  } catch (error) {
    console.error("Error al registrar paciente:", error);
    showAlert(error.message, "error");
  }
}
