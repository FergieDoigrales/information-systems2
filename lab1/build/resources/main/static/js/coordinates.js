document.addEventListener("DOMContentLoaded", function () {
    const coordinatesForm = document.querySelector("#createCoordinatesModal form");
    if (coordinatesForm) {
        coordinatesForm.addEventListener("submit", function (event) {
            event.preventDefault();
            const formData = new FormData(coordinatesForm);

            fetch(coordinatesForm.getAttribute("action"), {
                method: "POST",
                body: formData,
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        document.getElementById("createCoordinatesModal").style.display = "none";
                        updateCoordinatesList(data.updatedCoordinatesList);
                    } else {
                        alert(data.message || "Failed to save coordinates.");
                    }
                })
                .catch(error => {
                    console.error("Error saving coordinates:", error);
                });
        });

        function updateCoordinatesList(coordinates) {
            const coordinatesSelect = document.querySelector("#coordinates");
            if (coordinatesSelect) {
                coordinatesSelect.innerHTML = coordinates
                    .map(coordinate => `<option value="${coordinate.id}">${coordinate.x}, ${coordinate.y}</option>`)
                    .join("");
            }
        }
    }
});