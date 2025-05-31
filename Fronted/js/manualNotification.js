const API_EMAIL = "http://localhost:8085/api/email/medicationReminder";

document.getElementById("manualNotificationForm").addEventListener("submit", async (event) => {
  event.preventDefault();
  const email = document.getElementById("email").value.trim();
  const patientName = document.getElementById("patientName").value.trim();
  const medicamento = document.getElementById("medicamento").value.trim();
  const dosis = document.getElementById("dosis").value.trim();
  const horarios = document.getElementById("horarios").value.trim();

  if (!email || !patientName || !medicamento || !dosis || !horarios) {
    showAlert("Todos los campos son obligatorios", "danger");
    return;
  }
  
  try {
    // Se codifican los parámetros para la URL
    const url = `${API_EMAIL}/${encodeURIComponent(email)}/${encodeURIComponent(patientName)}/${encodeURIComponent(medicamento)}/${encodeURIComponent(dosis)}/${encodeURIComponent(horarios)}`;
    const response = await fetch(url);
    const result = await response.text();
    showAlert(result, "success");
  } catch (error) {
    showAlert("Error: " + error.message, "danger");
  }
});

function showAlert(message, type) {
  const alertDiv = document.getElementById("notificationAlert");
  alertDiv.innerHTML = `<div class="alert alert-${type}">${message}</div>`;
  setTimeout(() => alertDiv.innerHTML = "", 3000);
}
