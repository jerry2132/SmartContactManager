
/*function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    sidebar.style.width = sidebar.style.width === '200px' ? '0' : '200px';
}*/

function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    const content = document.querySelector('.content');

    if (sidebar.style.width === '200px') {
        sidebar.style.width = '0';
        content.style.marginLeft = '0';
       // navbar.style.top = '0';
    } else {
        sidebar.style.width = '200px';
        content.style.marginLeft = '200px';
       // navbar.style.top = '-60px'; 
    }
}

