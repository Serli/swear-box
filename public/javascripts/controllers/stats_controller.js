app.controller('statsCtrl',
	['$scope', '$http', 'membersService',
		function ($scope, $http, membersService) {

	/*----------------------------------------------------------------------*
	 *-- Initialisation of the objects used in this controller (statsCtrl) -*
	 *----------------------------------------------------------------------*/
	
    $scope.members = {};
	$scope.series = {};
    var plot1 = {};
    var bd_unit = 12;
    var bd_type_unit = 1;
    $scope.unit = bd_unit;
    $scope.type_unit = bd_type_unit;
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

	// Function which retrieves the data list to redraw the stats.
    $scope.getStats = function () {
        if(document.getElementById('weeks').checked) {
            $scope.type_unit = 1;
        }
        if(document.getElementById('months').checked) {
            $scope.type_unit = 2;
        }
        var ids = '';
        var cpt = 0;
        for(var i in $scope.members) {
            if(document.getElementById('checkbox'+$scope.members[i].idPerson).checked) {
                if(cpt > 0) {
                    ids = ids +',';
                }
                ids = ids + $scope.members[i].idPerson;
                cpt = cpt +1;
            }
        }

    	$http.get('/stats/'+$scope.unit+'/'+$scope.type_unit+'?ids='+ids)
        .success(function (data) {
            $scope.stats = data;
            plot1.destroy();
            drawStats();
        })
        .error(function (error) {
            alert('Unable to load stats data');
        });
    };

    //Function which draws the stats in the canvas
    function drawStats() {

    	var series = [];
    	var legend = [];
    	var j =0;
    	for(var i in $scope.stats) {
    		if(i !== 'ticks') {
    			series[j] = $scope.stats[i];
    			for(var m in $scope.members) {
    				if(i == ('p'+$scope.members[m].idPerson))
    				legend[j] = $scope.members[m].firstname+'  ';
    			}
    		}
    		j++;
    	}
    	
        var ticks = $scope.stats.ticks;	
         
        plot1 = $.jqplot('chart1', series, {
        	
        	seriesColors: [ "#428bca", "#5cb85c", "#5bc0de", "#f0ad4e", "#d9534f", "#8A6DE9", "#444444" ],
        	
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

            legend: {
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

        window.onresize = function (e) {
            plot1.destroy();
            drawStats();
        };
    }

    /*----------------------------------------------------------------------*/
	
}]);

