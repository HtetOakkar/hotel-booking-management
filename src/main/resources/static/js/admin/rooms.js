document.addEventListener('DOMContentLoaded', () => {
    // --- Add/Edit Room Modal ---
    const roomModal = document.getElementById('room-modal');
    if (roomModal) {
        const modalContent = roomModal.querySelector('.bg-white');
        const openModalButton = document.getElementById('add-room-button');
        const closeModalButton = document.getElementById('cancel-room-button');
        const editButtons = document.querySelectorAll('.edit-room-button');
        const modalTitle = document.getElementById('room-modal-title');
        const roomForm = document.getElementById('room-form');
        const roomIdInput = document.getElementById('roomId');

        const openModal = () => {
            roomModal.classList.remove('modal-hidden');
            setTimeout(() => modalContent.classList.remove('modal-hidden'), 10);
        };
        const closeModal = () => {
            modalContent.classList.add('modal-hidden');
            setTimeout(() => roomModal.classList.add('modal-hidden'), 300);
        };

        // Open for ADDING
        openModalButton.addEventListener('click', () => {
            roomForm.reset();
            roomIdInput.value = '';
            modalTitle.textContent = 'Add New Room';
            roomForm.action = '/admins/settings/rooms/add';
            openModal();
        });

        // Open for EDITING
        editButtons.forEach(button => {
            button.addEventListener('click', () => {
                const data = button.dataset;
                roomIdInput.value = data.id;
                document.getElementById('roomNumber').value = data.number;
                document.getElementById('status').value = data.status;
                document.getElementById('roomTypeId').value = data.roomtypeid;
                modalTitle.textContent = 'Edit Room';
                roomForm.action = `/admins/settings/rooms/update/${data.id}`;
                openModal();
            });
        });

        closeModalButton.addEventListener('click', closeModal);
        roomModal.addEventListener('click', (event) => {
            if (event.target === roomModal) closeModal();
        });
    }

    // --- Delete Room Confirmation ---
    const deleteForm = document.getElementById('delete-room-form');
    if(deleteForm) {
        handleConfirmationModal(
            '.delete-room-button',
            'Are you sure you want to delete Room',
            (button) => {
                const id = button.dataset.id;
                deleteForm.action = `/admins/settings/rooms/delete/${id}`;
                deleteForm.submit();
            }
        );
    }

    // --- Direct Status Change in Table ---
    const statusSelects = document.querySelectorAll('.status-change-select');
    statusSelects.forEach(select => {
        select.addEventListener('change', () => {
            // Find the closest parent form and submit it
            const form = select.closest('form');
            if (form) {
                form.submit();
            }
        });
    });
});

