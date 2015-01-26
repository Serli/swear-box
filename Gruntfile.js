module.exports = function(grunt) {
	
	grunt.initConfig({
		jshint: {
		    all: ['public/javascripts/*.js','!public/javascripts/min.js','!public/javascripts/bootstrap.js']
		},
		uglify: {
			options:{
				mangle : false
			},
			dist: {
				files: {
			        'public/javascripts/min.js': ['!public/javascripts/bootstrap.js','public/javascripts/*.js']
			     }
			 }
		}
			  
	});
	
	grunt.loadNpmTasks('grunt-contrib-uglify');
	grunt.loadNpmTasks('grunt-contrib-jshint');
	grunt.registerTask('default',['jshint','uglify']);

}