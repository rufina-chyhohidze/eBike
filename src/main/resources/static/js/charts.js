google.charts.load('current', { packages: ['corechart', 'bar'] });
google.charts.setOnLoadCallback(fetchInitialChartData);

const id = window.location.pathname.split('/').pop();
const metricSelector = document.getElementById("metricSelector")

console.log(metricSelector)

metricSelector.addEventListener("change", updateMetric)
let metric = "batteryUsage";

function updateMetric(){
    metric = metricSelector.value
    fetchInitialChartData()
}

function fetchInitialChartData() {
    const chartId = "metricChart";

    fetch(`/api/reports/${id}`)
        .then(validateResponse)
        .then(async (data) => {
            let relevantData = [];

            console.log(data)

            if (data.length > 0) {
                // Parse the starting time from the first entry
                const startTime = new Date(data[0].dateTime).getTime();

                data.forEach((testLine) => {
                    // Calculate seconds since the first test entry
                    const currentTime = new Date(testLine.dateTime).getTime();
                    const secondsSinceStart = (currentTime - startTime) / 1000; // Convert ms to seconds

                    relevantData.push([secondsSinceStart, testLine[metric]]);
                });

                renderChart(relevantData, metric, chartId, "LineChart", ["Seconds Since Start", metricSelector.options[metricSelector.selectedIndex].getAttribute("data-metric")]);
            }
        })
        .catch(error => console.error(`Error fetching data from /api/reports/${id}:`, error));
}

function renderChart(data, title, elementId, chartType, columns, additionalOptions = {}) {
    const chartData = new google.visualization.DataTable();
    chartData.addColumn('number', columns[0]);
    chartData.addColumn('number', columns[1]);

    if (Array.isArray(data)) {
        data.forEach(item => chartData.addRow(item));
    } else {
        console.error(`Unexpected data format for ${title}:`, data);
        return;
    }

    const options = {
        title,
        ...additionalOptions
    };

    const chart = new google.visualization[chartType](document.getElementById(elementId));
    chart.draw(chartData, options);
}

function validateResponse(response) {
    if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
    }
    return response.json();
}