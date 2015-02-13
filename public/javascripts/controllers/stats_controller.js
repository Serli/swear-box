app.controller('statsCtrl',
	['$scope', '$http', 'membersService', 'statsService',
		function ($scope, $http, membersService, statsService) {

	/*----------------------------------------------------------------------*
	 *-- Initialisation of the objects used in this controller (statsCtrl) -*
	 *----------------------------------------------------------------------*/
	
	var plot1 = null;
	$scope.members = {};
	$scope.series = {};
	loadMembers();
	$scope.unit = 12;
	loadDefaultStats();

	/*----------------------------------------------------------------------*/


	/*----------------------------------------------------------------------*
	 *--- Functions which use membersService from ../services/members.js ---*
	 *----------------------------------------------------------------------*/

	// Function which retrieves the list of members of the database calling 
	// corresponding function through the server.
	function loadMembers() {
		membersService.getMembers()
		.success(function (membs) {
			$scope.members = membs;
			if($scope.members.length > 0) {
            	getStats($scope.members[0].idPerson);
            }
            else {
            	$scope.error_title = 'Chargement des membres';
				$scope.error_message = "Impossible d'afficher les statistiques : aucun membre";
				$('#errorModal').modal('show');
            }
		})
		.error(function () {
			$scope.error_title = 'Chargement des membres';
			$scope.error_message = 'Impossible de charger les données';
			$('#errorModal').modal('show');
		});
	}

	/*----------------------------------------------------------------------*/


	/*----------------------------------------------------------------------*
	 *----- Functions which use statsService from ../services/stats.js -----*
	 *----------------------------------------------------------------------*/

	// Function which retrieves the data list to draw the stats.
	function loadDefaultStats() {
		statsService.getStats($scope.unit, '1', '1,2,3,4,5,6')
		.success(function (data) {
			$scope.stats = data;
			drawStats();
		})
		.error(function () {
			$scope.error_title = 'Chargement des statistiques';
			$scope.error_message = 'Impossible de charger les données';
			$('#errorModal').modal('show');
		});
	}

	// Function which retrieves the data list to redraw the stats.
	$scope.loadStats = function () {
		var membersToShowInChart = getMembersToShowInChart();
		if(membersToShowInChart !== "") {
			if(document.getElementById('weeks').checked) {
				$scope.type_unit = 1;
			}
			if(document.getElementById('months').checked) {
				$scope.type_unit = 2;
			}
			 statsService.getStats($scope.unit, $scope.type_unit, membersToShowInChart)
			.success(function (data) {
				$scope.stats = data;
				drawStats();
			})
			.error(function () {
				$scope.error_title = 'Paramétrage des statistiques';
				$scope.error_message = 'Impossible de charger les données';
				$('#errorModal').modal('show');
			});
		} else {
			$scope.error_title = 'Paramétrage des statistiques';
			$scope.error_message = 'Au moins un membre doit être sélectionné.';
			$('#errorModal').modal('show');
		}
		
	};

	// Function which draws the stats in the canvas
	function drawStats() {
		if(plot1 !== null) {
			plot1.destroy();
		}

		var series = [];
		var legend = [];
		var j = 0;
		for(var i in $scope.stats) {
			if(i !== 'ticks') {
				series[j] = $scope.stats[i];
				for(var m in $scope.members) {
					if(i == ('p'+$scope.members[m].idPerson))
					legend[j] = $scope.members[m].firstname;
				}
			}
			j++;
		}
		
		var ticks = $scope.stats.ticks;	

		plot1 = $.jqplot('chart1', series, {
			
			/* Bootstrap colors : Primary, Success, Info, Warning, Danger, Base, Navbar */
			seriesColors: [ "#428bca", "#5cb85c", "#5bc0de", "#f0ad4e", "#d9534f", "#8A6DE9", "#444444" ],
			
			seriesDefaults: {
				pointLabels: {
					show: true,
					ypadding : 0,
					location : 's',
					hideZeros : true
				},
				shadow: false,
				shadowOffset: 0,
				renderer:$.jqplot.BarRenderer,
				rendererOptions: {
					fillToZero: true,
					highlightMouseOver: false,
					barPadding: 1,
					barMargin: 5
				}
			},
			
			legend: {
				textColor : '#0A0A0A',
				border : 'none',
				labels : legend,
				renderer: $.jqplot.EnhancedLegendRenderer,
				show: true,
				placement: 'outside',
				location: 'n',
				rendererOptions: {
					numberRows: 1
				}
			},
			
			axes: {
				xaxis: {
					renderer: $.jqplot.CategoryAxisRenderer,
					ticks: ticks
				},
				yaxis: {
					showTicks: false 
				}
			}
		});
	}

	/*----------------------------------------------------------------------*/
	

	/*----------------------------------------------------------------------*
	 *-------------------------- Others functions --------------------------*
	 *----------------------------------------------------------------------*/

	// Function which clear (even destroy) the chart show at the resize to
	// redraw it cleanly.
	window.onresize = function (e) {
		drawStats();
	};

	// Scope's function which refresh the array where is saved the state
	// of the checkboxes.
	$scope.refreshMembersChecked = function (idt) {
		for(var i in $scope.members) {
			if($scope.members[i].idPerson == idt) {
				if($scope.members[i].check) {
					$scope.members[i].check = false;
				}
				else {
					$scope.members[i].check = true;
				}
			}
		} 
	};

	// Function which transforms the members checked into the correct string
	// to use in the http.get request.
	function getMembersToShowInChart() {
		var membersToShowInChart = '';
		var counter = 0;
		for(var i in $scope.members) {
			if($scope.members[i].check) {
				if(counter > 0) {
					membersToShowInChart = membersToShowInChart +',';
				}
				membersToShowInChart = membersToShowInChart + $scope.members[i].idPerson;
				counter++;
			}
		}
		return membersToShowInChart;
	}

	/*----------------------------------------------------------------------*/
}]);

