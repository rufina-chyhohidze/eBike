const activateBtn = () => {
    const compareId = document.getElementById('compareId');
    const selectBtns = document.getElementsByClassName('selectBtn');
    for (let i = 0; i < selectBtns.length; i++) {
        selectBtns[i].addEventListener("click",  (e) => {
            const selectedId = e.target.id
            const currentUrl = new URL(window.location.href);
            currentUrl.searchParams.set('compareId', selectedId);
            window.location.href = currentUrl.toString();
            if (compareId) {
                compareId.value = selectedId;
            }
            compareContent.classList.add("d-none");
            compareContent.classList.add("hidden");
            mainContent.style.display = "block";
        })
    }
}

export default activateBtn;