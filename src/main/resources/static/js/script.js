// this is to add tiny mce in our text area
 tinymce.init({
        selector: '#mytextarea'
      });
      
function deleteContact(cid){
	
	swal({
  title: "Are you sure?",
  text: "Once deleted, you will not be able to recover this Contact!",
  icon: "warning",
  buttons: true,
  dangerMode: true,
})
.then((willDelete) => {
  if (willDelete) {
    window.location="/user/delete/"+cid;
    }
   else {
    swal("Your contact is safe!");
  }
});
}

const search=()=>{
	
	var word = $("#search-input").val();
	
	if(word==''){
		$(".search-result").hide();
		 }
	else{
		
		let url =`http://localhost:8080/user/search/${word}`;
		
		fetch(url).then((response) => {
			
			return response.json();
		}).then((data) =>{
			
			//console.log(data);
			
			let text= `<div class="list-group active">`;
			
			data.forEach((contact) => {
				
				text+= `<a href="/user/contact/${contact.c_Id}" class="list-group-item list-group-item-action">${contact.name}</a>`;
			});
			
			
			text+=`</div>`;
			
			$(".search-result").html(text);
			
			$(".search-result").show();
		});
		
		
		
	}
	 
}