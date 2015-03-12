app.controller('boCtrl',
	['$scope', 'userService',
		function ($scope, userService) {

	/*----------------------------------------------------------------------*
	 *--- Initialisation of the objects used in this controller (boCtrl) ---*
	 *----------------------------------------------------------------------*/
	
	getConsumers();
	
	/*----------------------------------------------------------------------*/


	/*----------------------------------------------------------------------*
	 *------ Functions which use userService from ../services/user.js ------*
	 *----------------------------------------------------------------------*/
	
	// Function which retrieves the list of consumers of the database calling 
	// corresponding function through the server.
    function getConsumers() {
        userService.getConsumers()
        .success(function (consrs) {
            $scope.consumers = consrs;
        })
        .error(function (error) {
	    	$scope.error_title = 'Chargement des utilisateurs';
			$scope.error_message = "Impossible de récupérer les données des utilisateurs : " + error.message;
			$('#errorModal').modal('show');
        });
    }


	// Scope's function which redirect to the userbackoffice page. 
	$scope.goConsumer = function (id) {
		document.location.href = "/userbackoffice/"+id;
	};

	/*----------------------------------------------------------------------*/
}]);

