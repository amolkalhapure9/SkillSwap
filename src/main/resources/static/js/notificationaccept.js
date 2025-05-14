

const accept=document.querySelectorAll(".accept-button");

accept.forEach(button=>{
	
	button.addEventListener("click", function(event){
		event.preventDefault();
		const senderid=this.getAttribute('data-user')
		fetch("/acceptRequest",{
			method:'POST',
			headers:{
				'Content-Type': 'application/json'
				
			},
			body: JSON.stringify({senderid:senderid})
			
		})
		.then(response=>{
			if(response.ok){
				document.getElementById("isAccepted").innerText="Accepted";
				document.getElementById("isAccepted").style.background="gray";
				
			
			}
			else{
				alaert("request failed");
			}
		})
		.catch(error =>console.error('Error:', error));
			
		
			
		
	})
})