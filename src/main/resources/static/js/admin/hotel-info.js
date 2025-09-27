document.addEventListener('DOMContentLoaded', () => {
    // This code only runs on the hotel-info page.
    const hotelModal = document.getElementById('edit-hotel-modal');
    if (hotelModal) {
        handleModal('edit-hotel-modal', 'edit-hotel-button', 'cancel-edit-button', 'modal-content');
    }
});
