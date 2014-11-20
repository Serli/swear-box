var adminApp = angular.module('adminApp',[]);

adminApp.controller('listCtrl', ['$scope', '$http', function($scope, $http){

	$scope.members = {};

	$http.get('//localhost:9000/person')
	.success(function(data, status, headers, config){
		$scope.members = data;
	})
	.error(function(data, status, headers, config){
	});

	$scope.addMember = function(){
		$scope.members.push($scope.newMember);	

		var dataObj = {
			name : $scope.newMember.name,
			firstname : $scope.newMember.firstname
		};			

  	$http.post('//localhost:9000/addPerson', dataObj)
		.success(function(data, status, headers, config){
			console.log("suc");
		})
		.error(function(data, status, headers, config){
			console.log("err");
		});

  	$scope.newMember.name= '';
  	$scope.newMember.firstname= '';

		$http.get('//localhost:9000/person')
		.success(function(data, status, headers, config){
			$scope.members = data;
		})
		.error(function(data, status, headers, config){
		});

  }
}]);

