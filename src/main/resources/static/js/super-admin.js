const ctx = document.getElementById('pieChart').getContext('2d');
const pieChart = new Chart(ctx, {
    type: 'pie',
    data: {
        labels: ['Passed', 'Failed', 'Pending'],
        datasets: [{
            data: [60, 25, 15],
            backgroundColor: ['#dda3f8', '#974dc5', '#551e6c'],
            hoverOffset: 4
        }]
    }
});
const lineCtx = document.getElementById('lineChart').getContext('2d');
const lineChart = new Chart(lineCtx, {
    type: 'line',
    data: {
        labels: ['January', 'February', 'March', 'April', 'May', 'June'],
        datasets: [{
            label: 'Monthly Performance',
            data: [65, 59, 80, 81, 56, 55],
            fill: true,
            backgroundColor: '#EDE9FE',
            borderColor: '#6366F1',
            tension: 0.1
        }]
    }
});