const API_PACIENTES_URL = "http://localhost:8085/api/pacientes";

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
  alertContainer.className = ""; // Se limpian las clases previas.
  alertContainer.classList.add(type === "success" ? "alert-success" : "alert-error");
  alertContainer.style.display = "block";

  // Forzar reflow para que la transición funcione.
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

// Validación del email.
function isValidEmail(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

// Validación del teléfono: permite opcionalmente un '+' al inicio y luego solo dígitos.
function isValidTelefono(telefono) {
  const telefonoRegex = /^\+?\d+$/;
  return telefonoRegex.test(telefono);
}

// Registrar un nuevo paciente con manejo mejorado de excepciones.
async function registerPaciente(event) {
  event.preventDefault(); // Prevenir el envío por defecto.

  // Obtener los valores de los campos.
  const nombre = document.getElementById("paciente-nombre").value.trim();
  const email = document.getElementById("paciente-email").value.trim();
  const telefono = document.getElementById("paciente-telefono").value.trim();
  const receiveNotifications = document.getElementById("paciente-receive-notifications").checked;

  // Realizar validaciones básicas.
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

  // Preparar el payload conforme al DTO.
  // Si el paciente desea recibir notificaciones, recordatoriosSuspendidos será false.
  const payload = {
    nombre: nombre,
    email: email,
    telefono: telefono,
    recordatoriosSuspendidos: !receiveNotifications
  };

  try {
    // Realizar la petición POST al backend.
    const response = await fetch(API_PACIENTES_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });
    
    // Determinar el método de parseo según el Content-Type.
    const contentType = response.headers.get("Content-Type");
    let data;
    if (contentType && contentType.includes("application/json")) {
      data = await response.json();
    } else {
      data = await response.text();
    }
    
    // Si el response no es OK, se intenta extraer un mensaje amigable.
    if (!response.ok) {
      let errorMsg = "Error al registrar el paciente.";
      if (data) {
        if (typeof data === "object" && data.message) {
          errorMsg = data.message;
        } else if (typeof data === "string") {
          try {
            const parsed = JSON.parse(data);
            if (parsed.message) {
              errorMsg = parsed.message;
            } else {
              errorMsg = data;
            }
          } catch (parseErr) {
            errorMsg = data;
          }
        }
      }
      // Imprimir en consola el error detallado.
      console.error("Detalle del error:", data);
      throw new Error(errorMsg);
    }
    
    // Si todo sale bien, mostrar confirmación y limpiar el formulario.
    showAlert("Paciente registrado exitosamente.", "success");
    document.getElementById("registroForm").reset();
  } catch (error) {
    // En caso de error, se muestra solamente el mensaje amigable en consola y en la alerta.
    console.error("Error al registrar paciente:", error.message);
    showAlert(error.message, "error");
  }
}
