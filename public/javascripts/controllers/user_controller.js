app.controller('userCtrl',
	['$scope', 'membersService', 'userService',
		function ($scope, membersService, userService) {

	/*----------------------------------------------------------------------*
	 *-- Initialisation of the objects used in this controller (userCtrl) --*
	 *----------------------------------------------------------------------*/
	
	$scope.numPage = 0;
	$scope.itemsPerPage = 5;
	$scope.amountTag = '';
	getAmount();
	getMembers();

	
	/*----------------------------------------------------------------------*/


	/*----------------------------------------------------------------------*
	 *------ Functions which use userService from ../services/user.js ------*
	 *----------------------------------------------------------------------*/
	
	// Function which retrieves the amount of the database calling 
	// corresponding function through the server.
    function getAmount() {
		userService.getAmount()
		.success(function (amount) {
			$scope.amountTag = amount;
		})
		.error(function (error) {
	    	$scope.error_title = 'Chargement de la dette';
			$scope.error_message = "Impossible de récupérer le montant de la dette : " + error.message;
			$('#errorModal').modal('show');
		});
	}	
	/*----------------------------------------------------------------------*/


	/*----------------------------------------------------------------------*
	 *--- Functions which use membersService from ../services/members.js ---*
	 *----------------------------------------------------------------------*/

	// Function which retrieves the list of members of the database calling 
	// corresponding function through the server.
    function getMembers() {
        membersService.getMembers()
        .success(function (membs) {
            $scope.members = membs;
            getRange();
        })
        .error(function (error) {
	    	$scope.error_title = 'Chargement des membres';
			$scope.error_message = "Impossible de récupérer les données des membres : " + error.message;
			$('#errorModal').modal('show');
        });
    }

	// Scope's function which increases the id-member's debt calling the 
	// corresponding function through the server and refresh the list of 
	// members and the range.
	$scope.increase = function (id) {
		for(var i in $scope.members) {
			if($scope.members[i].idPerson == id)
				$scope.members[i].debt += $scope.amountTag;
		}
		membersService.increaseDebt(id)
		.success(function () {
		})
		.error(function (error) {
			getMembers();
	    	$scope.error_title = 'Incrémenter la dette';
			$scope.error_message = "Impossible d'incrémenter la dette : " + error.message;
			$('#errorModal').modal('show');
		});
	};

	/*----------------------------------------------------------------------*/


	/*----------------------------------------------------------------------*
	 *-------------------------- Others functions --------------------------*
	 *----------------------------------------------------------------------*/

    // Function which fill the range with all the pages.
    function getRange() {
    	$scope.range = [];
		for(var i=0; i<$scope.members.length/$scope.itemsPerPage; i++) {
			$scope.range.push(i+1);
		}
	}

	// Scope's function which get the current page showed in the view.
	$scope.pagination = function (id) {
		$scope.numPage = id;
	};
	
	/*----------------------------------------------------------------------*/
}]);

