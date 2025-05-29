// Customer Dashboard JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Search functionality for bike reports
    const frameNumberSearch = document.getElementById('frameNumberSearch');
    const bikeModelSearch = document.getElementById('bikeModelSearch');
    const reportDateSearch = document.getElementById('reportDateSearch');
    
    if (frameNumberSearch && bikeModelSearch && reportDateSearch) {
        const searchFunction = function() {
            const frameNumber = frameNumberSearch.value.toLowerCase();
            const bikeModel = bikeModelSearch.value.toLowerCase();
            const reportDate = reportDateSearch.value.toLowerCase();
            
            const rows = document.querySelectorAll('tbody tr');
            
            rows.forEach(row => {
                const frameNumberCell = row.cells[2].textContent.toLowerCase();
                const reportDateCell = row.cells[1].textContent.toLowerCase();
                
                // We don't have bike model in the table, so we'll just check frame number and report date
                const matchesFrameNumber = frameNumber === '' || frameNumberCell.includes(frameNumber);
                const matchesReportDate = reportDate === '' || reportDateCell.includes(reportDate);
                
                if (matchesFrameNumber && matchesReportDate) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        };
        
        frameNumberSearch.addEventListener('input', searchFunction);
        bikeModelSearch.addEventListener('input', searchFunction);
        reportDateSearch.addEventListener('input', searchFunction);
    }
});