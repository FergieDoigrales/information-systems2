document.addEventListener("click", function (event) {
    if (event.target.classList.contains("accept-request") || event.target.classList.contains("reject-request")) {
        const isAccept = event.target.classList.contains("accept-request");
        const requestId = event.target.getAttribute("data-id");
        const actionUrl = isAccept ? `/admin/approve/${requestId}` : `/admin/reject/${requestId}`;

        fetch(actionUrl, {
            method: "POST",
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Failed to process the request");
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    const row = event.target.closest("tr");
                    const statusCell = row.querySelector("td:nth-child(3)");
                    const actionsCell = row.querySelector("td:nth-child(4)");
                    statusCell.textContent = isAccept ? "ACCEPTED" : "REJECTED";
                    actionsCell.innerHTML = `<span>${statusCell.textContent}</span>`;
                } else {
                    alert("Failed to update request status: " + data.message);
                }
            })
            .catch(error => {
                console.error("Error processing request:", error);
            });
    }
});