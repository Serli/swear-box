var adminApp = angular.module('adminApp',[]);

adminApp.controller('listCtrl', ['$scope', '$http', function($scope, $http){

	//Objects declaration
	//----------------------------------------------------------------------
	$scope.members = {};
	$scope.newMember = {};
	$scope.modMember = {};

	//Retrieves data from the database through the server
	//----------------------------------------------------------------------
	$http.get('/members')
	.success(function(data, status, headers, config){
		$scope.members = data;
	})
	.error(function(data, status, headers, config){
	});

	//Checks if the pressed key is a number, a tab or <-
	//----------------------------------------------------------------------
	$scope.isNumeric = function(event) {
		if ((event.which < 48 || event.which > 57) 
				&& (event.which != 0) 
				&& (event.which != 8)) {
			event.preventDefault();
			return false;
		}
	}

	//Add a member to the database through the server
	//----------------------------------------------------------------------
	$scope.addMember = function(){	

		var dataObj = {
				name : $scope.newMember.name,
				firstname : $scope.newMember.firstname
		};			

		$http.post('/member', dataObj)
		.success(function(data, status, headers, config){
			$http.get('/members')
			.success(function(data, status, headers, config){
				$scope.members = data;
			})
			.error(function(data, status, headers, config){
			});
		})
		.error(function(data, status, headers, config){
		});

		$scope.newMember.name= '';
		$scope.newMember.firstname= '';

	}

	//Deleted a member in the database through the server
	//----------------------------------------------------------------------
	$scope.deleteMember = function () {
		$scope.members.splice($scope.idx,1);

		$http.delete('/members/'+$scope.idt)
		.success(function(data, status, headers, config){
			console.log("success");
			$('#deleteMember').modal('hide');
			$http.get('/members')
			.success(function(data, status, headers, config){
				$scope.members = data;
			})
			.error(function(data, status, headers, config){
			});
		})
		.error(function(data, status, headers, config){
			console.log("error");
		});

	};
	
	//Discharge the debt of member
	//----------------------------------------------------------------------
	$scope.dischargeMember = function () {
		$http.put('/discharge/'+$scope.idt, {})
		.success(function(data, status, headers, config){
			$('#dischargeMember').modal('hide');
			$http.get('/members')
			.success(function(data, status, headers, config){
				$scope.members = data;
			})
			.error(function(data, status, headers, config){
			});
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

		$scope.amount= '';

	};

	//Modify the name or/and the firstname of a member
	//----------------------------------------------------------------------
	$scope.modifyMember = function () {

		var dataObj = {
				name : $scope.modMember.name,
				firstname : $scope.modMember.firstname
		};			

		$http.put('/member/'+$scope.idt, dataObj)
		.success(function(data, status, headers, config){
			$('#modifyMember').modal('hide');
			$http.get('/members')
			.success(function(data, status, headers, config){
				$scope.members = data;
			})
			.error(function(data, status, headers, config){
			});
		})
		.error(function(data, status, headers, config){
		});

		$scope.modMember.name= '';
		$scope.modMember.firstname= '';
	};

	//Open the modify modal
	//----------------------------------------------------------------------
	$scope.openModifyModal = function (idt, firstname, name) {
		$scope.idt = idt;
		$scope.modMember.firstname = firstname;
		$scope.modMember.name = name;
		$('#modifyMember').modal('show');
	};

	//Open the delete modal
	//----------------------------------------------------------------------
	$scope.openDeleteModal = function (idx, idt, firstname, name) {
		$scope.idx = idx;
		$scope.idt = idt;
		$scope.firstname = firstname;
		$scope.name = name;
		$('#deleteMember').modal('show');
	};
	
	//Open the modify modal
	//----------------------------------------------------------------------
	$scope.openDischargeModal = function (idt, firstname, name) {
		$scope.idt = idt;
		$scope.firstname = firstname;
		$scope.name = name;
		$('#dischargeMember').modal('show');
	};
	
}]);

