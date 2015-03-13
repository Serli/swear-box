app.controller('boUserCtrl',
	['$scope', 'userService',
		function ($scope, userService) {

	/*----------------------------------------------------------------------*
	 *--- Initialisation of the objects used in this controller (boUserCtrl) ---*
	 *----------------------------------------------------------------------*/
	var url = document.location.href;
	var email = url.substring(url.lastIndexOf("/")+1);
	getConsumer(email);
	
	/*----------------------------------------------------------------------*/


	/*----------------------------------------------------------------------*
	 *------ Functions which use userService from ../services/user.js ------*
	 *----------------------------------------------------------------------*/
	
	// Function which retrieves the consumer which has email as ID.
    function getConsumer(email) {
        userService.getConsumer(email)
        .success(function (consr) {
            $scope.consumer = consr;
        })
        .error(function (error) {
            $scope.error_title = "Récupération d'un utilisateur";
			$scope.error_message = "Impossible récupérer les données de " + email + " : " + error.message;
			$('#errorModal').modal('show');
        });
    }

    // Scope's function 
	$scope.setBlacklisted = function (email) {
		if(!$scope.consumer.admin) {
			$scope.consumer.blackListed=true;
			userService.setBlacklisted(email)
	        .success(function (consr) {
	        	$scope.error_title = "Mettre en liste noire";
				$scope.error_message = email + " a été ajouté dans la liste noire";
				$('#errorModal').modal('show');
	        })
	        .error(function (error) {
	        	$scope.consumer.blackListed=false;
	        	$scope.error_title = 'Mettre en liste noire';
				$scope.error_message = "Impossible de mettre " + email + " en liste noire : " + error.message;
				$('#errorModal').modal('show');
	        });
	    } else {
	    	document.getElementById("radio").checked = false;
	    	$scope.error_title = 'Mettre en liste noire';
			$scope.error_message = "Impossible de mettre un administrateur en liste noire.";
			$('#errorModal').modal('show');
	    }
	};

	// Scope's function 
	$scope.unsetBlacklisted = function (email) {
		$scope.consumer.blackListed=false;
		userService.unsetBlacklisted(email)
        .success(function (consr) {
        	$scope.error_title = "Suppression de l'utilisateur de la liste noire";
			$scope.error_message = email + " a été retiré de la liste noire";
			$('#errorModal').modal('show');
        })
        .error(function (error) {
        	$scope.consumer.blackListed=true;
	        $scope.error_title = "Suppression de l'utilisateur de la liste noire";
			$scope.error_message = "Impossible de supprimer " + email + " de la liste noire : " + error.message;
			$('#errorModal').modal('show');
		});
	};

	// Scope's function 
	$scope.setAdmin = function (email) {
		if(!$scope.consumer.blackListed) {
			$scope.consumer.admin=true;
			userService.setAdmin(email)
	        .success(function (consr) {
	        	$scope.error_title = "Passer en administrateur";
				$scope.error_message = email + " est devenu un administrateur de ISwearBox";
				$('#errorModal').modal('show');
	        })
	        .error(function (error) {
	        	$scope.consumer.admin=false;
		        	$scope.error_title = 'Passer en administrateur';
					$scope.error_message = "Impossible de passer " + email + " administrateur : " + error.message;
					$('#errorModal').modal('show');
	        });
	    } else {
	    	$scope.error_title = 'Passer en administrateur';
			$scope.error_message = "Impossible de passer un membre en liste noire en tant qu'administrateur.";
			$('#errorModal').modal('show');
	    }
	};

	$scope.openSetAdminModal = function () {
		$scope.confirm_title = 'Passer en administrateur';
		$scope.confirm_message = "Voulez vous vraiment passer " + email + " en administrateur ?";
		$('#confirmModal').modal('show');
	};

	/*----------------------------------------------------------------------*/

}]);

