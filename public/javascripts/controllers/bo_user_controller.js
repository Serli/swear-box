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
            alert('Unable to load consumer data: ' + error.message);
        });
    }

    // Scope's function 
	$scope.setBlacklisted = function (email) {
		if(!$scope.consumer.admin) {
			$scope.consumer.blackListed=true;
			userService.setBlacklisted(email)
	        .success(function (consr) {
	        })
	        .error(function (error) {
	        	$scope.consumer.blackListed=false;
	            alert('Unable to modify consumer data: ' + error.message);
	        });
	    } else {
	    	document.getElementById("radio").checked = false;
	    	alert('Unable to put an administrator to the blacklist');
	    }
	};

	// Scope's function 
	$scope.unsetBlacklisted = function (email) {
		$scope.consumer.blackListed=false;
		userService.unsetBlacklisted(email)
        .success(function (consr) {
        })
        .error(function (error) {
        	$scope.consumer.blackListed=true;
            alert('Unable to modify consumer data: ' + error.message);
        });
	};

	// Scope's function 
	$scope.setAdmin = function (email) {
		$scope.consumer.admin=true;
		userService.setAdmin(email)
        .success(function (consr) {
        })
        .error(function (error) {
        	$scope.consumer.admin=false;
            alert('Unable to modify consumer data: ' + error.message);
        });
	};

	/*----------------------------------------------------------------------*/

}]);

