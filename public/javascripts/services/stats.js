app.service('statsService', ['$http', function ($http) {
    var urlBase = '/stats';

    this.getStats = function (unit, t_unit, l_id) {
        return $http.get(urlBase + '/' + unit + '/' + t_unit + '?ids=' + l_id);
    };

    this.getAllStats = function () {
    	return $http.get(urlBase);
    };
    
    this.getSpecificStats = function () {
    	return $http.get('/specific-stats');
    };
    
}]);