
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

function deleteContact(cid){
	
	Swal.fire({
  title: "Are you sure?",
  text: "You won't be able to revert this!",
  icon: "warning",
  showCancelButton: true,
  confirmButtonColor: "#3085d6",
  cancelButtonColor: "#d33",
  confirmButtonText: "Yes, delete it!",
  allowOutsideClick: false,  // Prevent clicking outside the dialog to close it
  showConfirmButton: true  // Hide the default confirmation button
}).then((result) => {
  if (result.isConfirmed) {
    Swal.fire({
      title: "Deleted!",
      text: "Your file has been deleted.",
      icon: "success",
      allowOutsideClick: false,  // Allow clicking outside the dialog to close it
	showConfirmButton: true,  // Show the "OK" button
	confirmButtonText: "OK"
      
    }).then(() =>
    {
		window.location = "/admin/delete/"+cid;	
	});
    
   
     
  }else{
	  swal.fire({
		  title:"Cancelled",
		  text:"Your imaginary file is safe :)",
		  icon:"error"
	  });
  }
  //window.location = "/user/delete/"+cid;
});
}


