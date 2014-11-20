var adminApp = angular.module('adminApp',[]);

adminApp.controller('listCtrl', ['$scope', '$http', function($scope, $http){
	$scope.members = {};
	$http.get('//localhost:9000/person')
	.success(function(data, status, headers, config){
		$scope.members = data;
	})
	.error(function(data, status, headers, config){
	});
}]);