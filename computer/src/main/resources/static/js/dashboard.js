document.addEventListener("DOMContentLoaded", function() {
    // Nhận dữ liệu từ HTML (Nếu không có thì dùng dữ liệu mẫu)
    const labels = window.chartLabels || ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6'];
    const dataValues = window.chartData || [15000000, 22000000, 18000000, 29000000, 24000000, 35000000];

    // Tìm thẻ canvas
    const canvas = document.getElementById('revenueChart');
    if (!canvas) return;

    const ctx = canvas.getContext('2d');

    // Tiến hành vẽ biểu đồ
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Doanh thu (VNĐ)',
                data: dataValues,
                backgroundColor: 'rgba(13, 138, 188, 0.7)',
                borderColor: 'rgba(13, 138, 188, 1)',
                borderWidth: 1,
                borderRadius: 4,
                maxBarThickness: 60
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'top',
                }
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
});