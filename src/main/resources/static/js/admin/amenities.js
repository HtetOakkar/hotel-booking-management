document.addEventListener('DOMContentLoaded', () => {
    // --- Add/Edit Amenity Modal ---
    const amenityModal = document.getElementById('amenity-modal');
    if (amenityModal) {
        const modalContent = document.getElementById('amenity-modal-content');
        const openModalButton = document.getElementById('add-amenity-button');
        const closeModalButton = document.getElementById('cancel-amenity-button');
        const editButtons = document.querySelectorAll('.edit-amenity-button');
        const modalTitle = document.getElementById('amenity-modal-title');
        const amenityForm = document.getElementById('amenity-form');
        const amenityIdInput = document.getElementById('amenityId');
        const checkboxesContainer = document.getElementById('room-type-checkboxes');

        const openModal = () => {
            amenityModal.classList.remove('modal-hidden');
            setTimeout(() => modalContent.classList.remove('modal-hidden'), 10);
        };
        const closeModal = () => {
            modalContent.classList.add('modal-hidden');
            setTimeout(() => amenityModal.classList.add('modal-hidden'), 300);
        };

        const resetCheckboxes = () => {
            checkboxesContainer.querySelectorAll('input[type="checkbox"]').forEach(cb => cb.checked = false);
        };

        // Open modal for ADDING
        openModalButton.addEventListener('click', () => {
            amenityForm.reset();
            resetCheckboxes();
            amenityIdInput.value = '';
            modalTitle.textContent = 'Add New Amenity';
            amenityForm.action = '/admins/settings/amenities/add';
            openModal();
        });

        // Open modal for EDITING
        editButtons.forEach(button => {
            button.addEventListener('click', () => {
                const data = button.dataset;
                amenityForm.reset();
                resetCheckboxes();

                amenityIdInput.value = data.id;
                document.getElementById('name').value = data.name;
                document.getElementById('description').value = data.description;
                document.getElementById('amenityType').value = data.type;

                // Check the correct room type checkboxes
                if (data.roomtypeids) {
                    const ids = data.roomtypeids.split(',');
                    ids.forEach(id => {
                        const checkbox = document.getElementById(`rt-${id}`);
                        if (checkbox) checkbox.checked = true;
                    });
                }

                modalTitle.textContent = 'Edit Amenity';
                amenityForm.action = `/admins/settings/amenities/update/${data.id}`;
                openModal();
            });
        });

        closeModalButton.addEventListener('click', closeModal);
        amenityModal.addEventListener('click', (event) => {
            if (event.target === amenityModal) closeModal();
        });
    }

    // --- Delete Amenity Confirmation ---
    const deleteForm = document.getElementById('delete-amenity-form');
    if(deleteForm) {
        handleConfirmationModal(
            '.delete-amenity-button',
            'Are you sure you want to delete the amenity',
            (button) => {
                const id = button.dataset.id;
                deleteForm.action = `/admins/settings/amenities/delete/${id}`;
                deleteForm.submit();
            }
        );
    }
});

