module.exports = function(grunt) {
	
	require('load-grunt-tasks')(grunt);
	
	grunt.initConfig({
		jshint: {
		    all: ['public/javascripts/*.js','!public/javascripts/swearbox.min.js','!public/javascripts/bootstrap.js']
		},
		uglify: {
			options:{
				mangle : false
			},
			dist: {
				files: {
			        'public/javascripts/swearbox.min.js': ['!public/javascripts/bootstrap.js','public/javascripts/*.js']
			     }
			 }
		},
		cssmin: {
			  target: {
			    files: {
			      'public/stylesheets/swearbox.min.css': ['public/stylesheets/main.css', 'public/stylesheets/admin.css','public/stylesheets/index.css','public/stylesheets/user.css']
			    }
			  }
			}
			  
	});

	grunt.registerTask('default',['jshint','uglify']);

}