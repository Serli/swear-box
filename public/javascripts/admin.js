var adminApp = angular.module('adminApp',[]);

adminApp.controller('listCtrl', ['$scope', '$http', function($scope, $http){

	//Objects declaration
	//----------------------------------------------------------------------
	$scope.members = {};
	$scope.newMember = {};
	$scope.newMember.name= '';
	$scope.modMember = {};
	$scope.modMember.name= '';

	$scope.range = [];
	$scope.itemsPerPage = 5;
	$scope.numPage = 0;
	
	$scope.idImage = 0;	
	
	
	
	//controller
	//Retrieves data from the database through the server
	//----------------------------------------------------------------------
	$http.get('/members')
	.success(function(data, status, headers, config){
		$scope.members = data;
		$scope.range = [];
		for (var i=0; i<$scope.members.length/$scope.itemsPerPage; i++) {
		    $scope.range.push(i+1);
		  }
	})
	.error(function(data, status, headers, config){
	});

	$http.get('/amount')
	.success(function(data, status, headers, config){
		$scope.amountTag = data;
	})
	.error(function(data, status, headers, config){
	});

	//----------------------------------------------------------------------
	$scope.getMembers = function() {
		$http.get('/members')
		.success(function(data, status, headers, config){
			$scope.members = data;				
			$scope.range = [];
			for (var i=0; i<$scope.members.length/$scope.itemsPerPage; i++) {
			    $scope.range.push(i+1);
			  }
		})
		.error(function(data, status, headers, config){
		});
	};
	
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
			$scope.getMembers();
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
			//$('#deleteMember').modal('hide');
			ModalHide('#deleteMember');
			$scope.getMembers();
		})
		.error(function(data, status, headers, config){
		});

	};
	
	//Discharge the debt of member
	//----------------------------------------------------------------------
	$scope.dischargeMember = function () {
		$http.put('/members/discharge/'+$scope.idt, {})
		.success(function(data, status, headers, config){
			//$('#dischargeMember').modal('hide');
			ModalHide('#dischargeMember');
			$scope.getMembers();
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
			//$('#modifyMember').modal('hide');
			ModalHide('#modifyMember');
			$scope.getMembers();
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
		//var $load = $('#span-load'+$scope.idImage).show();
		//var $unload = $('#img'+$scope.idImage).hide();
		ChargementImage('#span-load'+$scope.idImage,'#img'+$scope.idImage);
		fd.append('file',file);		
		$http.put('/members/picture/'+$scope.idImage, fd, {
			transformRequest: angular.identity,
			headers: {'Content-Type': undefined}
		})
		.success(function(data, status, headers, config){
			$scope.getMembers();
			ChargementImage('#img'+$scope.idImage,'#span-load'+$scope.idImage);
			//$load.hide();
			//$unload.show();
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
		//$('#deleteMember').modal('show');
		ModalShow('#deleteMember');
	};
	
	//Open the modify modal
	//----------------------------------------------------------------------
	$scope.openDischargeModal = function (idt, firstname, name) {
		$scope.idt = idt;
		$scope.firstname = firstname;
		$scope.name = name;
		//$('#dischargeMember').modal('show');
		ModalShow('#dischargeMember');
	};
	
	//Open the modify modal
	//----------------------------------------------------------------------
	var OpenModifyModal = function (idt, firstname, name) {
		scope.idt = idt;
		scope.modMember.firstname = firstname;
		scope.modMember.name = name;
		//$('#modifyMember').modal('show');
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