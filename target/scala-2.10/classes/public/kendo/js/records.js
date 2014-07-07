//modification of local data sample provided by kendo ui docs
var firstNames = ["Nancy", "Andrew", "Janet", "Margaret", "Steven", "Michael", "Robert", "Laura", "Anne", "Nige"],
	lastNames = ["Davolio", "Fuller", "Leverling", "Peacock", "Buchanan", "Suyama", "King", "Callahan", "Dodsworth", "White"],
    crewIds = ["a","b","c","d","e","f", ""],
    deviceIds = ["Device1","Device2","Device3","Device4" ],
	outageLevels = ["Feeder","OH Tap","UG Tap","OH TR","OH Service","UG Service"],
	outageCodes1 = ["all out","part out","no-out","wire down","broken pole","medical","hospital","primary customer","major/critical customer"],
	outageCodes2 = ["wire down","broken pole","medical","hospital","primary customer","major/critical customer"],
	jobStatuses = ["Active","Dispatched","Completed","In progess","En route"],
	cities = ["Alphavilla", "Bravo Town", "Delta City"],
    workAreas = ["Maple Grove", "Shorewood", "Minneapolis" ,"Edina" ],
flags = [ "", "", "","", "", "", "", "Hazard", "911", "ETOR Warning" ],
	numContacts = [3,4,50,100,10,20],
	numAffected = [30,80,10];

function createRandomData(count) {
	var data = [],
		now = new Date();
	for (var i = 0; i < count; i++) {
		var firstName = firstNames[Math.floor(Math.random() * firstNames.length)],
			lastName = lastNames[Math.floor(Math.random() * lastNames.length)],
			crewId = crewIds[Math.floor(Math.random() * crewIds.length)],
			deviceId = deviceIds[Math.floor(Math.random() * deviceIds.length)],
		customersOut = Math.floor(Math.random() * 1000 ),
		criticalCustomersOut = Math.floor(Math.random() * 50 ) - 25,
			outageLevel = outageLevels[Math.floor(Math.random() * outageLevels.length)],
			outageCode1 = outageCodes1[Math.floor(Math.random() * outageCodes1.length)],
			outageCode2 = outageCodes2[Math.floor(Math.random() * outageCodes2.length)],
		jobStatus = jobStatuses[Math.floor(Math.random() * jobStatuses.length)],			
		workArea = workAreas[Math.floor(Math.random() * workAreas.length)], 
			city = cities[Math.floor(Math.random() * cities.length)],
			contacts = numContacts[Math.floor(Math.random() * numContacts.length)],
			affected = numAffected[Math.floor(Math.random() * numAffected.length)];
		flag = flags[Math.floor(Math.random() * flags.length)];

		data.push({	
			Id: i + 1000,
			CustomerId: 33000000 + Math.floor(Math.random() * 2000),
			PremiseId: 10000 + Math.floor(Math.random() * 9000),
			FirstName: firstName,
			LastName: lastName,
                        DeviceId: deviceId,
			CrewId: crewId,
			CustomersOut: customersOut,
			CriticalCustomersOut: criticalCustomersOut,
			OutageLevel: outageLevel,
			OutageCode1: outageCode1,
			OutageCode2: outageCode2,
			JobStatus: jobStatus,
			WorkArea: workArea,
			Flags: flag,
			City: city,
			numContacts: contacts,
			numAffected: affected
		});
	}
	return data;
}

function generatePeople(itemCount, callback) {
	var data = [],
		delay = 25,
		interval = 500,
		starttime;
	var now = new Date();
	
	setTimeout(function() {
		starttime = +new Date();
		do {
			var firstName = firstNames[Math.floor(Math.random() * firstNames.length)],
				lastName = lastNames[Math.floor(Math.random() * lastNames.length)],
				outageLevel = outageLevels[Math.floor(Math.random() * outageLevels.length)],
				outageCode1 = outageCodes1[Math.floor(Math.random() * outageCodes1.length)],
				outageCode2 = outageCodes2[Math.floor(Math.random() * outageCodes2.length)],
				jobStatus = jobStatuses[Math.floor(Math.random() * jobStatuses.length)],			
				city = cities[Math.floor(Math.random() * cities.length)],
				contacts = numContacts[Math.floor(Math.random() * numContacts.length)],
				affected = numAffected[Math.floor(Math.random() * numAffected.length)];

			data.push({
				Id: data.length + 1000,
				FirstName: firstName,
				LastName: lastName,
				OutageLevel: outageLevel,
				OutageCode1: outageCode1,
				OutageCode2: outageCode2,
				JobStatus: jobStatus,
				City: city,
				numContacts: contacts,
				numAffected: affected,
			});
		} while(data.length < itemCount && +new Date() - starttime < interval);
			if (data.length < itemCount) {
				setTimeout(arguments.callee, delay);
			} else {
				callback(data);
		}
	}, delay);
}
