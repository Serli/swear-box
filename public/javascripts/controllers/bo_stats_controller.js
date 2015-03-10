app.controller('boStatsCtrl',
		['$scope', '$http', 'statsService',
		 function ($scope, $http, statsService) {

			/*----------------------------------------------------------------------*
			 *-- Initialisation of the objects used in this controller (userCtrl) --*
			 *----------------------------------------------------------------------*/

			$scope.stats = {};
			loadStats();

			/*----------------------------------------------------------------------*/


			/*----------------------------------------------------------------------*
			 *----- Functions which use statsService from ../services/stats.js -----*
			 *----------------------------------------------------------------------*/

			// Function which retrieves the data list to draw the stats.
			function loadStats() {
				statsService.getAllStats()
				.success(function (data) {
					$scope.stats = data;
					drawStatsD3();
				})
				.error(function () {
					$scope.error_title = 'Chargement des statistiques';
					$scope.error_message = 'Impossible de charger les données';
					$('#errorModal').modal('show');
				});
			}


			/*----------------------------------------------------------------------*/

			window.onresize = function (e) {
				drawStatsD3(1);
			};
			
			/*----------------------------------------------------------------------*/
			
			/*----------------------------------------------------------------------*
			 *---------------Function which draws the stats with D3 ----------------*
			 *----------------------------------------------------------------------*/
			function drawStatsD3(mod) {
				
				data = $scope.stats;

				if(mod == 1) {
					d3.select(document.getElementById('svg_id')).remove();
				}
				
				var margin = {top: 10, right: 15, bottom: 100, left: 40},
				margin2 = {top: 430, right: 15, bottom: 20, left: 40},
				width = document.getElementById('sub-body').offsetWidth - margin.left - margin.right,
				height = 500 - margin.top - margin.bottom,
				height2 = 500 - margin2.top - margin2.bottom;

				var parseDate = d3.time.format("%b %Y").parse;

				var x = d3.time.scale().range([0, width]),
				x2 = d3.time.scale().range([0, width]),
				y = d3.scale.linear().range([height, 0]),
				y2 = d3.scale.linear().range([height2, 0]);

				var xAxis = d3.svg.axis().scale(x).orient("bottom"),
				xAxis2 = d3.svg.axis().scale(x2).orient("bottom"),
				yAxis = d3.svg.axis().scale(y).orient("left");

				var brush = d3.svg.brush()
				.x(x2)
				.on("brush", brushed);

				var area = d3.svg.area()
				.interpolate("monotone")
				.x(function(d) { return x(parseDate(d.date)); })
				.y0(height)
				.y1(function(d) { return y(d.nb); });

				var area2 = d3.svg.area()
				.interpolate("monotone")
				.x(function(d) { return x2(parseDate(d.date)); })
				.y0(height2)
				.y1(function(d) { return y2(d.nb); });

				var svg = d3.select(document.getElementById('sub-body')).append("svg")
				.attr("id", "svg_id")
				.attr("width", width + margin.left + margin.right)
				.attr("height", height + margin.top + margin.bottom);

				svg.append("defs").append("clipPath")
				.attr("id", "clip")
				.append("rect")
				.attr("width", width)
				.attr("height", height);

				var focus = svg.append("g")
				.attr("class", "focus")
				.attr("transform", "translate(" + margin.left + "," + margin.top + ")");

				var context = svg.append("g")
				.attr("class", "context")
				.attr("transform", "translate(" + margin2.left + "," + margin2.top + ")");
				
				x.domain(d3.extent(data.map(function(d) { return parseDate(d.date); })));
				y.domain([0, d3.max(data.map(function(d) { return d.nb; }))]);
				x2.domain(x.domain());
				y2.domain(y.domain());

				focus.append("path")
				.datum(data)
				.attr("class", "area")
				.attr("d", area);

				focus.append("g")
				.attr("class", "x axis")
				.attr("transform", "translate(0," + height + ")")
				.call(xAxis);

				focus.append("g")
				.attr("class", "y axis")
				.call(yAxis);

				context.append("path")
				.datum(data)
				.attr("class", "area")
				.attr("d", area2);

				context.append("g")
				.attr("class", "x axis")
				.attr("transform", "translate(0," + height2 + ")")
				.call(xAxis2);

				context.append("g")
				.attr("class", "x brush")
				.call(brush)
				.selectAll("rect")
				.attr("y", -6)
				.attr("height", height2 + 7);

				function brushed() {
					x.domain(brush.empty() ? x2.domain() : brush.extent());
					focus.select(".area").attr("d", area);
					focus.select(".x.axis").call(xAxis);
				}

				function type(d) {
					d.date = parseDate(d.date);
					d.nb = +d.nb;
					return d;
				}
			}

			/*----------------------------------------------------------------------*/
		}]);
