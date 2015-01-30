app.controller('adminCtrl',
	['$scope', '$http', 'membersService', 'userService',
		function ($scope, $http, membersService, userService) {

	/*----------------------------------------------------------------------*
	 *-- Initialisation of the objects used in this controller (userCtrl) --*
	 *----------------------------------------------------------------------*/
	
	$scope.idImage = 0;	
	$scope.numPage = 0;
	$scope.itemsPerPage = 5;
	$scope.newMember = {};
	$scope.newMember.name= '';
	$scope.modMember = {};
	getMembers();
	getAmount();

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
			 alert('Unable to load amount data: ' + error.message);
		});
	}	

	// Scope's function which update the amount of penalty in the database  
	// calling corresponding function through the server.
	$scope.updateAmount = function () {	
		var dataObj = {
			amount : $scope.amount
		};			

		userService.updateAmount(dataObj)
		.success(function () {
			$scope.amountTag = $scope.amount;
			$scope.amount = '';
		})
		.error(function (error) {
            alert('Unable to update the penalty amount: ' + error.message);
        });
	};
	
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

	// Scope's function which add a member to the database calling 
	// corresponding function through the server.
	$scope.addMember = function () {	
		var dataObj = {
			name : $scope.newMember.name,
			firstname : $scope.newMember.firstname
		};			

		membersService.addMember(dataObj)
		.success(function () {
			getMembers();
			$scope.newMember.name= '';
			$scope.newMember.firstname= '';
		})
		.error(function (error) {
            alert('Unable to add a new member: ' + error.message);
        });
	};

	// Scope's function which delete a member in the database calling 
	// corresponding function through the server.
	$scope.deleteMember = function () {
		if(($scope.members.length % $scope.itemsPerPage == 1) && ($scope.idx+1 == $scope.members.length) && ($scope.members.length !== 1)) {
			$scope.numPage = $scope.numPage-1;
		}
		
		$scope.members.splice($scope.idx,1);

		membersService.deleteMember($scope.idt)
		.success(function () {
			ModalHide('#deleteMember');
			getMembers();
		})
		.error(function (error) {
            alert('Unable to delete a member: ' + error.message);
        });
	};
	
	// Scope's function which discharge the debt of a member in the database  
	// calling corresponding function through the server.
	$scope.dischargeMember = function () {
		membersService.dischargeMember($scope.idt)
		.success(function () {
			ModalHide('#dischargeMember');
			getMembers();
		})
		.error(function (error) {
            alert('Unable to discharge a member: ' + error.message);
        });
	};

	// Scope's function which update the name or/and the firstname of a member 
	// in the database calling corresponding function through the server.
	$scope.modifyMember = function () {
		var dataObj = {
			name : $scope.modMember.name,
			firstname : $scope.modMember.firstname
		};			

		membersService.updateNameFirstname($scope.idt, dataObj)
		.success(function () {
			ModalHide('#modifyMember');
			getMembers();
			$scope.modMember.name= '';
			$scope.modMember.firstname= '';
		})
		.error(function (error) {
            alert('Unable to update the penalty amount: ' + error.message);
        });
	};
	
	// Uses membersService from ../services/members.js
	$scope.onChange = function () {
		var file = document.getElementById('hiddenfile').files[0];
		var fd = new FormData();

		ChargementImage('#span-load'+$scope.idImage,'#img'+$scope.idImage);
		fd.append('file', file);

		var dataObj = {
			transformRequest: angular.identity,
			headers: {'Content-Type': undefined}
		};		

		membersService.updatePicture($scope.idImage, fd, dataObj)
		.success(function(data, status, headers, config){
			getMembers();
			ChargementImage('#img'+$scope.idImage,'#span-load'+$scope.idImage);
		})
		.error(function (error) {
            alert('Unable to change member picture: ' + error.message);
        });
	};

	/*----------------------------------------------------------------------*/


	/*----------------------------------------------------------------------*
	 *----------------- Functions which use to open modals -----------------*
	 *----------------------------------------------------------------------*/

	// Scope's function which open the delete modal.
	$scope.openDeleteModal = function (idx, idt, firstname, name) {
		$scope.idx = idx;
		$scope.idt = idt;
		$scope.firstname = firstname;
		$scope.name = name;
		ModalShow('#deleteMember');
	};
	
	// Scope's function which open the discharge modal.
	$scope.openDischargeModal = function (idt, firstname, name) {
		$scope.idt = idt;
		$scope.firstname = firstname;
		$scope.name = name;
		ModalShow('#dischargeMember');
	};
	
	// Scope's function which open the modify modal.
	$scope.openModifyModal = function (idt, firstname, name) {
		$scope.idt = idt;
		$scope.modMember.firstname = firstname;
		$scope.modMember.name = name;
		ModalShow('#modifyMember');
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
	//----------------------------------------------------------------------
	$scope.pagination = function (id) {
		$scope.numPage = id;
	};

	// Scope's function which get the id of the picture member clicked.
	//----------------------------------------------------------------------
	$scope.browseImage = function (idt) {
		$('#hiddenfile').click();
		$scope.idImage = idt;
	};
	
	// Scope's function which checks if the pressed key is a number.
	//----------------------------------------------------------------------
	$scope.isNumeric = function (event) {
		if((event.which<48 || event.which>57) && (event.which !== 0) && (event.which !== 8)) {
			event.preventDefault();
			return false;
		}
	};

	// Function which show the modal gived in parameter.
	//----------------------------------------------------------------------
	function ModalShow(vModal) {
		$(vModal).modal('show');
	}
	
	// Function which hide the modal gived in parameter.
	//----------------------------------------------------------------------
	function ModalHide(vModal) {
		$(vModal).modal('hide');
	}
	
	// Function which manage the loading picture.
	//----------------------------------------------------------------------
	function ChargementImage(s, h) {
		$(s).show();
		$(h).hide();
	}

	/*----------------------------------------------------------------------*/
}]);

