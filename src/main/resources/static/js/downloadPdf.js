window.downloadPDF = function() {
    const element = document.getElementById('reportContent');
    const opt = {
        margin: 0.5,
        filename: 'test-report.pdf',
        image: { type: 'jpeg', quality: 0.98 },
        html2canvas: { scale: 2 },
        jsPDF: { unit: 'in', format: 'a4', orientation: 'portrait' }
    };

    html2pdf().from(element).set(opt).save();
}