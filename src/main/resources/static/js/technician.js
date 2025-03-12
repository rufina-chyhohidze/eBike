const ctx1 = document.getElementById('testsChart').getContext('2d');
const ctx2 = document.getElementById('clientsChart').getContext('2d');

new Chart(ctx1, {
    type: 'line',
    data: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May'],
        datasets: [{
            label: 'Tests',
            data: [180, 200, 220, 230, 245],
            borderColor: 'purple',
            fill: false
        }]
    }
});

new Chart(ctx2, {
    type: 'line',
    data: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May'],
        datasets: [{
            label: 'Clients',
            data: [75, 80, 85, 90, 98],
            borderColor: 'purple',
            fill: false
        }]
    }
});
