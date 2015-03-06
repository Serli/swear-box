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
			$scope.unit = 8;

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
						$scope.members[0].check = true;
						loadDefaultStats($scope.members[0].idPerson);
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
			function loadDefaultStats(idMember) {
				statsService.getStats($scope.unit, '1', idMember)
				.success(function (data) {
					$scope.stats = data;
					//drawStats();

					drawStatsD3(1);
				})
				.error(function () {
					$scope.error_title = 'Chargement des statistiques';
					$scope.error_message = 'Impossible de charger les données';
					$('#errorModal').modal('show');
				});
			}

			
			/*----------------------------------------------------------------------*/


			/*----------------------------------------------------------------------*
			 *----- Function which retrieves the data list to redraw the stats -----*
			 *----------------------------------------------------------------------*/
			
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
						//drawStats();
						drawStatsD3(2);
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

			
			/*----------------------------------------------------------------------*/


			/*----------------------------------------------------------------------*
			 *----------- Function which draws the stats in the canvas -------------*
			 *----------------------------------------------------------------------*/
			
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

					//Bootstrap colors : Primary, Success, Info, Warning, Danger, Base, Navbar 
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
			 *---------------Function which draws the stats with svg ---------------*
			 *----------------------------------------------------------------------*/
			
			function drawStatsD3(mod) {	

				var names = [];
				var j = 0;
				for(var i in $scope.members) {
					if($scope.members[i].check) {
						names[j] = $scope.members[i].firstname;
						j++;
					}
				}

				var data = $scope.stats;
				
				var margin = {
						top : 0,
						right : 0,
						bottom : 30,
						left : 0
				}, width = document.getElementById('sub-body').offsetWidth, height = 400 - margin.top - margin.bottom;

				var x0 = d3.scale.ordinal().rangeRoundBands([ 0, width ], 0.1);

				var x1 = d3.scale.ordinal();

				var y = d3.scale.linear().range([ height, 0 ]);

				var color = d3.scale.ordinal().range(
						[ "#428bca", "#5cb85c", "#5bc0de", "#f0ad4e", "#d9534f", "#8A6DE9", "#444444" ]);

				var xAxis = d3.svg.axis().scale(x0).orient("bottom");

				var yAxis = d3.svg.axis().scale(y).orient("left").tickFormat(
						d3.format(".0"));

				var tip = d3.tip()
				  .attr('class', 'd3-tip')
				  .offset([-10, 0])
				  .html(function(d) {
				    return "<strong>" + d.value + "</strong>";
				  });
				
				var ageNames = names;

				data.forEach(function(d) {
					d.ages = ageNames.map(function(name) {
						return {
							name : name,
							value : +d[name]
						};
					});
				});

				x0.domain(data.map(function(d) {
					return d.Date;
				}));
				
				x1.domain(ageNames).rangeRoundBands([ 0, x0.rangeBand() ]);
				
				y.domain([ 0, d3.max(data, function(d) {
					return d3.max(d.ages, function(d) {
						return d.value*1.5;
					});
				}) ]);
				
				var date;
				var legend;
				
				if(mod == 1) {
					var svg = d3.select(document.getElementById('sub-body')).append("svg").attr("id", "svg_id").attr("width",
							width + margin.left + margin.right).attr("height",
									height + margin.top + margin.bottom).append("g").attr("transform",
											"translate(" + margin.left + "," + margin.top + ")");

					svg.call(tip);
					
					svg.append("g").attr("class", "x axis").attr("transform",
							"translate(0," + height + ")").call(xAxis);

					svg.append("g").attr("class", "y axis").call(yAxis).append("text").attr(
							"transform", "rotate(-90)").attr("y", 1).attr("dy", ".71em").style(
									"text-anchor", "end").text("Gros mots prononcés");

					date = svg.selectAll(".date").data(data).enter().append("g").attr(
							"class", "g").attr("transform", function(d) {
								return "translate(" + x0(d.Date) + ",0)";
							});

					date.selectAll("rect").data(function(d) {
						return d.ages;
					}).enter().append("rect").attr("width", x1.rangeBand()).attr("x",
							function(d) {
						return x1(d.name);
					}).attr("y", function(d) {
						return y(d.value);
					}).attr("height", function(d) {
						return height - y(d.value);
					}).style("fill", function(d) {
						return color(d.name);
					}).on('mouseover', tip.show)
				      .on('mouseout', tip.hide);

					legend = svg.selectAll(".legend").data(ageNames.slice().reverse())
					.enter().append("g").attr("class", "legend").attr("transform",
							function(d, i) {
						return "translate(0," + i * 20 + ")";
					});

					legend.append("rect").attr("x", width - 18).attr("width", 18).attr(
							"height", 18).style("fill", color);

					legend.append("text").attr("x", width - 24).attr("y", 9)
					.attr("dy", ".35em").style("text-anchor", "end").text(function(d) {
						return d;
					});

				}
				else if(mod == 2 ) {

					var transition = d3.select(document.getElementById('svg_id')).call(tip).transition().duration(750),
					delay = function(d, i) { return 0; };
					
					var state2 = transition.selectAll(".date")
					.delay(delay)
					.attr("x", function(d) { return x0(d.Date); });

					d3.select(document.getElementById('svg_id')).selectAll("rect").remove();
					
					date = d3.select(document.getElementById('svg_id')).selectAll(".date").data(data).enter().append("g").attr(
							"class", "g").attr("transform", function(d) {
								return "translate(" + x0(d.Date) + ",0)";
							});

					date.selectAll("rect").data(function(d) {
						return d.ages;
					}).enter().append("rect").on('mouseover', tip.show)
				      .on('mouseout', tip.hide).transition().duration(750).attr("width", x1.rangeBand()).attr("x",
							function(d) {
						return x1(d.name);
					}).attr("y", function(d) {
						return y(d.value);
					}).attr("height", function(d) {
						return height - y(d.value);
					}).style("fill", function(d) {
						return color(d.name);
					});
					
					d3.select(document.getElementById('svg_id')).selectAll(".legend").remove();
					
					legend = d3.select(document.getElementById('svg_id')).selectAll(".legend").data(ageNames.slice().reverse())
					.enter().append("g").attr("class", "legend").attr("transform",
							function(d, i) {
						return "translate(0," + i * 20 + ")";
					});

					legend.append("rect").transition().duration(750).attr("x", width - 18).attr("width", 18).attr(
							"height", 18).style("fill", color);

					legend.append("text").transition().duration(750).attr("x", width - 24).attr("y", 9)
					.attr("dy", ".35em").style("text-anchor", "end").text(function(d) {
						return d;
					});

					transition.select(".x.axis")
					.call(xAxis)
					.selectAll("g")
					.delay(delay);

					transition.select(".y.axis")
					.call(yAxis)
					.selectAll("g")
					.delay(delay);
				}
			}

			/*----------------------------------------------------------------------*/


			/*----------------------------------------------------------------------*
			 *-------------------------- Others functions --------------------------*
			 *----------------------------------------------------------------------*/

			// Function which clear (even destroy) the chart show at the resize to
			// redraw it cleanly.
			window.onresize = function (e) {
				//drawStats();
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
				$scope.loadStats();
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

