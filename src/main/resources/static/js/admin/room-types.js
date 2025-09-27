document.addEventListener('DOMContentLoaded', () => {
    // --- Add/Edit Room Type Modal ---
    const roomTypeModal = document.getElementById('room-type-modal');
    if (roomTypeModal) {
        const modalContent = document.getElementById('room-type-modal-content');
        const openModalButton = document.getElementById('add-room-type-button');
        const closeModalButton = document.getElementById('cancel-room-type-button');
        const editButtons = document.querySelectorAll('.edit-room-type-button');
        const modalTitle = document.getElementById('modal-title');
        const roomTypeForm = document.getElementById('room-type-form');
        const roomTypeIdInput = document.getElementById('roomTypeId');
        const isFeaturedCheckbox = document.getElementById('isFeatured');

        const openModal = () => {
            roomTypeModal.classList.remove('modal-hidden');
            setTimeout(() => modalContent.classList.remove('modal-hidden'), 10);
        };
        const closeModal = () => {
            modalContent.classList.add('modal-hidden');
            setTimeout(() => roomTypeModal.classList.add('modal-hidden'), 300);
        };

        openModalButton.addEventListener('click', () => {
            roomTypeForm.reset();
            roomTypeIdInput.value = '';
            modalTitle.textContent = 'Add New Room Type';
            roomTypeForm.action = '/admins/settings/room-types/add';
            isFeaturedCheckbox.checked = false;
            openModal();
        });

        editButtons.forEach(button => {
            button.addEventListener('click', () => {
                const data = button.dataset;
                roomTypeIdInput.value = data.id;
                document.getElementById('name').value = data.name;
                document.getElementById('description').value = data.description;
                document.getElementById('price').value = data.price;
                document.getElementById('maxAdults').value = data.maxAdults;
                document.getElementById('maxChildren').value = data.maxChildren;
                isFeaturedCheckbox.checked = (data.isfeatured === 'true');
                modalTitle.textContent = 'Edit Room Type';
                roomTypeForm.action = `/admins/settings/room-types/update/${data.id}`;
                openModal();
            });
        });

        closeModalButton.addEventListener('click', closeModal);
        roomTypeModal.addEventListener('click', (event) => {
            if (event.target === roomTypeModal) closeModal();
        });
    }

    // --- Delete Room Type Confirmation ---
    const deleteForm = document.getElementById('delete-room-type-form');
    if(deleteForm){
        handleConfirmationModal(
            '.delete-room-type-button',
            'Are you sure you want to delete the room type',
            (button) => {
                const id = button.dataset.id;
                deleteForm.action = `/admins/settings/room-types/delete/${id}`;
                deleteForm.submit();
            }
        );
    }

    // --- Manage Amenities Modal ---
    const amenitiesModal = document.getElementById('manage-amenities-modal');
    if(amenitiesModal) {
        const openButtons = document.querySelectorAll('.manage-amenities-button');
        const closeButton = document.getElementById('close-amenities-button');
        const modalTitle = document.getElementById('amenities-modal-title');
        const amenitiesList = document.getElementById('amenities-list');

        const openAmenitiesModal = () => amenitiesModal.classList.remove('modal-hidden');
        const closeAmenitiesModal = () => amenitiesModal.classList.add('modal-hidden');

        openButtons.forEach(button => {
            button.addEventListener('click', async () => {
                const roomTypeId = button.dataset.roomtypeid;
                const roomTypeName = button.dataset.roomtypename;
                modalTitle.textContent = `Amenities for ${roomTypeName}`;

                // Fetch and display amenities
                try {
                    const response = await fetch(`/admins/settings/amenities/by-room-type/${roomTypeId}`);
                    if(!response.ok) throw new Error('Network response was not ok');
                    const amenities = await response.json();

                    amenitiesList.innerHTML = ''; // Clear previous list
                    if(amenities.length > 0) {
                        amenities.forEach(amenity => {
                            const amenityEl = document.createElement('div');
                            amenityEl.className = 'p-2 bg-gray-100 rounded-md';
                            amenityEl.textContent = amenity.name;
                            amenitiesList.appendChild(amenityEl);
                        });
                    } else {
                        amenitiesList.innerHTML = '<p class="text-gray-500">No amenities assigned to this room type.</p>';
                    }
                } catch (error) {
                    amenitiesList.innerHTML = '<p class="text-red-500">Could not load amenities.</p>';
                    console.error('Fetch error:', error);
                }

                openAmenitiesModal();
            });
        });

        closeButton.addEventListener('click', closeAmenitiesModal);
        amenitiesModal.addEventListener('click', (event) => {
            if (event.target === amenitiesModal) closeAmenitiesModal();
        });
    }
});
