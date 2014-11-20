var adminApp = angular.module('adminApp',[]);

adminApp.controller('listCtrl', ['$scope', '$http', function($scope, $http){

	$scope.members = {};

	$http.get('/person')
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

		$http.post('/addPerson', dataObj)
		.success(function(data, status, headers, config){
			console.log("suc");
			$http.get('/person')
			.success(function(data, status, headers, config){
				$scope.members = data;
			})
			.error(function(data, status, headers, config){
			});
		})
		.error(function(data, status, headers, config){
			console.log("err");
		});

		$scope.newMember.name= '';
		$scope.newMember.firstname= '';

	}

	$scope.deleteMember = function (idt) {
		var dataObj = {
				id : idt
		};

		$http.post('/deletePerson', dataObj)
		.success(function(data, status, headers, config){
			console.log("suc");
			$http.get('/person')
			.success(function(data, status, headers, config){
				$scope.members = data;
			})
			.error(function(data, status, headers, config){
			});
		})
		.error(function(data, status, headers, config){
			console.log("err");
		});

	};

}]);

