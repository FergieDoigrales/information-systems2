document.addEventListener("DOMContentLoaded", function () {
    const locationForm = document.querySelector("#createLocationModal form");
    if (locationForm) {
        locationForm.addEventListener("submit", function (event) {
            event.preventDefault();
            const formData = new FormData(locationForm);

            fetch(locationForm.getAttribute("action"), {
                method: "POST",
                body: formData,
            })
                .then(response => {
                    if (!response.ok) {
                        return response.json().then(errData => {
                            throw new Error(errData.message || `HTTP error! Status: ${response.status}`);
                        });
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        document.getElementById("createLocationModal").style.display = "none";
                        updateLocationsList(data.updatedLocationsList);
                    } else {
                        alert(data.message || "Failed to save location.");
                    }
                })
                .catch(error => {
                    alert(error.message || "Failed to save location.");
                    console.error("Error saving location:", error, error.message);
                });
        });

        function updateLocationsList(locations) {
            const locationsSelect = document.querySelector("#location");
            if (locationsSelect) {
                locationsSelect.innerHTML = locations
                    .map(location => `<option value="${location.id}">${location.x}, ${location.y}, ${location.z}</option>`)
                    .join("");
            }
        }
    }
});