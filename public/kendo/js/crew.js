//modification of local data sample provided by kendo ui docs
var photos = ["a","b","c","d","e","f"],
	names = ["Davolio", "Fuller", "Leverling", "Peacock", "Buchanan", "Suyama", "King", "Callahan", "Dodsworth", "White"],
	availabilties = ["Available","Assigned","On Call","Blocked","Off Duty"],
	capabilities = ["Maintenance", "Construction", "Trouble"];

function createRandomCrewData(count) {
	var data = [],
		now = new Date();
	for (var i = 0; i < count; i++) {
		var photo = photos[Math.floor(Math.random() * photos.length)],
			name = names[Math.floor(Math.random() * names.length)],
			availability = availabilties[Math.floor(Math.random() * availabilties.length)],
			capability = capabilities[Math.floor(Math.random() * capabilities.length)];

		data.push({	
			Photo: photo,
			Name: name,
			Availability: availability,
			Capability: capability
		});
	}
	return data;
}

function generateCrew(itemCount, callback) {
	var data = [],
		delay = 25,
		interval = 500,
		starttime;
	var now = new Date();
	
	setTimeout(function() {
		starttime = +new Date();
		do {
		var photo = photos[Math.floor(Math.random() * photos.length)],
			name = names[Math.floor(Math.random() * names.length)],
			availability = availabilties[Math.floor(Math.random() * availabilties.length)],
			capability = capabilities[Math.floor(Math.random() * capabilities.length)];

			data.push({
				Photo: photo,
				Name: name,
				Availability: availability,
				Capability: capability
			});
		} while(data.length < itemCount && +new Date() - starttime < interval);
			if (data.length < itemCount) {
				setTimeout(arguments.callee, delay);
			} else {
				callback(data);
		}
	}, delay);
}
