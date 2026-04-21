function confirmDelete(message) {
  return confirm(message);
}

document.addEventListener("DOMContentLoaded", function () {
  const canvas = document.getElementById("barChart");
  if (canvas && window.Chart) {
    const students = Number(canvas.dataset.students || 0);
    const courses = Number(canvas.dataset.courses || 0);

    new Chart(canvas, {
      type: "bar",
      data: {
        labels: ["Students", "Courses"],
        datasets: [{
          label: "Count",
          data: [students, courses]
        }]
      },
      options: {
        responsive: true,
        plugins: { legend: { display: false } }
      }
    });
  }
});