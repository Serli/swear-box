var adminApp = angular.module('adminApp',[]);

adminApp.controller('listCtrl', ['$scope', '$http', function($scope, $http){

	$scope.newMember = {};

	$http.get('//localhost:9000/person')
	.success(function(data, status, headers, config){
		$scope.members = data;
	})
	.error(function(data, status, headers, config){
	});

	/*$scope.addMember = function(){
		$scope.members.push($scope.newMember);
		console.log("try");

  	$http.post('//localhost:9000/addPerson', {name:"nfe"})
		.success(function(data, status, headers, config){
			console.log("suc");
		})
		.error(function(data, status, headers, config){
			console.log("err");
		});

  	$scope.newMember= {};
  }*/
}]);

