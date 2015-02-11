app.controller('statsCtrl',
	['$scope', '$http', 'membersService',
		function ($scope, $http, membersService) {

	/*----------------------------------------------------------------------*
	 *-- Initialisation of the objects used in this controller (statsCtrl) -*
	 *----------------------------------------------------------------------*/
	
    $scope.members = {};
	$scope.series = {};
	getStats();
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
        })
        .error(function (error) {
            alert('Unable to load members data: ' + error.message);
        });
    }

    /*----------------------------------------------------------------------*/


	/*----------------------------------------------------------------------*
	 *----- Functions which use statsService from ../services/stats.js -----*
	 *----------------------------------------------------------------------*/

	// Function which retrieves the data list to draw the stats.
    function getStats() {
    	$http.get('/stats/12/2?ids=1,2,3,4,5,6')
        .success(function (data) {
            $scope.stats = data;
            drawStats();
        })
        .error(function (error) {
            alert('Unable to load stats data');
        });
        
        
    }

    //Function which draws the stats in the canvas
    function drawStats() {

    	var series = [];
    	var j =0;
    	for(var i in $scope.stats) {
    		if(i !== 'ticks') {
    			series[j] = $scope.stats[i];
    		}
    		j++;
    	}

        var ticks = $scope.stats.ticks;
         
        var plot1 = $.jqplot('chart1', series, {
        	seriesColors: [ "#428bca", "#5cb85c", "#5bc0de", "#f0ad4e", "#AA6708", "#d9534f"],
            seriesDefaults:{
                pointLabels: {
                    show: true
                },
                shadow: false,
                shadowOffset: 0,
                renderer:$.jqplot.BarRenderer,

                rendererOptions: {
                	fillToZero: true,
                	highlightMouseOver: false,
                    barPadding: 1,
                    barMargin: 5}
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

        window.onresize = function (e) {
            plot1.destroy();
            drawStats();
        };
    }

    /*----------------------------------------------------------------------*/
	
}]);

