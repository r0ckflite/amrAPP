function customerCallTree($scope) { 
	this.contact = [{
		type:"contactId",value:"C2013_223",
		type:"createdOn", value:"1380748471821",
		type:"updatedOn", value:"null",
		type:"createdBy", value:"null",
		type:"updatedBy", value:"null",
		type:"workArea", value:{
		  type:"id", value:"201301010",
		  type:"name",value:"West of River",
		  type:"parent", value:"null"
		  },
		type:"networkId", value:"null",
		type:"contactCustomer", value:"null",
		type:"criticalCustomer", value:"null",
		type:"priorityCustomer", value:"null",
		type:"retailer", value:"null",
		type:"jobId", value:"0",
		type:"hazards", value:"null",
		type:"calledInByEmergencyOperatorFlag", value:"false",
		type:"hazardFlag", value:"false",
		type:"activeFlag", value:"false",
		type:"manuallyCancelledFlag", value:"false",
		type:"customerInformationOverriddenFlag", value:"false",
		type:"callbackCustomerOnRestoreFlag", value:"false",
		type:"callbackOnETRFlag", value:"false",
		type:"name", value:"null",
		type:"address1", value:"null",
		type:"address2", value:"null",
		type:"city", value:"null",
		type:"state", value:"null",
		type:"zip", value:"null",
		type:"callbackContact", value:"null",
		type:"geoLatCoordinate", value:"0.0",
		type:"geoLongCoordinate", value:"0.0",
		type:"contactMethod", value:"null",
		type:"entryMethod", value:"null",
		type:"numberOfCalls", value:"0"
	}];

	$scope.record= {};

	$scope.details = function(customer){
		$scope.record= angular.copy(customer);
	};

}
