var adminApp = angular.module('adminApp',[]);

adminApp.controller('listCtrl', ['$scope', '$http', function($scope, $http){

	//Déclaration des objets
	//----------------------------------------------------------------------
	$scope.members = {};

	//Récupère les données de la BD via le serveur
	//----------------------------------------------------------------------
	$http.get('/person')
	.success(function(data, status, headers, config){
		$scope.members = data;
	})
	.error(function(data, status, headers, config){
	});

	//Ajoute un membre dans la BD via le serveur
	//----------------------------------------------------------------------
	$scope.addMember = function(){	

		var dataObj = {
				name : $scope.newMember.name,
				firstname : $scope.newMember.firstname
		};			

		$http.post('/addPerson', dataObj)
		.success(function(data, status, headers, config){
			$http.get('/person')
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

	//Supprime un membre dans la BD via le serveur
	//----------------------------------------------------------------------
	$scope.deleteMember = function (idt, index) {
		
		var dataObj = {
				id : idt
		};
		
		$scope.members.splice(index,1);
		
		$http.post('/deletePerson', dataObj)
		.success(function(data, status, headers, config){
			$http.get('/person')
			.success(function(data, status, headers, config){
				$scope.members = data;
			})
			.error(function(data, status, headers, config){
			});
		})
		.error(function(data, status, headers, config){
		});

	};

}]);

