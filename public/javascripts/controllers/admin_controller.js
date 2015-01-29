app.controller('adminCtrl', ['$scope', '$http', 'membersService', function($scope, $http, membersService){

	//Objects declaration
	//----------------------------------------------------------------------
	$scope.idImage = 0;	
	$scope.numPage = 0;
	$scope.itemsPerPage = 5;
	$scope.members = {};
	$scope.newMember = {};
	$scope.newMember.name= '';
	$scope.modMember = {};
	$scope.modMember.name= '';
	$scope.range = [];

	getMembers();
	
	// Function which retrieves the list of members of the database calling 
	// corresponding function through the server.
	//----------------------------------------------------------------------
	function getMembers() {
        membersService.getMembers()
        .success(function (membs) {
            $scope.members = membs;
            getRange();
        })
        .error(function (error) {
            alert( 'Unable to load members data: ' + error.message);
        });
    };
    
    // Function which fill the range with all the pages.
	//----------------------------------------------------------------------
    function getRange() {
		for (var i=0; i<$scope.members.length/$scope.itemsPerPage; i++) {
			$scope.range.push(i+1);
		}
	};

	$http.get('/amount')
	.success(function(data, status, headers, config){
		$scope.amountTag = data;
	})
	.error(function(data, status, headers, config){
	});

	
	//----------------------------------------------------------------------
	$scope.pagination = function(id) {
		$scope.numPage = id;
	};
	
	//Checks if the pressed key is a number, a tab or <-
	//----------------------------------------------------------------------
	$scope.isNumeric = function(event) {
		if ((event.which < 48 || event.which > 57) && (event.which !== 0) && (event.which !== 8)) {
			event.preventDefault();
			return false;
		}
	};

	//Add a member to the database through the server
	//----------------------------------------------------------------------
	$scope.addMember = function(){	

		var dataObj = {
				name : $scope.newMember.name,
				firstname : $scope.newMember.firstname
		};			

		$http.post('/members', dataObj)
		.success(function(data, status, headers, config){
			getMembers();
		})
		.error(function(data, status, headers, config){
		});

		$scope.newMember.name= '';
		$scope.newMember.firstname= '';

	};

	//Deleted a member in the database through the server
	//----------------------------------------------------------------------
	$scope.deleteMember = function () {
		if( ($scope.members.length % $scope.itemsPerPage == 1) && ($scope.idx+1 == $scope.members.length) && ($scope.members.length!==1) ) {
			$scope.numPage = $scope.numPage-1;
		}
		
		$scope.members.splice($scope.idx,1);

		$http.delete('/members/'+$scope.idt)
		.success(function(data, status, headers, config){
			ModalHide('#deleteMember');
			getMembers();
		})
		.error(function(data, status, headers, config){
		});

	};
	
	//Discharge the debt of member
	//----------------------------------------------------------------------
	$scope.dischargeMember = function () {
		$http.put('/members/discharge/'+$scope.idt, {})
		.success(function(data, status, headers, config){
			ModalHide('#dischargeMember');
			getMembers();
		})
		.error(function(data, status, headers, config){
		});

	};
	
	//Update the amount of penalty
	//----------------------------------------------------------------------
	$scope.updateAmount = function(){	

		var dataObj = {
				amount : $scope.amount
		};			

		$http.put('/user', dataObj)
		.success(function(data, status, headers, config){
		})
		.error(function(data, status, headers, config){
		});

		$scope.amountTag= $scope.amount;
		$scope.amount= '';

	};

	//Modify the name or/and the firstname of a member
	//----------------------------------------------------------------------
	$scope.modifyMember = function () {

		var dataObj = {
				name : $scope.modMember.name,
				firstname : $scope.modMember.firstname
		};			

		$http.put('/members/name/'+$scope.idt, dataObj)
		.success(function(data, status, headers, config){
			ModalHide('#modifyMember');
			getMembers();
		})
		.error(function(data, status, headers, config){
		});

		$scope.modMember.name= '';
		$scope.modMember.firstname= '';
	};

	
	//----------------------------------------------------------------------
	$scope.browseImage = function (idt) {
		$('#hiddenfile').click();
		$scope.idImage = idt;
	};
	
	//----------------------------------------------------------------------
	$scope.onChange = function () {
		var file = document.getElementById('hiddenfile').files[0];
		var fd = new FormData();
		ChargementImage('#span-load'+$scope.idImage,'#img'+$scope.idImage);
		fd.append('file',file);		
		$http.put('/members/picture/'+$scope.idImage, fd, {
			transformRequest: angular.identity,
			headers: {'Content-Type': undefined}
		})
		.success(function(data, status, headers, config){
			getMembers();
			ChargementImage('#img'+$scope.idImage,'#span-load'+$scope.idImage);
		})
		.error(function(data, status, headers, config){
		});

	};

	//Open the delete modal
	//----------------------------------------------------------------------
	$scope.openDeleteModal = function (idx, idt, firstname, name) {
		$scope.idx = idx;
		$scope.idt = idt;
		$scope.firstname = firstname;
		$scope.name = name;
		ModalShow('#deleteMember');
	};
	
	//Open the modify modal
	//----------------------------------------------------------------------
	$scope.openDischargeModal = function (idt, firstname, name) {
		$scope.idt = idt;
		$scope.firstname = firstname;
		$scope.name = name;
		ModalShow('#dischargeMember');
	};
	
	//Open the modify modal
	//----------------------------------------------------------------------
	$scope.openModifyModal = function (idt, firstname, name) {
		$scope.idt = idt;
		$scope.modMember.firstname = firstname;
		$scope.modMember.name = name;
		ModalShow('#modifyMember');
	};
}]);

var ModalShow = function (vModal){
	$(vModal).modal('show');
};

var ModalHide = function (vModal){
	$(vModal).modal('hide');
};

var ChargementImage = function (s,h){
	$(s).show();
	$(h).hide();
};