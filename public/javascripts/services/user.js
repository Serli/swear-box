app.service('userService', ['$http', function ($http) {
    var urlBase = '/user';

    this.getAmount = function () {
        return $http.get(/*urlBase+*/'/amount');
    };

	this.updateAmount = function (data) {
		return $http.put(urlBase, data);
    };
}]);