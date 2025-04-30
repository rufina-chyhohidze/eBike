let selectedMetrics = ["batteryVoltage"];
let interval = 1; // seconds
let normalized = false;

const pathParts = window.location.pathname.split("/");
const reportId = pathParts[pathParts.indexOf("report") + 1];
const metricSelector = document.getElementById("metricsSelect");
const intervalSelector = document.getElementById("intervalSelect");
const modeSelector = document.getElementById("modeSelect");

metricSelector.addEventListener("change", () => {
    selectedMetrics = Array.from(metricSelector.selectedOptions).map(opt => opt.value);
    drawDetailedChart();
});

intervalSelector.addEventListener("change", () => {
    interval = parseInt(intervalSelector.value);
    drawDetailedChart();
});

modeSelector.addEventListener("change", () => {
    normalized = modeSelector.value === "normalized";
    drawDetailedChart();
});

function drawDetailedChart() {
    fetch(`/api/reports/${reportId}`)
        .then(res => res.json())
        .then(data => {
            if (data.length === 0) return;

            const startTime = new Date(data[0].dateTime).getTime();
            const groupedData = new Map();
            const maxValues = {}; // for normalization

            selectedMetrics.forEach(m => maxValues[m] = 0);

            data.forEach(d => {
                const time = Math.floor((new Date(d.dateTime).getTime() - startTime) / 1000);
                const bucket = Math.floor(time / interval) * interval;

                if (!groupedData.has(bucket)) {
                    groupedData.set(bucket, []);
                }

                groupedData.get(bucket).push(d);

                selectedMetrics.forEach(metric => {
                    if (d[metric] !== null && d[metric] > maxValues[metric]) {
                        maxValues[metric] = d[metric];
                    }
                });
            });

            const chartData = [["Time", ...selectedMetrics]];

            groupedData.forEach((entries, time) => {
                const row = [time];

                selectedMetrics.forEach(metric => {
                    const avg = entries.reduce((sum, item) => sum + (item[metric] || 0), 0) / entries.length;
                    row.push(normalized ? avg / (maxValues[metric] || 1) : avg);
                });

                chartData.push(row);
            });

            renderMultiAxisChart(chartData);
        });
}

function renderMultiAxisChart(data) {
    const dataTable = google.visualization.arrayToDataTable(data);

    const series = {};
    const vAxes = {};

    selectedMetrics.forEach((metric, i) => {
        const axisIdx = i > 1 ? 1 : i; // Only index 0 (left) and 1 (right)
        const side = axisIdx === 0 ? 'left' : 'right';

        vAxes[axisIdx] = {
            title: selectedMetrics[i],
            side: side,
            textStyle: { fontSize: 12 },
            titleTextStyle: { fontSize: 12 }
        };

        series[i] = {
            targetAxisIndex: axisIdx
        };
    });

    const options = {
        title: "Detailed Test Metrics",
        hAxis: {
            title: `Time (every ${interval}s)`,
            titleTextStyle: { italic: true }
        },
        vAxes: normalized
            ? { 0: { title: "Normalized Value", textStyle: { fontSize: 12 }, titleTextStyle: { fontSize: 12 } } }
            : vAxes,
        series: series,
        curveType: "function",
        legend: { position: "bottom" },
        height: 500,
        backgroundColor: "#fff"
    };

    const chart = new google.visualization.LineChart(document.getElementById("detailedChart"));
    chart.draw(dataTable, options);
}

google.charts.load("current", { packages: ["corechart"] });
google.charts.setOnLoadCallback(drawDetailedChart);
