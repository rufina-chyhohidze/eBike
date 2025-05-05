document.addEventListener('DOMContentLoaded', function () {
    const compareSelect = document.getElementById('compareId');
    const compareButton = document.getElementById('compareButton');

    if (compareSelect) {
        compareSelect.addEventListener('change', function () {
            const selectedId = this.value;
            const currentUrl = new URL(window.location.href);

            if (selectedId) {
                currentUrl.searchParams.set('compareId', selectedId);
                compareButton.disabled = false;
            } else {
                currentUrl.searchParams.delete('compareId');
                compareButton.disabled = true;
            }

            window.location.href = currentUrl.toString();
        });
    }
});
