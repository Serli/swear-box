app.controller('statsCtrl',
	['$scope', '$http',
		function ($scope, $http) {

	/*----------------------------------------------------------------------*
	 *-- Initialisation of the objects used in this controller (statsCtrl) -*
	 *----------------------------------------------------------------------*/
	
	$scope.stats = {};
	getStats();

	/*----------------------------------------------------------------------*/


	/*----------------------------------------------------------------------*
	 *----- Functions which use statsService from ../services/stats.js -----*
	 *----------------------------------------------------------------------*/

	// Function which retrieves the data list to draw the stats.
    function getStats() {
    	$http.get('/stats')
        .success(function (data) {
            $scope.stats = data;
        })
        .error(function (error) {
            alert('Unable to load stats data');
        });
        
        drawStats();
    }

    //Function which draws the stats in the canvas
    function drawStats() {
        var s1 = [5, 4, 2, 8, 5, 4, 9, 6, 5, 4, 1, 8];

        var ticks = ['JAN', 'FEV', 'MAR', 'AVR','MAI', 'JUN', 'JUL', 'AOU','SEP', 'OCT', 'NOV', 'DEC'];
         
        var plot1 = $.jqplot('chart1', [s1], {
            seriesDefaults:{
                pointLabels: {
                    show: true
                },
                renderer:$.jqplot.BarRenderer,

                rendererOptions: {
                	fillToZero: true,
                	highlightMouseOver: false,
                    barMargin: 5},
                color : '#d9534f'
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

