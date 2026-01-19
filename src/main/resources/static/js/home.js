document.addEventListener('DOMContentLoaded', function() {
    /*----------------------------------------MENU-------------------------------------*/
    const menuBtn = document.getElementById("menuToggle");
    const menuCollapse = document.getElementById("menuCollapse");
    const closeBtn = document.getElementById("close_button");
    const navItems = document.getElementsByClassName("nav-link");
    const homeNavItem = document.getElementById("home-item-id");

    function openMenu() {
        menuCollapse.classList.remove("close")
        menuCollapse.classList.add("open");
    }

    function closeMenu() {
        menuCollapse.classList.remove("open")
        menuCollapse.classList.add("close");
    }

    function activeMenuItem() {
    }

    menuBtn.addEventListener('click', openMenu);
    closeBtn.addEventListener('click', closeMenu);
    /*--------------------------------------------EXPAND CATEGORIES-------------------------------*/
    // Handle submenu toggle
    window.toggleSubmenu = function (event, submenuId) {
        event.preventDefault();
        const submenu = document.getElementById(submenuId);
        const navLink = event.currentTarget;

        if (submenu && navLink) {
            submenu.classList.toggle('active');
            navLink.classList.toggle('expanded');
        }
    };
    /*---------------------------------------------------------------------------------------------*/
    document.querySelectorAll('.register-password-toggle').forEach(button => {
        button.addEventListener('click', function () {
            const input = this.previousElementSibling;
            const icon = this.querySelector('.material-symbols-outlined');

            if (input.type === 'password') {
                input.type = 'text';
                icon.textContent = 'visibility';
            } else {
                input.type = 'password';
                icon.textContent = 'visibility_off';
            }
        });
    });
    /*-------------------------------------------------------------------------------------------------*/
    window.closeModal = function (){
        const modal = document.getElementById("notifyModal");
        if (modal) {
            modal.style.display = "none";
        }
    }
});