document.addEventListener('DOMContentLoaded', function() {
    // Date validation for the booking form
    const checkinInput = document.getElementById('checkInDate');
    const checkoutInput = document.getElementById('checkOutDate');

    if (checkinInput && checkoutInput) {
        // Use a timezone-safe method to get today's date in YYYY-MM-DD format
        const now = new Date();
        const year = now.getFullYear();
        const month = (now.getMonth() + 1).toString().padStart(2, '0');
        const day = now.getDate().toString().padStart(2, '0');
        const today = `${year}-${month}-${day}`;

        // Set the minimum check-in date to today
        checkinInput.setAttribute('min', today);

        // If no check-in date is pre-filled, default to today
        if (!checkinInput.value) {
            checkinInput.value = today;
        }

        // Function to update checkout date constraints
        const updateCheckoutMinDate = () => {
            if (checkinInput.value) {
                let checkinDate = new Date(checkinInput.value);
                // The day after check-in
                checkinDate.setDate(checkinDate.getDate() + 1);

                const checkoutYear = checkinDate.getFullYear();
                const checkoutMonth = (checkinDate.getMonth() + 1).toString().padStart(2, '0');
                const checkoutDay = checkinDate.getDate().toString().padStart(2, '0');
                const minCheckoutDate = `${checkoutYear}-${checkoutMonth}-${checkoutDay}`;

                checkoutInput.setAttribute('min', minCheckoutDate);

                // If the current checkout date is invalid, update it
                if (!checkoutInput.value || checkoutInput.value <= checkinInput.value) {
                    checkoutInput.value = minCheckoutDate;
                }
            }
        };

        // Add event listener to check-in date input
        checkinInput.addEventListener('change', updateCheckoutMinDate);

        // Run on page load to set initial state
        updateCheckoutMinDate();
    }
});

