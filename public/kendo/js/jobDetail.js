
function createJobDetailData(jobId) {
	var data = [],
		now = new Date();
		
	data.push({	
		Id: "Summary", 
		Value: "Critical Outage Job",
		NumberAffected: 30,
		Photo: "house-icon.png",
	});

	data.push({	
		Id: "Device", 
		Value: "Fuse AB99232-01",
		NumberAffected: 10,
		Photo: "house-icon.png",
	});

	data.push({	
		Id: "Crew #1", 
		Value: "Crew A - On Site",
		NumberAffected: 1,
		Photo: "industry-icon.png",
	});

 	data.push({	
		Id: "Crew #2", 
		Value: "Bubba",
		NumberAffected: 10,
		Photo: "law-enforcement.jpg",
	});


	data.push({	
		Id: "Number of Customers Affected", 
		Value: "30 Residential, 1 Commercial",
		NumberAffected: 10,
		Photo: "industry-icon.png",
	});

	data.push({	
		Id: "Customer Contacts", 
		Value: "34 Call In,  10 Call Backs Pending",
		NumberAffected: 10,
		Photo: "law-enforcement.jpg",
	});

	data.push({	
		Id: "Alarms and Notifications", 
		Value: "Hazard, Medical Equipment",
		NumberAffected: 10,
		Photo: "law-enforcement.jpg",
	});

	data.push({	
		Id: "Area / Division", 
		Value: "Northwest Zone 1",
		NumberAffected: 10,
		Photo: "medical.jpg",
	});


	return data;
}

