document.getElementById('compareId').addEventListener('change', function () {
    const selectedId = this.value;
    const currentUrl = new URL(window.location.href);
    if (selectedId) {
        currentUrl.searchParams.set('compareId', selectedId);
    } else {
        currentUrl.searchParams.delete('compareId');
    }
    window.location.href = currentUrl.toString();
});
