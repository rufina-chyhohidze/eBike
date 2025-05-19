const activateBtn = () => {
    const selectBtns = document.getElementsByClassName('selectBtn');
    for (let i = 0; i < selectBtns.length; i++) {
        selectBtns[i].addEventListener("click",  (e) => {
            const selectedId = e.target.id
            const currentUrl = new URL(window.location.href);
            currentUrl.searchParams.set('compareId', selectedId);
            window.location.href = currentUrl.toString();
            compareContent.classList.add("d-none");
            mainContent.style.display = "block";
        })
    }
}

export default activateBtn;