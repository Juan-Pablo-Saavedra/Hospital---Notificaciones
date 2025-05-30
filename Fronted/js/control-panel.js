const API_MEDICAMENTOS_URL = "http://localhost:8085/api/medicamentos";  
let editingMedicamentoId = null;

// Configurar eventos al cargar la página.
document.addEventListener("DOMContentLoaded", function () {
  loadMedicamentoTable();
  document.getElementById("addMedicamentoForm").addEventListener("submit", addMedicamento);
  document.getElementById("add-horario-button").addEventListener("click", addHorario);
});

function showAlert(message, type) {
  const alertContainer = document.getElementById("panel-alert-container");
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

async function loadMedicamentoTable() {
  try {
    const response = await fetch(API_MEDICAMENTOS_URL);
    if (!response.ok) throw new Error("Error al cargar medicamentos.");
    const medicamentos = await response.json();
    renderMedicamentoTable(medicamentos);
  } catch (error) {
    console.error("Error cargando medicamentos:", error);
    showAlert("Error cargando medicamentos.", "error");
  }
}

function renderMedicamentoTable(medicamentos) {
  const tbody = document.querySelector("#medicamento-table tbody");
  tbody.innerHTML = "";
  if (medicamentos.length === 0) {
    tbody.innerHTML = "<tr><td colspan='4'>No hay medicamentos registrados.</td></tr>";
    return;
  }
  medicamentos.forEach(medicamento => {
    const row = document.createElement("tr");
    // Se omite el ID y se muestran solo los campos relevantes para el usuario.
    row.innerHTML = `
      <td>${medicamento.nombre}</td>
      <td>${medicamento.dosis}</td>
      <td>${medicamento.horarios}</td>
      <td>
        <button onclick="editMedicamento(${medicamento.id})">Editar</button>
        <button onclick="deleteMedicamento(${medicamento.id})">Eliminar</button>
      </td>
    `;
    tbody.appendChild(row);
  });
}

function addHorario() {
  const container = document.getElementById("horarios-container");
  const div = document.createElement("div");
  div.classList.add("horario-input");
  div.innerHTML = `
    <input type="time" class="horario-field" required>
    <button type="button" onclick="removeHorario(this)" class="remove-horario" title="Eliminar horario">&times;</button>
  `;
  container.appendChild(div);
}

function removeHorario(button) {
  const container = document.getElementById("horarios-container");
  if (container.querySelectorAll(".horario-input").length > 1) {
    button.parentElement.remove();
  } else {
    showAlert("Debe haber al menos un horario.", "error");
  }
}

async function addMedicamento(event) {
  event.preventDefault();

  const nombre = document.getElementById("medicamento-nombre").value.trim();
  const dosis = document.getElementById("medicamento-dosis").value.trim();
  // Recopilar todos los valores de los campos "time"
  const horarioNodes = document.querySelectorAll(".horario-field");
  const horariosArray = [];
  horarioNodes.forEach(input => {
    if (input.value) {
      horariosArray.push(input.value);
    }
  });
  const horarios = horariosArray.join(",");
  
  if (!nombre || !dosis || horariosArray.length === 0) {
    showAlert("Todos los campos son obligatorios y se debe ingresar al menos un horario.", "error");
    return;
  }
  
  // Confirmación antes de enviar
  if (editingMedicamentoId === null) {
    if (!confirm("¿Desea agregar este medicamento?")) return;
  } else {
    if (!confirm("¿Desea actualizar este medicamento?")) return;
  }
  
  const payload = { nombre, dosis, horarios };
  
  try {
    let response;
    if (editingMedicamentoId === null) {
      response = await fetch(API_MEDICAMENTOS_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });
    } else {
      response = await fetch(`${API_MEDICAMENTOS_URL}/${editingMedicamentoId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });
    }
    
    const contentType = response.headers.get("Content-Type");
    let data;
    if (contentType && contentType.includes("application/json")) {
      data = await response.json();
    } else {
      data = await response.text();
    }
    
    if (!response.ok) {
      let errorMsg = "Error al registrar el medicamento.";
      if (data) {
        try {
          const parsed = typeof data === "object" ? data : JSON.parse(data);
          if (parsed.message) errorMsg = parsed.message;
        } catch (e) {
          errorMsg = data;
        }
      }
      throw new Error(errorMsg);
    }
    
    if (editingMedicamentoId === null) {
      showAlert("Medicamento agregado exitosamente.", "success");
    } else {
      showAlert("Medicamento actualizado exitosamente.", "success");
      editingMedicamentoId = null;
      document.querySelector("#addMedicamentoForm button[type='submit']").textContent = "Agregar Medicamento";
    }
    
    document.getElementById("addMedicamentoForm").reset();
    // Se puede opcionalmente resetear el contenedor de horarios para dejar uno solo:
    document.getElementById("horarios-container").innerHTML = `
      <div class="horario-input">
        <input type="time" class="horario-field" required>
        <button type="button" onclick="removeHorario(this)" class="remove-horario" title="Eliminar horario">&times;</button>
      </div>
    `;
    
    loadMedicamentoTable();
  } catch (error) {
    console.error("Error al agregar/actualizar medicamento:", error.message);
    showAlert(error.message, "error");
  }
}

async function editMedicamento(id) {
  try {
    const response = await fetch(`${API_MEDICAMENTOS_URL}/${id}`);
    if (!response.ok) throw new Error("Error al obtener detalles del medicamento.");
    const medicamento = await response.json();
    
    document.getElementById("medicamento-nombre").value = medicamento.nombre;
    document.getElementById("medicamento-dosis").value = medicamento.dosis;
    
    // Precargar los horarios: separamos la cadena y generamos un campo por cada hora.
    const horariosArray = medicamento.horarios.split(",").map(t => t.trim());
    let horariosHtml = "";
    horariosArray.forEach(() => {
      horariosHtml += `
        <div class="horario-input">
          <input type="time" class="horario-field" required>
          <button type="button" onclick="removeHorario(this)" class="remove-horario" title="Eliminar horario">&times;</button>
        </div>
      `;
    });
    document.getElementById("horarios-container").innerHTML = horariosHtml;
    
    // Asignamos los valores a cada campo de horario.
    const horarioFields = document.querySelectorAll(".horario-field");
    horarioFields.forEach((input, index) => {
      input.value = horariosArray[index] || "";
    });
    
    editingMedicamentoId = id;
    document.querySelector("#addMedicamentoForm button[type='submit']").textContent = "Actualizar Medicamento";
  } catch (error) {
    console.error("Error al editar medicamento:", error.message);
    showAlert("Error al cargar datos para editar el medicamento.", "error");
  }
}

async function deleteMedicamento(id) {
  if (!confirm("¿Está seguro de eliminar este medicamento?")) return;
  try {
    const response = await fetch(`${API_MEDICAMENTOS_URL}/${id}`, { method: "DELETE" });
    if (!response.ok) throw new Error("Error al eliminar el medicamento.");
    showAlert("Medicamento eliminado.", "success");
    loadMedicamentoTable();
  } catch (error) {
    console.error("Error eliminando medicamento:", error.message);
    showAlert(error.message, "error");
  }
}
