app.controller('statsCtrl',
	['$scope', '$http',
		function ($scope, $http) {

	/*----------------------------------------------------------------------*
	 *-- Initialisation of the objects used in this controller (statsCtrl) --*
	 *----------------------------------------------------------------------*/
	
	$scope.stats = {};
	getStats();

	/*----------------------------------------------------------------------*/


	/*----------------------------------------------------------------------*
	 *--- Functions which use statsService from ../services/stats.js ---*
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
        var s2 = [3, 1, 9, 2, 8, 1, 5, 3, 2, 1, 3, 7];
        var s3 = [6, 4, 4, 1, 6, 3, 4, 8, 1, 3, 5, 4];
        var s4 = [9, 5, 2, 3, 3, 8, 1, 6, 4, 8, 9, 4];

        var ticks = ['JAN', 'FEV', 'MAR', 'AVR','MAI', 'JUN', 'JUL', 'AOU','SEP', 'OCT', 'NOV', 'DEC'];
         
        var plot1 = $.jqplot('chart1', [s1,s2], {
        	seriesColors: [ "#428bca", "#5cb85c"],
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
    }
	
}]);

