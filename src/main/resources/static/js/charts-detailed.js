google.charts.load('current', { packages: ['corechart'] });
google.charts.setOnLoadCallback(init);

function init() {
    const metricSel   = document.getElementById('metricsSelect');
    const intervalSel = document.getElementById('intervalSelect');
    const modeSel     = document.getElementById('modeSelect');
    const compareSel  = document.getElementById('compareId');
    let selectedMetrics = ['batteryVoltage'];
    let interval        = 1;     // seconds
    let normalized      = false;
    const pathParts     = window.location.pathname.split('/');
    const reportId      = pathParts[pathParts.indexOf('report') + 1];

    let chart;       // Global chart reference
    let dataTable;   // Global data reference
    let options;     // Global options reference

    metricSel.addEventListener('change', () => {
        selectedMetrics = Array.from(metricSel.selectedOptions)
            .map(o => o.value)
            .slice(0,4);
        draw();
    });
    intervalSel.addEventListener('change', () => {
        interval = parseInt(intervalSel.value,10);
        draw();
    });
    modeSel.addEventListener('change', () => {
        normalized = modeSel.value === 'normalized';
        draw();
    });
    compareSel.addEventListener('change', () => draw());

    // Redraw chart on window resize
    window.addEventListener('resize', () => {
        if (chart && dataTable && options) {
            chart.draw(dataTable, options);
        }
    });

    draw();

    function draw() {
        const urls = [`/api/reports/${reportId}`];
        if (compareSel.value) urls.push(`/api/reports/${compareSel.value}`);
        Promise.all(urls.map(u => fetch(u).then(r=>r.ok?r.json():[])))
            .then(([dataA, dataB]) => plot(dataA, dataB || []))
            .catch(err => console.error(err));
    }

    function bucketData(data) {
        if (!data.length) return { buckets:new Map(), max:{} };
        const start = new Date(data[0].dateTime).getTime();
        const buckets = new Map(), max={};
        selectedMetrics.forEach(m => max[m] = 0);
        data.forEach(d => {
            const t = Math.floor((new Date(d.dateTime).getTime() - start) / 1000);
            const b = Math.floor(t / interval) * interval;
            if (!buckets.has(b)) buckets.set(b, []);
            buckets.get(b).push(d);
            selectedMetrics.forEach(m => {
                const v = d[m] || 0;
                if (v > max[m]) max[m] = v;
            });
        });
        return { buckets, max };
    }

    function plot(dataA, dataB) {
        const { buckets: ba, max: maxA } = bucketData(dataA);
        const { buckets: bb, max: maxB } = bucketData(dataB);
        const allTimes = Array.from(new Set([...ba.keys(), ...bb.keys()]))
            .sort((a, b) => a - b);

        const header = ['Time (s)'];
        selectedMetrics.forEach(m => header.push(`A – ${m}`));
        if (dataB.length) selectedMetrics.forEach(m => header.push(`B – ${m}`));

        const rows = allTimes.map(time => {
            const row = [time];
            selectedMetrics.forEach(m => {
                const arr = ba.get(time) || [];
                const avg = arr.length ? arr.reduce((s, x) => s + (x[m] || 0), 0) / arr.length : null;
                row.push(normalized ? avg / (maxA[m] || 1) : avg);
            });
            if (dataB.length) selectedMetrics.forEach(m => {
                const arr = bb.get(time) || [];
                const avg = arr.length ? arr.reduce((s, x) => s + (x[m] || 0), 0) / arr.length : null;
                row.push(normalized ? avg / (maxB[m] || 1) : avg);
            });
            return row;
        });

        dataTable = google.visualization.arrayToDataTable([header, ...rows]);

        const series = {};
        const vAxes = {};

        if (normalized) {
            vAxes[0] = { title: 'Normalized Value' };
            for (let i = 0; i < header.length - 1; i++) {
                series[i] = { targetAxisIndex: 0 };
            }
        } else {
            vAxes[0] = { title: selectedMetrics[0] };
            const rightList = selectedMetrics.slice(1).join(', ');
            vAxes[1] = { title: rightList || '(other)' };

            const metricCount = selectedMetrics.length;
            for (let s = 0; s < header.length - 1; s++) {
                const metricIdx = s % metricCount;
                const axisIdx = metricIdx === 0 ? 0 : 1;
                series[s] = { targetAxisIndex: axisIdx };
            }
        }

        options = {
            title: `Report ${reportId}` + (dataB.length ? ` vs ${compareSel.value}` : ''),
            titleTextStyle: {
                fontSize: 20,
                bold: true,
                color: '#4B0082',
                fontName: 'sans-serif'
            },
            hAxis: {
                title: `Time (every ${interval}s)`,
                titleTextStyle: { color: '#4B5563', italic: false },
                textStyle: { color: '#6B7280' }
            },
            vAxes,
            series,
            curveType: 'function',
            legend: {
                position: 'bottom',
                textStyle: {
                    color: '#4B5563',
                    fontSize: 12,
                    fontName: 'sans-serif'
                }
            },
            backgroundColor: '#F9FAFB',
            height: 600,
            chartArea: {
                left: '10%',
                top: 60,
                width: '85%',
                height: '75%'
            },
            colors: ['#7C3AED', '#EC4899', '#10B981', '#F59E0B', '#3B82F6', '#EF4444'],
            lineWidth: 3,
            pointSize: 5,
            animation: {
                startup: true,
                duration: 800,
                easing: 'out'
            },
            tooltip: {
                textStyle: {
                    color: '#111827',
                    fontSize: 12
                },
                showColorCode: true,
                isHtml: true
            }
        };

        if (!chart) {
            chart = new google.visualization.LineChart(
                document.getElementById('detailedChart')
            );
        }

        chart.draw(dataTable, options);
    }
}
