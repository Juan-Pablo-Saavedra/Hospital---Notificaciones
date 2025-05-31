const API_MEDICAMENTO = "http://localhost:8085/api/medicamentos";
const API_PACIENTE = "http://localhost:8085/api/pacientes";

// Cachés para almacenar medicamentos y pacientes
let medicamentosCache = [];
let pacientesCache = [];

/* ==========================
   Función para Mostrar Alertas
========================== */
function showAlert(message, type = "info") {
  const container = document.getElementById("panel-alert-container");
  if (container) {
    container.innerHTML = `<div class="alert alert-${type}">${message}</div>`;
    setTimeout(() => { container.innerHTML = ""; }, 3000);
  } else {
    alert((type === "danger") ? `Error: ${message}` : message);
  }
}

/* ==========================
   Función para Obtener el Nombre del Paciente por ID
========================== */
function getPacienteNameById(id) {
  const paciente = pacientesCache.find(p => p.id == id);
  return paciente ? paciente.nombre : "N/A";
}

/* ==========================
   Cargar Opciones de Pacientes
========================== */
async function loadPacienteOptions() {
  try {
    const response = await fetch(API_PACIENTE);
    if (!response.ok) {
      throw new Error("Error al cargar pacientes: " + response.status);
    }
    const data = await response.json();
    // Se asume que el endpoint retorna directamente un arreglo; de lo contrario, busca la propiedad "pacientes"
    pacientesCache = Array.isArray(data) ? data : (data.pacientes ? data.pacientes : []);
    populatePacienteSelect("paciente-select");
    populatePacienteSelect("edit-paciente-select");
  } catch (error) {
    showAlert("Error al cargar pacientes", "danger");
  }
}

function populatePacienteSelect(selectId) {
  const select = document.getElementById(selectId);
  if (!select) return;
  select.innerHTML = '<option value="">Seleccione un paciente</option>';
  pacientesCache.forEach(paciente => {
    if (paciente.id && paciente.nombre) {
      const option = document.createElement("option");
      option.value = paciente.id;
      option.text = paciente.nombre;
      select.appendChild(option);
    }
  });
}

/* ==========================
   Cargar y Mostrar Medicamentos
========================== */
async function loadMedicamentoTable() {
  try {
    const response = await fetch(API_MEDICAMENTO);
    if (!response.ok) {
      throw new Error("Error al cargar medicamentos: " + response.status);
    }
    medicamentosCache = await response.json();
    populateMedicamentoTable(medicamentosCache);
  } catch (error) {
    showAlert("Error cargando medicamentos: " + error.message, "danger");
  }
}

function populateMedicamentoTable(medicamentos) {
  const tableBody = document.querySelector("#medicamento-table tbody");
  if (!tableBody) return;
  tableBody.innerHTML = "";
  if (!medicamentos.length) {
    tableBody.innerHTML = `<tr><td colspan="6" class="text-center">No hay medicamentos registrados.</td></tr>`;
    return;
  }
  
  medicamentos.forEach(med => {
    // Se muestra el valor real de "horarios" o "N/A" si viene vacío.
    const medHorarios = (med.horarios && med.horarios.trim() !== "") ? med.horarios : "N/A";
    // Se muestra el nombre del paciente: si el objeto "paciente" está presente, se usa; de lo contrario se busca por pacienteId.
    const pacienteNombre = (med.paciente && med.paciente.nombre)
                              ? med.paciente.nombre
                              : getPacienteNameById(med.pacienteId);
    const recordatorio = med.notificaciones ? "Sí" : "No";
    
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${med.nombre}</td>
      <td>${med.dosis}</td>
      <td>${medHorarios}</td>
      <td>${pacienteNombre}</td>
      <td>${recordatorio}</td>
      <td>
        <button class="btn btn-primary btn-sm" onclick="openEditMedicamento(${med.id})">Editar</button>
        <button class="btn btn-danger btn-sm" onclick="deleteMedicamento(${med.id})">Eliminar</button>
      </td>
    `;
    tableBody.appendChild(tr);
  });
}

/* ==========================
   Filtrado de Medicamentos
========================== */
function filterMedicamentos() {
  const filterNombre = document.getElementById("filterMedicamentoNombre")?.value.trim().toLowerCase() || "";
  const filtered = medicamentosCache.filter(med =>
    med.nombre.toLowerCase().includes(filterNombre)
  );
  populateMedicamentoTable(filtered);
}

function clearMedicamentoFilters() {
  const filterInput = document.getElementById("filterMedicamentoNombre");
  if (filterInput) {
    filterInput.value = "";
  }
  loadMedicamentoTable();
}

/* ==========================
   Creación de Medicamento (Formulario Principal)
========================== */
async function saveMedicamento(event) {
  event.preventDefault();
  
  const idField = document.getElementById("medicamento-id");
  const nombre = document.getElementById("medicamento-nombre").value.trim();
  const dosis = document.getElementById("medicamento-dosis").value.trim();
  const horarios = document.getElementById("horario-field").value.trim();
  const pacienteId = document.getElementById("paciente-select").value;
  const notificaciones = document.getElementById("medicamento-notificaciones").checked;
  
  if (!nombre || !dosis || !horarios || !pacienteId) {
    showAlert("Todos los campos son obligatorios", "danger");
    return;
  }
  
  const medicamentoId = idField.value;
  const payload = {
    nombre,
    dosis,
    horarios,
    pacienteId: parseInt(pacienteId),
    notificaciones
  };
  
  try {
    let response;
    if (!medicamentoId) {
      response = await fetch(API_MEDICAMENTO, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });
    } else {
      if (!confirm("¿Está seguro de actualizar el medicamento?")) return;
      response = await fetch(`${API_MEDICAMENTO}/${medicamentoId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });
    }
    
    if (!response.ok) {
      const errorMsg = await response.text();
      showAlert(errorMsg, "danger");
      return;
    }
    showAlert(`Medicamento ${medicamentoId ? "actualizado" : "registrado"} exitosamente`, "success");
    resetForm();
    loadMedicamentoTable();
  } catch (error) {
    showAlert("Error al guardar el medicamento: " + error.message, "danger");
  }
}

function resetForm() {
  document.getElementById("addMedicamentoForm").reset();
  document.getElementById("medicamento-id").value = "";
}

/* ==========================
   Edición de Medicamento mediante Modal
========================== */
async function openEditMedicamento(id) {
  try {
    const response = await fetch(`${API_MEDICAMENTO}/${id}`);
    if (!response.ok) {
      throw new Error("Error " + response.status + " al obtener el medicamento");
    }
    const med = await response.json();
    document.getElementById("edit-medicamento-id").value = med.id;
    document.getElementById("edit-medicamento-nombre").value = med.nombre;
    document.getElementById("edit-medicamento-dosis").value = med.dosis;
    document.getElementById("edit-horario-field").value = (med.horarios && med.horarios.trim() !== "") ? med.horarios : "";
    if (med.paciente && med.paciente.id) {
      document.getElementById("edit-paciente-select").value = med.paciente.id;
    } else if (med.pacienteId) {
      document.getElementById("edit-paciente-select").value = med.pacienteId;
    } else {
      document.getElementById("edit-paciente-select").value = "";
    }
    document.getElementById("edit-medicamento-notificaciones").checked = !!med.notificaciones;
    
    const modalEl = document.getElementById("editMedicamentoModal");
    const modal = new bootstrap.Modal(modalEl);
    modal.show();
  } catch (error) {
    showAlert("Error al cargar el medicamento para editar: " + error.message, "danger");
  }
}

/* ==========================
   Actualización mediante Modal
========================== */
document.getElementById("editMedicamentoForm")?.addEventListener("submit", async function(event) {
  event.preventDefault();
  
  const id = document.getElementById("edit-medicamento-id").value;
  const nombre = document.getElementById("edit-medicamento-nombre").value.trim();
  const dosis = document.getElementById("edit-medicamento-dosis").value.trim();
  const horarios = document.getElementById("edit-horario-field").value.trim();
  const pacienteId = document.getElementById("edit-paciente-select").value;
  const notificaciones = document.getElementById("edit-medicamento-notificaciones").checked;
  
  if (!nombre || !dosis || !horarios || !pacienteId) {
    showAlert("Todos los campos son obligatorios", "danger");
    return;
  }
  
  const payload = {
    nombre,
    dosis,
    horarios,
    pacienteId: parseInt(pacienteId),
    notificaciones
  };
  
  if (!confirm("¿Está seguro de actualizar el medicamento?")) return;
  
  try {
    const response = await fetch(`${API_MEDICAMENTO}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });
    if (!response.ok) {
      const errorMsg = await response.text();
      showAlert(errorMsg, "danger");
      return;
    }
    showAlert("Medicamento actualizado correctamente", "success");
    const modalEl = document.getElementById("editMedicamentoModal");
    const modal = bootstrap.Modal.getInstance(modalEl);
    modal.hide();
    loadMedicamentoTable();
  } catch (error) {
    showAlert("Error al actualizar el medicamento: " + error.message, "danger");
  }
});

/* ==========================
   Eliminación de Medicamento
========================== */
async function deleteMedicamento(id) {
  if (!confirm("¿Está seguro de eliminar este medicamento?")) return;
  try {
    const response = await fetch(`${API_MEDICAMENTO}/${id}`, { method: "DELETE" });
    if (!response.ok) {
      const errorMsg = await response.text();
      showAlert(errorMsg, "danger");
      return;
    }
    showAlert("Medicamento eliminado correctamente", "success");
    loadMedicamentoTable();
  } catch (error) {
    showAlert("Error al eliminar el medicamento: " + error.message, "danger");
  }
}

/* ==========================
   Eventos al Cargar el DOM
========================== */
document.addEventListener("DOMContentLoaded", () => {
  loadMedicamentoTable();
  loadPacienteOptions();
  
  const filterNombreElem = document.getElementById("filterMedicamentoNombre");
  if (filterNombreElem) {
    filterNombreElem.addEventListener("keyup", filterMedicamentos);
  }
  
  const form = document.getElementById("addMedicamentoForm");
  if (form) {
    form.addEventListener("submit", saveMedicamento);
  }
});
