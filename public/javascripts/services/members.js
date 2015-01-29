app.service('membersService', ['$http', function ($http) {

        var urlBase = '/members';

        this.getMembers = function () {
            return $http.get(urlBase);
        };

        this.increaseDebt = function (id) {
            return $http.put(urlBase + '/increase/' + id);
        };
    }]);