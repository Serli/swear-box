var userApp = angular.module('userApp',[]);

userApp.controller('listCtrl', ['$scope', '$http', function($scope, $http){

	//Objects declaration
	//----------------------------------------------------------------------
	$scope.members = {};

	$scope.range = [];
	$scope.itemsPerPage = 5;
	$scope.numPage = 0;
	
	//Retrieves the data of the BDD through the server
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

	//Increase the member debt
	//----------------------------------------------------------------------
	$scope.increase = function (idt) {
		$http.put('/increase/'+idt, {})
		.success(function(data, status, headers, config){
			$http.get('/members')
			.success(function(data, status, headers, config){
				$scope.members = '';
				$scope.members = data;
				$scope.range = [];
				for (var i=0; i<$scope.members.length/$scope.itemsPerPage; i++) {
				    $scope.range.push(i+1);
				  }
			})
			.error(function(data, status, headers, config){
			});
		})
		.error(function(data, status, headers, config){
		});

	};
	
	//----------------------------------------------------------------------
	$scope.pagination = function(id) {
		$scope.numPage = id;
	};
	
}]);

