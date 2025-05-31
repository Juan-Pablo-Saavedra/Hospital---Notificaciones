const API_RECORDATORIOS = "http://localhost:8085/api/recordatorios";

async function loadNotificaciones() {
  try {
    const response = await fetch(API_RECORDATORIOS);
    if (!response.ok) {
      throw new Error("Error al cargar notificaciones: " + response.status);
    }
    const notificaciones = await response.json();
    populateNotificacionesTable(notificaciones);
  } catch (error) {
    showError(error.message);
  }
}

function populateNotificacionesTable(notificaciones) {
  const tbody = document.querySelector("#notificaciones-table tbody");
  tbody.innerHTML = "";
  if (!notificaciones.length) {
    tbody.innerHTML = `<tr><td colspan="7" class="text-center">No hay registros de notificaciones.</td></tr>`;
    return;
  }
  notificaciones.forEach(n => {
    const confirmadoText = n.confirmado ? "Sí" : "No";
    let accionBtn = "";
    if (!n.confirmado) {
      accionBtn = `<button class="btn btn-success btn-sm confirmBtn" data-id="${n.id}">Confirmar</button>`;
    }
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${n.id}</td>
      <td>${n.pacienteNombre || "N/A"}</td>
      <td>${n.medicamentoNombre || "N/A"}</td>
      <td>${n.fechaEnvio || "N/A"}</td>
      <td>${confirmadoText}</td>
      <td>${n.observacion || ""}</td>
      <td>${accionBtn}</td>
    `;
    tbody.appendChild(tr);
  });
  // Añadir listener a cada botón de confirmación.
  document.querySelectorAll(".confirmBtn").forEach(button => {
    button.addEventListener("click", function () {
      const id = this.getAttribute("data-id");
      confirmNotificacion(id);
    });
  });
}

function showError(message) {
  const tbody = document.querySelector("#notificaciones-table tbody");
  tbody.innerHTML = `<tr><td colspan="7" class="text-center text-danger">${message}</td></tr>`;
}

async function confirmNotificacion(id) {
  try {
    const response = await fetch(`${API_RECORDATORIOS}/confirm/${id}`, {
      method: "PUT"
    });
    if (!response.ok) {
      throw new Error("Error al confirmar notificación: " + response.status);
    }
    await loadNotificaciones();
  } catch (error) {
    alert(error.message);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  loadNotificaciones();
});
