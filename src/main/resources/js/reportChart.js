function drawChart(metric) {
    fetch('/api/testLines')
        .then(response => response.json())
        .then(testLines => {
            let data = new google.visualization.DataTable();
            data.addColumn('number', 'Test Line');
            data.addColumn('number', metric);

            testLines.forEach((line, index) => {
                data.addRow([index + 1, parseFloat(line[metric])]);
            });

            let options = {
                title: metric + ' Evolution',
                curveType: 'function',
                legend: { position: 'bottom' },
                hAxis: { title: 'Test Line' },
                vAxis: { title: 'Value' }
            };

            let chart = new google.visualization.LineChart(document.getElementById('metricChart'));
            chart.draw(data, options);
        })
        .catch(error => console.error('Error loading test data:', error));
}

function updateChart() {
    let metric = document.getElementById('metricSelect').value;
    drawChart(metric);
}
