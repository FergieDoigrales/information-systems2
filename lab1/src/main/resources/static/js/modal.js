document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".open-modal").forEach(button => {
        button.addEventListener("click", (event) => {
            const modalId = button.getAttribute("data-modal");
            openModal(modalId);
        });
    });

    function openModal(modalId) {
        const modal = document.getElementById(modalId);
        if (!modal) return;
        modal.style.display = "block";

        const closeBtn = modal.querySelector(".close");
        if (closeBtn) {
            closeBtn.onclick = function () {
                modal.style.display = "none";
            };
        }

        window.onclick = function (event) {
            if (event.target === modal) {
                modal.style.display = "none";
            }
        };

        //role request
        const confirmButton = modal.querySelector("#roleRequestModal form");
        if (confirmButton) {
            confirmButton.addEventListener("submit", function (event) {
                event.preventDefault();
                const formData = new FormData(confirmButton);

                fetch("/sendRequest", {
                    method: "POST",
                    body: formData,
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            alert("Request sent successfully!");
                        } else {
                            alert(data.message || "Failed to send request.");
                        }
                    })
                    .catch(error => {
                        console.error("Error", error);
                    });
            });
        }
    }
});