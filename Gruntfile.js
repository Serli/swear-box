module.exports = function(grunt) {
	
	require('load-grunt-tasks')(grunt);
	
	grunt.initConfig({
		
		jshint: {
		    all: ['public/javascripts/*/*.js']
		},
		
		uglify: {
			options:{
				mangle : false
			},
			dist: {
				files: {
			        'public/javascripts/swearbox.min.js': ['!public/javascripts/bootstrap.min.js','!public/javascripts/d3.v3.min.js','public/javascripts/app.js','public/javascripts/services/*.js','public/javascripts/controllers/*.js']
			     }
			 }
		},
		
		cssmin: {
			  target: {
			    files: {
			      'public/stylesheets/swearbox.min.css': ['public/stylesheets/*.css','!public/stylesheets/bootstrap.min.css','!public/stylesheets/bootstrap.css.map','!public/stylesheets/swearbox.min.css']
			    }
			  }
			},
		
		watch: {
			  js: {
			    files: ['public/javascripts/*.js','!public/javascripts/bootstrap.js','!public/javascripts/swearbox.min.js'],
			    tasks: ['jshint','uglify'],
			    options: {
			      spawn: false
			    }
			  },
			css: {
			    files: ['public/stylesheets/*.css','!public/stylesheets/bootstrap.min.css','!public/stylesheets/bootstrap.css.map','!public/stylesheets/swearbox.min.css'],
			    tasks: ['cssmin'],
			    options: {
			      spawn: false
			    }
			  }
			},
			
		devUpdate: {
		    main: {
		        options: {
		              updateType: 'force',
		              reportUpdated: false,
		              semver: true,
		              packages: {
		                  devDependencies: true,
		                  dependencies: false
		              },
		              packageJson: null,
		              reportOnlyPkgs: []
		        }
		    }
		}
			  
	});

	grunt.registerTask('default',['devUpdate','jshint','uglify','cssmin']);


}