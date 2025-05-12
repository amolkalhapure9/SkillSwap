



const buttons=document.querySelectorAll(".send-button-request");

buttons.forEach(button=>{
	
	button.addEventListener('click', function(event){
		event.preventDefault();
		
		const userid=this.getAttribute("data-user");
		
		fetch('/send-request',{
			
			method:'POST',
			headers:{
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({ userid: userid })
			
		})
		.then(response=>{
			if(response.ok){
				this.innerText = 'Request Sent';
				this.disabled = true;
				this.style.backgroundColor = '#9E9E9E';
			}
			else {
			                alert('Request failed!');
			            }
		})
		.catch(error => console.error('Error:', error));
			

			
		
		
	})
	
})
