const API_PACIENTES_URL = "http://localhost:8085/api/pacientes";

document.addEventListener("DOMContentLoaded", function () {
  document.getElementById("registroForm").addEventListener("submit", registerPaciente);
});

function showAlert(message, type) {
  const alertContainer = document.getElementById("alert-container");
  alertContainer.textContent = message;
  alertContainer.className = "";
  alertContainer.classList.add(type === "success" ? "alert-success" : "alert-error");
  alertContainer.style.display = "block";
  void alertContainer.offsetWidth;
  alertContainer.style.opacity = 1;
  setTimeout(() => {
    alertContainer.style.opacity = 0;
    setTimeout(() => {
      alertContainer.style.display = "none";
    }, 500);
  }, 3000);
}

function isValidEmail(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

function isValidTelefono(telefono) {
  const telefonoRegex = /^\+?\d+$/;
  return telefonoRegex.test(telefono);
}

async function registerPaciente(event) {
  event.preventDefault();
  const nombre = document.getElementById("paciente-nombre").value.trim();
  const email = document.getElementById("paciente-email").value.trim();
  const telefono = document.getElementById("paciente-telefono").value.trim();
  const receiveNotifications = document.getElementById("paciente-receive-notifications").checked;

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
    
    const contentType = response.headers.get("Content-Type");
    let data;
    if (contentType && contentType.includes("application/json")) {
      data = await response.json();
    } else {
      data = await response.text();
    }
    
    if (!response.ok) {
      let errorMsg = "Error al registrar el paciente.";
      if (data) {
        try {
          const parsed = typeof data === "object" ? data : JSON.parse(data);
          if (parsed.message) errorMsg = parsed.message;
        } catch (e) {
          errorMsg = data;
        }
      }
      console.error("Error detalle:", data);
      throw new Error(errorMsg);
    }
    
    showAlert("Paciente registrado exitosamente.", "success");
    document.getElementById("registroForm").reset();
    
    // Después de unos segundos, redirigir al panel de control.
    setTimeout(() => {
      window.location.href = "panel-control.html";  // Página del panel, crea este archivo a continuación.
    }, 1500);
  } catch (error) {
    console.error("Error al registrar paciente:", error.message);
    showAlert(error.message, "error");
  }
}
