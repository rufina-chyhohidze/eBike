document.addEventListener("DOMContentLoaded", () => {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");

    const confirmModal = document.getElementById("confirmModal");
    const confirmBtn = document.getElementById("confirmBtn");
    const cancelBtn = document.getElementById("cancelBtn");

    const noBikesMessage = document.getElementById("noBikesMessage");

    let currentButton = null;

    bikeGrid.addEventListener("click", (e) => {
        const button = e.target.closest(".unlink-bike-btn");
        if (!button) return;

        e.preventDefault();
        currentButton = button;
        confirmModal.classList.remove("hidden");
    });

    cancelBtn.addEventListener("click", () => {
        confirmModal.classList.add("hidden");
        currentButton = null;
    });

    confirmBtn.addEventListener("click", async () => {
        if (!currentButton) return;

        const frameNumber = currentButton.getAttribute("data-frame");

        try {
            const response = await fetch(`/api/bike-management/${frameNumber}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    [csrfHeader]: csrfToken
                },
            });

            if (response.ok) {
                const bikeCard = currentButton.closest(".bike-card");
                bikeCard.remove();

                // Check if it's the last card and show message
                const remainingBikes = document.querySelectorAll(".bike-card");
                if (remainingBikes.length === 0 && noBikesMessage) {
                    noBikesMessage.classList.remove("hidden");
                }
            } else {
                alert("Failed to unlink bike.");
            }
        } catch (err) {
            console.error("Error unlinking bike:", err);
            alert("Something went wrong.");
        } finally {
            confirmModal.classList.add("hidden");
            currentButton = null;
        }
    });
});
