app.service('userService', ['$http', function ($http) {
    var urlBase = '/consumers';

    this.getAmount = function () {
        return $http.get('/amount');
    };

	this.updateAmount = function (data) {
		return $http.put('/user', data);
    };

    this.getConsumers = function () {
		return $http.get(urlBase);
    };

    this.getConsumer = function (id) {
		return $http.get(urlBase + "/" + id);
    };

    this.setAdmin = function (id) {
		return $http.put(urlBase + "/admin" +"/" + id);
    };

    this.setBlacklisted = function (id) {
		return $http.put(urlBase + "/blacklist" + "/" + id);
    };

    this.unsetBlacklisted = function (id) {
		return $http.put(urlBase + "/unblacklist" + "/" + id);
    };

}]);