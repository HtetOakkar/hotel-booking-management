document.addEventListener('DOMContentLoaded', () => {
    // --- Add/Edit Payment Method Modal ---
    const paymentModal = document.getElementById('payment-modal');
    if (paymentModal) {
        const openModalButton = document.getElementById('add-payment-button');
        const closeModalButton = document.getElementById('cancel-payment-button');
        const editButtons = document.querySelectorAll('.edit-payment-button');
        const modalTitle = document.getElementById('payment-modal-title');
        const paymentForm = document.getElementById('payment-form');
        const paymentIdInput = document.getElementById('paymentId');
        const isActiveCheckbox = document.getElementById('isActive');

        const openModal = () => paymentModal.classList.remove('modal-hidden');
        const closeModal = () => paymentModal.classList.add('modal-hidden');

        // Open for ADDING
        openModalButton.addEventListener('click', () => {
            paymentForm.reset();
            paymentIdInput.value = '';
            isActiveCheckbox.checked = true; // Default to active
            modalTitle.textContent = 'Add New Payment Method';
            paymentForm.action = '/admins/settings/payment-methods/add';
            openModal();
        });

        // Open for EDITING
        editButtons.forEach(button => {
            button.addEventListener('click', () => {
                const data = button.dataset;
                paymentIdInput.value = data.id;
                document.getElementById('name').value = data.name;
                isActiveCheckbox.checked = (data.isactive === 'true');
                modalTitle.textContent = 'Edit Payment Method';
                paymentForm.action = `/admins/settings/payment-methods/update/${data.id}`;
                openModal();
            });
        });

        closeModalButton.addEventListener('click', closeModal);
        paymentModal.addEventListener('click', (event) => {
            if (event.target === paymentModal) closeModal();
        });
        window.addEventListener('keydown', (event) => {
            if (event.key === 'Escape' && !paymentModal.classList.contains('modal-hidden')) {
                closeModal();
            }
        });
    }

    // --- Delete Payment Method Confirmation ---
    const deleteForm = document.getElementById('delete-payment-form');
    if (deleteForm) {
        handleConfirmationModal(
            '.delete-payment-button',
            'Are you sure you want to delete the payment method',
            (button) => {
                const id = button.dataset.id;
                deleteForm.action = `/admins/settings/payment-methods/delete/${id}`;
                deleteForm.submit();
            }
        );
    }
});

