$(document).ready(function(){
    var s1 = [5, 4, 2, 8, 5, 4, 9, 6, 5, 4, 1, 8];
    // Can specify a custom tick Array.
    // Ticks should match up one for each y value (category) in the series.
    var ticks = ['JAN', 'FEV', 'MAR', 'AVR','MAI', 'JUN', 'JUL', 'AOU','SEP', 'OCT', 'NOV', 'DEC'];
     
    var plot1 = $.jqplot('chart1', [s1], {
        // The "seriesDefaults" option is an options object that will
        // be applied to all series in the chart.
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
            // Use a category axis on the x axis and use our custom ticks.
            xaxis: {
                renderer: $.jqplot.CategoryAxisRenderer,
                ticks: ticks
            },
            // Pad the y axis just a little so bars can get close to, but
            // not touch, the grid boundaries.  1.2 is the default padding.
            yaxis: {
            	showTicks: false 
            	//show: false,
                //pad: 1.05,
                //tickOptions: {formatString: ''}
            }
        }
    });
});