var adminApp = angular.module('adminApp',[]);

adminApp.controller('listCtrl', ['$scope', '$http', function($scope, $http){

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

	//Vérifie si la touche du clavier préssée est un chiffre, une tab ou un <-
	//----------------------------------------------------------------------
	$scope.isNumeric = function(event) {
		if ((event.which < 48 || event.which > 57) 
				&& (event.which != 0) 
				&& (event.which != 8)) {
			event.preventDefault();
			return false;
		}
	}

	//Ajoute un membre dans la BD via le serveur
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

	//Supprime un membre dans la BD via le serveur
	//----------------------------------------------------------------------
	$scope.deleteMember = function (idt, index) {
		$scope.members.splice(index,1);

		$http.delete('/members/'+idt)
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

	};
	
	//Acquitte la dette du membre
	//----------------------------------------------------------------------
	$scope.debtMember = function (idt, index) {
		$http.put('/debt/'+idt, {})
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

	};

}]);

