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
			        'public/javascripts/swearbox.min.js': ['public/javascripts/*.js','!public/javascripts/bootstrap.js']
			     }
			 }
		},
		
		cssmin: {
			  target: {
			    files: {
			      'public/stylesheets/swearbox.min.css': ['public/stylesheets/main.css', 'public/stylesheets/admin.css','public/stylesheets/index.css','public/stylesheets/user.css']
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
			    files: ['public/stylesheets/*.css','!public/stylesheets/bootstrap.css','!public/stylesheets/swearbox.min.css'],
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