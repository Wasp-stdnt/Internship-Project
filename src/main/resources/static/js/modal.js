document.addEventListener("DOMContentLoaded", function() {
    const modal = document.getElementById("confirmationModal");
    const confirmBtn = document.getElementById("confirmDeleteBtn");
    const cancelBtn = document.getElementById("cancelDeleteBtn");

    let currentActionUrl = "";

    function openModal(actionUrl, message) {
        currentActionUrl = actionUrl;
        if (message) {
            modal.querySelector("p").innerText = message;
        }
        modal.style.display = "block";
    }

    function closeModal() {
        modal.style.display = "none";
        currentActionUrl = "";
    }

    confirmBtn.addEventListener("click", function() {
        if (currentActionUrl) {
            let form = document.createElement("form");
            form.method = "post";
            form.action = currentActionUrl;

            document.body.appendChild(form);
            form.submit();
        }
    });

    cancelBtn.addEventListener("click", closeModal);

    window.addEventListener("click", function(e) {
        if (e.target === modal) {
            closeModal();
        }
    });

    deleteButtons.forEach(function(button) {
        button.addEventListener("click", function(e) {
            e.preventDefault();
            const urlTemplate = button.getAttribute("data-post-url-template");
            const id = button.getAttribute("data-id");
            const actionUrl = urlTemplate.replace("{id}", id);
            const message = button.getAttribute("data-confirm-message") || "Are you sure you want to delete this item?";
            if (actionUrl) {
                openModal(actionUrl, message);
            }
        });
    });

    const logoutButtons = document.querySelectorAll(".btn-logout");
    logoutButtons.forEach(function(button) {
        button.addEventListener("click", function(e) {
            e.preventDefault();
            const actionUrl = button.getAttribute("data-post-url");
            const message = button.getAttribute("data-confirm-message") || "Are you sure you want to logout?";
            if (actionUrl) {
                openModal(actionUrl, message);
            }
        });
    });
});
