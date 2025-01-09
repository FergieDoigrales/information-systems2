document.addEventListener("DOMContentLoaded", function () {
    const personForm = document.querySelector("#createPersonModal form");
    if (personForm) {
        personForm.addEventListener("submit", function (event) {
            event.preventDefault();

            const errorElements = personForm.querySelectorAll(".error-message");
            errorElements.forEach(el => el.textContent = "");

            const formData = new FormData(personForm);
            const passportIDInput = document.getElementById("passportID");
            const passportIDError = document.getElementById("passportIDError");

            passportIDError.textContent = "";

            let hasErrors = false;

            if (passportIDInput.value.length < 10 || passportIDInput.value.length > 43) {
                passportIDError.textContent = "Passport ID must be between 10 and 43 characters.";
                hasErrors = true;
            }

            if (hasErrors) {
                return;
            }

            fetch(personForm.getAttribute("action"), {
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
                        document.getElementById("createPersonModal").style.display = "none";
                        updatePersonsList(data.updatedPersonsList);
                        openModal("createMovieModal");
                        location.reload();
                    } else if (data.errors) {
                        for (const [field, message] of Object.entries(data.errors)) {
                            const fieldElement = personForm.querySelector(`[th\\:field="*{${field}}"]`);
                            if (fieldElement) {
                                const errorElement = document.createElement("div");
                                errorElement.className = "error-message";
                                errorElement.style.color = "red";
                                errorElement.textContent = message;
                                fieldElement.insertAdjacentElement("afterend", errorElement);
                            }
                        }
                    } else {
                        alert(data.message || "Failed to save person.");
                    }
                })
                .catch(error => {
                    alert("Failed to save person." || error.message);
                    console.error("Error saving person:", error, error.message);
                });
        });

        function updatePersonsList(persons) {
            const selectors = ["#director", "#screenwriter", "#operator"];

            selectors.forEach(selector => {
                const selectElement = document.querySelector(selector);
                if (selectElement) {
                    selectElement.innerHTML = persons
                        .map(person => `<option value="${person.id}">${person.name}</option>`)
                        .join("");
                }
            });
        }
    }
});

