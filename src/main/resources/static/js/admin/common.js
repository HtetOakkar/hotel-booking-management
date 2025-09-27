document.addEventListener('DOMContentLoaded', () => {
    // --- Collapsible Settings Menu in Sidebar ---
    const settingsButton = document.getElementById('settings-toggle');
    const settingsSubmenu = document.getElementById('settings-submenu');
    const arrowOpen = document.getElementById('settings-arrow-open');
    const arrowClosed = document.getElementById('settings-arrow-closed');

    if (settingsButton && settingsSubmenu && arrowOpen && arrowClosed) {
        settingsButton.addEventListener('click', (event) => {
            event.preventDefault();
            settingsSubmenu.classList.toggle('hidden');
            arrowOpen.classList.toggle('hidden');
            arrowClosed.classList.toggle('hidden');
        });
    }

    // --- Mobile sidebar toggle (in header) ---
    const toggleButton = document.getElementById('sidebar-toggle');
    if (toggleButton) {
        toggleButton.addEventListener('click', () => {
            const mainSidebar = document.querySelector('aside.w-64');
            if (mainSidebar) {
                mainSidebar.classList.toggle('hidden');
            }
        });
    }

    // --- NEW: Profile Dropdown in Header ---
    const profileButton = document.getElementById('profile-dropdown-button');
    const profileMenu = document.getElementById('profile-dropdown-menu');
    if (profileButton && profileMenu) {
        profileButton.addEventListener('click', (event) => {
            event.stopPropagation(); // Prevent the window click listener from firing immediately
            profileMenu.classList.toggle('hidden');
        });

        // Close dropdown if clicked outside
        window.addEventListener('click', (event) => {
            if (!profileButton.contains(event.target) && !profileMenu.contains(event.target)) {
                profileMenu.classList.add('hidden');
            }
        });
        // Close dropdown on Escape key press
        window.addEventListener('keydown', (event) => {
            if (event.key === 'Escape' && !profileMenu.classList.contains('hidden')) {
                profileMenu.classList.add('hidden');
            }
        });
    }
});


/**
 * Generic function for handling delete confirmation modals.
 */
function handleConfirmationModal(buttonSelector, messagePrefix, onConfirm) {
    const confirmationModal = document.getElementById('confirmation-modal');
    if (!confirmationModal) return;

    const messageElement = document.getElementById('confirmation-message');
    const confirmButton = document.getElementById('confirm-delete-button');
    const cancelButton = document.getElementById('cancel-delete-button');
    const deleteButtons = document.querySelectorAll(buttonSelector);

    let confirmCallback = null;

    const openModal = (callback) => {
        confirmationModal.classList.remove('modal-hidden');
        confirmCallback = callback;
    };

    const closeModal = () => {
        confirmationModal.classList.add('modal-hidden');
        confirmCallback = null;
    };

    deleteButtons.forEach(button => {
        button.addEventListener('click', () => {
            const name = button.dataset.name;
            messageElement.textContent = `${messagePrefix} "${name}"? This action cannot be undone.`;
            openModal(() => onConfirm(button));
        });
    });

    confirmButton.addEventListener('click', () => {
        if (confirmCallback) {
            confirmCallback();
        }
        closeModal();
    });

    cancelButton.addEventListener('click', closeModal);
}

function handleModal(modalId, openBtnId, closeBtnId, contentId) {
    const modal = document.getElementById(modalId);
    const openBtn = document.getElementById(openBtnId);
    const closeBtn = document.getElementById(closeBtnId);
    const content = document.getElementById(contentId);

    if (!modal || !openBtn || !closeBtn || !content) return;

    const open = () => {
        modal.classList.remove('modal-hidden');
        setTimeout(() => content.classList.remove('modal-hidden'), 10);
    };
    const close = () => {
        content.classList.add('modal-hidden');
        setTimeout(() => modal.classList.add('modal-hidden'), 300);
    };

    openBtn.addEventListener('click', open);
    closeBtn.addEventListener('click', close);
    modal.addEventListener('click', (event) => {
        if (event.target === modal) close();
    });
    window.addEventListener('keydown', (event) => {
        if (event.key === 'Escape' && !modal.classList.contains('modal-hidden')) {
            close();
        }
    });
}