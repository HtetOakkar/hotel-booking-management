// Mobile menu toggle
document.addEventListener('DOMContentLoaded', function() {
    const mobileMenuButton = document.getElementById('mobile-menu-button');
    const mobileMenu = document.getElementById('mobile-menu');

    if (mobileMenuButton && mobileMenu) {
        mobileMenuButton.addEventListener('click', function() {
            mobileMenu.classList.toggle('hidden');
        });
    }

    // Date validation for search form
    const checkinInput = document.getElementById('checkin');
    const checkoutInput = document.getElementById('checkout');

    if (checkinInput && checkoutInput) {
        const today = new Date().toISOString().split('T')[0];
        checkinInput.setAttribute('min', today);

        checkinInput.addEventListener('change', () => {
            if (checkinInput.value) {
                let checkinDate = new Date(checkinInput.value);
                checkinDate.setDate(checkinDate.getDate() + 1);
                let minCheckoutDate = checkinDate.toISOString().split('T')[0];
                checkoutInput.setAttribute('min', minCheckoutDate);
                if (checkoutInput.value && checkoutInput.value <= checkinInput.value) {
                    checkoutInput.value = minCheckoutDate;
                }
            }
        });
    }

    // Collapsible room card descriptions
    const roomCards = document.querySelectorAll('.read-more-content');
    roomCards.forEach(content => {
        const roomTypeId = content.dataset.roomtypeId;
        const toggleButton = document.querySelector(`.read-more-toggle[data-roomtype-id='${roomTypeId}']`);

        // Check if content is overflowing based on line-clamp
        if (content.scrollHeight > content.clientHeight) {
            toggleButton.classList.remove('hidden');
        }

        toggleButton.addEventListener('click', () => {
            content.classList.toggle('line-clamp-3');
            if (content.classList.contains('line-clamp-3')) {
                toggleButton.textContent = 'Read More';
            } else {
                toggleButton.textContent = 'Read Less';
            }
        });
    });
});
