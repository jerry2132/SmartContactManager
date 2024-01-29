
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

//////////

function deleteUser(id){
	
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
      text: "User has been deleted.",
      icon: "success",
      allowOutsideClick: false,  // Allow clicking outside the dialog to close it
	showConfirmButton: true,  // Show the "OK" button
	confirmButtonText: "OK"
      
    }).then(() =>
    {
		window.location = "/admin/deleteUser/"+id;	
	});
    
   
     
  }else{
	  swal.fire({
		  title:"Cancelled",
		  text:"User is safe :)",
		  icon:"error"
	  });
  }
  //window.location = "/user/delete/"+cid;
});
}

const search = () => {

  console.log("searching......");
  
  let query = $("#search-input").val();

  if(query == ""){
    $(".searchResult").hide();
  }else {
	
	$(".searchResult").empty();
    $(".searchResult").show();
   // console.log(query);

    let url = `http://localhost:8080/search/${query}`;

    fetch(url)
    .then((response) => {
      return response.json();
    }).then((data) => {
      //console.log(data);

     let text = `<div class='searched-items'>`;

      data.forEach((contact) => {
        text += `<a href='#'> ${contact.name} </a><br>`;
      });

      text += `</div>`; 
       $(".searchResult").html(text);

    }).catch((error) => {
		console.log("error fetching results",error);
	});
  //  $(".searchResult").show();
  }
};

$(document).on('click', function (e) {
    // Check if the clicked element is not within the searchResult or searchBar
    if (!$(e.target).closest('.searchResult, .searchBar').length) {
        // If not, hide the searchResult
        $('.searchResult').hide();
    }
});

