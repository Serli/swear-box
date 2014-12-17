var userApp = angular.module('userApp',[]);

userApp.controller('listCtrl', ['$scope', '$http', function($scope, $http){

	//Objects declaration
	//----------------------------------------------------------------------
	$scope.members = {};

	//Retrieves the data of the BDD through the server
	//----------------------------------------------------------------------
	$http.get('/members')
	.success(function(data, status, headers, config){
		$scope.members = data;
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
			})
			.error(function(data, status, headers, config){
			});
		})
		.error(function(data, status, headers, config){
		});

	};
	
}]);

