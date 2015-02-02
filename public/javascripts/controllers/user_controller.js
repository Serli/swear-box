app.controller('userCtrl',
	['$scope', 'membersService',
		function ($scope, membersService) {

	/*----------------------------------------------------------------------*
	 *-- Initialisation of the objects used in this controller (userCtrl) --*
	 *----------------------------------------------------------------------*/
	
	$scope.numPage = 0;
	$scope.itemsPerPage = 5;
	getMembers();

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
            alert('Unable to load members data: ' + error.message);
        });
    }

	// Scope's function which increases the id-member's debt calling the 
	// corresponding function through the server and refresh the list of 
	// members and the range.
	$scope.increase = function (id) {
		membersService.increaseDebt(id)
		.success(function () {
			getMembers();
		})
		.error(function (error) {
			alert('Unable to increase member debt: ' + error.message);
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
