var userApp = angular.module('userApp',[]);

userApp.controller('listCtrl', ['$scope', '$http', function($scope, $http){

	//Déclaration des objets
	//----------------------------------------------------------------------
	$scope.members = {};

	//Récupère les données de la BD via le serveur
	//----------------------------------------------------------------------
	$http.get('/members')
	.success(function(data, status, headers, config){
		$scope.members = data;
	})
	.error(function(data, status, headers, config){
	});

	//Incremente la dette du membre
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

